import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class HashTable implements Map {

    private final int HASH_CONST = 47;
    private int size = 0;
    private int bufferSize = 10;
    private Cell[] hashTable;
    private boolean[] deletedCells;

    public HashTable() {
        hashTable = new Cell[bufferSize];
        deletedCells = new boolean[bufferSize];
    }

    public HashTable(int bufferSize) {
        this.bufferSize = bufferSize;
        hashTable = new Cell[bufferSize];
        deletedCells = new boolean[bufferSize];
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private boolean contains(Object key) {
        int hash1 = hash1(key);
        int hash2 = hash2(key);
        int n = -1;
        while (n != bufferSize - 1) {
            n++;
            int index = (hash1 + n * hash2) % (bufferSize - 1);
            Cell cell = hashTable[index];
            if (cell != null && cell.getKey().equals(key)) {
                if (!deletedCells[index]) {
                    return false;
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }
        return contains(key);
    }

    @Override
    public boolean containsValue(Object value) {
        if (value == null) {
            throw new NullPointerException();
        }
        for (int i = 0; i < bufferSize; i++) {
            if (deletedCells[i]) {
                if (value.equals(hashTable[i].getValue())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Object get(Object key) {
        return null;
    }

    public Object put(Object key, Object value) {
        return null;
    }

    public Object remove(Object key) {
        return null;
    }

    public void putAll(Map m) {

    }

    public void clear() {

    }

    public Set keySet() {
        return null;
    }

    public Collection values() {
        return null;
    }

    public Set<Entry> entrySet() {
        return null;
    }

    public Object getOrDefault(Object key, Object defaultValue) {
        return null;
    }

    public void forEach(BiConsumer action) {

    }

    public void replaceAll(BiFunction function) {

    }

    public Object putIfAbsent(Object key, Object value) {
        return null;
    }

    public boolean remove(Object key, Object value) {
        return false;
    }

    public boolean replace(Object key, Object oldValue, Object newValue) {
        return false;
    }

    public Object replace(Object key, Object value) {
        return null;
    }

    public Object computeIfAbsent(Object key, Function mappingFunction) {
        return null;
    }

    public Object computeIfPresent(Object key, BiFunction remappingFunction) {
        return null;
    }

    public Object compute(Object key, BiFunction remappingFunction) {
        return null;
    }

    public Object merge(Object key, Object value, BiFunction remappingFunction) {
        return null;
    }

    private int hash1(Object key) {
        return (key.hashCode() * HASH_CONST)  % bufferSize;
    }

    private int hash2(Object key) {
        int hash = (key.hashCode() * HASH_CONST) % (bufferSize - 1);
        if (hash % 2 == 0) {
            ++hash;
        }
        return hash;
    }
}
