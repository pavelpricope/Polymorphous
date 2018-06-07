package com.polymorphous.main;


import javafx.scene.image.Image;

import java.util.ArrayList;

public class LoadImage {

    private String path = "com/polymorphous/resources/";
    final ArrayList<ArrayList<Image>> themes = new ArrayList<>();
    final ArrayList<ArrayList<Image>> skins = new ArrayList<>();
    final ArrayList<Image> powerUps = new ArrayList<>();
    final ArrayList<Image> panel = new ArrayList<>();

    private Image background;
    private Image floating_left;
    private Image floating_right;
    private Image floor;
    private Image wall;
    private Image box;
    private Image bomb;
    private Image explosion;


    private Image char_left;
    private Image char_right;
    private Image char_up;
    private Image char_down;
    private Image char_left_g;
    private Image char_right_g;
    private Image char_up_g;
    private Image char_down_g;

    private Image b_p_up;
    private Image bomb_size_p_up;
    private Image speed_p_up;
    private Image life_p_up;
    private Image freeze_p_up;
    private Image lose_life_p_up;
    private Image random_p_up;

    private Image heart_icon;
    private Image freeze_icon;
    private Image mega_b_icon;
    private Image multi_bomb_icon;
    private Image speed_icon;
    private Image panel_bg;
    private Image panel_won;
    private Image panel_lost;
    private Image char_icon;

    public LoadImage() {
        for (int i = 1; i < 4; i++) {
            ArrayList<Image> theme = new ArrayList<>();
            background = new Image(path + "themes/" + i + "/bg.png");
            floor = new Image(path + "themes/" + i + "/floor.png");
            wall = new Image(path + "themes/" + i + "/wall.png");
            box = new Image(path + "themes/" + i + "/box.png");
            floating_left = new Image(path + "themes/" + i + "/floatingBg-Left.png");
            floating_right = new Image(path + "themes/" + i + "/floatingBg-Right.png");
            bomb = new Image(path + "themes/" + i + "/bomb.png");
            explosion = new Image(path + "themes/" + i + "/explosion.gif");
            theme.add(background);
            theme.add(floor);
            theme.add(wall);
            theme.add(box);
            theme.add(bomb);
            theme.add(explosion);
            theme.add(floating_left);
            theme.add(floating_right);
            themes.add(theme);
        }

        for (int i = 1; i < 12; i++) {
            ArrayList<Image> skin = new ArrayList<>();
            char_left = new Image(path + "chars/" + i + "/sl.png");
            char_right = new Image(path + "chars/" + i + "/sr.png");
            char_up = new Image(path + "chars/" + i + "/su.png");
            char_down = new Image(path + "chars/" + i + "/sd.png");
            char_left_g = new Image(path + "chars/" + i + "/l.gif");
            char_right_g = new Image(path + "chars/" + i + "/r.gif");
            char_up_g = new Image(path + "chars/" + i + "/u.gif");
            char_down_g = new Image(path + "chars/" + i + "/d.gif");
            char_icon = new Image(path + "chars/" + i + "/icon.png");

            skin.add(char_left);
            skin.add(char_right);
            skin.add(char_up);
            skin.add(char_down);
            skin.add(char_left_g);
            skin.add(char_right_g);
            skin.add(char_up_g);
            skin.add(char_down_g);
            skin.add(char_icon);
            skins.add(skin);
        }

        b_p_up = new Image(path + "powerups/multiBombPower.png");
        bomb_size_p_up = new Image(path + "powerups/megaBombPower.png");
        speed_p_up = new Image(path + "powerups/speedPower.png");
        life_p_up = new Image(path + "powerups/plusLifePower.png");
        freeze_p_up = new Image(path + "powerups/icePower.png");
        lose_life_p_up = new Image(path + "powerups/minusLifePower.png");
        random_p_up = new Image(path + "powerups/randomPower.png");
        powerUps.add(b_p_up);
        powerUps.add(bomb_size_p_up);
        powerUps.add(speed_p_up);
        powerUps.add(life_p_up);
        powerUps.add(freeze_p_up);
        powerUps.add(lose_life_p_up);
        powerUps.add(random_p_up);

        heart_icon = new Image(path + "panel/heart.png");
        freeze_icon = new Image(path + "panel/freezePowerIcon.png");
        mega_b_icon = new Image(path + "panel/megaBombPowerIcon.png");
        multi_bomb_icon = new Image(path + "panel/multiBombPowerIcon.png");
        speed_icon = new Image(path + "panel/speedPowerIcon.png");
        panel_bg = new Image(path + "panel/panelBg.png");
        panel_won = new Image(path + "panel/youWon.png");
        panel_lost = new Image(path + "panel/youLost.png");
        panel.add(heart_icon);
        panel.add(freeze_icon);
        panel.add(mega_b_icon);
        panel.add(multi_bomb_icon);
        panel.add(speed_icon);
        panel.add(panel_bg);
        panel.add(panel_won);
        panel.add(panel_lost);
    }


    public Image BACKGROUND(int theme_no) {
        return themes.get(theme_no - 1).get(0);
    }

    public Image FLOOR(int theme_no) {
        return themes.get(theme_no - 1).get(1);
    }

    public Image WALL(int theme_no) {
        return themes.get(theme_no - 1).get(2);
    }

    public Image BOX(int theme_no) {
        return themes.get(theme_no - 1).get(3);
    }

    public Image BOMB(int theme_no) {
        return themes.get(theme_no - 1).get(4);
    }

    public Image EXPLOSION(int theme_no) {
        return themes.get(theme_no - 1).get(5);
    }

    public Image FLOATING_LEFT(int theme_no) {
        return themes.get(theme_no - 1).get(6);
    }

    public Image FLOATING_RIGHT(int theme_no) {
        return themes.get(theme_no - 1).get(7);
    }

    public Image CHAR_LEFT(int skin_no) {
        return skins.get(skin_no - 1).get(0);
    }

    public Image CHAR_RIGHT(int skin_no) {
        return skins.get(skin_no - 1).get(1);
    }

    public Image CHAR_UP(int skin_no) {
        return skins.get(skin_no - 1).get(2);
    }

    public Image CHAR_DOWN(int skin_no) {
        return skins.get(skin_no - 1).get(3);
    }


    public Image CHAR_LEFT_G(int skin_no) {
        return skins.get(skin_no - 1).get(4);
    }

    public Image CHAR_RIGHT_G(int skin_no) {
        return skins.get(skin_no - 1).get(5);
    }

    public Image CHAR_UP_G(int skin_no) {
        return skins.get(skin_no - 1).get(6);
    }

    public Image CHAR_DOWN_G(int skin_no) {
        return skins.get(skin_no - 1).get(7);
    }

    public Image CHAR_ICON(int skin_no) {
        return skins.get(skin_no - 1).get(8);
    }


    public Image B_P_UP(int theme_no) {
        return powerUps.get(0);
    }

    public Image BOMB_SIZE_P_UP(int theme_no) {
        return powerUps.get(1);
    }

    public Image SPEED_P_UP(int theme_no) {
        return powerUps.get(2);
    }

    public Image RANDOM_P_UP(int theme_no) {
        return powerUps.get(3);
    }

    public Image LIFE_P_UP(int theme_no) {
        return powerUps.get(4);
    }

    public Image LOSE_LIFE_P_UP(int theme_no) {
        return powerUps.get(5);
    }

    public Image FREEZE_P_UP(int theme_no) {
        return powerUps.get(6);
    }

    public Image HEART_ICON(int theme_no) {
        return panel.get(0);
    }

    public Image FREEZE_ICON(int theme_no) {
        return panel.get(1);
    }

    public Image MEGA_B_ICON(int theme_no) {
        return panel.get(2);
    }

    public Image MULTI_B_ICON(int theme_no) {
        return panel.get(3);
    }

    public Image SPEED_ICON(int theme_no) {
        return panel.get(4);
    }

    public Image PANEL_BG(int theme_no) {
        return panel.get(5);
    }

    public Image PANEL_WON(int theme_no) {
        return panel.get(6);
    }

    public Image PANEL_LOST(int theme_no) {
        return panel.get(7);
    }

}
