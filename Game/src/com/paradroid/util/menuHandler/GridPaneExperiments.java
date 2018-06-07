package com.paradroid.util.menuHandler;

import com.paradroid.util.AchievementHandler.Reader;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class GridPaneExperiments extends Application  {


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("GridPane Experiment");
        Reader r = new Reader();
        r.restoreAchievements("Game/src/com/paradroid/util/AchievementHandler/achievements.txt");
        VBox b = new VBox();
        b.getChildren().add(GridAchievements.grid.initGrid());
        b.setStyle("-fx-background-color: #34ace0");
        Scene scene = new Scene(b, 800, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}