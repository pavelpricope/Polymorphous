package com.paradroid.main;

import com.paradroid.Network.packets.PacketGameState;
import com.paradroid.main.Handlers.KeyInput;
import com.paradroid.util.menuHandler.FlippingButton;
import com.paradroid.util.menuHandler.UIobjectsAssets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.net.URISyntaxException;

import static com.paradroid.main.Game.*;


public class TemporaryPanel {


    public Pane createInGameMenu(Group group, KeyInput input) throws URISyntaxException {

        Pane pause_panel = new Pane();
        pause_panel.setLayoutX(0);
        pause_panel.setLayoutY(0);
        pause_panel.setMinWidth(SCREEN_WIDTH);
        pause_panel.setMinHeight(SCREEN_HEIGHT);
        pause_panel.getStylesheets().add(getClass().getResource("application.css").toURI().toString());

        Button resume = UIobjectsAssets.buttonResume.getButton();
        Button main_menu = UIobjectsAssets.buttonMainMenu.getButton();
        Node music = FlippingButton.musicButton.flipPane;
        Node music_fx = FlippingButton.fxButton.flipPane;

        resume.setLayoutX(SCREEN_WIDTH / 2 - 300);
        resume.setLayoutY(SCREEN_HEIGHT / 2 - 200);
        music.setLayoutX(SCREEN_WIDTH / 2 - 300);
        music.setLayoutY(SCREEN_HEIGHT / 2 - 100);
        music_fx.setLayoutX(SCREEN_WIDTH / 2 - 300);
        music_fx.setLayoutY(SCREEN_HEIGHT / 2);
        main_menu.setLayoutX(SCREEN_WIDTH / 2 - 300);
        main_menu.setLayoutY(SCREEN_HEIGHT / 2 + 100);
        resume.setOnMousePressed(event -> {
            input.esc = true;
        });

        main_menu.setOnMousePressed(event -> {
            {
                sendDisconnect();
            }
            Game.state = State.Menu;
            Game.resetGame();
            group.getChildren().remove(pause_panel);


        });

        pause_panel.getChildren().add(resume);
        pause_panel.getChildren().add(main_menu);
        pause_panel.getChildren().add(music_fx);
        pause_panel.getChildren().add(music);

        return pause_panel;

    }


    public Pane createWinningScreen(Group group) {
        Pane winning_panel = new Pane();
        winning_panel.setLayoutX(0);
        winning_panel.setLayoutY(0);
        winning_panel.setMinWidth(SCREEN_WIDTH);
        winning_panel.setMinHeight(SCREEN_HEIGHT);


        try {
            winning_panel.getStylesheets().add(getClass().getResource("application.css").toURI().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        Button back = UIobjectsAssets.buttonBack.getButton();

        back.setLayoutX(SCREEN_WIDTH / 2 - 300);
        back.setLayoutY(SCREEN_HEIGHT / 2 - 100);
        back.setOnMousePressed(event ->
        {
            Game.state = State.Menu;
            Game.resetGame();
            group.getChildren().remove(winning_panel);
        });


        Image image = Game.getImg.PANEL_WON(0);
        ImageView imageView = new ImageView(image);

        imageView.setLayoutX(SCREEN_WIDTH / 2 - 400);
        imageView.setLayoutY(SCREEN_HEIGHT / 2 - 300);

        winning_panel.getChildren().add(imageView);
        winning_panel.getChildren().add(back);


        return winning_panel;
    }

    public Pane createAdminPanel(Group group) {
        Pane adminPanel = new Pane();
        adminPanel.setLayoutX(0);
        adminPanel.setLayoutY(0);
        adminPanel.setMinWidth(SCREEN_WIDTH);
        adminPanel.setMinHeight(SCREEN_HEIGHT);

        try {
            adminPanel.getStylesheets().add(getClass().getResource("application.css").toURI().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        Button start = UIobjectsAssets.buttonPlay.getButton();
        Button back = UIobjectsAssets.buttonBack.getButton();

        start.setLayoutX(SCREEN_WIDTH / 2 - 300);
        start.setLayoutY(SCREEN_HEIGHT / 2 - 300);
        start.setOnMousePressed(event ->
        {
            sendGameState();
            Game.multiplayerState = 3;
            Game.state = State.Multiplayer;
            // group.getChildren().remove(adminPanel);
        });

        back.setLayoutX(SCREEN_WIDTH / 2 - 300);
        back.setLayoutY(SCREEN_HEIGHT / 2);
        back.setOnMousePressed(event ->
        {
            sendDisconnect();
            Game.state = State.Menu;
            Game.resetGame();
            multiplayerState = 0;
            group.getChildren().remove(adminPanel);
        });

        Text label = new Text(0, BLOCK_SIZE * 2, "You can now start the game.");
        label.setFont(Font.font("Verdana", 50));
        label.setX(SCREEN_WIDTH / 2 - label.getBoundsInLocal().getWidth() / 2);
        adminPanel.getChildren().add(start);
        adminPanel.getChildren().add(back);
        adminPanel.getChildren().add(label);

        return adminPanel;
    }

    private void sendGameState() {
        PacketGameState packetGameState = new PacketGameState(Game.player_user_name, 3, Game.lobbyID);
        packetGameState.writeData(Game.socketClient);
        System.out.println(packetGameState.getUsername() + " - " + packetGameState.getLobbyID());
    }


    public Pane createWaitingPanel(Group group) {
        Pane waitingPanel = new Pane();
        waitingPanel.setLayoutX(0);
        waitingPanel.setLayoutY(0);
        waitingPanel.setMinWidth(SCREEN_WIDTH);
        waitingPanel.setMinHeight(SCREEN_HEIGHT);

        try {
            waitingPanel.getStylesheets().add(getClass().getResource("application.css").toURI().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        Button back = UIobjectsAssets.buttonBack.getButton();

        back.setLayoutX(SCREEN_WIDTH / 2 - 300);
        back.setLayoutY(SCREEN_HEIGHT / 2 - 200);
        back.setOnMousePressed(event ->
        {
            sendDisconnect();
            multiplayerState = 0;
            state = State.Menu;
            resetGame();
            group.getChildren().remove(waitingPanel);
        });


        Text label = new Text(0, BLOCK_SIZE * 2, "Please wait while the other players are connecting.");
        label.setFont(Font.font("Verdana", 50));
        label.setX(SCREEN_WIDTH / 2 - label.getBoundsInLocal().getWidth() / 2);

        waitingPanel.getChildren().add(back);
        waitingPanel.getChildren().add(label);

        return waitingPanel;
    }

    public Pane createDeathScreen(Group group) {
        Pane deathscreen_panel = new Pane();

        deathscreen_panel.setLayoutX(0);
        deathscreen_panel.setLayoutY(0);
        deathscreen_panel.setMinWidth(SCREEN_WIDTH);
        deathscreen_panel.setMinHeight(SCREEN_HEIGHT);

        try {
            deathscreen_panel.getStylesheets().add(getClass().getResource("application.css").toURI().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        Button back = UIobjectsAssets.buttonBack.getButton();

        back.setLayoutX(SCREEN_WIDTH / 2 - 300);
        back.setLayoutY(SCREEN_HEIGHT / 2 - 100);
        back.setOnMousePressed(event ->
        {
            Game.state = State.Menu;
            Game.resetGame();
            group.getChildren().remove(deathscreen_panel);
        });


        Image image = Game.getImg.PANEL_LOST(0);
        ImageView imageView = new ImageView(image);

        imageView.setLayoutX(SCREEN_WIDTH / 2 - 400);
        imageView.setLayoutY(SCREEN_HEIGHT / 2 - 300);

        deathscreen_panel.getChildren().add(back);
        deathscreen_panel.getChildren().add(imageView);

        return deathscreen_panel;
    }
}
