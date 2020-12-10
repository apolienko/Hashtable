import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class HashTable<K, V> implements Map<K, V> {

    private final int HASH_CONST = 47;

    private int size = 0;
    private int capacity = 16;
    private boolean[] deletedCells;
    private float loadFactor = 0.75f;
    private Cell<K,V>[] table;


    public HashTable(int capacity, float loadFactor) {
        if (capacity < 0) {
            throw new IllegalArgumentException(
                    "Illegal Capacity: " + capacity
            );
        }

        if (loadFactor <= 0 || Float.isNaN(loadFactor)) {
            throw new IllegalArgumentException("Illegal Load: " + loadFactor);
        }

        if (capacity==0) {
            capacity = 1;
        }

        this.loadFactor = loadFactor;
        table = new Cell[capacity];
        deletedCells = new boolean[capacity];
    }

    public HashTable(int capacity) {
        this(capacity, 0.75f);
    }

    public HashTable() {
        this(16, 0.75f);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return contains(key) >= 0;
    }

    @Override
    public boolean containsValue(Object value) {
        if (value == null) {
            throw new NullPointerException("HashTable does not permit null values");
        }

        for (int i = 0; i < capacity; i++) {
            if (!deletedCells[i] && table[i] != null) {
                if (value.equals(table[i].getValue())) {
                    return true;
                }
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
        if (value == null || key == null) {
            throw new NullPointerException();
        }

        if (contains(key) >= 0) {
            int index = this.contains(key);
            V oldValue = table[index].getValue();
            table[index].setValue(value);
            return oldValue;
        }

        size++;
        double newLoadFactor = (double) size / capacity;
        if (newLoadFactor >= loadFactor) {
            rehash();
        }

        int index = findEmptyIndex(key);
        Cell<K,V> newCell = new Cell<>(key, value);
        table[index] = newCell;

//      Returns null if there was no mapping for key before
        return null;
    }


    @Override
    public V remove(Object key) {
        int index = contains(key);

        if (index < 0) {
            return null;
        }

        V deletedValue = table[index].getValue();

        size--;
        deletedCells[index] = true;
        return deletedValue;
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
        deletedCells = new boolean[capacity];
    }

    private  Set<K> keySet;
    private  Set<Map.Entry<K,V>> entrySet;
    private  Collection<V> values;

    private static final int KEYS = 0;
    private static final int VALUES = 1;
    private static final int ENTRIES = 2;

    @Override
    public Set<K> keySet() {
        if (keySet == null) {
            keySet = new KeySet();
        }
        return keySet;
    }

    private class KeySet extends AbstractSet<K> {
        public Iterator<K> iterator() {
            return getIterator(KEYS);
        }

        public int size() {
            return size;
        }

        public boolean contains(Object o) {
            return containsKey(o);
        }

        public boolean remove(Object o) {
            return HashTable.this.remove(o) != null;
        }

        public void clear() {
            HashTable.this.clear();
        }
    }

    @Override
    public Collection<V> values() {
        if (values == null) {
            values = new ValueCollection();
        }
        return values;
    }

    private class ValueCollection extends AbstractCollection<V> {
        public Iterator<V> iterator() {
            return getIterator(VALUES);
        }

        public int size() {
            return size;
        }

        public boolean contains(Object o) {
            return containsValue(o);
        }

        public void clear() {
            HashTable.this.clear();
        }
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        if (entrySet == null) {
            entrySet = new EntrySet();
        }
        return entrySet;
    }

    class EntrySet extends AbstractSet<Map.Entry<K, V>> {
        public Iterator<Entry<K, V>> iterator() {
            return getIterator(ENTRIES);
        }

        public boolean add(Map.Entry<K, V> o) {
            return super.add(o);
        }

        public boolean contains(Object o) {
            if (!(o instanceof Cell)) {
                return false;
            }
            Map.Entry<?, ?> entry = (Map.Entry<?, ?>) o;
            Object key = entry.getKey();
            return HashTable.this.containsKey(key);
        }

        public boolean remove(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry<?, ?> entry = (Map.Entry<?, ?>) o;
            Object key = entry.getKey();
            return HashTable.this.remove(key) != null;
        }

        public void clear() {
            HashTable.this.clear();
        }

        @Override
        public int size() {
            return size;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof Map)) {
            return false;
        }

        Map<?,?> t = (Map<?,?>) o;
        if (t.size() != size())
            return false;

        try {
            for (Map.Entry<K, V> e : entrySet()) {
                K key = e.getKey();
                V value = e.getValue();
                if (!value.equals(t.get(key))) {
                    return false;
                }
            }
        } catch (ClassCastException | NullPointerException ex)   {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        for (Entry<K, V> entry : entrySet) {
            hash += entry.hashCode();
        }
        return hash;
    }

    @Override
    public String toString() {
        return Arrays.toString(table);
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

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        Objects.requireNonNull(action);

        if (entrySet == null) {
            entrySet = entrySet();
        }

        int size = entrySet.size();

        for (Entry<K, V> cell : entrySet) {
            action.accept(cell.getKey(), cell.getValue());
            if (entrySet.size() != size) {
                throw new ConcurrentModificationException();
            }
        }
    }

    @Override
    public synchronized void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        Objects.requireNonNull(function);

        if (entrySet == null) {
            entrySet = entrySet();
        }

        int size = entrySet.size();

        for (Entry<K, V> cell : entrySet) {
            Objects.requireNonNull(
                    cell.setValue(function.apply(cell.getKey(), cell.getValue()))
            );
            if (entrySet.size() != size) {
                throw new ConcurrentModificationException();
            }
        }
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

        if (!Objects.equals(curValue, value)) {
            return false;
        }

        remove(key);
        return true;
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        if (oldValue == null) throw new NullPointerException();

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

    @Override
    public synchronized V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        Objects.requireNonNull(mappingFunction);

        int index = contains(key);
        if (index >= 0) {
            return table[index].getValue();
        } else {
            V newValue = mappingFunction.apply(key);

            if (newValue == null) {
                throw new NullPointerException("Computed value is null");
            }

            put(key, newValue);
            return newValue;
        }
    }

    @Override
    public synchronized V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);

        int index = contains(key);
        if (index >= 0) {
            Cell<K,V> cell = table[index];
            V newValue = remappingFunction.apply(key, cell.getValue());

            if (newValue == null) {
                throw new NullPointerException("Computed value is null");
            }

            cell.setValue(newValue);
            return newValue;
        }

        return null;
    }

    @Override
    public synchronized V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);

        int index = contains(key);

        if (index >= 0) {
            V oldValue = table[index].getValue();
            V newValue = remappingFunction.apply(key, oldValue);

            if (newValue != null)
                return put(key, newValue);
            else
                return remove(key);
        } else {
            V newValue = remappingFunction.apply(key, null);
            return put(key, newValue);
        }
    }


    @Override
    public synchronized V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);

        int index = contains(key);

        if (index >= 0) {
            Cell<K,V> cell = table[index];
            V newValue = remappingFunction.apply(cell.getValue(), value);
            if (newValue != null) {
                cell.setValue(newValue);
                return newValue;
            } else {
                return remove(key);
            }
        } else {
            return put(key, value);
        }

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

        Cell<K, V>[] subTable = Arrays.copyOf(table, oldCapacity);
        table = new Cell[capacity];
        deletedCells = new boolean[capacity];

        for (int i = 0; i < oldCapacity; i++) {
            if (subTable[i] != null) {
                int index = findEmptyIndex(subTable[i].getKey());
                table[index] = subTable[i];
            }
        }
    }

    private int findEmptyIndex(K key) {
        int n = -1;
        while (true) {
            n++;
            int index = (hash1(key) + n * hash2(key)) % (capacity - 3);
            if (table[index] == null) {
                return index;
            } else if (deletedCells[index]){
                deletedCells[index] = false;
                return  index;
            }
        }
    }

    private int contains(Object key) {
        if (key == null) throw new NullPointerException("HashTable does not permit null keys");
        int hash1 = hash1(key);
        int hash2 = hash2(key);
        int n = -1;
        while (n != capacity - 1) {
            n++;
            int index = (hash1 + n * hash2) % (capacity - 3);
            Cell<K, V> cell = table[index];
            if (cell != null && cell.getKey().equals(key)) {
                if (deletedCells[index]) {
                    return -1;
                } else {
                    return index;
                }
            }
        }
        return -1;
    }

    private <T> Iterator<T> getIterator(int type) {
        if (size == 0) {
            return Collections.emptyIterator();
        } else {
            return new HashIterator<T>(type);
        }
    }

    class HashIterator<T> implements Iterator<T> {
        boolean[] iterExistedCells = deletedCells;
        int index = -1;
        int count = 0;
        int iterSize = size;
        int type;

        HashIterator(int type) {
            this.type = type;
        }

        @Override
        public boolean hasNext() {
            return count < iterSize;
        }

        @Override
        public T next() {
            index++;
            while (index < capacity) {
                if (iterExistedCells[index]) {
                    Cell<K,V> cell = table[index];
                    count++;
                    return type == KEYS ? (T) cell.getKey() : (type == VALUES ? (T) cell.getValue() : (T) cell);
                } else {
                    index++;
                }
            }
            return null;
        }
    }

    private static class Cell<K, V> implements Map.Entry<K, V> {

        private K key;
        private V value;

        public Cell(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            this.value = value;
            return value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Cell<?, ?> cell = (Cell<?, ?>) o;
            return Objects.equals(key, cell.key) &&
                    Objects.equals(value, cell.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, value);
        }

        @Override
        public String toString() {
            return "Cell{" +
                    "key=" + key +
                    ", value=" + value +
                    '}';
        }
    }

}
