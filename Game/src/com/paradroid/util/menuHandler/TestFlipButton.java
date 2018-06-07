package com.paradroid.util.menuHandler;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TestFlipButton extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("TestFlipButton");
        VBox b = new VBox();
        b.getChildren().add(FlippingButton.testButton.flipPane);
        b.setStyle("-fx-background-color: #34ace0");
        Scene scene = new Scene(b, 800, 100);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
