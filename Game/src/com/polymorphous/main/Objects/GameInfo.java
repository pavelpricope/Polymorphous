package com.polymorphous.main.Objects;

import com.polymorphous.main.Game;
import com.polymorphous.main.Handlers.PlayersHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;


public class GameInfo {

    private int width = 278;
    private int height = 182;
    private double[] x = {0, Game.SCREEN_WIDTH - width, 0, Game.SCREEN_WIDTH - width};
    private double[] y = {0, 0, Game.SCREEN_HEIGHT - height, Game.SCREEN_HEIGHT - height};
    private PlayersHandler handler;

    public GameInfo() {
        handler = Game.getPlayersHandler();
    }


    public void update() {
    }

    public void render(GraphicsContext g) {

        for (int i = 0; i < Game.getPlayersHandler().players.size(); i++) {

            g.drawImage(Game.getImg.PANEL_BG(Game.theme), x[i], y[i]);
            g.drawImage(Game.getImg.CHAR_ICON(Game.char_no), x[i] + 15, y[i] + 15, Game.BLOCK_SIZE, Game.BLOCK_SIZE);

            g.setFont(new Font("Verdana", 30));
            g.fillText(handler.players.get(i).getUsername(), x[i] + Game.BLOCK_SIZE * 1.5, y[i] + Game.BLOCK_SIZE * 0.6);

            for (int j = 0; j < handler.players.get(i).getLives(); j++) {
                g.drawImage(Game.getImg.HEART_ICON(Game.theme), x[i] + Game.BLOCK_SIZE * 1.5 + j * 20, y[i] + Game.BLOCK_SIZE, Game.BLOCK_SIZE / 5, Game.BLOCK_SIZE / 5);
            }

            g.setFont(new Font("Verdana", 15));
            g.drawImage(Game.getImg.SPEED_ICON(Game.theme), x[i] + 15, y[i] + height - 50, Game.BLOCK_SIZE / 3, Game.BLOCK_SIZE / 3);
            g.fillText("X " + (handler.players.get(i).getSpeed() - 3), x[i] + 50, y[i] + height - 32);


            g.drawImage(Game.getImg.MEGA_B_ICON(Game.theme), x[i] + 85, y[i] + height - 50, Game.BLOCK_SIZE / 3, Game.BLOCK_SIZE / 3);
            g.fillText("X " + (handler.players.get(i).getBombSize() - 1), x[i] + 120, y[i] + height - 32);


            g.drawImage(Game.getImg.MULTI_B_ICON(Game.theme), x[i] + 155, y[i] + height - 50, Game.BLOCK_SIZE / 3, Game.BLOCK_SIZE / 3);
            g.fillText("X " + (handler.players.get(i).getMax_bombs() - 1), x[i] + 190, y[i] + height - 32);

            if (handler.players.get(i).getFrozen()) {
                g.drawImage(Game.getImg.FREEZE_ICON(Game.theme), x[i] + 225, y[i] + height - 50, Game.BLOCK_SIZE / 3, Game.BLOCK_SIZE / 3);
            }
        }
    }

}
