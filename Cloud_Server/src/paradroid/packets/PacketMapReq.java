package paradroid.packets;


import paradroid.Server;

/**
 * @author pxp660
 */

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
    public void writeData(Server server) {
        server.sendDataToAllClients(getData(), lobbyID);
    }

    @Override
    public byte[] getData() {
        return (("12" + lobbyID).getBytes());

    }

    public int getLobbyID() {
        return lobbyID;
    }


}
