package com.paradroid.util.audioHandler;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class testAudio extends Application {
    /**
     * @author jxv603
     */

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        //Initialization of AudioManager class
        AudioManager m = new AudioManager();

        //Buttons declaration creation
        Button btn_music1, btn_music2, btn_music3, btn_music4, btn_efx, btn_efx2, btn_stop;
        btn_music1 = new Button("StartMusic getRandomTrack");
        btn_music2 = new Button("StartMusic getRandomGeneralTrack");
        btn_music3 = new Button("StartMusic getRandomWesternTrack");
        btn_music4 = new Button("StartMusic getRandomGameOfThronesTrack");
        btn_efx = new Button("Fx");
        btn_efx2 = new Button("Fx2");
        btn_stop = new Button("StopMusic");

        //Buttons Handler
        btn_music1.setOnAction(arg0 -> {
            m.stopMusic();
            m.playMusic(m.music.getRandomTrack(), 100);
        });
        btn_music2.setOnAction(arg0 -> {
            m.stopMusic();
            m.playMusic(m.music.getRandomGeneralTrack(), 100);
        });
        btn_music3.setOnAction(arg0 -> {
            m.stopMusic();
            m.playMusic(m.music.getRandomWesternTrack(), 100);
        });
        btn_music4.setOnAction(arg0 -> {
            m.stopMusic();
            m.playMusic(m.music.getRandomGoTTrack(), 100);
        });
        btn_efx.setOnAction(handle -> m.playSFX(m.sfx.getRandomExplosionSound()));
        btn_efx2.setOnAction(arg0 -> m.playSFX(m.sfx.click));
        btn_stop.setOnAction(arg0 -> m.stopMusic());

        //Stuff just to create a little "player"
        VBox root = new VBox();
        root.setStyle("-fx-background-color: #34ace0; -fx-alignment: center ;");
        Label l = new Label("In order to do this test you need to click each \nbutton and list if the name of the button is correct with its action");
        root.getChildren().addAll(l,btn_music1,btn_music2,btn_music3,btn_music4, btn_efx, btn_efx2, btn_stop);
        Scene scene = new Scene(root, 500, 400);
        stage.setScene(scene);
        stage.show();
    }


}
