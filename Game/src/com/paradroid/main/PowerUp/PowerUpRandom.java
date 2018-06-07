package com.paradroid.main.PowerUp;

import com.paradroid.main.Game;
import com.paradroid.main.Objects.GameObject;
import com.paradroid.main.Objects.ID;
import com.paradroid.main.Player.Player;
import com.paradroid.util.AchievementHandler.Reader;
import com.paradroid.util.physics.Collision;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Polygon;

import java.util.Random;

/**
 * @author pxp660
 */

public class PowerUpRandom extends GameObject {

    int width = Game.BLOCK_SIZE;
    int height = Game.BLOCK_SIZE;
    private Polygon bounds;

    public PowerUpRandom(int x, int y, ID id) {
        super(x, y, id);//up
        double sx = x + width / 2;
        double sy = y + Game.BLOCK_SIZE / 3;
        //right
        double sw = x + width;
        double sh = y + height / 2 + 3;
        //down
        double dx = x + width / 2;
        double dy = y + height - Game.BLOCK_SIZE / 5;
        //left
        double dw = x;
        double dh = y + height / 2 + 3;

        bounds = new Polygon();
        bounds.getPoints().addAll(sx, sy, sw, sh, dx, dy, dw, dh);
    }

    @Override
    public void update() {

        collide();
    }


    @Override
    public void render(GraphicsContext g) {
        g.drawImage(Game.getImg.RANDOM_P_UP(0), x, y, width, height);
    }

    @Override
    public Polygon getBounds() {

        return bounds;
    }

    private void collide() {

        for (Player player : Game.getPlayersHandler().players) {
            if (Collision.checkPolygonsCollision(getBounds(), player.getBounds())) {
                Game.audioManager.sfx.randomic.play();
                applyRandomPowerUp(player);
                Reader.increasePowerUpAchivement();
                Game.getHandler().removeObject(this);
                break;
            }
        }
    }

    private void applyRandomPowerUp(Player player) {
        int[] powerUp = {0, 0, 0, 0, 0, 1, 2, 3, 4, 5, 6};

        Random rand = new Random();
        int n = rand.nextInt(powerUp.length) + 0;

        switch (powerUp[n]) {

            case 1:
                player.increaseBombNo();
                break;
            case 2:
                player.increaseBombSize();
                break;
            case 3:
                freezePlayers(player);
                break;
            case 4:
                player.loseLife();
                break;
            case 5:
                player.increaseSpeed();
        }
    }

    private void freezePlayers(Player nplayer) {

        for (Player player : Game.getPlayersHandler().players) {
            if (player != nplayer) {
                player.freeze();
                Game.getHandler().removeObject(this);
                break;
            }
        }
    }

}
