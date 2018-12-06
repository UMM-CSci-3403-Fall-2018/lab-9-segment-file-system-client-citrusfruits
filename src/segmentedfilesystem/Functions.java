package segmentedfilesystem;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

class Functions {
    static int assignNumber(byte a, byte b) {
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

    static int getSize(ArrayList<byte[]> numPacks, byte fileID) {
        int initial = -1;

        for (byte[] array : numPacks) {
            if (array[1] == fileID) {
                initial = assignNumber(array[2], array[3]) + 1;
            }
        }

        return initial;
    }

    static ArrayList<byte[]> genList(ArrayList<byte[]> data, int fileSize, byte fileID) {
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

    static String getName(ArrayList<byte[]> header, int fileNum) {
        String name;
        byte[] byteName = new byte[header.get(fileNum).length - 2];
        System.arraycopy(header.get(fileNum), 2, byteName, 0, byteName.length);
        name = new String(byteName);
        return name;
    }

    static void write(ArrayList<byte[]> list, String fileName) throws IOException {
        FileOutputStream file = new FileOutputStream(fileName);

        for (byte[] aList : list) {
            for (int j = 4; j < aList.length; j++) {
                file.write(aList[j]);
            }
        }

        file.close();
    }
}
