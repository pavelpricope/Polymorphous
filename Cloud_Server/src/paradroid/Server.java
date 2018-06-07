package paradroid;

/**
 * @author jas555, pxp660
 */

import paradroid.packets.*;
import paradroid.packets.mapGen.GenMap;
import paradroid.packets.mapGen.SaveMap;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.List;

public class Server extends Thread {

    private String path = "/home/pavelpricope/map";
    private DatagramSocket socket;
    private List<Lobby> lobbies = new LinkedList<>();
    private int lobbyId = 1;

    public Server() {

        try {
            this.socket = new DatagramSocket(4445);
            System.out.println("Server is running...");
        } catch (SocketException e) {
            e.printStackTrace();
        }


    }

    public void run() {
        while (true) {
            byte[] data = new byte[1082];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.parsePacket(packet.getData(), packet.getAddress(), packet.getPort());


        }
    }

    private void parsePacket(byte[] data, InetAddress address, int port) {
        String message = new String(data).trim();
        Packet.PacketTypes types = Packet.lookupPacket(message.substring(0, 2));
        Packet packet = null;

        switch (types) {
            default:
            case LOGIN:
                packet = new PacketLogin(data);
                System.out.println("[" + address.getHostAddress() + ": " + port + "] " + ((PacketLogin) packet).getUsername() + " has connected.");
                PlayerMP player = new PlayerMP(((PacketLogin) packet).getX(), ((PacketLogin) packet).getY(), ID.Player, ((PacketLogin) packet).getUsername(), ((PacketLogin) packet).getChar_skin(), address, port, ((PacketLogin) packet).getLobbyID());
                System.out.println("Player " + player.getUsername() + " requested to login to lobby: " + player.getLobbyID());
                this.addConnection(player, (PacketLogin) packet);
                break;
            case DISCONNECT:
                packet = new PacketDisconnect(data);
                System.out.println("[" + address.getHostAddress() + ": " + port + "] " + ((PacketDisconnect) packet).getUsername() + " has left.");
                this.removeConnection((PacketDisconnect) packet);
                break;
            case MOVE:
                packet = new PacketMove(data);
                this.handleMove((PacketMove) packet);
                break;
            case Bomb:
                packet = new PacketBomb(data);
                this.handleBomb((PacketBomb) packet);
                break;
            case MapReq:
                packet = new PacketMapReq(data);
                this.sendMap((PacketMapReq) packet, address, port);
                break;
            case PowerUP:
                packet = new PacketPowerUp(data);
                this.handlePowerUp((PacketPowerUp) packet);
                break;
            case LobbyInfo:
                this.handleLobbyInfo(address, port);
                break;
            case CreateLobby:
                packet = new PacketCreateLobby(data);
                this.handleLobbyCreation((PacketCreateLobby) packet, address, port);
                break;
            case GameState:
                packet = new PacketGameState(data);
                this.handleGameState((PacketGameState) packet);
                break;
            case Life:
                packet = new PacketPlayerLife(data);
                this.handlePlayerLife((PacketPlayerLife) packet);
                break;
            case INVALID:
                break;
        }

    }

    private void addConnection(PlayerMP player, PacketLogin packet) {

        if (getLobby(packet.getLobbyID()).getPlayers().size() > 0) {
            PacketGameState packetGameState = new PacketGameState(player.getUsername(), 1, packet.getLobbyID());
            sendData(packetGameState.getData(), player.ipAddress, player.port);
            notifyAdmin(packet.getLobbyID());
        } else {
            PacketGameState packetGameState = new PacketGameState(packet.getUsername(), 1, lobbyId);        // state 1 means that the game admin waits for other players to connect
            sendData(packetGameState.getData(), player.ipAddress, player.port);
        }
        Lobby cLobby = getLobby(packet.getLobbyID());
        cLobby.addPlayer(player);
        if (cLobby.getPlayers().size() == 4)
            cLobby.available = false;
        for (PlayerMP playerMP : getLobby(packet.getLobbyID()).getPlayers()) {
            if (!playerMP.getUsername().equalsIgnoreCase(player.getUsername())) {
                //send data to the already connected players
                sendData(packet.getData(), playerMP.ipAddress, playerMP.port);
                Packet newPlayer = new PacketLogin(playerMP.getUsername(), playerMP.x, playerMP.y, playerMP.getSkin(), playerMP.getLobbyID());
                //send data to the new player with the already connected ones
                sendData(newPlayer.getData(), player.ipAddress, player.port);
            }
        }

    }

    private void removeConnection(PacketDisconnect packet) {
        getLobby(packet.getLobbyID()).removePlayer(packet.getUsername());
        sendDataToAllClients(packet.getData(), packet.getLobbyID());
    }

    private void handleMove(PacketMove packet) {

        for (PlayerMP cPlayer : getLobby(packet.getLobbyID()).getPlayers()) {
            if (!cPlayer.getUsername().equals(packet.getUsername())) {
                sendData(packet.getData(), cPlayer.getIpAddress(), cPlayer.getPort());
            }

        }

    }

    private void handleBomb(PacketBomb packet) {

        for (PlayerMP cPlayer : getLobby(packet.getLobbyID()).getPlayers()) {
            if (!cPlayer.getUsername().equals(packet.getUsername())) {
                sendData(packet.getData(), cPlayer.getIpAddress(), cPlayer.getPort());
            }

        }
    }

    private void sendMap(PacketMapReq packetMapReq, InetAddress address, int port) {
        try {
            System.out.println(path + packetMapReq.getLobbyID() + ".png");
            byte[] map_img = extractBytes(path + packetMapReq.getLobbyID() + ".png");
            PacketMap packetMap = new PacketMap(map_img);
            sendData(packetMap.getData(), address, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handlePowerUp(PacketPowerUp packet) {
        for (PlayerMP cPlayer : getLobby(packet.getLobbyID()).getPlayers()) {
            if (!cPlayer.getUsername().equals(packet.getUsername())) {
                sendData(packet.getData(), cPlayer.getIpAddress(), cPlayer.getPort());
            }

        }
    }

    private void handleLobbyInfo(InetAddress ipAddress, int port) {

        if (lobbies.size() > 0) {
            String lobbies = this.lobbies.size() + ":";
            for (Lobby lobby : this.lobbies) {
                if (lobby.available)
                    lobbies += lobby.getLobbyName() + "/" + lobby.getID() + "/" + lobby.getNumberPlayers() + "/";
            }
            PacketLobbyInfo packetLobbyInfo = new PacketLobbyInfo(lobbies);
            sendData(packetLobbyInfo.getData(), ipAddress, port);
        }
    }

    private void handleLobbyCreation(PacketCreateLobby packet, InetAddress address, int port) {
        createLobby(lobbyId, packet.getLobbyName(), packet.getUsername());
        PacketCreateLobby packetLobbyID = new PacketCreateLobby(packet.getUsername(), packet.getLobbyName(), lobbyId);
        sendData(packetLobbyID.getData(), address, port);
        lobbyId++;
    }

    private void handleGameState(PacketGameState packet) {
        System.out.println(packet.getState() + " - " + packet.getLobbyID());
        Lobby cLobby = getLobby(packet.getLobbyID());
        if (packet.getState() == 3)
            cLobby.available = false;
        for (PlayerMP playerMP : cLobby.getPlayers()) {
            if (!playerMP.getUsername().equals(cLobby.getLobbyAdmin())) {
                sendData(packet.getData(), playerMP.ipAddress, playerMP.port);
            }
        }
    }

    private void handlePlayerLife(PacketPlayerLife packet) {
        Lobby cLobby = getLobby(packet.getLobbyID());
        for (PlayerMP playerMP : cLobby.getPlayers()) {
            if (!playerMP.getUsername().equals(packet.getUsername())) {
                sendData(packet.getData(), playerMP.ipAddress, playerMP.port);

            }
        }
    }


    // notify the admin of a lobby that a new player has connected and he can start the game
    private void notifyAdmin(int lobbyId) {
        Lobby cLobby = getLobby(lobbyId);
        for (PlayerMP playerMP : cLobby.getPlayers()) {
            if (playerMP.getUsername().equals(cLobby.getLobbyAdmin())) {
                PacketGameState packetGameState = new PacketGameState(playerMP.getUsername(), 2, playerMP.getLobbyID());
                sendData(packetGameState.getData(), playerMP.ipAddress, playerMP.port);
            }
        }

    }


    public void sendData(byte[] data, InetAddress ipAddress, int port) {

        DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
        try {
            this.socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendDataToAllClients(byte[] data, int lobbyID) {
        Lobby lobby = getLobby(lobbyID);
        for (PlayerMP p : lobby.getPlayers()) {

            sendData(data, p.ipAddress, p.port);

        }

    }


    private byte[] extractBytes(String ImageName) throws IOException {
        // open image
        File imgPath = new File(ImageName);
        BufferedImage bufferedImage = ImageIO.read(imgPath);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", byteArrayOutputStream);

        return (byteArrayOutputStream.toByteArray());
    }


    private void createLobby(int id, String lobbyName, String lobbyAdmin) {
        GenMap map = new GenMap(19, 19);
        SaveMap.writeMap(map.getWidth(), map.getHeight(), map.getNodes(), path + id + ".png");
        Lobby lobby = new Lobby(id, lobbyName, map);
        lobby.setLobbyAdmin(lobbyAdmin);
        lobbies.add(lobby);

    }

    public Lobby getLobby(int lobbyName) {
        for (Lobby lobby : lobbies) {
            if (lobby.getID() == lobbyName)
                return lobby;
        }
        return null;
    }

    public void removeLobby(Lobby lobby) {
        lobbies.remove(lobby);
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }
}
