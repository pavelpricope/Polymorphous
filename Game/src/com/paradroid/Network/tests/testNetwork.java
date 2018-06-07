package com.paradroid.Network.tests;
import static com.paradroid.Network.packets.Packet.PacketTypes.Map;
import static org.junit.Assert.assertSame;

import com.paradroid.Network.GameClient;
import com.paradroid.Network.packets.PacketLogin;
import org.junit.Test;
import java.io.IOException;
import java.net.DatagramSocket;


public class testNetwork {

    @Test
    public void SendPacket() throws IOException {
        boolean sent = false;
        GameClient socketClient = new GameClient("35.204.91.111");
        DatagramSocket socket = new DatagramSocket();
        PacketLogin packet = new PacketLogin("test", 0, 0, 0, 0);
        packet.writeData(socketClient);
        sent = true;
        assertSame(sent, true);
    }
}