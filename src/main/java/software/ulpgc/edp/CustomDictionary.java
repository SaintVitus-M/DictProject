package software.ulpgc.edp;


public class CustomDictionary<K, V> {
    //Definimos un objeto clave valor para almacenar dentro del diccionario
    protected static class Entry<K, V> {
        K key;
        V value;
        int keyHash;

        Entry(K newKey, V newValue) {
            this.key = newKey;
            this.value = newValue;
            this.keyHash = hash(key);
        }
    }

    private static final int MIN_SIZE = 8;
    private static final int PERTURB_SHIFT = 8;
    private int fill;
    private int used;
    private int mask;
    private int capacity;
    private Object dummy = null;
    private Entry<K, V>[] table;
    private static final double LOAD_FACTOR = 0.75; //rehashing

    //Constructor
    public CustomDictionary() {
        this.fill = 0;
        this.used = 0;
        this.mask = MIN_SIZE - 1;
        this.capacity = MIN_SIZE;
        this.table = new Entry[MIN_SIZE];
    }

    private static int hash(Object key) {
        return Math.abs(key.hashCode());
    }

    //Duplicamos la capacidad en caso de estar lleno
    private void dictResize(int minSize) {
        int newCapacity = capacity * 2;
        Entry<K, V>[] oldEntries = table;
        table = new Entry[newCapacity];
        capacity = newCapacity;
        fill = 0;
        used = 0;  //Se reinicia porque lo vuelve a calcular

        for (Entry<K, V> entry : oldEntries) {
            if (entry != null && !entry.deleted) {
                add(entry.key, entry.value);  //Reinserta en la nueva tabla
            }
        }
    }
    
    public void add(K key, V value) {
        int hashC = hash(key);
        int n_used = used;
        insert(key, hashC, value);
        if(used > n_used && fill*3 >= (mask+1)*2)
            dictResize((used > 50000 ? 2 : 4) * used);
    }

    private void insert(K key, int hash, V value) {
        Entry<K, V> ep;

        ep = lookDict_string(key, hash);

        if(ep.value != null) {
            ep.value = value;
        }
        else {
            fill++;
            ep.key = key;
            ep.keyHash = hash;
            ep.value = value;
            used++;
        }
    }


    public V get(K key) {
    
    }
    
    private Entry<K, V> lookDict_string(K key, int hash) {
        int i;
        int perturb;
        Entry<K, V> freeslot;
        Entry<K, V> ep;

        i = hash & mask;
        ep = table[i];
        if(ep.key == key) {
            return ep;
        if(ep.key == dummy) {
            freeslot = ep;
        } else {
            if(ep.keyHash == hash && ep.key.equals(key)) {
                return ep;
                freeslot = null;
            }
        }
        for(perturb = hash; ; perturb >>= PERTURB_SHIFT) {
            i = (i << 2) + i + perturb + 1;
            ep = table[i & mask];
            if(ep.key == null) {
                return freeslot == null ? ep : freeslot;
            if(ep.key == key
                    || (ep.keyHash == hash
                    && ep.key != dummy
                    && ep.key.equals(key)))
                return ep;
            if(ep.key == dummy && freeslot == null) {
                freeslot = ep;
            }
        }
    }
            
    public boolean remove(K key) {
        long hashC = hash(key);
        for (int i = 0; i < capacity; i++) {
            int probeIndex = ((int) hashC + i) % capacity;
            if (table[probeIndex] == null) {
                return false;
            }
            if (!table[probeIndex].deleted && table[probeIndex].key.equals(key)) {
                table[probeIndex].deleted = true;
                size--;
                return true;
            }
        }
        return false;
    }

    public boolean containsKey(K key) {
        return get(key) != null;
    }
    
    public int size() {
        return size;
    }
    
    public void clear() {
        table = new Entry[capacity];
        size = 0;
    }
    
    public K[] keys() {
        K[] keysArray = (K[]) new Object[size];
        int index = 0;
        for (Entry<K, V> entry : table) {
            if (entry != null && !entry.deleted) {
                keysArray[index++] = entry.key;
            }
        }
        return keysArray;
    }
    
    public V[] values() {
        V[] valuesArray = (V[]) new Object[size];
        int index = 0;
        for (Entry<K, V> entry : table) {
            if (entry != null && !entry.deleted) {
                valuesArray[index++] = entry.value;
            }
        }
        return valuesArray;
    }
}