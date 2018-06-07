package com.polymorphous.main.Objects;

import com.polymorphous.main.Game;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Polygon;

/**
 * @author pxp660
 */
public class Explosion extends GameObject {


    private String player;
    private int direction;
    private int time;
    private int width = Game.BLOCK_SIZE;
    private int height = Game.BLOCK_SIZE;
    private int size;
    private Polygon bounds;
    //up
    private double sx = x + width / 4;
    private double sy = y + Game.BLOCK_SIZE / 8;
    //right
    private double sw = x + width / 2;
    private double sh = y + height / 4;
    //down
    private double dx = x + width / 4;
    private double dy = y + height / 2 - Game.BLOCK_SIZE / 8;
    //left
    private double dw = x;
    private double dh = y + height / 4;

    public Explosion(int x, int y, ID id, String player, int direction, int size, int time) {
        super(x, y, id);
        this.direction = direction;
        this.player = player;
        this.size = size;
        this.time = time;
        calculateExplosionSize();
        bounds = new Polygon();
        bounds.getPoints().addAll(sx, sy, sw, sh, dx, dy, dw, dh);
    }


    @Override
    public void update() {
        if (time == 0)
            Game.getHandler().removeObject(this);
        else
            time--;
    }

    @Override
    public void render(GraphicsContext g) {

        double[] xPoints = {sx, sw, dx, dw};
        double[] yPoints = {sy, sh, dy, dh};

        g.setFill(new ImagePattern(Game.getImg.EXPLOSION(Game.theme), 0, 0, 1, 1, true));
        g.fillPolygon(xPoints, yPoints, 4);

    }

    @Override
    public Polygon getBounds() {

        return bounds;

    }

    private void calculateExplosionSize() {
        int size_ = 0;
        while (size_ < size) {
            switch (direction) {

                case 0:

                    GameObject left = Game.map.getTileAt((int) Math.round(sx - Game.BLOCK_SIZE / 2), (int) Math.round(sy - Game.BLOCK_SIZE / 4));
                    if (left == null) {

                        sx -= Game.BLOCK_SIZE / 2;
                        sy -= Game.BLOCK_SIZE / 4;
                        dw -= Game.BLOCK_SIZE / 2;
                        dh -= Game.BLOCK_SIZE / 4;
                    } else if (left instanceof Box) {

                        sx -= Game.BLOCK_SIZE / 2;
                        sy -= Game.BLOCK_SIZE / 4;
                        dw -= Game.BLOCK_SIZE / 2;
                        dh -= Game.BLOCK_SIZE / 4;
                        size_ = size;
                    }
                    break;
                case 1:
                    GameObject right = Game.map.getTileAt((int) Math.round(sw + Game.BLOCK_SIZE / 2), (int) Math.round(sh + Game.BLOCK_SIZE / 4));
                    if (right == null) {

                        sw += Game.BLOCK_SIZE / 2;
                        sh += Game.BLOCK_SIZE / 4;
                        dx += Game.BLOCK_SIZE / 2;
                        dy += Game.BLOCK_SIZE / 4;
                    } else if (right instanceof Box) {

                        sw += Game.BLOCK_SIZE / 2;
                        sh += Game.BLOCK_SIZE / 4;
                        dx += Game.BLOCK_SIZE / 2;
                        dy += Game.BLOCK_SIZE / 4;
                        size_ = size;
                    }

                    break;
                case 2:
                    GameObject up = Game.map.getTileAt((int) Math.round(sx + Game.BLOCK_SIZE / 2), (int) Math.round(sy - Game.BLOCK_SIZE / 4));
                    if (up == null) {

                        sx += Game.BLOCK_SIZE / 2;
                        sy -= Game.BLOCK_SIZE / 4;
                        sw += Game.BLOCK_SIZE / 2;
                        sh -= Game.BLOCK_SIZE / 4;

                    } else if (up instanceof Box) {

                        sx += Game.BLOCK_SIZE / 2;
                        sy -= Game.BLOCK_SIZE / 4;
                        sw += Game.BLOCK_SIZE / 2;
                        sh -= Game.BLOCK_SIZE / 4;
                        size_ = size;
                    }

                    break;
                case 3:
                    GameObject bottom = Game.map.getTileAt((int) Math.round(dw - Game.BLOCK_SIZE / 2), (int) Math.round(dh + Game.BLOCK_SIZE / 4));
                    if (bottom == null) {
                        dw -= Game.BLOCK_SIZE / 2;
                        dh += Game.BLOCK_SIZE / 4;
                        dx -= Game.BLOCK_SIZE / 2;
                        dy += Game.BLOCK_SIZE / 4;
                    } else if (bottom instanceof Box) {

                        dw -= Game.BLOCK_SIZE / 2;
                        dh += Game.BLOCK_SIZE / 4;
                        dx -= Game.BLOCK_SIZE / 2;
                        dy += Game.BLOCK_SIZE / 4;
                        size_ = size;
                    }
                    break;
            }
            size_++;

        }

    }


}
