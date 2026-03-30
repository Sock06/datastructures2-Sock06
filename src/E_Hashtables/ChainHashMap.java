package E_Hashtables;

import D_PriorityQueues.DefaultComparator;
import interfaces.Entry;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/*
 * Map implementation using hash table with separate chaining.
 */

public class ChainHashMap<K, V> extends AbstractHashMap<K, V> {
    // a fixed capacity array of UnsortedTableMap that serve as buckets
    private UnsortedTableMap<K, V>[] table; // initialized within createTable

    /**
     * Creates a hash table with capacity 11 and prime factor 109345121.
     */
    public ChainHashMap() {
        super();
    }

    /**
     * Creates a hash table with given capacity and prime factor 109345121.
     */
    public ChainHashMap(int cap) {
        super(cap);
    }

    /**
     * Creates a hash table with the given capacity and prime factor.
     */
    public ChainHashMap(int cap, int p) {
        super(cap, p);
    }

    /**
     * Creates an empty table having length equal to current capacity.
     */
    @Override
    @SuppressWarnings({"unchecked"})
    protected void createTable() {
        table = new UnsortedTableMap[capacity];
    }

    /**
     * Returns value associated with key k in bucket with hash value h. If no such
     * entry exists, returns null.
     *
     * @param h the hash value of the relevant bucket
     * @param k the key of interest
     * @return associate value (or null, if no such entry)
     */
    @Override
    protected V bucketGet(int h, K k) {
        if (this.table[h] == null) {
            return null;
        }
        return this.table[h].get(k);
    }

    /**
     * Associates key k with value v in bucket with hash value h, returning the
     * previously associated value, if any.
     *
     * @param h the hash value of the relevant bucket
     * @param k the key of interest
     * @param v the value to be associated
     * @return previous value associated with k (or null, if no such entry)
     */
    @Override
    protected V bucketPut(int h, K k, V v) {
        if (table[h] != null) {
            if (table[h].get(k) != null) {
                V old = table[h].get(k);
                table[h].put(k, v);
                return old;
            }
            else {
                table[h].put(k, v);
                return null;
            }
        }
        else {
            table[h] = new UnsortedTableMap<>();
            table[h].put(k, v);
            return null;
        }
    }

    /**
     * Removes entry having key k from bucket with hash value h, returning the
     * previously associated value, if found.
     *
     * @param h the hash value of the relevant bucket
     * @param k the key of interest
     * @return previous value associated with k (or null, if no such entry)
     */
    @Override
    protected V bucketRemove(int h, K k) {
        if (table[h] != null){
            if (table[h].get(k) != null) {
                return table[h].remove(k);
            }
        }
        return null;
    }


    /**
     * Returns an iterable collection of all key-value entries of the map.
     *
     * @return iterable collection of the map's entries
     */
    @Override
    public Iterable<Entry<K, V>> entrySet() {
        /*
        for each element in (UnsortedTableMap []) table
            for each element in bucket:
                print element
        */
        ArrayList<Entry<K, V>> entries = new ArrayList<>();
        for (UnsortedTableMap<K, V> tm : table) {
            if (tm != null) {
                for (Entry<K, V> e : tm.entrySet()) {
                    entries.add(e);
                }
            }
        }
        return entries;
    }

    public String toString() {
        return entrySet().toString();
    }

    public static void sortValues(ChainHashMap<String, Integer> map) {
        for (int i = 0 ; i < map.table.length ; i++) {
            if (map.table[i] != null){
                map.table[i].sortMap(map.table[i]);
                if (i > 0){
                    if (map.table[i].getFirst() < map.table[i - 1].getFirst()){

                    }
                }
            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        /*
        ChainHashMap<String, Integer> map = new ChainHashMap<>();

        int n = 10;
        for (int i = 0; i < n; ++i) {
            map.put(Integer.toString(i), i);
        }

        System.out.println(map);
        int r = map.remove("5");
        System.out.println("Size: " + map.size() + ", removed " + r);

        int[] hash = map.getHashValues(new int[]{12, 44, 13, 88, 23, 94, 11, 39, 20, 16, 5});
        System.out.println("Q4 getHashValues:" + Arrays.toString(hash));

        ChainHashMap<Integer, String> m = new ChainHashMap<>();
        m.put(1, "One");
        m.put(10, "Ten");
        m.put(11, "Eleven");
        m.put(20, "Twenty");

        System.out.println("m: " + m);

        m.remove(11);
        System.out.println("m: " + m);
        */

        File f = new File("C:\\Users\\Senan\\OneDrive\\Documents\\sample_2.txt"); // check the path to the file
        ChainHashMap<String, Integer> counter = new ChainHashMap<>();

        Scanner scanner = new Scanner(f);
        while (scanner.hasNext()) { // read the file word at a time
            String word = scanner.next();
            //System.out.println("word:" + word);
            if (counter.get(word) == null) {
                counter.put(word, 1);
            }
            else {
                counter.put(word, counter.get(word) + 1);
            }
        }

        System.out.println(counter);
        sortValues(counter);
        System.out.println(counter);
    }
}



class ChainHashMapTest {
    @Test
    void testSize() {
        ChainHashMap<Integer, String> map = new ChainHashMap<>();

        int n = 10;
        for (int i = 0; i < n; ++i) {
            map.put(i, Integer.toString(i));
        }
        assertEquals(n, map.size());
    }

    @Test
    void testGet() {
        ChainHashMap<String, Integer> map = new ChainHashMap<>();

        int n = 10;
        for (int i = 0; i < n; ++i) {
            map.put(Integer.toString(i), i);
        }

        assertEquals(5, map.get("5"));
        assertEquals(2, map.get("2"));
    }

    @Test
    void testRemove() {
        ChainHashMap<String, Integer> map = new ChainHashMap<>();

        int n = 10;
        for (int i = 0; i < n; ++i) {
            map.put(Integer.toString(i), i);
        }
        assertEquals(5, map.remove("5"));
        assertEquals(n - 1, map.size());
    }

    @Test
    void testPut() {
        ChainHashMap<String, Integer> map = new ChainHashMap<>();

        int n = 10;
        for (int i = 0; i < n; ++i) {
            map.put(Integer.toString(i), i);
        }
        assertEquals(n, map.size());
    }

    @Test
    void testIsEmpty() {
        ChainHashMap<String, Integer> map = new ChainHashMap<>();

        assertTrue(map.isEmpty());

        int n = 10;
        for (int i = 0; i < n; ++i) {
            map.put(Integer.toString(i), i);
        }
        assertFalse(map.isEmpty());
    }

    @Test
    void testKeySet() {
        ChainHashMap<String, Integer> map = new ChainHashMap<>();

        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);
        ArrayList<String> buf = new ArrayList<>();
        for (String s : map.keySet()) buf.add(s);
        buf.sort(new DefaultComparator<>());
        assertEquals("[one, three, two]", buf.toString());
    }

    @Test
    void testValues() {
        ChainHashMap<String, Integer> map = new ChainHashMap<>();

        int n = 10;
        for (int i = 0; i < n; ++i) {
            map.put(Integer.toString(i), i);
        }
        ArrayList<Integer> buf = new ArrayList<>();
        for (Integer s : map.values()) buf.add(s);
        buf.sort(new DefaultComparator<>());
        assertEquals("[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]", buf.toString());
    }
}
