package com.polymorphous.main.Player;

import com.polymorphous.main.Objects.ID;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

/**
 * @author pxp660
 */
public abstract class Mob {


    protected double x, y;
    protected ID id;
    protected double velX, velY;

    public Mob(double x, double y, ID id) {
        this.x = x;
        this.y = y;
        this.id = id;

    }

    public abstract void update();

    public abstract void render(GraphicsContext g);

    public abstract Polygon getBounds();

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    public double getVelX() {
        return velX;
    }

    public void setVelX(double velX) {
        this.velX = velX;
    }

    public double getVelY() {
        return velY;
    }

    public void setVelY(double velY) {
        this.velY = velY;
    }
}

