import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HashTableTest {

    HashTable<String, Integer> table = new HashTable<>();

    public static void setTable(Map<String, Integer> table) {
        table.clear();
        table.put("A", 10);
        table.put("B", 20);
        table.put("C", 30);
        table.put("D", 40);
        table.put("E", 50);
    }

    @Test
    void testSize() {
        setTable(table);
        assertEquals(5, table.size());
    }

    @Test
    void isEmpty() {
        setTable(table);
        assertFalse(table.isEmpty());

        table.clear();
        assertTrue(table.isEmpty());
    }

    @Test
    void containsKey() {
        setTable(table);

        assertTrue(table.containsKey("A"));
        assertTrue(table.containsKey("B"));
        assertFalse(table.containsKey("GG"));

    }

    @Test
    void containsValue() {
        setTable(table);

        assertTrue(table.containsValue(10));
        assertTrue(table.containsValue(20));

        assertFalse(table.containsValue(1000));
        assertFalse(table.containsValue(167));
    }

    @Test
    void get() {
        setTable(table);

        assertNull(table.get("GG"));
        assertNull(table.get("R"));

        assertEquals(10, table.get("A"));
        assertEquals(20, table.get("B"));

        assertThrows(NullPointerException.class, () ->
                table.get(null));
    }

    @Test
    void put() {
        setTable(table);

        assertEquals(10, table.put("A", 1)); // так как старое value равнялось 10
        assertTrue(table.containsValue(1)); // теперь в table есть value 1

        assertNull(table.put("GG", 160)); // возвращает null, т.к. до этого такого ключа в таблице не было
        assertTrue(table.containsValue(160));
        assertTrue(table.containsKey("GG"));

        assertEquals(table.size(), 6);

        assertThrows(NullPointerException.class, () ->
                table.put(null, 1));
        assertThrows(NullPointerException.class, () ->
                table.put("12nul89l", null));
    }

    @Test
    void remove() {
        setTable(table);

        assertNull(table.remove("GG"));
        assertNull(table.remove("Y"));

        assertEquals(10, table.remove("A")); // Возращает value, которому соответствовал key
        assertEquals(20, table.remove("B"));

        assertEquals(table.size(), 3);
    }

    @Test
    void putAll() {
        setTable(table);

        Map<String, Integer> table2 = new HashMap<>();
        table2.put("a", 1);
        table2.put("b", 2);
        table2.put("c", 3);

        table.putAll(table2);
        assertTrue(table.containsValue(1));
        assertTrue(table.containsKey("a"));
        assertTrue(table.containsValue(2));
        assertTrue(table.containsKey("b"));
        assertTrue(table.containsValue(3));
        assertTrue(table.containsKey("c"));

        table2.put(null, null);
        assertThrows(NullPointerException.class, () ->
                table.putAll(table2));


    }

    @Test
    void clear() {
        setTable(table);
        table.clear();
        assertEquals(table.size(), 0);
    }

    @Test
    void keySet() {
        Map<String, Integer> table2 = new HashMap<>();

        setTable(table);
        setTable(table2);
        assertEquals(table.keySet(), table2.keySet());

        table.remove("A");
        assertNotEquals(table.keySet(), table2.keySet());

        table.clear();
        assertTrue(table.keySet().isEmpty());
    }

    @Test
    void values() {
        Map<String, Integer> table2 = new HashMap<>();

        setTable(table);
        setTable(table2);

        assertEquals(table.values().size(), table2.values().size());

        table2.remove("A");
        assertNotEquals(table.values().size(), table2.values().size());

        table.values().clear();
        assertTrue(table.values().isEmpty());
    }

    @Test
    void entrySet() {
        Map<String, Integer> table2 = new HashMap<>();

        setTable(table);
        setTable(table2);

        assertEquals(table.entrySet().size(), table2.entrySet().size());

        table2.remove("A");
        assertNotEquals(table.values().size(), table2.values().size());

        table.clear();
        assertTrue(table.entrySet().isEmpty());
    }

    @Test
    void getOrDefault() {


    }

    @Test
    void forEach() {
    }

    @Test
    void replaceAll() {
    }

    @Test
    void putIfAbsent() {
    }

    @Test
    void testRemove() {
    }

    @Test
    void replace() {
    }

    @Test
    void testReplace() {
    }

    @Test
    void computeIfAbsent() {
    }

    @Test
    void computeIfPresent() {
    }

    @Test
    void compute() {
    }

    @Test
    void merge() {
    }
}