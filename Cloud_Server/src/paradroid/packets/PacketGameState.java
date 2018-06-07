package paradroid.packets;

import paradroid.Server;

/**
 * @author pxp660
 */
public class PacketGameState extends Packet {

    private String username;
    private int state;
    private int lobbyID;

    public PacketGameState(byte[] data) {
        super(11);
        String[] dataArray = readData(data).split(",");
        this.username = dataArray[0];                           //lobby admin
        this.state = Integer.parseInt(dataArray[1]);
        this.lobbyID = Integer.parseInt(dataArray[2]);
    }

    public PacketGameState(String username, int state, int lobbyID) {
        super(11);
        this.username = username;
        this.state = state;
        this.lobbyID = lobbyID;
    }

    @Override
    public void writeData(Server server) {
        server.sendDataToAllClients(getData(), lobbyID);
    }

    @Override
    public byte[] getData() {
        return ("11" + this.username + "," + this.state + "," + this.lobbyID).getBytes();
    }

    public String getUsername() {
        return username;
    }

    public int getState() {
        return state;
    }

    public int getLobbyID() {
        return lobbyID;
    }

}
