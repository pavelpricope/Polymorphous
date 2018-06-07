package paradroid.packets;

import paradroid.Server;

/**
 * @author jas555
 */

public class PacketCreateLobby extends Packet {

    private String username;
    private String lobbyName;
    private int lobbyID;

    public PacketCreateLobby(byte[] data) {
        super(07);
        String[] dataArray = readData(data).split(",");
        this.username = dataArray[0];
        this.lobbyName = dataArray[1];
    }

    public PacketCreateLobby(String username, String lobbyName, int lobbyID) {
        super(07);
        this.username = username;
        this.lobbyName = lobbyName;
        this.lobbyID = lobbyID;
    }

    @Override
    public void writeData(Server server) {
        server.sendDataToAllClients(getData(), lobbyID);
    }

    @Override
    public byte[] getData() {
        return ("07" + this.username + "," + this.lobbyName + "," + this.lobbyID).getBytes();
    }

    public String getUsername() {
        return username;
    }

    public String getLobbyName() {
        return lobbyName;
    }

    public int getLobbyID() {
        return lobbyID;
    }

}