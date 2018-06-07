package com.polymorphous.main;

import com.polymorphous.Network.GameClient;
import com.polymorphous.Network.Lobby;
import com.polymorphous.Network.packets.PacketDisconnect;
import com.polymorphous.Network.packets.PacketLogin;
import com.polymorphous.Network.packets.PacketMapReq;
import com.polymorphous.ai.agents.Bot;
import com.polymorphous.main.Handlers.Handler;
import com.polymorphous.main.Handlers.KeyInput;
import com.polymorphous.main.Handlers.PlayersHandler;
import com.polymorphous.main.Objects.GameInfo;
import com.polymorphous.main.Objects.GameObject;
import com.polymorphous.main.Objects.ID;
import com.polymorphous.main.Objects.Map;
import com.polymorphous.main.Player.Player;
import com.polymorphous.util.AchievementHandler.Reader;
import com.polymorphous.util.audioHandler.AudioManager;
import com.polymorphous.util.mapGen.GenMap;
import com.polymorphous.util.menuHandler.FlipCanvas;
import com.polymorphous.util.menuHandler.GridAchievements;
import com.polymorphous.util.menuHandler.MenuPages;
import com.polymorphous.util.menuHandler.TableLobby;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

/**
 * @author pxp660
 */
public class Game extends Application {

    public static final ObservableList<Lobby> data =
            FXCollections.observableArrayList();
    public static Pane achiPane;
    public static boolean[] themeStatus = new boolean[]{true, true, true};
    public static boolean[] characterStatus = new boolean[]{true, true, true, true, true, false, false, false, false, false, false, false};
    public static int musicVolume, soundEffectsVolume;
    public static Game game;
    public static double SCREEN_WIDTH, SCREEN_HEIGHT;   // width and height of the window
    public static int HEIGHT, WIDTH;                    // width and height of the game panel
    public static int BLOCK_SIZE;                       // size of all the blocks in the game
    public static State state = State.Menu;
    public static int multiplayerState = 0;
    public static GameClient socketClient;
    public static int theme = 2;
    public static int char_no = 5;
    public static int lobbyID;
    public static KeyInput input;
    public static Map map;
    public static String player_user_name;
    public static GenMap gen_map;
    public static AudioManager audioManager;
    public static boolean switchCaseMusic = false;
    public static boolean switchCaseSoundEffects = false;
    private static TableLobby tableLobby = new TableLobby(data);
    public static TableView tableView = tableLobby.createObject();
    private static int spawn_pos = 0;
    private static Handler handler;
    private static PlayersHandler playersHandler;
    public static LoadImage getImg;
    private final long[] frameTimes = new long[100];
    public Player player;
    public Bot bot;
    private Group group;
    private Stage primaryStage;
    private Scene menuScene;
    private Scene gameScene;
    private Pane game_panel;
    private Pane pause_panel;
    private Pane deathscreen_panel;
    private Pane winning_panel;
    private Pane adminPanel;
    private Pane waitingPanel;
    private VBox vBox;
    private GameInfo infoPanel;
    private FlipCanvas mainMenu;
    private GraphicsContext gc_game_b;
    private GraphicsContext gc_obj;
    private boolean game_paused = false;
    private int frameTimeIndex = 0;
    private boolean arrayFilled = false;

    public static void resetGame() {
        game = null;
        map.getObstacles().clear();
        map.getSpaces().clear();
        map.getSpawnPos().clear();
        handler.object.clear();
        playersHandler.players.clear();
    }

    public static void setLobbyID() {
        lobbyID = Integer.parseInt(data.get(tableView.getSelectionModel().getFocusedIndex()).getID());
        spawn_pos = Integer.parseInt(data.get(tableView.getSelectionModel().getFocusedIndex()).getPlayers());
    }

    public static void sleep(int n) {
        try {
            TimeUnit.SECONDS.sleep(n);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static double clamp(double var, double min, double max) {
        if (var >= max)
            return var = max;
        else if (var <= min)
            return var = min;
        else
            return var;
    }

    public static Handler getHandler() {
        return handler;
    }

    public static PlayersHandler getPlayersHandler() {
        return playersHandler;
    }

    public static void main(String args[]) {

        launch(args);


    }

    public void init() {

        audioManager = new AudioManager();
        handler = new Handler();
        playersHandler = new PlayersHandler();
        mainMenu = new FlipCanvas();
        input = new KeyInput();

    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        primaryStage.setTitle("Polymorphous");
        musicVolume = 100;
        soundEffectsVolume = 75;
        audioManager.playMusic(audioManager.music.getRandomGeneralTrack(), musicVolume);
        audioManager.updateVolume(musicVolume, soundEffectsVolume);

        Reader.restoreAchievements("Game/src/com/polymorphous/util/AchievementHandler/achievements.txt");
        achiPane = GridAchievements.grid.initGrid();
        TemporaryPanel temporaryPanel = new TemporaryPanel();
        getImg = new LoadImage();
        calculateScreenSize();
        createMenu();
        createGame();
        try {
            pause_panel = temporaryPanel.createInGameMenu(group, input);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        deathscreen_panel = temporaryPanel.createDeathScreen(group);
        winning_panel = temporaryPanel.createWinningScreen(group);
        adminPanel = temporaryPanel.createAdminPanel(group);
        waitingPanel = temporaryPanel.createWaitingPanel(group);
        primaryStage.setScene(menuScene);
        primaryStage.setFullScreen(true);
        primaryStage.setResizable(false);
        primaryStage.show();
        socketClient = new GameClient(this, "35.185.84.98");
        socketClient.start();
        gameScene.setOnKeyPressed(input);
        gameScene.setOnKeyReleased(input);
        primaryStage.setOnCloseRequest(we -> sendDisconnect());
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
                render();
                //printFPS(now);
            }

        }.start();

    }

    public static void sendDisconnect() {
        PacketDisconnect packet = new PacketDisconnect(Game.player_user_name, Game.lobbyID);
        packet.writeData(Game.socketClient);
        System.out.println("disconnect requested " + packet.getUsername() + " - " + packet.getLobbyID());
    }

    private void update() {

        handler.update();
        playersHandler.update();

        detectPausedGame();
        detectEndGame();
        detectWaitingScreen();
        if (state == State.Singleplayer && game == null) {
            audioManager.stopMusic();
            switch (theme) {
                case 1:
                    audioManager.playMusic(audioManager.music.getRandomGeneralTrack(), musicVolume);
                    break;
                case 2:
                    audioManager.playMusic(audioManager.music.getRandomGoTTrack(), musicVolume);
                    break;
                case 3:
                    audioManager.playMusic(audioManager.music.getRandomWesternTrack(), musicVolume);
                    break;
            }

            createSinglePlayerGame();
        }
        if (state == State.Multiplayer && game == null) {
            audioManager.stopMusic();
            switch (theme) {
                case 1:
                    audioManager.playMusic(audioManager.music.getRandomGeneralTrack(), musicVolume);
                    break;
                case 2:
                    audioManager.playMusic(audioManager.music.getRandomGoTTrack(), musicVolume);
                    break;
                case 3:
                    audioManager.playMusic(audioManager.music.getRandomWesternTrack(), musicVolume);
                    break;
            }
            createMultiPlayerPlayerGame();
        }

    }

    private void render() {
        primaryStage.setFullScreen(true);
        if (state == State.Singleplayer || state == State.Multiplayer) {
            if (multiplayerState != 1 && multiplayerState != 2) {

                double mid_screen = (HEIGHT - BLOCK_SIZE * 7) / 2;
                double end_tile = BLOCK_SIZE + (BLOCK_SIZE / 2) * 20;
                gc_game_b.drawImage(getImg.BACKGROUND(theme), 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);


                //up
                double sx = WIDTH / 2;
                double sy = 125;
                //right
                double sw = WIDTH + 15;
                double sh = mid_screen;
                //down
                double dx = WIDTH / 2;
                double dy = end_tile + 5;
                //left
                double dw = -15;
                double dh = mid_screen;

                double[] xPoints = {sx, sw, dx, dw};
                double[] yPoints = {sy, sh, dy, dh};

                gc_obj.clearRect(0, 0, WIDTH, HEIGHT);
                gc_obj.drawImage(Game.getImg.FLOATING_LEFT(theme), 0, mid_screen + BLOCK_SIZE / 10, WIDTH / 2, HEIGHT / 2.4);
                gc_obj.drawImage(Game.getImg.FLOATING_RIGHT(theme), WIDTH / 2 - BLOCK_SIZE / 10, mid_screen + BLOCK_SIZE / 10, WIDTH / 2 + BLOCK_SIZE / 10, HEIGHT / 2.4);

                gc_obj.setFill(new ImagePattern(Game.getImg.FLOOR(theme), 0, 0, 1, 1, true));
                gc_obj.fillPolygon(xPoints, yPoints, 4);
                handler.render(gc_obj);

                playersHandler.render(gc_obj);

                infoPanel.render(gc_game_b);

            }
        } else if (state == State.Menu) {
            primaryStage.setScene(menuScene);

        }


    }

    private void createGame() {
        group = new Group();
        gameScene = new Scene(group);
        Canvas background_canvas = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT);
        Canvas objects_canvas = new Canvas(WIDTH, SCREEN_HEIGHT);

        game_panel = new Pane();
        game_panel.setLayoutX(SCREEN_WIDTH / 2 - WIDTH / 2);                      // set the position of the game panel
        game_panel.getChildren().add(objects_canvas);
        gc_game_b = background_canvas.getGraphicsContext2D();
        gc_obj = objects_canvas.getGraphicsContext2D();


        group.getChildren().add(background_canvas);
        group.getChildren().add(game_panel);
        infoPanel = new GameInfo();

        group.setAutoSizeChildren(true);
        gameScene.setRoot(group);

    }

    private void createMenu() {
        vBox = new VBox();
        vBox.getStylesheets().add("com/polymorphous/resources/application.css");
        vBox.getChildren().add(mainMenu.createAnimationMenu(MenuPages.MAIN_MENU));

        menuScene = new Scene(vBox, SCREEN_WIDTH, SCREEN_HEIGHT);

    }

    private void calculateScreenSize() {
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getBounds();
        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setMaxWidth(bounds.getWidth());
        primaryStage.setMaxHeight(bounds.getHeight());

        SCREEN_HEIGHT = bounds.getHeight();
        SCREEN_WIDTH = bounds.getWidth();

        BLOCK_SIZE = (int) Math.round(SCREEN_HEIGHT / 14);
        HEIGHT = (int) Math.round(SCREEN_HEIGHT) + 5 * BLOCK_SIZE;
        WIDTH = HEIGHT;
    }

    private void detectPausedGame() {
        if (input.esc && !game_paused) {
            group.getChildren().add(pause_panel);
            state = State.Pause;
            game_paused = true;

            bot.setActive(false);

            input.esc = false;
        } else if (input.esc && game_paused) {
            group.getChildren().remove(pause_panel);
            game_paused = false;
            bot.setActive(true);
            input.esc = false;
            state = State.Singleplayer;
        }
    }

    private void detectWaitingScreen() {

        if (multiplayerState == 1 && !group.getChildren().contains(waitingPanel)) {
            group.getChildren().add(waitingPanel);
            state = State.WaitingScreen;
        } else if (multiplayerState == 2 && !group.getChildren().contains(adminPanel)) {
            group.getChildren().remove(waitingPanel);
            group.getChildren().add(adminPanel);
            state = State.WaitingScreen;
        } else if (multiplayerState == 3 && group.getChildren().contains(adminPanel)) {
            group.getChildren().remove(adminPanel);
        } else if (multiplayerState == 3 && group.getChildren().contains(waitingPanel)) {
            group.getChildren().remove(waitingPanel);
            state = State.Multiplayer;
        }

    }

    private void detectEndGame() {

        if (state != State.Deathscreen) {
            for (Player player : playersHandler.players) {
                if (player.getLives() == 0) {
                    if (player.getUsername().equals(player_user_name)) {
                        state = State.Deathscreen;
                        group.getChildren().add(deathscreen_panel);

                    }
                }
            }

        }
        LinkedList<Player> deadPlayers = new LinkedList<>();
        if (state != State.WinningScreen) {
            for (Player player : playersHandler.players) {
                if (player.getLives() == 0) {
                    if (!player.getUsername().equals(player_user_name) && player.getLives() == 0) {
                        deadPlayers.add(player);
                    }
                }
            }

            if ((playersHandler.players.size() > 1) && deadPlayers.size() == playersHandler.players.size() - 1) {
                state = State.WinningScreen;
                group.getChildren().add(winning_panel);
            }
        }

    }

    public void createSinglePlayerGame() {
        game = this;
        primaryStage.setScene(gameScene);


        map = new Map(WIDTH, HEIGHT, ID.Wall);
        gen_map = map.getMap();


        for (GameObject gameObject : map.getObstacles()) {
            handler.addObject(gameObject);
        }
        player_user_name = "Khaleesi";
        player = new Player(map.getSpawnPos().get(spawn_pos).getX() + 20, map.getSpawnPos().get(spawn_pos).getY() + 20, ID.Player, handler, input, player_user_name);
        playersHandler.addPlayer(player);


        // Add a bot to the game
        bot = new Bot(map.getSpawnPos().get(3).getX() + 20, map.getSpawnPos().get(3).getY() + 20, ID.Bot, handler, "Bot", true, false);
        playersHandler.addPlayer(bot);

       /* bot = new Bot(map.getSpawnPos().get(3).getX() + 20, map.getSpawnPos().get(3).getY() + 20, ID.Bot, handler, "Bot", true, false);
        playersHandler.addPlayer(bot);
        bot = new Bot(map.getSpawnPos().get(2).getX() + 20, map.getSpawnPos().get(2).getY() + 20, ID.Bot, handler, "Bot", true, false);
        playersHandler.addPlayer(bot);
*/
    }

    public void createMultiPlayerPlayerGame() {
        game = this;
        primaryStage.setScene(gameScene);

        sleep(2);
        PacketMapReq reqMap = new PacketMapReq(lobbyID); // request map from the lobby/server
        System.out.println("Requested map for lobby: " + lobbyID);
        reqMap.writeData(socketClient);
        sleep(2);

        map = new Map(WIDTH, HEIGHT, ID.Wall);

        player = new Player(map.getSpawnPos().get(spawn_pos).getX() + 20, map.getSpawnPos().get(spawn_pos).getY() + 20, ID.Player, handler, input, player_user_name);
        Game.getPlayersHandler().addPlayer(player);

        PacketLogin loginPacket = new PacketLogin(player.getUsername(), player.getX(), player.getY(), char_no, lobbyID);
        loginPacket.writeData(socketClient);

        for (GameObject gameObject : map.getObstacles()) {
            handler.addObject(gameObject);
        }


    }

    private void printFPS(long now) {

        long oldFrameTime = frameTimes[frameTimeIndex];
        frameTimes[frameTimeIndex] = now;
        frameTimeIndex = (frameTimeIndex + 1) % frameTimes.length;
        if (frameTimeIndex == 0) {
            arrayFilled = true;
        }
        if (arrayFilled) {
            long elapsedNanos = now - oldFrameTime;
            long elapsedNanosPerFrame = elapsedNanos / frameTimes.length;
            int frameRate = (int) (1_000_000_000.0 / elapsedNanosPerFrame);
            System.out.println("FPS:" + frameRate);
            //label.setText(String.format("Current frame rate: %.3f", frameRate));
        }
    }

}

