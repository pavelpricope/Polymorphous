package com.polymorphous.util.menuHandler;

import com.polymorphous.Network.packets.PacketCreateLobby;
import com.polymorphous.Network.packets.PacketLobbyInfo;
import com.polymorphous.main.Game;
import com.polymorphous.main.State;
import com.polymorphous.util.AchievementHandler.Reader;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public enum MenuPages {
    /**
     * @author jxv603
     * This enum contains all the menu pages
     */

    MAIN_MENU(new Node[]{
            new ImageView(new Image(new File("Game//src//com//polymorphous//resources//logo.png").toURI().toString())),
            UIobjectsAssets.buttonPlay.getButton(),
            UIobjectsAssets.buttonAchievements.getButton(),
            UIobjectsAssets.buttonInstructions.getButton(),
            UIobjectsAssets.buttonSettings.getButton(),
            UIobjectsAssets.buttonQuit.getButton(),
    }, new Runnable[]{null, null, () -> updateAchievement(), null, null, () -> {
        Platform.exit();
        Reader.backupAchievements("Game/src/com/polymorphous/util/AchievementHandler/achievements.txt");
        System.exit(0);
    }}, true),

    PREGAME_MENU(new Node[]{
            Sliders.slider.themeSlider(),
            Sliders.slider.characterSlider(),
            UIobjectsAssets.buttonPlay.getButton(),
            UIobjectsAssets.buttonBack.getButton()
    //}, new Runnable[]{null, null, () -> applyThemeAndChar(), null}, false),
    }, new Runnable[]{null, null, () -> applyThemeAndChar(), null}, false),
    PLAY_MENU(new Button[]{
            UIobjectsAssets.buttonSingleplayer.getButton(),
            UIobjectsAssets.buttonMultiplayer.getButton(),
            UIobjectsAssets.buttonBack.getButton(),
    }, new Runnable[]{
            () -> {
                Game.state = State.Singleplayer;
            }, () -> {
        sendLobbyRequest();
    }, null}, true),
    INSTRUCTIONS_MENU(new Node[]{
            new ImageView(new Image(new File("Game//src//com//polymorphous//util//menuHandler//Instructions.png").toURI().toString())),
            UIobjectsAssets.buttonBack.getButton(),
    }, new Runnable[]{null, null}, false),
    ACHIEVEMENTS_MENU(new Node[]{
            Game.achiPane,
            UIobjectsAssets.buttonBack.getButton(),
    }, new Runnable[]{null, null}, false),
    SETTINGS_MENU(new Node[]{
            FlippingButton.musicButton.flipPane,
            FlippingButton.fxButton.flipPane,
            UIobjectsAssets.buttonBack.getButton(),
    }, new Runnable[]{null, null, null}, false),
    LOBBY_MENU(new Node[]{
            Game.tableView,
            UIobjectsAssets.getNameLable(),
            user = UIobjectsAssets.usernameTextField.getTextField(),
            UIobjectsAssets.getLobbyLable(),
            lobby_name = UIobjectsAssets.serverTextField.getTextField(),
            UIobjectsAssets.buttonCreateLobby.getButton(),
            UIobjectsAssets.buttonJoinLobby.getButton(),
            UIobjectsAssets.buttonBack.getButton(),
    }, new Runnable[]{null,null,null, null, null, () -> {
        createLobby();
    }, () -> {
        joinLobby();
    }, null}, false);

    static TextField user;
    static TextField lobby_name;

    static {
        values();
        MAIN_MENU.menus = new MenuPages[]{null, PREGAME_MENU, ACHIEVEMENTS_MENU, INSTRUCTIONS_MENU, SETTINGS_MENU, null};
        PLAY_MENU.menus = new MenuPages[]{null, LOBBY_MENU, PREGAME_MENU};
        ACHIEVEMENTS_MENU.menus = new MenuPages[]{null, MAIN_MENU};
        SETTINGS_MENU.menus = new MenuPages[]{null, null, MAIN_MENU};
        INSTRUCTIONS_MENU.menus = new MenuPages[]{null, MAIN_MENU};
        LOBBY_MENU.menus = new MenuPages[]{null, null,null,null, null, null, null, PLAY_MENU};
        PREGAME_MENU.menus = new MenuPages[]{null, null, PLAY_MENU, MAIN_MENU};

        for (MenuPages menu : values()) {
            if (menu.nodes.length != menu.actions.length)
                System.err.println("Length in menu actions differs for " + menu);
            if (menu.nodes.length != menu.menus.length)
                System.err.println("Length in menu segues differs for " + menu);
        }
    }

    final Node[] nodes;
    final Runnable[] actions;
    final boolean animation;
    MenuPages[] menus;

    MenuPages(Node[] nodes, Runnable[] actions, boolean animation) {
        this.nodes = nodes;
        this.actions = actions;
        this.animation = animation;
    }


    static void updateAchievement() {
        Game.achiPane.getChildren().clear();
        Game.achiPane.getChildren().add(GridAchievements.grid.refresh());
    }

    static void sendLobbyRequest() {
        PacketLobbyInfo packetLobbyInfo = new PacketLobbyInfo(Game.player_user_name);
        packetLobbyInfo.writeData(Game.socketClient);
    }

    private static void applyThemeAndChar() {
        Game.theme = Sliders.slider.setSelectedTheme();
        Game.char_no = Sliders.slider.setSelectedCharacter();
    }

    static void joinLobby() {
        Game.player_user_name = user.getText();
        Game.setLobbyID();
        Game.state = State.Multiplayer;
    }

    static void createLobby() {
        Game.player_user_name = user.getText();
        PacketCreateLobby packetCreateLobby = new PacketCreateLobby(user.getText(), lobby_name.getText(), 1);
        packetCreateLobby.writeData(Game.socketClient);
        Game.state = State.Multiplayer;

    }
}
