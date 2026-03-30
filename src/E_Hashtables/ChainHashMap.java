package E_Hashtables;

import D_PriorityQueues.DefaultComparator;
import interfaces.Entry;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
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
        return Arrays.toString(table);
        //return entrySet().toString();
    }

    public static void sortValues(ChainHashMap<String, Integer> map) {
        int[] not_null = new int[map.size()];
        int index = 0;

        for (int i = 0 ; i < map.table.length ; i++) {
            if (map.table[i] != null){
                map.table[i].sortMap(map.table[i]);
                not_null[index] = i;
                index++;
            }
        }

        //System.out.println("UnsortedMaps sorted:\n" + map);
        for (int i = 0 ; i < not_null.length ; i++) {
            //System.out.println("Map[" + i + "] = " + map.table[i]);
            for (int j = 0 ; j < not_null.length - 1 ; j++) {
                chainSwap(map, not_null[j], not_null[j + 1]);
            }
        }
    }

    private static void chainSwap(ChainHashMap<String, Integer> map, int i, int j) {
        if (map.table[i].getFirst() > map.table[j].getFirst()){
            UnsortedTableMap<String, Integer> temp = map.table[i];
            map.table[i] = map.table[j];
            map.table[j] = temp;
        }
    }

    private static ChainHashMap<String, Integer> wordCounter(String path) throws FileNotFoundException {
        File f = new File(path); // check the path to the file
        ChainHashMap<String, Integer> counter = new ChainHashMap<>();

        Scanner scanner = new Scanner(f);
        while (scanner.hasNext())
        { // read the file word at a time
            String word = scanner.next();
            //System.out.println("word:" + word);
            if (counter.get(word) == null) {
                counter.put(word, 1);
            }
            else {
                counter.put(word, counter.get(word) + 1);
            }
        }
        return counter;
    }

    private static int sumCollisions(int[] collisions){
        int c = 0;
        for (int i = 0 ; i < collisions.length ; i++) {
            for (int j = i + 1 ; j < collisions.length ; j++) {
                if (collisions[i] == collisions[j] && collisions[i] != 0) {
                    System.out.println("c = " + c + ": " + i + " = " + j + " where " + collisions[i] + " = " + collisions[j]);
                    c++;
                }
            }
        }
        return c;
    }

    public static int poly_col(ChainHashMap<String, Integer> map, int a) {
        int[] collisions = new int[map.size()];
        int h = 0;

        for (UnsortedTableMap<String, Integer> m : map.table) {
            if (m == null) {
                continue;
            }
            for (MapEntry<String, Integer> ent : m.getTable()) {
                collisions[h] = map.hash_poly(ent.getKey(), a);
                h++;
            }
        }

        System.out.println("Hashes: " + Arrays.toString(collisions));
        return sumCollisions(collisions);
    }

    public static int cyclic_col(ChainHashMap<String, Integer> map, int shift) {
        int[] collisions = new int[map.size()];
        int h = 0;

        for (UnsortedTableMap<String, Integer> m : map.table) {
            if (m == null) {
                continue;
            }
            for (MapEntry<String, Integer> ent : m.getTable()) {
                collisions[h] = map.hash_cyclic(ent.getKey(), shift);
                h++;
            }
        }

        System.out.println("Cyclic Hashes: " + Arrays.toString(collisions));
        return sumCollisions(collisions);
    }

    public static int oldCol(ChainHashMap<String, Integer> map) {
        int[] collisions = new int[map.size()];
        int h = 0;

        for (UnsortedTableMap<String, Integer> m : map.table) {
            if (m == null) {
                continue;
            }
            for (MapEntry<String, Integer> ent : m.getTable()) {
                collisions[h] = map.hashOldCode(ent.getKey());
                h++;
            }
        }

        System.out.println("Old Java Hashes: " + Arrays.toString(collisions));
        return sumCollisions(collisions);
    }

    private int hash_poly(String s, int a) {
        int h = 0;
        int n = s.length();
        for(int i = 0; i < n; i++) {
            char s_i = s.charAt(i);
            int v = s_i * ((int) Math.pow(a, n - i - 1));
            h += v;
        }
        return h;
    }
    private int hash_cyclic(String s, int shift) {
        int h = 0;
        for (int i = 0; i < s.length(); ++i) {
            h = (h << shift) | (h >>> (32 - shift));
            h += s.charAt(i);
        }
        return h;
    }
    private int hashOldCode(String s) {
        int hash = 0;
        int skip = Math.max(1, s.length() / 8);
        for (int i = 0; i < s.length(); i += skip) {
            hash = (hash * 37) + s.charAt(i);
        }
        return hash;
    }
    
    public static void main(String[] args) throws FileNotFoundException {
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

        /*
        // "C:\Users\Senan\OneDrive\Documents\College\2nd Yr Spring\COMP20280 - Data Structures\sample_text.txt"
        // "C:\Users\Senan\OneDrive\Documents\College\2nd Yr Spring\COMP20280 - Data Structures\words.txt"
        ChainHashMap<String, Integer> counter = wordCounter("C:\\Users\\Senan\\OneDrive\\Documents\\College\\2nd Yr Spring\\COMP20280 - Data Structures\\sample_text.txt");
        System.out.println(counter);

        sortValues(counter);
        //System.out.println(counter);

        System.out.println("Collisions with poly_acc, a = 41: " + poly_col(counter, 41));
        System.out.println("Collisions with poly_acc, a = 17: " + poly_col(counter, 17));
        System.out.println("Collisions with cyclic shift = 7: " + cyclic_col(counter, 7));
        System.out.println("Collisions with old java hash method: " + oldCol(counter));


        for (int i = 0 ; i < 32 ; i++) {
            System.out.println("Collisions with cyclic shift " + i + ": " + cyclic_col(counter, i));
        }
        */
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
