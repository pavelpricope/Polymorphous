package paradroid.packets;


import paradroid.Server;

/**
 * @author jas555, pxp660
 */

public class PacketBomb extends Packet {

    private String username;
    private double x, y;
    private int lobbyID;


    public PacketBomb(byte[] data) {
        super(03);
        String[] dataArray = readData(data).split(",");
        this.username = dataArray[0];                                        //   the username of the player who places the bomb
        this.x = Double.parseDouble(dataArray[1]);
        this.y = Double.parseDouble(dataArray[2]);
        this.lobbyID = Integer.parseInt(dataArray[3]);
    }

    public PacketBomb(String username, double x, double y) {
        super(03);
        this.username = username;
        this.x = x;
        this.y = y;

    }

    @Override
    public void writeData(Server server) {
        server.sendDataToAllClients(getData(), lobbyID);
    }

    @Override
    public byte[] getData() {
        return ("03" + this.username + "," + this.x + "," + this.y + "," + this.lobbyID).getBytes();

    }

    public String getUsername() {
        return username;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public int getLobbyID() {
        return lobbyID;
    }

}
