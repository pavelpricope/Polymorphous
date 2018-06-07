package paradroid.packets;


import paradroid.Server;

/**
 * @author jas555
 */

public class PacketPowerUp extends Packet {

    private String username;
    private int x, y;
    private int ability;
    private int lobbyID;

    public PacketPowerUp(byte[] data) {
        super(05);
        String[] dataArray = readData(data).split(",");
        this.username = dataArray[0];                        //   the username of the player who places PowerUp
        this.x = Integer.parseInt(dataArray[1]);
        this.y = Integer.parseInt(dataArray[2]);
        this.ability = Integer.parseInt((dataArray[3]));
        this.lobbyID = Integer.parseInt((dataArray[4]));
    }


    @Override
    public void writeData(Server server) {
        server.sendDataToAllClients(getData(), 0);
    }

    @Override
    public byte[] getData() {
        return ("05" + this.username + "," + this.x + "," + this.y + "," + this.ability + "," + this.lobbyID).getBytes();

    }

    public String getUsername() {
        return username;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getAbility() {
        return this.ability;
    }

    public int getLobbyID() {
        return lobbyID;
    }

}
