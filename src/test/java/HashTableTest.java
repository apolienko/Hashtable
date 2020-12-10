import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

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

        assertThrows(NullPointerException.class, () ->
                table.put(null, 1));
        assertThrows(NullPointerException.class, () ->
                table.put("12nul89l", null));
    }

    @Test
    void remove() {
    }

    @Test
    void putAll() {
    }

    @Test
    void clear() {
        setTable(table);
        table.clear();
        assertEquals(table.size(), 0);
    }

    @Test
    void keySet() {
    }

    @Test
    void values() {
    }

    @Test
    void entrySet() {
    }

    @Test
    void testEquals() {
    }

    @Test
    void testHashCode() {
    }

    @Test
    void testToString() {
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