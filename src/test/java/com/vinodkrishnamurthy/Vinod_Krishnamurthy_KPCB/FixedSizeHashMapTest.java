package vinodKPCBChallenge;

import java.util.Random;
import static org.junit.Assert.*;
import org.junit.*;

public class FixedSizeHashMapTest {
    //bounds for the size of the hash map
    private static final int MIN_SIZE = 500;
    private static final int MAX_SIZE = 15000;
    private static Random rand;
    private static int size;
    private FixedSizeHashMap<Integer> integer_map;
    private FixedSizeHashMap<String> string_map;
    private FixedSizeHashMap<Boolean> boolean_map;

    // preliminary setup
    public static void setup() {
        rand = new Random();
        size = rand.nextInt(MAX_SIZE - MIN_SIZE + 1) + MIN_SIZE;
        System.out.printf("The size for this test is %d.%n", size);
    }

    // initialize hash maps of integer, string, and boolean types
    public void initializeHashMaps() {
        integer_map = new FixedSizeHashMap<Integer>(size);
        string_map = new FixedSizeHashMap<String>(size);
        boolean_map = new FixedSizeHashMap<Boolean>(size);
    }

    // test set method
    public void testSet() {
        for (int i = 0; i < size; i++) {
            String k = "test_key_" + Integer.toString(i);
            int v = rand.nextInt();
            assertTrue(integer_map.set(k, v));
            assertTrue(string_map.set(k, Integer.toString(v)));
            assertTrue(boolean_map.set(k, v%2 == 0));
        }
    }

    // test set method with duplicate key
    public void testSetDuplicateKey() {
        for (int i = 0; i < size; i++) {
            String k = "test_key_" + Integer.toString(i);
            int v = rand.nextInt();
            assertTrue(integer_map.set(k, v));
            assertTrue(string_map.set(k, Integer.toString(v)));
            assertTrue(boolean_map.set(k, v%2 == 0));
            assertFalse(integer_map.set(k, v));
            assertFalse(string_map.set(k, Integer.toString(v)));
            assertFalse(boolean_map.set(k, v%2 == 0));
        }
    }

    // test get method
    public void testGet() {
        for (int i = 0; i < size; i++) {
            String k = "test_key_" + Integer.toString(i);
            int v = rand.nextInt();
            if (integer_map.set(k, v)) {
                assertEquals((int)v, (int)integer_map.get(k));
            }
            if (string_map.set(k, Integer.toString(v))) {
                assertEquals(Integer.toString(v), string_map.get(k));
            }
            if (boolean_map.set(k, v%2 == 0)) {
                assertEquals(v%2 == 0, boolean_map.get(k));
            }
        }
    }

    // test delete method
    public void testDelete() {
        for (int i = 0; i < size; i++) {
            String k = "test_key_" + Integer.toString(i);
            int v = rand.nextInt();
            if (integer_map.set(k, v)) {
                assertEquals((int)integer_map.delete(k), (int)v);
                assertEquals(0.0, integer_map.load(), 0.00001);
            }
            if (string_map.set(k, Integer.toString(v))) {
                assertEquals(string_map.delete(k), Integer.toString(v));
                assertEquals(0.0, string_map.load(), 0.00001);
            }
            if (boolean_map.set(k, v%2 == 0)) {
                assertEquals(boolean_map.delete(k), v%2 == 0);
                assertEquals(0.0, boolean_map.load(), 0.00001);
            }
        }
    }

    // test load method
    public void testLoad() {
        for (int i = 0; i < size; i++) {
            String k = "test_key_" + Integer.toString(i);
            int v = rand.nextInt();
            if (integer_map.set(k, v)) {
                assertEquals((float)(i + 1)/size, integer_map.load(), 0.00001);
            }
            if (string_map.set(k, Integer.toString(v))) {
                assertEquals((float)(i + 1)/size, string_map.load(), 0.00001);
            }
            if (boolean_map.set(k, v%2 == 0)) {
                assertEquals((float)(i + 1)/size, boolean_map.load(), 0.00001);
            }
        }
    }
}
