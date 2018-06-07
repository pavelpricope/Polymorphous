package com.paradroid.ai.util;

import com.paradroid.main.Game;
import com.paradroid.main.Objects.GameObject;
import com.paradroid.main.Objects.ID;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

/**
 * @author dxf209
 *
 */
public class Internals extends GameObject{

	private Color colour;
	private Polygon bounds;
	private int width, height;
	
	
	/**
	 * Game Object tile used for debugging the AI system
	 * 
	 * @param x			The x position of the tile
	 * @param y			The y position of the tile
	 * @param id		The game object id
	 * @param colour	The tile colour
	 */
	public Internals(int x, int y, ID id, Color colour) {
		super(x, y, id);
		this.colour = colour;
        this.width = Game.BLOCK_SIZE;
        this.height = Game.BLOCK_SIZE;
        //up
        double sx = x + width / 2;
        double sy = y + Game.BLOCK_SIZE / 3-3;
        //right
        double sw = x + width;
        double sh = y + height / 2 + 3;
        //down
        double dx = x + width / 2;
        double dy = y + height - Game.BLOCK_SIZE / 5;
        //left
        double dw = x;
        double dh = y + height / 2 + 3;

//        double sx = x + width / 2;
//        double sy = y + Game.BLOCK_SIZE / 8;
//        //right
//        double sw = x + width;
//        double sh = y + height / 2 + 1;
//        //down
//        double dx = x + width / 2;
//        double dy = y + height - Game.BLOCK_SIZE / 10;
//        //left
//        double dw = x;
//        double dh = y + height / 2 + 1;
        
        bounds = new Polygon();
        bounds.getPoints().addAll(sx, sy, sw, sh, dx, dy, dw, dh);


    }

    @Override
    public void update() {}

    @Override
    public void render(GraphicsContext g) {

        double[] xPoints = {bounds.getPoints().get(0), bounds.getPoints().get(2), bounds.getPoints().get(4), bounds.getPoints().get(6)};
        double[] yPoints = {bounds.getPoints().get(1), bounds.getPoints().get(3), bounds.getPoints().get(5), bounds.getPoints().get(7)};
        g.setFill(colour);
        g.fillPolygon(xPoints, yPoints, 4);

    }

    @Override
    public Polygon getBounds() {
        return bounds;
    }

}
