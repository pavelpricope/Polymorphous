package paradroid.packets;


import paradroid.Server;

/**
 * @author jas555, pxp660
 */

public class PacketMap extends Packet {

    private byte[] map;
    private int lobbyID;

    public PacketMap(byte[] data) {
        super(04);
        this.map = data;


    }

    @Override
    public void writeData(Server server) {
        server.sendDataToAllClients(getData(), 0);
    }

    @Override
    public byte[] getData() {
        return combine("04".getBytes(), this.map);

    }

    public static byte[] combine(byte[] a, byte[] b) {
        int length = a.length + b.length;
        byte[] result = new byte[length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }

    public byte[] getMap() {
        return this.map;
    }

    public int getLobbyID() {
        return lobbyID;
    }


}
