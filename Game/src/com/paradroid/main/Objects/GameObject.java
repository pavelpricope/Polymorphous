package com.paradroid.main.Objects;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

/**
 * @author pxp660
 */
public abstract class GameObject {


    protected int x, y;
    protected ID id;
    protected int velX, velY;

    /**
     *
     * @param x The x coordinate of the object
     * @param y The y coordinate of the object
     * @param id The id coordinate of the object
     */
    public GameObject(int x, int y, ID id) {
        this.x = x;
        this.y = y;
        this.id = id;

    }

    public abstract void update();

    public abstract void render(GraphicsContext g);

    public abstract Polygon getBounds();

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    public int getVelX() {
        return velX;
    }

    public void setVelX(int velX) {
        this.velX = velX;
    }

    public int getVelY() {
        return velY;
    }

    public void setVelY(int velY) {
        this.velY = velY;
    }
}

