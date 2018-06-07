package com.paradroid.util.physics;

import com.paradroid.main.Game;
import javafx.scene.shape.Polygon;

/**
 * @author pxp660
 */
public class IsometricUtil {

    public static Polygon calculateIsometricTile(int x, int y, int width, int height) {
        Polygon polygon = new Polygon();
        //up
        double sx = x + width / 2;
        double sy = y + Game.BLOCK_SIZE / 3 - 3;
        //right
        double sw = x + width;
        double sh = y + height / 2 + 3;
        //down
        double dx = x + width / 2;
        double dy = y + height - Game.BLOCK_SIZE / 5;
        //left
        double dw = x;
        double dh = y + height / 2 + 3;


        polygon.getPoints().addAll(sx, sy, sw, sh, dx, dy, dw, dh);
        return polygon;
    }


}
