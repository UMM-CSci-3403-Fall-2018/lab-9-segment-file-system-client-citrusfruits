package segmentedfilesystem;

import org.junit.*;
import java.util.ArrayList;

public class FunctionsTest {

    @Test
    public void assignNumberTest() {
        int g = Functions.assignNumber((byte) 23, (byte) 45);
        Assert.assertEquals(5933, g);
    }

    @Test
    public void assignNumberTestBelow0() {
        int a = Functions.assignNumber((byte) -8, (byte) 97);
        Assert.assertEquals(63585, a);
    }

    @Test
    public void getSizeTest() {
        byte[] bytes = {5, 3, 34, 23, 68, 23, 1, 4};
        byte ID = 3;
        ArrayList<byte[]> array = new ArrayList<>();
        array.add(bytes);
        Assert.assertEquals(8728, Functions.getSize(array, ID));
    }

    @Test
    public void genListTest() {
        byte[] bytes = {5, 3, 34, 23, 68, 23, 1, 4};
        ArrayList<byte[]> array = new ArrayList<>();
        array.add(bytes);
        int fileSize = 12;
        byte ID = 4;
        ArrayList<byte[]> test = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            test.add(null);
        }
        Assert.assertEquals(test, Functions.genList(array, fileSize, ID));
    }

    @Test
    public void getNameTest() {
        byte[] bytes = {47, 38, 57};
        ArrayList<byte[]> array = new ArrayList<>();
        array.add(bytes);
        int fileNum = 0;
        Assert.assertEquals("9", Functions.getName(array, fileNum));
    }

    @Test (expected = IndexOutOfBoundsException.class) // Header must be 0 for this function to run
    public void getNameTestError() {
        byte[] bytes = {47, 38, 57};
        ArrayList<byte[]> array = new ArrayList<>();
        array.add(bytes);
        int fileNum = 3;
        Functions.getName(array, fileNum);
    }
}
