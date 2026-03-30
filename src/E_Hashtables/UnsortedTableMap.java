package E_Hashtables;

import interfaces.Entry;

import java.util.*;
/**
 * An implementation of a map using an unsorted table.
 */

public class UnsortedTableMap<K, V> extends AbstractMap<K, V> {

    /**
     * Underlying storage for the map of entries.
     */
    private final ArrayList<MapEntry<K, V>> table = new ArrayList<>();

    /**
     * Constructs an initially empty map.
     */
    public UnsortedTableMap() {
    }

    // private utility

    /**
     * Returns the index of an entry with equal key, or -1 if none found.
     */
    private int findIndex(K key) {
        for (MapEntry<K, V> me : this.table) {
            if (me.getKey().equals(key)) {
                return this.table.indexOf(me);
            }
        }
        return -1;
    }

    // public methods

    /**
     * Returns the number of entries in the map.
     *
     * @return number of entries in the map
     */
    @Override
    public int size() {
        return table.size();
    }

    public ArrayList<MapEntry<K, V>> getTable() {
        return table;
    }

    /**
     * Returns the value associated with the specified key, or null if no such entry
     * exists.
     *
     * @param key the key whose associated value is to be returned
     * @return the associated value, or null if no such entry exists
     */
    @Override
    public V get(K key) {
        int i = findIndex(key);
        if (i < 0) {
            return null;
        }
        return this.table.get(i).getValue();
    }

    public V getFirst() {
        return this.table.getFirst().getValue();
    }

    public void sortMap(UnsortedTableMap<String, Integer> map) {
        for (int i = 0 ; i < map.size() ; i++) {
            for (int j = 0 ; j < map.size() - 1 ; j++) {
                if (map.table.get(j).getValue() > map.table.get(j + 1).getValue()) {
                    swap(map, j);
                }
            }
        }
    }

    private void swap(UnsortedTableMap<String, Integer> map, int index) {
        MapEntry<String, Integer> temp = map.table.get(index);

        map.table.set(index, map.table.get(index + 1));
        map.table.set(index + 1, temp);
    }

    /**
     * Associates the given value with the given key. If an entry with the key was
     * already in the map, this replaced the previous value with the new one and
     * returns the old value. Otherwise, a new entry is added and null is returned.
     *
     * @param key   key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @return the previous value associated with the key (or null, if no such
     * entry)
     */
    @Override
    public V put(K key, V value) {
        int i = findIndex(key);
        MapEntry<K, V> me;

        if (i != -1) {
            me = this.table.get(i);
            V old_value = me.getValue();
            me.setValue(value);
            return old_value;
        }
        else {
            me = new MapEntry<>(key, value);
            this.table.addLast(me);
            return null;
        }
    }

    /**
     * Removes the entry with the specified key, if present, and returns its value.
     * Otherwise, does nothing and returns null.
     *
     * @param key the key whose entry is to be removed from the map
     * @return the previous value associated with the removed key, or null if no
     * such entry exists
     */
    @Override
    public V remove(K key) {
        int i = findIndex(key);
        if (i != -1) {
            V old_value = this.table.get(findIndex(key)).getValue();
            this.table.remove(i);
            return old_value;
        }
        return null;
    }

    // ---------------- nested EntryIterator class ----------------
    private class EntryIterator implements Iterator<Entry<K, V>> {
        private int j = 0;

        public boolean hasNext() {
            return j < table.size();
        }

        public Entry<K, V> next() {
            if (j == table.size())
                throw new NoSuchElementException("No further entries");
            return table.get(j++);
        }

        public void remove() {
            throw new UnsupportedOperationException("remove not supported");
        }
    } // ----------- end of nested EntryIterator class -----------

    // ---------------- nested EntryIterable class ----------------
    private class EntryIterable implements Iterable<Entry<K, V>> {
        public Iterator<Entry<K, V>> iterator() {
            return new EntryIterator();
        }
    } // ----------- end of nested EntryIterable class -----------

    /**
     * Returns an iterable collection of all key-value entries of the map.
     *
     * @return iterable collection of the map's entries
     */
    @Override
    public Iterable<Entry<K, V>> entrySet() {
        return new EntryIterable();
    }

    public String toString() {
        return table.toString();
    }

    public static void main(String[] args) {
        UnsortedTableMap<String, String> utm = new UnsortedTableMap<>();

        for (int i = 0 ; i < 15 ; i++) {
            utm.put(Integer.toString(i), ("String: " + i));
        }

        System.out.println(utm);
        String v1 = utm.get("6");
        String v2 = utm.remove("8");
        System.out.println("Val get: " + v1 + ", Val removed: " + v2 + "\n" + utm);
    }
}

