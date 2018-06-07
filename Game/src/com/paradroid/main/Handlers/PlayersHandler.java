package com.paradroid.main.Handlers;

import com.paradroid.main.Player.Player;
import com.paradroid.main.Player.PlayerMP;
import javafx.scene.canvas.GraphicsContext;

import java.util.LinkedList;

/**
 * @author pxp660
 */
public class PlayersHandler {

    public LinkedList<Player> players = new LinkedList<Player>(); // here are stored all the players that are stored currently on the map

    public void update() {

        for (int i = 0; i < players.size(); i++) {
            Player tempPlayer = players.get(i);
            tempPlayer.update();
        }
    }

    public void render(GraphicsContext g) {
        for (int i = 0; i < players.size(); i++) {
            Player tempPlayer = players.get(i);
            tempPlayer.render(g);
        }
    }

    public void addPlayer(Player player) {
        this.players.add(player);
    }


    public void removePlayer(Player player) {
        this.players.remove(player);
    }

    /**
     * This method is called by the game client when one of the players is moving
     *
     * @param username the username of the user that has ti be moved
     * @param x,y      the new coordinates of the player
     */

    public void movePlayer(String username, double x, double y, int direction, boolean isMoveing) {

        for (Player player : this.players) {
            if (player.getUsername().equals(username)) {
                player.setX(x);
                player.setY(y);
                player.setDirection(direction);
                player.setMovement(isMoveing);

            }
        }

    }

    public Player getPlayer(String username) {
        for (Player player : players) {
            if (player.getUsername().equals(username))
                return player;
        }
        System.out.println("Error: Player not found. (PlayerHandler))");
        return null;
    }


}