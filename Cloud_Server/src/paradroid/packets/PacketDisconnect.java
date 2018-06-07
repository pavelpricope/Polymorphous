package paradroid.packets;

import paradroid.Server;

/**
 * @author jas555, pxp660
 */

public class PacketDisconnect extends Packet {

    private String username;
    private int lobbyID;

    public PacketDisconnect(byte[] data) {
        super(01);
        String[] dataArray = readData(data).split(",");
        this.username = dataArray[0];
        this.lobbyID = Integer.parseInt(dataArray[1]);
    }

    public PacketDisconnect(String username, int lobbyID) {
        super(01);
        this.username = username;
        this.lobbyID = lobbyID;
    }

    @Override
    public void writeData(Server server) {
        server.sendDataToAllClients(getData(), lobbyID);
    }

    @Override
    public byte[] getData() {
        return ("01" + this.username + "," + this.lobbyID).getBytes();
    }

    public String getUsername() {
        return username;
    }

    public int getLobbyID() {
        return lobbyID;
    }
}