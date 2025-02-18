package software.ulpgc.edp;

import java.util.Arrays;
import java.util.Objects;

public class Dictionary<K, V> {
    private static class Entry<K, V> {
        K key;
        V value;
        Entry<K, V> next;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }

    private Entry<K, V>[] table;
    private int size;
    private static final int INITIAL_CAPACITY = 16;

    @SuppressWarnings("unckecked")
    public Dictionary() {
        this.table = new Entry[INITIAL_CAPACITY];
        this.size = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Dictionary<?, ?> that = (Dictionary<?, ?>) o;
        return size == that.size && Objects.deepEquals(table, that.table);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.hashCode(table), size);
    }

    private int hash(K key) {
        return Math.abs(key.hashCode() % table.length);
    }

    public void put(K key, V value) {
        int index = hash(key);
        Entry<K, V> newEntry = new Entry<>(key, value);

        if(table[index] == null) {
            table[index] = newEntry;
        } else {
            Entry<K, V> current = table[index];
            while(current != null) {
                if(current.key.equals(key)) {
                    current.value = value;
                    return;
                }
                if(current.next == null) {
                    current.next = newEntry;
                    break;
                }
                current = current.next;
            }
        }
        size++;
    }

    public void remove() {

    }
}