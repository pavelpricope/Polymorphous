package paradroid;

/**
 * @author jas555, pxp660
 */

import java.net.InetAddress;

public class PlayerMP {

    public InetAddress ipAddress;
    public int port;
    private String username;
    public double x;
    public double y;
    private int skin;
    private int lobbyName;

    public PlayerMP(double x, double y, ID id, String username, int skin, InetAddress ipAddress, int port, int lobbyName) {
        this.x = x;
        this.y = y;
        this.username = username;
        this.skin = skin;
        this.ipAddress = ipAddress;
        this.port = port;
        this.lobbyName = lobbyName;
    }

    public InetAddress getIpAddress() {
        return ipAddress;
    }

    public int getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public int getSkin() {
        return skin;
    }

    public int getLobbyID() {
        return lobbyName;
    }

}
