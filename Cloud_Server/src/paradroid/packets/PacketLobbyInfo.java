package paradroid.packets;

import paradroid.Server;

/**
 * @author jas555
 */

public class PacketLobbyInfo extends Packet {

    private String lobbies;

    public PacketLobbyInfo(byte[] data) {
        super(06);
        this.lobbies = readData(data);
    }

    public PacketLobbyInfo(String lobbies) {
        super(06);
        this.lobbies = lobbies;
    }

    @Override
    public void writeData(Server server) {
        server.sendDataToAllClients(getData(), 0);
    }

    @Override
    public byte[] getData() {
        return ("06" + this.lobbies).getBytes();
    }

    public String getUsername() {
        return lobbies;
    }

}