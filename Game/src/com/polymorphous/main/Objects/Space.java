package com.polymorphous.main.Objects;

import com.polymorphous.main.Game;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Polygon;

/**
 * @author pxp660
 */
public class Space extends GameObject {

    private int width = Game.BLOCK_SIZE;
    private int height = Game.BLOCK_SIZE;

    private Polygon bounds;

    public Space(int x, int y, ID id) {
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
    }

    @Override
    public void render(GraphicsContext g) {
    }


    @Override
    public Polygon getBounds() {
        return bounds;
    }
}
