package com.paradroid.Network;

/**
 * @author jas555
 */
public class Lobby {
    private String name;
    private String id;
    private String players;

    public Lobby(String name, String id, String player) {
        this.name = name;
        this.id = id;
        this.players = player;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getID() { return id; }

    public void setID(String id) {
        this.id = id;
    }

    public String getPlayers() {
        return players;
    }

    public void setPlayers(String players) {
        this.players = players;
    }


}