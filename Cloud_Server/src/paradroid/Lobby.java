package paradroid;

/**
 * @author jas555
 */

import paradroid.packets.mapGen.GenMap;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Lobby {

    private int id;
    private String lobbyName;
    private List<PlayerMP> currentPlayers = new LinkedList<>();
    private String lobbyAdmin;
    private GenMap map;
    public boolean available = true;           // a game is available is it has less then 4 players connected and it has not started

    public Lobby(int id, String lobbyName, GenMap map) {
        this.id = id;
        this.lobbyName = lobbyName;
        this.map = map;

    }

    public int getID() {
        return id;
    }

    public String getLobbyName() {
        return lobbyName;
    }

    public void setLobbyName(String name) {
        this.lobbyName = name;
    }

    public void addPlayer(PlayerMP player) {
        currentPlayers.add(player);
    }

    public void removePlayer(String player) {

        Iterator<PlayerMP> iter = currentPlayers.iterator();
        while (iter.hasNext()) {
            PlayerMP cPlayer = iter.next();
            if (cPlayer.getUsername().equals(player)) {
                iter.remove();
            }

        }
    }

    public List<PlayerMP> getPlayers() {
        return currentPlayers;
    }

    public int getNumberPlayers() {
        return currentPlayers.size();
    }

    public void setLobbyAdmin(String lobbyAdmin) {
        this.lobbyAdmin = lobbyAdmin;
    }

    public GenMap getMap() {
        return map;
    }

    public String getLobbyAdmin() {
        return lobbyAdmin;
    }
}
