package segmentedfilesystem;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws IOException {
        int port = 6014;
        ArrayList<byte[]> header = new ArrayList<>();
        ArrayList<byte[]> data = new ArrayList<>();

        try {
            byte[] buffer = new byte[1028];
            InetAddress host = InetAddress.getByName("heartofgold.morris.umn.edu");
            DatagramPacket packet = new DatagramPacket(buffer, 0, host, port);
            DatagramSocket socket = new DatagramSocket(port);

            socket.send(packet);

            while (true) {
                DatagramPacket received = new DatagramPacket(buffer, buffer.length);
                socket.receive(received);
                byte[] rdata = received.getData();
                int length = received.getLength();
                System.out.println(rdata);
                System.out.println(length);
            }



//            while (true) {
//                if (buffer[0] == 0){
//                        header.add(buffer);
//                }
//                else {
//                    data.add(buffer);
//                }
//            }



        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

}