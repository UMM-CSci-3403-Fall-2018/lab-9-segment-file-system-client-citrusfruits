package segmentedfilesystem;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws IOException {
        int port = 6014;

        byte[] buffer = new byte[1028];
        InetAddress host = InetAddress.getByName("heartofgold.morris.umn.edu");
        DatagramPacket packet = new DatagramPacket(buffer, 0, host, port);
        DatagramSocket socket = new DatagramSocket(port);
        socket.send(packet);

        ArrayList<byte[]> header = new ArrayList<>();
        ArrayList<byte[]> data = new ArrayList<>();
        ArrayList<byte[]> numPackets = new ArrayList<>();

        int packets = 0;
        int total = 3;
        int count = 0;

        while (true) {
            count++;
            DatagramPacket received = new DatagramPacket(buffer, buffer.length);
            socket.receive(received);
            byte[] receivedArray = new byte[received.getLength()];

            System.arraycopy(received.getData(), 0, receivedArray, 0, receivedArray.length);
        }
    }
}