package software.ulpgc.edp;


public class Dictionary<K, V> implements Dict<K, V> {
    protected static class Entry<K, V> implements Dict.Entry<K, V>{
        private final K key;
        private V value;
        private final int keyHash;

        Entry(K newKey, V newValue, int keyHash) {
            this.key = newKey;
            this.value = newValue;
            this.keyHash = keyHash;
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
        public int getHashCode() {
            return keyHash;
        }

        @Override
        public String toString() {
            return key + ": " + value;
        }
    }
    private static final int MIN_SIZE = 1 << 4;
    private static final int MAX_SIZE = 1 << 30;
    private static final int PERTURB_SHIFT = 5;
    private int size;
    private int mask;
    private int threshold;
    private Entry<K, V>[] table;

    private static final float LOAD_FACTOR = 0.75F; //rehashing

    private static int tableSizeFor(int cap) {
        int n = -1 >>> Integer.numberOfLeadingZeros(cap - 1);
        return  (n < 0) ? 1 : (n >= MAX_SIZE) ? MAX_SIZE : n + 1;
    }

    public Dictionary() {
        this.size = 0;
        this.mask = MIN_SIZE - 1;
    }
    private static int hash(Object key) {
        return Math.abs(key.hashCode());
    }

    public Dictionary(int inititalCapacity) {
        if(inititalCapacity < 0)
            throw new IllegalArgumentException("Illegal initial capacity: " +
                    inititalCapacity);
        if(inititalCapacity > MAX_SIZE)
            inititalCapacity = MAX_SIZE;
        this.size = 0;
        this.threshold = tableSizeFor(inititalCapacity);
        this.mask = threshold - 1;

    }

    @Override
    public Dict.Entry<K, V>[] items() {
        if(size == 0) return null;
        @SuppressWarnings("unchecked")
        Entry<K, V>[] entryTab = (Entry<K, V>[]) new Entry[size];
        int i = 0;
        int j = 0;
        while(i < table.length && j < size) {
            if(table[i] == null) {
                i++;
            } else {
                entryTab[j] = table[i];
                i++;
                j++;
            }
        }
        return entryTab;
    }

    @Override
    public void add(K key, V value) {
        int hashC = hash(key);
        insert(key, hashC, value);
    }

    @Override
    public V get(K key) {
        int hashC = hash(key);
        Entry<K, V> entry = table[lookDict_string(key, hashC)];
        return entry == null ? null : entry.value;
    }

    @Override
    public boolean pop(K key) {
        int hashC = hash(key);
        int index = lookDict_string(key, hashC);
        Entry<K, V> entry = table[index];
        if(entry == null) return false;
        table[index] = null;
        size--;
        return true;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        Entry<K, V>[] tab;
        if((tab = table) != null && size > 0) {
            size = 0;
            for (int i = 0; i < tab.length; i++) tab[i] = null;
        }
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("{");
        boolean first = true;
        for (Dict.Entry<K, V> entry : table) {
            if(entry != null) {
                if(!first) result.append(", ");
                result.append(entry.getKey()).append(": ").append(entry.getValue());
                first = false;
            }
        }
        result.append("}");
        return result.toString();
    }

    private void insert(K key, int hash, V value) {
        if(table == null) resize();
        int index = lookDict_string(key, hash);
        Entry<K, V> entry = table[index];
        if(entry == null) {
            table[index] = new Entry<>(key, value, hash);
            if(++size > threshold)
                resize();
        } else {
            entry.value = value;
        }
    }

    private void resize() {
        Entry<K, V>[] oldTab = table;
        int oldCap = (oldTab == null) ? 0 : oldTab.length;
        int oldThr = threshold;
        int newCap, newThr = 0;
        if(oldCap > 0) {
            if(oldCap >= MAX_SIZE) {
                threshold = Integer.MAX_VALUE;
                return;
            }
            else if ((newCap = oldCap << 1) < MAX_SIZE &&
                    oldCap >= MIN_SIZE)
                newThr = oldThr << 1; // double threshold
        }
        else if (oldThr > 0) // initial capacity was placed in threshold
            newCap = oldThr;
        else {               // zero initial threshold signifies using defaults
            newCap = MIN_SIZE;
            newThr = (int)(LOAD_FACTOR * MIN_SIZE);
        }
        if (newThr == 0) {
            float ft = (float)newCap * LOAD_FACTOR;
            newThr = (newCap < MAX_SIZE && ft < (float)MAX_SIZE ?
                    (int)ft : Integer.MAX_VALUE);
        }
        threshold = newThr;
        @SuppressWarnings("unchecked")
        Entry<K, V>[] newTab = (Entry<K, V>[]) new Entry[newCap];
        table = newTab;
        mask = newCap - 1;
        if(oldTab != null) {
            for (int i = 0; i < oldCap; ++i) {
                Entry<K, V> e;
                if((e = oldTab[i]) != null) {
                    oldTab[i] = null;
                    newTab[lookDict_string(e.key, e.keyHash)] = e;
                }
            }
        }
    }

    /**
     * La primera parte de la resolución de colisiones utiliza
     * la siguiente recurrencia para visitar índices en la tabla:
     * <p>
     * j = ((5 * j) + 1) mod 2^i
     * <p>
     * Estrategia de resolución de colisiones
     * <p>
     *      Para cualquier j inicial en el rango 2**i, repetir esta fórmula 2**i veces genera cada número
     *      en el rango exactamente una vez.
     *      <h5>Ejemplo:</h5>
     *      Si tenemos una tabla de tamaño 2**3, el orden de exploración de índices es:
     * <p>
     *      {@code 0 -> 1 -> 6 -> 7 -> 4 -> 5 -> 2 -> 3 -> 0 (y luego se repite)}
     * <p>
     *     Si dos elementos colisionan en el índice 5, el siguiente lugar donde buscamos es 2 en lugar de 6,
     *     reduciendo el impacto de la colisión.
     * <p>
     *     Para mejorar la exploración, se usa un valor perturb,
     *     inicializado con el código hash completo. La fórmula se ajusta a:
     * <p>
     *     j = (5 * j) + 1 + perturb;
     *     perturb >>= PERTURB_SHIFT;
     * <p>
     *     Esto significa que el orden de exploración dependerá de todos
     *     los bits del hash y no solo de los i bits de menor orden.
     * <p>
     *     Si el proceso se repite muchas veces, perturb eventualmente
     *     se vuelve 0, y en ese caso, la búsqueda sigue la regla (5*j) + 1,
     *     asegurando que siempre se encuentre un espacio vacío.
     * <p>
     *      El valor PERTURB_SHIFT es un compromiso. Debe ser:
     *      - Pequeño para mantener la influencia de los bits altos
     *          del código hash en la secuencia de exploración.
     *      - Grande para que en casos problemáticos los bits altos
     *          afecten las primeras iteraciones.
     * <p>
     *      Las pruebas mostraron que 5 era el mejor valor para minimizar
     *      colisiones, aunque valores como 4 y 6 también eran aceptables.
     *
     *
     * @param key clave del par a buscar.
     * @param hash código hash de la clave necesaria en la recurrencia.
     * @return índice posición de la tabla.
     */
    private int lookDict_string(K key, int hash) {
        // Primera búsqueda del índice
        int i = Math.abs(hash & mask);
        Entry<K, V> entry = table[i];

        if(entry == null) { // Si el hueco está vacío...
            return i;
        } else {            // En el caso contrario...
            if(entry.keyHash == hash && entry.key.equals(key)) {    // ¿Coincide el valor de la clave y su hash?
                return i;                                           // Lo devuelvo
            }                                                       // Si no...
            for(int perturb = hash; ; perturb >>= PERTURB_SHIFT) {  // Iniciamos la recurrencia tantas veces hasta
                i = (Math.abs((i << 2) + i + perturb + 1)) & mask;  // encontrar un hueco vacío o uno con la misma clave
                entry = table[i];
                if(entry == null) {
                    return i;
                }
                else {
                    if (entry.keyHash == hash
                            && entry.key.equals(key))
                        return i;
                }
            }
        }

    }
}