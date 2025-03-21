package software.ulpgc.edp;

/**
 * Interfaz que representa un diccionario genérico (mapa de claves y valores)
 * similar a un dict de Python.
 *
 * @param <K> Tipo de dato para las claves.
 * @param <V> Tipo de dato para los valores.
 */
public interface Dict<K, V> {
    /**
     * Interfaz que representa una entrada (par clave-valor) para la tabla del diccionario.
     *
     * @param <K> Tipo para la clave.
     * @param <V> Tipo para el valor.
     */
    interface Entry<K, V> {
        /**
         * Obtiene la clave de esta entrada.
         *
         * @return Clave asociada a la entrada.
         */
        K getKey();

        /**
         * Obtiene el valor de esta entrada.
         *
         * @return Valor asociado a la entrada.
         */
        V getValue();

        /**
         * Obtiene el código hash asociado a la clave de esta entrada.
         *
         * @return Código hash de la clave.
         */
        int getHashCode();
    }

    /**
     * Agrega una nueva clave y su valor asociado al diccionario.
     *
     * @param key La clave que se desea agregar.
     * @param value El valor asociado a la clave.
     */
    void add(K key, V value);

    /**
     * Obtiene el valor asociado a la clave especificada.
     *
     * @param key La clave cuyo valor se desea obtener.
     * @return El valor asociado a la clave o {@code null} si la clave no existe.
     */
    V get(K key);

    /**
     * Elimina la clave y su valor asociado del diccionario.
     *
     * @param key La clave a eliminar.
     * @return {@code true} si la clave fue eliminada, {@code false} en caso contrario.
     */
    boolean pop(K key);

    /**
     * Obtiene la cantidad de elementos almacenados en el diccionario.
     *
     * @return El número de elementos almacenados en el diccionario.
     */
    int size();

    /**
     * Elimina todos los elementos del diccionario.
     */
    void clear();

    /**
     * Obtiene un arreglo con todas las entradas (par clave-valor) actuales del diccionario.
     *
     * @return
     */
    Dict.Entry<K, V>[] items();

    /**
     * Verifica si el diccionario está vacío.
     *
     * @return {@code true} si el diccionario no contiene elementos, {@code false} en caso contrario.
     */
    boolean isEmpty();

    /**
     * Verifica si la clave a buscar se encuentra dentro del diccionario.
     *
     * @param key La clave a buscar.
     * @return {@code true} si el diccionario contiene la clave a buscar, {@code false} en caso contrario.
     */
    boolean containsKey(K key);
}
