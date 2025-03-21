package software.ulpgc.edp;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
class DictionaryTest {
    Dict<String, Integer> dict;
    Random rand = new Random();

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        dict = new Dictionary<>(8);
    }

    @org.junit.jupiter.api.Test
    void addNormal() {
        for (int i = 0; i < 10; i++) {
            dict.add("P" + (i + 1), 100 + i);
        }
        assertEquals(dict.size(), 10);
        assertEquals(dict.get("P1"), 100);
        assertEquals(dict.get("P10"), 109);
        dict.clear();
        assertEquals(dict.size(), 0);
    }

    @org.junit.jupiter.api.Test
    void addLarge() {
        for (int i = 0; i < 100000; i++) {
            dict.add("P" + (i + 1), 100 + i);
        }
        assertEquals(dict.size(), 100000);
        assertEquals(dict.get("P1"), 100);
        dict.clear();
        assertEquals(dict.size(), 0);
    }
    
    @org.junit.jupiter.api.Test
    void addAndRemove() {
        assertTrue(dict.isEmpty());
        for (int i = 1; i <= 100; i++) {
            dict.add("Emp" + i, rand.nextInt(1000));
        }
        for (int i = 1; i <= 100; i++) {
            assertTrue(dict.containsKey("Emp" + i));
        }
        assertEquals(dict.size(), 100);
        for (int i = 50; i <= 100; i++) {
            dict.pop("Emp" + i);
        }
        assertEquals(dict.size(), 50);
        assertEquals(dict.items().length, 50);
    }

}