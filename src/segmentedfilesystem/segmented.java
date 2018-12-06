package segmentedfilesystem;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

public class segmented {

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

        while (packets != 3 || count != total) {
            count++;
            DatagramPacket received = new DatagramPacket(buffer, buffer.length);
            socket.receive(received);
            byte[] receivedArray = new byte[received.getLength()];

            System.arraycopy(received.getData(), 0, receivedArray, 0, receivedArray.length);


            byte status = receivedArray[0];
            if (status % 2 == 0) {
                header.add(receivedArray);
            } else if (status % 4 == 3) {
                int packetNumber = Functions.assignNumber(receivedArray[2], receivedArray[3]);
                total += packetNumber + 1;
                packets++;
                data.add(receivedArray);
                numPackets.add(receivedArray);
            } else {
                data.add(receivedArray);
            }
        }

        byte fileOneID = header.get(0)[1];
        byte fileTwoID = header.get(1)[1];
        byte fileThreeID = header.get(2)[1];

        int fileOneSize = Functions.getSize(numPackets, fileOneID);
        int fileTwoSize = Functions.getSize(numPackets, fileTwoID);
        int fileThreeSize = Functions.getSize(numPackets, fileThreeID);

        ArrayList<byte[]> fileOne = Functions.genList(data, fileOneSize, fileOneID);
        ArrayList<byte[]> fileTwo = Functions.genList(data, fileTwoSize, fileTwoID);
        ArrayList<byte[]> fileThree = Functions.genList(data, fileThreeSize, fileThreeID);


        String fileOneName = Functions.getName(header, 0);
        String fileTwoName = Functions.getName(header, 1);
        String fileThreeName = Functions.getName(header, 2);

        Functions.write(fileOne, fileOneName);
        Functions.write(fileTwo, fileTwoName);
        Functions.write(fileThree, fileThreeName);
        socket.close();
    }
}
