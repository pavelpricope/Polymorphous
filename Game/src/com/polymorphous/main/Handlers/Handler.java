package com.polymorphous.main.Handlers;

import com.polymorphous.main.Game;
import com.polymorphous.main.Objects.Bomb;
import com.polymorphous.main.Objects.GameObject;
import com.polymorphous.main.Objects.ID;
import com.polymorphous.main.PowerUp.*;
import javafx.scene.canvas.GraphicsContext;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author pxp660
 */
public class Handler {


    public LinkedList<GameObject> object = new LinkedList<GameObject>();   // here are stored all the static objects rendered on the map

    public void update() {

        for (int i = 0; i < object.size(); i++) {
            GameObject tempObject = object.get(i);
            tempObject.update();
        }
    }

    public void render(GraphicsContext g) {
        for (int i = 0; i < object.size(); i++) {
            GameObject tempObject = object.get(i);
            tempObject.render(g);
        }
    }

    public void addObject(GameObject object) {
        this.object.add(object);
    }

    public void removeObject(GameObject object) {
        this.object.remove(object);
    }

    // this method is used by the game client when a new bomb is placed by one of the players
    public void placeBomb(String username, double x, double y) {

        object.add(new Bomb((int) Math.round(x), (int) Math.round(y), ID.Bomb, this, Game.getPlayersHandler().getPlayer(username), 170, 1, false));
    }

    public void placePowerUp(int x, int y, int ability) {

        switch (ability) {
            case 1:
                addObject(new PowerUpSpeed(x, y, ID.PowerUp));
                break;
            case 2:
                addObject(new PowerUpBomb(x, y, ID.PowerUp));
                break;
            case 3:
                addObject(new PowerUpBombSize(x, y, ID.PowerUp));
                break;
            case 4:
                addObject(new PowerUpLife(x, y, ID.PowerUp));
                break;
            case 5:
                addObject(new PowerUpRandom(x, y, ID.PowerUp));
                break;
            case 6:
                addObject(new PowerUpFreeze(x, y, ID.PowerUp));
                break;
            case 7:
                addObject(new PowerUpLoseLife(x, y, ID.PowerUp));
                break;
        }
    }


}
