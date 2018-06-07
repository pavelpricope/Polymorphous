package com.polymorphous.Network.packets;

import com.polymorphous.Network.GameClient;

public class PacketPlayerLife extends Packet {


    private String username; // the player to killed a player
    private String username2; // the player who lost a life
    private int life;
    private int lobbyID;

    public PacketPlayerLife(byte[] data) {
        super(00);
        String[] dataArray = readData(data).split(",");
        this.username = dataArray[0];
        this.username2 = dataArray[1];
        this.life = Integer.parseInt(dataArray[2]);
        this.lobbyID = Integer.parseInt(dataArray[3]);
    }

    public PacketPlayerLife(String username, String username2, int life, int lobbyID) {
        super(13);
        this.username = username;
        this.username2 = username2;
        this.life = life;
        this.lobbyID = lobbyID;
    }

    @Override
    public void writeData(GameClient client) {
        client.sendData(getData());
    }

    @Override
    public byte[] getData() {
        return ("13" + this.username + "," + this.username2 + "," + this.life + "," + this.lobbyID).getBytes();
    }

    public String getUsername() {
        return username;
    }

    public String getUsername2() {
        return username2;
    }

    public double getX() {
        return life;
    }

    public int getLobbyID() {
        return lobbyID;
    }

}
