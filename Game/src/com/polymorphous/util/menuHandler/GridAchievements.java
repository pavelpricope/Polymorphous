package com.polymorphous.util.menuHandler;

import com.polymorphous.util.AchievementHandler.AchievementObj;
import com.polymorphous.util.AchievementHandler.Reader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.util.ArrayList;

public enum GridAchievements {
    grid;

    public VBox initGrid() {
        VBox test = new VBox();
        test.getChildren().add(refresh());
        return test;
    }

    public GridPane refresh() {
        GridPane gridPane = new GridPane();
        int counter = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 5; j++) {
                gridPane.add(prepareAchievement(Reader.listOfAchievements, counter), j, i, 1, 1);
                counter++;
            }
        }
        gridPane.setHgap(20);
        gridPane.setVgap(20);
        gridPane.setAlignment(Pos.CENTER);
        return gridPane;
    }

    private Pane prepareAchievement(ArrayList<AchievementObj> listOfAchievements, int counter) {
        VBox result = new VBox();

        ImageView achievementIcon;
        if (listOfAchievements.get(counter).total - listOfAchievements.get(counter).score <= 0) {
            achievementIcon = new ImageView(new Image(new File("Game//src//com//polymorphous//resources//menuSelection/unlockedTrophy.png").toURI().toString()));
        } else {
            achievementIcon = new ImageView(new Image(new File("Game//src//com//polymorphous//resources//menuSelection/lockedTrophy.png").toURI().toString()));
        }
        achievementIcon.setFitWidth(120);
        achievementIcon.setFitHeight(145);
        result.getChildren().add(achievementIcon);


        String achievementName= listOfAchievements.get(counter).name;
        String descriptionAchievement = "";
        String progress="";
        String whatDoYouUnlock="";
        switch(achievementName){
            case "Learner":
                descriptionAchievement = "Drop "+listOfAchievements.get(counter).total+" Bombs";
                progress= listOfAchievements.get(counter).score+" / "+listOfAchievements.get(counter).total;
                whatDoYouUnlock="Nothing";
                break;
            case "Bomberbaby":
                descriptionAchievement = "Drop "+listOfAchievements.get(counter).total+" Bombs";
                progress= listOfAchievements.get(counter).score+" / "+listOfAchievements.get(counter).total;
                whatDoYouUnlock="Nothing";
                break;
            case "Bomberman":
                descriptionAchievement = "Drop "+listOfAchievements.get(counter).total+" Bombs";
                progress= listOfAchievements.get(counter).score+" / "+listOfAchievements.get(counter).total;
                whatDoYouUnlock="Nothing";
                break;
            case "Pentakill":
                descriptionAchievement = "Kill "+listOfAchievements.get(counter).total+" Bots";
                progress= listOfAchievements.get(counter).score+" / "+listOfAchievements.get(counter).total;
                whatDoYouUnlock="Nothing";
                break;
            case "Killing spree":
                descriptionAchievement = "Kill "+listOfAchievements.get(counter).total+" Bots";
                progress= listOfAchievements.get(counter).score+" / "+listOfAchievements.get(counter).total;
                whatDoYouUnlock="Nothing";
                break;
            case "Assassin":
                descriptionAchievement = "Kill "+listOfAchievements.get(counter).total+" Bots";
                progress= listOfAchievements.get(counter).score+" / "+listOfAchievements.get(counter).total;
                whatDoYouUnlock="Nothing";
                break;
            case "Kamikaze":
                descriptionAchievement = "Kill yourself "+listOfAchievements.get(counter).total+" Times";
                progress= listOfAchievements.get(counter).score+" / "+listOfAchievements.get(counter).total;
                whatDoYouUnlock="Nothing";
                break;
            case "Boot Camp":
                descriptionAchievement = "Pick "+listOfAchievements.get(counter).total+" PowerUps";
                progress= listOfAchievements.get(counter).score+" / "+listOfAchievements.get(counter).total;
                whatDoYouUnlock="Nothing";
                break;
            case "Sonic":
                descriptionAchievement = "Pick "+listOfAchievements.get(counter).total+" PowerUps";
                progress= listOfAchievements.get(counter).score+" / "+listOfAchievements.get(counter).total;
                whatDoYouUnlock="Nothing";
                break;
            case "Super Saiyan":
                descriptionAchievement = "Pick "+listOfAchievements.get(counter).total+" PowerUps";
                progress= listOfAchievements.get(counter).score+" / "+listOfAchievements.get(counter).total;
                whatDoYouUnlock="Nothing";
                break;
            case "DecaKill":
                descriptionAchievement = "Win "+listOfAchievements.get(counter).total+" Multiplayer Games";
                progress= listOfAchievements.get(counter).score+" / "+listOfAchievements.get(counter).total;
                whatDoYouUnlock="Nothing";
                break;
            case "Domination":


                descriptionAchievement = "Win "+listOfAchievements.get(counter).total+" Multiplayer Games";
                progress= listOfAchievements.get(counter).score+" / "+listOfAchievements.get(counter).total;
                whatDoYouUnlock="Nothing";
                break;
            case "Legendary":
                descriptionAchievement = "Win "+listOfAchievements.get(counter).total+" Multiplayer Games";
                progress= listOfAchievements.get(counter).score+" / "+listOfAchievements.get(counter).total;
                whatDoYouUnlock="Nothing";
                break;
            case "Private":
                descriptionAchievement = "Win "+listOfAchievements.get(counter).total+" Singleplayer Games";
                progress= listOfAchievements.get(counter).score+" / "+listOfAchievements.get(counter).total;
                whatDoYouUnlock="Nothing";
                break;
            case "General":
                descriptionAchievement = "Win "+listOfAchievements.get(counter).total+" Singleplayer Games";
                progress= listOfAchievements.get(counter).score+" / "+listOfAchievements.get(counter).total;
                whatDoYouUnlock="Nothing";
                break;



        }
        Label labelAchievementName = new Label(achievementName);
        labelAchievementName.setStyle("-fx-font-size:20px;-fx-font-family: \"Verdana\";-fx-text-fill: white;");
        result.getChildren().add(labelAchievementName);

        Label labelDescriptionAchievement=new Label(descriptionAchievement);
        labelDescriptionAchievement.setStyle("-fx-font-size:15px;-fx-font-family: \"Verdana\";-fx-text-fill: white;");
        result.getChildren().add(labelDescriptionAchievement);

        Label labelProgressAchievement=new Label(progress);
        labelProgressAchievement.setStyle("-fx-font-size:15px;-fx-font-family: \"Verdana\";-fx-text-fill: white;");
        result.getChildren().add(labelProgressAchievement);

        Label labelWhatDoYouUnlockAchievement=new Label(whatDoYouUnlock);
        labelWhatDoYouUnlockAchievement.setStyle("-fx-font-size:15px;-fx-font-family: \"Verdana\";-fx-text-fill: white;");
        result.getChildren().add(labelWhatDoYouUnlockAchievement);

        result.setAlignment(Pos.CENTER);
        return result;
    }

}
