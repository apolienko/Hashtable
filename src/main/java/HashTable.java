import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class HashTable<K, V> implements Map<K, V> {

    private final int HASH_CONST = 47;

    private int size = 0;
    private int capacity = 16;
    private boolean[] existedCells;
    private double loadFactor = 0.75;
    private Cell<K, V>[] table;

    public HashTable() {
        table = new Cell[capacity];
        existedCells = new boolean[capacity];
    }

    public HashTable(int capacity) {
        if (capacity >= 0) {
            this.capacity = capacity;
        } else {
            throw new IllegalArgumentException("Illegal Capacity: "+ capacity);
        }
        table = new Cell[capacity];
        existedCells = new boolean[capacity];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }


    public int contains(Object key) {
        int hash1 = hash1(key);
        int hash2 = hash2(key);
        int n = -1;
        while (n != capacity - 1) {
            n++;
            int index = (hash1 + n * hash2) % (capacity - 1);
            Map.Entry<K, V> cell = table[index];
            if (cell != null && cell.getKey().equals(key) && existedCells[index]) {
                return index;
            }
        }
        return -1;
    }

    @Override
    public boolean containsKey(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }
        return contains(key) >= 0;
    }

    @Override
    public boolean containsValue(Object value) {
        if (value == null) {
            throw new NullPointerException();
        }
        for (int i = 0; i < capacity; i++) {
            if ((existedCells[i]) && (value.equals(table[i].getValue()))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public V get(Object key) {
        int index = contains(key);
        if (index < 0) {
            return null;
        }
        return table[index].getValue();
    }

    @Override
    public V put(K key, V value) {
        if (value == null) {
            throw new NullPointerException();
        }

        if (contains(key) >= 0) {
            return null;
        }

        size++;
        double newLoadFactor = (double) size / capacity;
        if (newLoadFactor >= loadFactor) {
            rehash();
        }

        int index = findIndex(key);
        table[index] =  new Cell(key, value);
        existedCells[index] = true;

        return value;
    }


    @Override
    public V remove(Object key) {
        int index = contains(key);
        if (index < 0) {
            return null;
        }
        size--;
        table[index] = null;
        return table[index].getValue();
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        for (Map.Entry<? extends K, ? extends V> elem : map.entrySet()) {
            put(elem.getKey(), elem.getValue());
        }
    }

    @Override
    public void clear() {
        for (int i = 0; i < capacity; i++) {
            table[i] = null;
        }
        size = 0;
        existedCells = new boolean[capacity];
    }

    public Set keySet() {
        return null;
    }

    public Collection values() {
        return null;
    }

    public Set<Entry<K, V>> entrySet() {
        return null;
    }

    @Override
    public V getOrDefault(Object key, V defaultValue) {
        int index = contains(key);
        if (index >= 0) {
            return table[index].getValue();
        } else {
            return defaultValue;
        }
    }

    public void forEach(BiConsumer action) {

    }

    public void replaceAll(BiFunction function) {

    }

    @Override
    public V putIfAbsent(K key, V value) {
        int index = contains(key);
        if (index >= 0) {
            return table[index].getValue();
        } else {
            return put(key, value);
        }
    }

    @Override
    public boolean remove(Object key, Object value) {
        V curValue = get(key);
        if (!Objects.equals(curValue, value) ||
                (curValue == null && !containsKey(key))) {
            return false;
        }
        remove(key);
        return true;
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        int index = contains(key);
        if (index < 0 || !table[index].getValue().equals(oldValue)) {
            return false;
        }
        table[index].setValue(newValue);
        return true;
    }

    @Override
    public V replace(K key, V value) {
        int index = contains(key);
        if (index < 0) {
            return null;
        }
        V old = table[index].getValue();
        table[index].setValue(value);
        return old;
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
        return (key.hashCode() * HASH_CONST) % capacity;
    }

    private int hash2(Object key) {
        int hash = (key.hashCode() * HASH_CONST) % (capacity - 1);
        if (hash % 2 == 0) {
            ++hash;
        }
        return hash;
    }

    private void rehash() {
        int oldCapacity = capacity;
        capacity = capacity * 2 + 1;
        loadFactor += (1 - loadFactor) / 2;

        Cell<K, V>[] subTable = Arrays.copyOf(table, oldCapacity);
        table = new Cell[capacity];
        existedCells = new boolean[capacity];

        for (int i = 0; i < oldCapacity; i++) {
            if (subTable[i] != null) {
                int index = findIndex(subTable[i].getKey());
                table[index] = subTable[i];
                existedCells[index] = true;
            }
        }
    }

    private int findIndex(K key) {
        int n = -1;
        while (true) {
            n++;
            int index = (hash1(key) + n * hash2(key)) % (capacity - 1);
            if (!existedCells[index]) {
                return index;
            }
        }
    }
}
