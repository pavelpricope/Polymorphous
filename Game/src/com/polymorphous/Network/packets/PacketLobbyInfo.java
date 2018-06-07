package com.polymorphous.Network.packets;


import com.polymorphous.Network.GameClient;

/**
 * @author jas555, pxp660
 */
public class PacketLobbyInfo extends Packet {

    private String lobbies;

    public PacketLobbyInfo(byte[] data) {
        super(06);
        this.lobbies = readData(data);
    }

    public PacketLobbyInfo(String username) {
        super(06);
        this.lobbies = username;
    }

    @Override
    public void writeData(GameClient client) {
        client.sendData(getData());
    }

    @Override
    public byte[] getData() {
        return ("06" + this.lobbies).getBytes();
    }

    public String getLobbies() {
        return lobbies;
    }

}