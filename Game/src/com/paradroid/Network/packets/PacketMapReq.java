package com.paradroid.Network.packets;


import com.paradroid.Network.GameClient;

public class PacketMapReq extends Packet {


    private int lobbyID;

    public PacketMapReq(byte[] data) {
        super(12);
        this.lobbyID = Integer.parseInt(readData(data));

    }

    public PacketMapReq(int lobbyID) {
        super(12);
        this.lobbyID = lobbyID;

    }

    @Override
    public void writeData(GameClient client) {
        client.sendData(getData());
    }


    @Override
    public byte[] getData() {
        return (("12" + lobbyID).getBytes());

    }

    public int getLobbyID() {
        return lobbyID;
    }


}
