package segmentedfilesystem;

import org.junit.*;
import java.io.File;

/**
 * This is just a stub test file. You should rename it to
 * something meaningful in your context and populate it with
 * useful tests.
 */

public class SegmentedTest {

    @Test
    public void assignNumberTest() {
        int g = Functions.assignNumber((byte) 23, (byte) 45);
        Assert.assertEquals(g, 5933);
    }

    @Test
    public void assignNumberTestBelow0() {
        int a = Functions.assignNumber((byte) -8, (byte) 97);
        Assert.assertEquals(a, 63585);
    }

}
