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

    private static int assignNumber(byte a, byte b) {
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

    private static int getSize(ArrayList<byte[]> numPacks, byte fileID) {
        int initial = -1;
        for (byte[] array : numPacks) {
            if (array[1] == fileID) {
                initial = assignNumber(array[2], array[3]) + 1;
            }
        }
        return initial;
    }

    private static ArrayList<byte[]> genList(ArrayList<byte[]> data, int fileSize, byte fileID) {
        ArrayList<byte[]> l = new ArrayList<>(fileSize);
        for (int i = 0; i < fileSize; i++) {
            l.add(null);
        }

        for (byte[] aData : data) {
            byte dataID = aData[1];
            int num = assignNumber(aData[2], aData[3]);

            if (dataID == fileID) {
                l.set(num, aData);
            }
        }
        return l;
    }

    private static String getName(ArrayList<byte[]> header, int fileNum) {
        String name;
        byte[] byteName = new byte[header.get(fileNum).length - 2];

        if (byteName.length >= 0) System.arraycopy(header.get(fileNum), 2, byteName, 0, byteName.length);
        name = new String(byteName);
        return name;
    }

    private static void write(ArrayList<byte[]> list, String fileName) throws IOException {
        FileOutputStream file = new FileOutputStream(fileName);

        for (byte[] aList : list) {
            for (int j = 4; j < aList.length; j++) {
                file.write(aList[j]);
            }
        }
        file.close();
    }
}
