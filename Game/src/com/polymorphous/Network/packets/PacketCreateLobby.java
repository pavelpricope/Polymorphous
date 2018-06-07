package com.polymorphous.Network.packets;

import com.polymorphous.Network.GameClient;

/**
 * @author jas555, pxp660
 */
public class PacketCreateLobby extends Packet {

    private String username;
    private String lobbyName;
    private String password;
    private int lobbyId;

    public PacketCreateLobby(byte[] data) {
        super(07);
        String[] dataArray = readData(data).split(",");
        this.username = dataArray[0];                                        //   the username of the player who places the bomb
        this.lobbyName = dataArray[1];
        this.lobbyId = Integer.parseInt(dataArray[2]);

    }

    public PacketCreateLobby(String username, String lobbyName, int id) {
        super(07);
        this.username = username;
        this.lobbyName = lobbyName;
        this.lobbyId = id;
    }

    @Override
    public void writeData(GameClient client) {
        client.sendData(getData());
    }

    @Override
    public byte[] getData() {
        return ("07" + this.username + "," + this.lobbyName + "," + this.lobbyId).getBytes();
    }

    public String getUsername() {
        return username;
    }

    public String getLobbyName() {
        return lobbyName;
    }

    public int getLobbyId() {
        return lobbyId;
    }
}