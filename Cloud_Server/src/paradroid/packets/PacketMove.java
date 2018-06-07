package paradroid.packets;

import paradroid.Server;

/**
 * @author jas555, pxp660
 */

public class PacketMove extends Packet {

    private String username;
    private double x, y;
    private int direction;
    private boolean isMoving;
    private int lobbyID;

    public PacketMove(byte[] data) {
        super(02);
        String[] dataArray = readData(data).split(",");
        this.username = dataArray[0];
        this.x = Double.parseDouble(dataArray[1]);
        this.y = Double.parseDouble(dataArray[2]);
        this.direction = Integer.parseInt(dataArray[3]);
        isMoving = dataArray[4].equals("true");
        this.lobbyID = Integer.parseInt(dataArray[5]);
    }

    @Override
    public void writeData(Server server) {
        server.sendDataToAllClients(getData(), lobbyID);
    }

    @Override
    public byte[] getData() {
        return ("02" + this.username + "," + this.x + "," + this.y + "," + this.direction + "," + this.isMoving + "," + this.lobbyID).getBytes();

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
