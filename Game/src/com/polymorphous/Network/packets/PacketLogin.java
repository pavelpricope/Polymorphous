package com.polymorphous.Network.packets;

import com.polymorphous.Network.GameClient;

/**
 * @author jas555, pxp660
 */

public class PacketLogin extends Packet {
    private String username;
    private double x, y;
    private int char_skin;
    private int lobbyID;

    public PacketLogin(byte[] data) {
        super(00);
        String[] dataArray = readData(data).split(",");
        this.username = dataArray[0];
        this.x = Double.parseDouble(dataArray[1]);
        this.y = Double.parseDouble(dataArray[2]);
        this.char_skin = Integer.parseInt(dataArray[3]);
        this.lobbyID = Integer.parseInt(dataArray[4]);
    }

    public PacketLogin(String username, double x, double y, int char_skin, int lobbyID) {
        super(00);
        this.username = username;
        this.x = x;
        this.y = y;
        this.char_skin = char_skin;
        this.lobbyID = lobbyID;
    }

    @Override
    public void writeData(GameClient client) {
        client.sendData(getData());
    }

    @Override
    public byte[] getData() {
        return ("00" + this.username + "," + this.x + "," + this.y + "," + this.char_skin + "," + this.lobbyID).getBytes();
    }

    public String getUsername() {
        return username;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getChar_skin() {
        return char_skin;
    }

    public int getLobbyID() {
        return lobbyID;
    }
}