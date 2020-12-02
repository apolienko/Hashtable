import java.util.Map;
import java.util.Objects;

public class Cell<K, V> implements Map.Entry<K, V> {

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
