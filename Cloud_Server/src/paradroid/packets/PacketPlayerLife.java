package paradroid.packets;

import paradroid.Server;

/**
 * @author
 */
public class PacketPlayerLife extends Packet {


    private String username;
    private String username2;
    private int life;
    private int lobbyID;

    public PacketPlayerLife(byte[] data) {
        super(13);
        String[] dataArray = readData(data).split(",");
        this.username = dataArray[0];
        this.username2 = dataArray[1];
        this.life = Integer.parseInt(dataArray[2]);
        this.lobbyID = Integer.parseInt(dataArray[3]);
    }


    @Override
    public void writeData(Server client) {
        client.sendDataToAllClients(getData(), lobbyID);
    }

    @Override
    public byte[] getData() {
        return ("13" + this.username + "," + this.username2 + "," + this.life + "," + this.lobbyID).getBytes();
    }

    public String getUsername() {
        return username;
    }

    public double getX() {
        return life;
    }

    public int getLobbyID() {
        return lobbyID;
    }


}
