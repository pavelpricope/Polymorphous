package com.polymorphous.Network;


import com.polymorphous.Network.packets.*;
import com.polymorphous.Network.packets.Packet.PacketTypes;
import com.polymorphous.main.Game;
import com.polymorphous.main.Objects.ID;
import com.polymorphous.main.Player.Player;
import com.polymorphous.main.Player.PlayerMP;
import com.polymorphous.util.mapGen.GenMap;
import com.polymorphous.util.mapGen.LoadMap;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.Arrays;

/**
 * @author jas555, pxp660
 */
public class GameClient extends Thread {

    private InetAddress ipAddress;
    private DatagramSocket socket;
    private Game game;

    public GameClient(String ipAddress){
        try {
            this.socket = new DatagramSocket();
            this.ipAddress = InetAddress.getByName(ipAddress);
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public GameClient(Game game, String ipAddress) {
        this.game = game;

        try {
            this.socket = new DatagramSocket();
            this.ipAddress = InetAddress.getByName(ipAddress);
        } catch (SocketException | UnknownHostException e) {
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
        PacketTypes types = Packet.lookupPacket(message.substring(0, 2));
        Packet packet = null;

        switch (types) {
            default:
            case LOGIN:
                packet = new PacketLogin(data);
                handleLogin((PacketLogin) packet, address, port);
                break;
            case DISCONNECT:
                packet = new PacketDisconnect(data);
                System.out.println("[" + address.getHostAddress() + ": " + port + ((PacketDisconnect) packet).getUsername() + " has left the world.");
                Game.getPlayersHandler().removePlayer(Game.getPlayersHandler().getPlayer(((PacketDisconnect) packet).getUsername()));
                break;
            case MOVE:
                packet = new PacketMove(data);
                handleMove((PacketMove) packet);
                break;
            case Bomb:
                packet = new PacketBomb(data);
                handleBomb((PacketBomb) packet);
                break;
            case Map:
                packet = new PacketMap(data);
                handleMap((PacketMap) packet);
                break;
            case PowerUP:
                packet = new PacketPowerUp(data);
                handlePowerUp((PacketPowerUp) packet);
                break;
            case LobbyInfo:
                packet = new PacketLobbyInfo(data);
                handleLobbyInfo((PacketLobbyInfo) packet);
                break;
            case CreateLobby:
                packet = new PacketCreateLobby(data);
                Game.lobbyID = ((PacketCreateLobby) packet).getLobbyId();
                System.out.println("Client received lobby id: " + Game.lobbyID);
                break;
            case GameState:
                packet = new PacketGameState(data);
                handleGameState((PacketGameState) packet);
                break;
            case Life:
                packet = new PacketPlayerLife(data);
                handlePlayerLife((PacketPlayerLife) packet);
                break;
            case INVALID:
                break;
        }

    }

    private void handlePlayerLife(PacketPlayerLife packet) {

        for (Player player : Game.getPlayersHandler().players) {
            if (player.getUsername().equals(packet.getUsername2()))
                player.loseLife();
        }
    }

    private void handleGameState(PacketGameState packetGameState) {
        Game.multiplayerState = packetGameState.getState();
        System.out.println(Game.multiplayerState);
    }

    public void sendData(byte[] data) {

        DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, 4445);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void handleLogin(PacketLogin packet, InetAddress address, int port) {
        System.out.println("[" + address.getHostAddress() + ": " + port + "] " + packet.getUsername() + " has joined the game.");
        PlayerMP player = new PlayerMP(packet.getX(), packet.getY(), ID.Player, packet.getUsername(), packet.getChar_skin(), address, port);
        Game.getPlayersHandler().addPlayer(player);
    }

    private void handlePowerUp(PacketPowerUp packet) {
        Game.getHandler().placePowerUp(packet.getX(), packet.getY(), packet.getAbility());
    }

    private void handleMove(PacketMove packet) {
        Game.getPlayersHandler().movePlayer(packet.getUsername(), packet.getX(), packet.getY(), packet.getDirection(), packet.isMoving());
    }

    private void handleBomb(PacketBomb packet) {
        Game.getHandler().placeBomb(packet.getUsername(), packet.getX(), packet.getY());
    }


    private void handleMap(PacketMap packetMap) {
        byte[] mapb = Arrays.copyOfRange(packetMap.getMap(), 2, packetMap.getData().length);
        System.out.println("received map:" + mapb.length);
        InputStream in = new ByteArrayInputStream(mapb);
        BufferedImage img = null;
        try {
            img = ImageIO.read(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        GenMap map = LoadMap.readImage(img);
        Game.gen_map = map;


    }

    private void handleLobbyInfo(PacketLobbyInfo packet) {
        String data = packet.getLobbies();
        int no_lobbies = Integer.parseInt(data.split(":")[0]);
        String[] lobbies = data.split(":")[1].split("/");
        for (int i = 0; i < lobbies.length; i += 3) {
            Lobby lobby = new Lobby(lobbies[i], lobbies[i + 1], lobbies[i + 2]);
            System.out.println(lobbies[i]);
            Game.data.add(lobby);
        }


    }

}