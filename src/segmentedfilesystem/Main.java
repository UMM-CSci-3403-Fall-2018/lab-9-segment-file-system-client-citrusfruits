package segmentedfilesystem;

import java.io.FileOutputStream;
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

            byte status = receivedArray[0];
            if (status % 2 == 0) {
                header.add(receivedArray);
            } else if (status % 4 == 3) {
                int packetNumber = assignNumber(receivedArray[2], receivedArray[3]);
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

        int fileOneSize = getSize(numPackets, fileOneID);
        int fileTwoSize = getSize(numPackets, fileTwoID);
        int fileThreeSize = getSize(numPackets, fileThreeID);

        ArrayList<byte[]> fileOne = genList(data, fileOneSize, fileOneID);
        ArrayList<byte[]> fileTwo = genList(data, fileTwoSize, fileTwoID);
        ArrayList<byte[]> fileThree = genList(data, fileThreeSize, fileThreeID);


        String fileOneName = getName(header, 0);
        String fileTwoName = getName(header, 1);
        String fileThreeName = getName(header, 2);

        write(fileOne, fileOneName);
        write(fileTwo, fileTwoName);
        write(fileThree, fileThreeName);
        socket.close();
    }

    public static int assignNumber(byte a, byte b) {
        int num;
        int newPacket;
        int newerPacket;

        if (a < 0) {
            newPacket = a + 256;
        } else {
            newPacket = a;
        }

        if (b < 0) {
            newerPacket = b + 256;
        } else {
            newerPacket = b;
        }
        num = 256 * newPacket + newerPacket;
        return num;
    }

    public static int getSize(ArrayList<byte[]> numPacks, byte fileID) {
        int initial = -1;
        for (int i = 0; i < numPacks.size(); i++) {
            byte[] array = numPacks.get(i);
            if (array[1] == fileID) {
                initial = assignNumber(array[2], array[3]) + 1;
            }
        }
        return initial;
    }

    public static ArrayList<byte[]> genList(ArrayList<byte[]> data, int fileSize, byte fileID) {
        ArrayList<byte[]> l = new ArrayList<byte[]>(fileSize);
        for (int i = 0; i < fileSize; i++) {
            l.add(null);
        }

        for (int i = 0; i < data.size(); i++) {
            byte dataID = data.get(i)[1];
            int num = assignNumber(data.get(i)[2], data.get(i)[3]);

            if (dataID == fileID) {
                l.set(num, data.get(i));
            }
        }
        return l;
    }

    public static String getName(ArrayList<byte[]> header, int fileNum) {
        String name = null;
        byte[] byteName = new byte[header.get(fileNum).length - 2];

        for (int i = 0; i < byteName.length; i++) {
            byteName[i] = header.get(fileNum)[i + 2];
        }
        name = new String(byteName);
        return name;
    }

    public static void write(ArrayList<byte[]> list, String fileName) throws IOException {
        FileOutputStream file = new FileOutputStream(fileName);

        for (int i = 0; i < list.size(); i++) {
            for (int j = 4; j < list.get(i).length; j++) {
                file.write(list.get(i)[j]);
            }
        }
        file.close();
    }
}
