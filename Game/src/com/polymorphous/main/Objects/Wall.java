package com.polymorphous.main.Objects;

import com.polymorphous.main.Game;
import com.polymorphous.util.physics.IsometricUtil;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Polygon;

/**
 * @author pxp660
 */
public class Wall extends GameObject {

    private int width;
    private int height;
    private Polygon bounds;

    public Wall(int x, int y, ID id, int width, int height) {
        super(x, y, id);
        this.width = width;
        this.height = height;
        bounds = IsometricUtil.calculateIsometricTile(x, y, width, height);
    }

    @Override
    public void update() {

    }

    @Override
    public void render(GraphicsContext g) {

        g.drawImage(Game.getImg.WALL(Game.theme), x, y, width, height);

    }

    @Override
    public Polygon getBounds() {

        return bounds;
    }

}

