package software.ulpgc.edp;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class Dictionary<K, V>  implements Iterable<Dictionary.Entry<K, V>>{
    protected static class Entry<K, V> {
        K key;
        V value;
        Entry<K, V> next;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }

        @Override
        public String toString() {
            return key + ": " + value;
        }
    }

    private final Entry<K, V>[] table;
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
    public Iterator<Entry<K, V>> iterator() {
        return new DictionaryIterator();
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

    public V get(K key) {
        int index = hash(key);
        for (Entry<K, V> e = table[index]; e != null; e = e.next) {
            if (e.key != null && e.key.equals(key)) {
                return e.value;
            }
        }
        return null;
    }

    public void remove(K key) {
        int index = hash(key);
        if(table[index] == null) return;
        else {
            Entry<K, V> current = table[index];
            while(current.next != null) {
                if(current.next.key.equals(key)) {
                    current.next = current.next.next;
                }
                current = current.next;
            }
        }
        size--;
    }

    public int getSize() {
        return size;
    }

    private class DictionaryIterator implements Iterator<Entry<K, V>> {
        private int index = 0;
        private Entry<K, V> current = null;

        public DictionaryIterator() {
            advanceToNextBucket();
        }

        private void advanceToNextBucket() {
            while (index < table.length && table[index] == null) {
                index++;
            }
            if (index < table.length) {
                current = table[index];
            } else {
                current = null;
            }
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Entry<K, V> next() {
            if(!hasNext()) throw new NoSuchElementException();
            Entry<K, V> entryToReturn = current;
            if(current.next != null) {
                current = current.next;
            } else {
                index++;
                advanceToNextBucket();
            }
            return entryToReturn;
        }
    }

}