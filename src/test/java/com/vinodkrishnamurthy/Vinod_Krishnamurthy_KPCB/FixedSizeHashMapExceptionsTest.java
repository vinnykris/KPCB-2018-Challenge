package vinodKPCBChallenge;

import java.util.Random;
import org.junit.*;

public class FixedSizeHashMapExceptionsTest {
    private FixedSizeHashMap<String> string_map;

    @Test(expected = IllegalArgumentException.class)
    public void negativeSizeInstantiation() {
        string_map = new FixedSizeHashMap<String>(-10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void zeroSizeInstantiation() {
        string_map = new FixedSizeHashMap<String>(0);
    }
}
