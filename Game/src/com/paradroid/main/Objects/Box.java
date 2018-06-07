package com.paradroid.main.Objects;

import com.paradroid.main.Game;
import com.paradroid.util.physics.IsometricUtil;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Polygon;

/**
 * @author pxp660
 */
public class Box extends GameObject {

    private int width = Game.BLOCK_SIZE;
    private int height = Game.BLOCK_SIZE;

    private Polygon bounds;

    public Box(int x, int y, ID id) {
        super(x, y, id);
        bounds = IsometricUtil.calculateIsometricTile(x, y, width, height);
    }

    @Override
    public void update() {
    }

    @Override
    public void render(GraphicsContext g) {

        g.drawImage(Game.getImg.BOX(Game.theme), x, y, width, height);
    }


    @Override
    public Polygon getBounds() {

        return bounds;
    }
}
