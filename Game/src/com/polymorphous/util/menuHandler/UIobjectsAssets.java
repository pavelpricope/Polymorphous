package com.polymorphous.util.menuHandler;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;


public enum UIobjectsAssets {

    /**
     * Enum class that contains all the images for the buttons
     * @autor jxv603
     */
    //Audio related nodes
    buttonMusicOn("audioOnB.png"),
    buttonMusicOff("audioOffB.png"),
    buttonSoundFxOn("soundFxOnB.png"),
    buttonSoundFxOff("soundFxOffB.png"),

    //Other Buttons
    buttonPlay("playB.png"),
    buttonAchievements("achievementsB.png"),
    buttonBack("backB.png"),
    buttonGameMode("gameModeB.png"),
    buttonResume("resumeGameB.png"),
    buttonMainMenu("mainMenuB.png"),
    buttonQuit("quitB.png"),
    buttonInstructions("instructionsB.png"),
    buttonMultiplayer("multiplayerB.png"),
    buttonSingleplayer("singleplayerB.png"),
    buttonSettings("settingsB.png"),
    buttonJoinLobby("joinLobby.png"),
    buttonCreateLobby("createLobbyB.png"),
    serverTextField,
    usernameTextField;


    Image img;
    public TextField user;

    public static Label getNameLable(){
        Label nameUI = new Label("Username");
        nameUI.setStyle("-fx-font-size:20px;-fx-font-family: \"Verdana\";-fx-text-fill: white;");
        return nameUI;
    }
    public static Label getLobbyLable(){
        Label lobbyUI = new Label("Lobby");
        lobbyUI.setStyle("-fx-font-size:20px;-fx-font-family: \"Verdana\";-fx-text-fill: white;");
        return lobbyUI;
    }


    UIobjectsAssets(String path) {
        img = new Image(new File("Game//src//com//polymorphous//resources//buttons//" + path).toURI().toString());
    }


    UIobjectsAssets() {

    }


    public Button getButton() {
        return new Button("", new ImageView(img));
    }

    public Image getImg() {
        return img;
    }

    TextField getTextField() {
        TextField x = new TextField();
        x.setMaxSize(400, 200);
        return x;
    }


}
