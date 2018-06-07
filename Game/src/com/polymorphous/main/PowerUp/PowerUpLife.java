package com.polymorphous.main.PowerUp;

import com.polymorphous.main.Game;
import com.polymorphous.main.Objects.GameObject;
import com.polymorphous.main.Objects.ID;
import com.polymorphous.main.Player.Player;
import com.polymorphous.util.AchievementHandler.Reader;
import com.polymorphous.util.physics.Collision;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Polygon;

/**
 * @author pxp660
 */

public class PowerUpLife extends GameObject {

    int width = Game.BLOCK_SIZE;
    int height = Game.BLOCK_SIZE;
    private Polygon bounds;

    public PowerUpLife(int x, int y, ID id) {
        super(x, y, id);
        //up
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
        g.drawImage(Game.getImg.LIFE_P_UP(0), x, y, width, height);
    }

    @Override
    public Polygon getBounds() {
        return bounds;
    }

    private void collide() {

        for (Player player : Game.getPlayersHandler().players) {
            if (Collision.checkPolygonsCollision(getBounds(), player.getBounds())) {
                player.gainLife();
                Game.audioManager.sfx.life.play();
                Reader.increasePowerUpAchivement();
                Game.getHandler().removeObject(this);
                break;
            }
        }
    }
}
