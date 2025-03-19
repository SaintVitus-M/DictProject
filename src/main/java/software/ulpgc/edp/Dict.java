package software.ulpgc.edp;

public interface Dict<K, V> {
    interface Entry<K, V> {
        K getKey();
        V getValue();
        int getHashCode();
    }
    void add(K key, V value);
    V get(K key);
    boolean pop(K key);
    int size();
    void clear();
    Dict.Entry<K, V>[] items();
    boolean isEmpty();
}
