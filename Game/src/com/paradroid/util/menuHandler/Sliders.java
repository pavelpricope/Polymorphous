package com.paradroid.util.menuHandler;

import com.jfoenix.controls.JFXTabPane;
import com.paradroid.main.Game;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public enum Sliders {
    /**
     * @autor jxv603
     */
    slider;

    JFXTabPane tabPaneChar;
    JFXTabPane tabPaneTheme;

    public Node characterSlider() {
        tabPaneChar = new JFXTabPane();
        tabPaneChar.setMaxWidth(400);
        int counter = 1;
        for (ImageView imageView : Customizations.CHARACTERS.images) {
            Tab tab = new Tab(""+counter);
            imageView.setFitWidth(640);
            imageView.setFitHeight(640);
            tab.setContent(imageView);
            tabPaneChar.getTabs().add(tab);
            counter++;
        }
        return tabPaneChar;
    }

    public Node themeSlider() {
        tabPaneTheme = new JFXTabPane();
        tabPaneTheme.setMaxWidth(896);
        final ImageView[] images = Customizations.THEMES.images;
        final String[] themes = new String[]{"CLASSIC", "GAME OF THRONES", "WESTERN"};
        for (int i = 0; i < images.length; i++) {
            Tab tab = new Tab(themes[i]);
            images[i].setFitWidth(896);
            images[i].setFitHeight(504);
            tab.setContent(images[i]);
            tabPaneTheme.getTabs().add(tab);
        }
        return tabPaneTheme;
    }

    public int setSelectedTheme() {
        int x = tabPaneTheme.getSelectionModel().getSelectedIndex();
        if(Game.themeStatus[x]){
            return x+1;
        }else{
            for (int i= x;i>0;i--){
                if(Game.themeStatus[i]){
                    return i+1;
                }
            }
        }
        return x+1;
    }

    public int setSelectedCharacter() {
        int x = tabPaneChar.getSelectionModel().getSelectedIndex();
        if(Game.characterStatus[x]){
            return x+1;
        }else{
            for (int i= x;i>0;i--){
                if(Game.characterStatus[i]){
                    return i+1;
                }
            }
        }
        return x+1;
    }

    enum Customizations {
        THEMES(3, "theme"), CHARACTERS(12, "");
        private static final String path = "Game//src//com//paradroid//resources//menuSelection//";
        public final ImageView[] images;

        static {
            values();
        }

        Customizations(int n, String fname) {
            images = new ImageView[n];
            for (int i = 0; i < n; i++){
                if(fname.equals("")){
                    if(Game.characterStatus[i]){
                        images[i] = new ImageView(new Image(new File(path + fname + (i + 1) + ".png").toURI().toString()));
                    }else{
                        images[i] = new ImageView(new Image(new File(path + fname + (i + 1) + "l.png").toURI().toString()));
                    }
                }else{
                    if(Game.themeStatus[i]){
                        images[i] = new ImageView(new Image(new File(path + fname + (i + 1) + ".png").toURI().toString()));
                    }else{
                        images[i] = new ImageView(new Image(new File(path + fname + (i + 1) + "l.png").toURI().toString()));
                    }
                }

            }

        }
    }
}
