package D_PriorityQueues;

import interfaces.Entry;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import java.util.stream.IntStream;

/**
 * An implementation of a priority queue using an array-based heap.
 */

public class HeapPriorityQueue<K, V> extends AbstractPriorityQueue<K, V> {
    protected ArrayList<Entry<K, V>> heap = new ArrayList<>();

    /**
     * Creates an empty priority queue based on the natural ordering of its keys.
     */
    public HeapPriorityQueue() {
        super();
    }

    /**
     * Creates an empty priority queue using the given comparator to order keys.
     *
     * @param comp comparator defining the order of keys in the priority queue
     */
    public HeapPriorityQueue(Comparator<K> comp) {
        super(comp);
    }

    /**
     * Creates a priority queue initialized with the respective key-value pairs. The
     * two arrays given will be paired element-by-element. They are presumed to have
     * the same length. (If not, entries will be created only up to the length of
     * the shorter of the arrays)
     *
     * @param keys   an array of the initial keys for the priority queue
     * @param values an array of the initial values for the priority queue
     */
    public HeapPriorityQueue(K[] keys, V[] values) {
        for (int i = 0; i < Math.min(keys.length, values.length) ; i++)
        {
            this.insert(keys[i], values[i]);
        }
    }

    // protected utilities
    protected int parent(int j) {
        return (j - 1)/2;
    }

    protected int left(int j) {
        return 2*j + 1;
    }

    protected int right(int j) {
        return 2*j + 2;
    }

    protected boolean hasLeft(int j) {
        if (2*j + 1 < heap.size() && heap.get(2*j + 1) != null)
        {
            return true;
        }
        return false;
    }

    protected boolean hasRight(int j) {
        if (2*j + 2 < heap.size() && heap.get(2*j + 2) != null)
        {
            return true;
        }
        return false;
    }

    /**
     * Exchanges the entries at indices i and j of the array list.
     */
    protected void swap(int i, int j) {
        V temp = heap.get(i).getValue();
        K tempKey = heap.get(i).getKey();
        heap.set(i, new PQEntry<>(heap.get(j).getKey(), heap.get(j).getValue()));

        heap.set(j, new PQEntry<>(tempKey, temp));
    }

    /**
     * Moves the entry at index j higher, if necessary, to restore the heap
     * property.
     */
    protected void upheap(int j) {
        if (compare(heap.get(j), heap.get(parent(j))) < 0) {
            swap(parent(j), j);
        }
    }

    /**
     * Moves the entry at index j lower, if necessary, to restore the heap property.
     */
    protected void downheap(int j) {
        if (hasLeft(j)) {
            if (compare(heap.get(j), heap.get(left(j))) > 0)
            {
                swap(left(j), j);
            }
        }

        if (hasRight(j)) {
            if (compare(heap.get(j), heap.get(right(j))) > 0)
            {
                swap(right(j), j);
            }
        }
    }

    public ArrayList<Entry<K, V>> heapsort()
    {
        ArrayList<Entry<K, V>> al = new ArrayList<>();
        int j;

        while (!heap.isEmpty())
        {
            j = 0;
            Entry<K,V> entry = this.removeMin();
            al.add(entry);
            if (size() != 0)
            {
                this.leafToRoot();
            }
            while (hasLeft(j) || hasRight(j))
            {
                downheap(j);
                j++;
            }
        }

        return al;
    }

    /**
     * Performs a bottom-up construction of the heap in linear time.
     */
    protected void heapify() {
        for (int i = size() - 1 ; i >= 0 ; i--)
        {
            downheap(i);
        }
    }

    public void PQSort(ArrayList<Integer> arr)
    {
        AbstractPriorityQueue<Integer, Integer> pq = new HeapPriorityQueue<>();

        while (!arr.isEmpty())
        {
            Integer key = arr.getFirst();
            pq.insert(key, key);
            arr.removeFirst();
        }

        while (!pq.isEmpty())
        {
            Integer e = pq.removeMin().getKey();
            arr.addLast(e);
        }
    }

    // public methods
    /**
     * Returns the number of items in the priority queue.
     *
     * @return number of items
     */
    @Override
    public int size() {
        return heap.size();
    }

    /**
     * Returns (but does not remove) an entry with minimal key.
     *
     * @return entry having a minimal key (or null if empty)
     */
    @Override
    public Entry<K, V> min() {
        return heap.getFirst();
    }

    /**
     * Inserts a key-value pair and return the entry created.
     *
     * @param key   the key of the new entry
     * @param value the associated value of the new entry
     * @return the entry storing the new key-value pair
     * @throws IllegalArgumentException if the key is unacceptable for this queue
     */
    @Override
    public Entry<K, V> insert(K key, V value) throws IllegalArgumentException {
        PQEntry<K, V> ent = new PQEntry<>(key, value);
        int index;

        heap.add(ent);
        index = heap.size() - 1;

        while (compare(heap.get(index), heap.get(parent(index))) < 0 && !isEmpty())
        {
            upheap(index);
            index = parent(index);
        }

        //System.out.println("Heap on call " + heap.size() + ": " + heap);
        return ent;
    }

    /**
     * Removes and returns an entry with minimal key.
     *
     * @return the removed entry (or null if empty)
     */
    @Override
    public Entry<K, V> removeMin() {
        Entry<K, V> del = heap.getFirst();
        heap.removeFirst();
        int j = 0;
        // Downheap new root (left child) if needed
        while (hasLeft(j) || hasRight(j))
        {
            downheap(j);
            j++;
        }
        return del;
    }

    public String toString() {
        return "Heap: " + heap.toString() + " - Size = " + heap.size();
    }

    /**
     * Used for debugging purposes only
     */
    private void sanityCheck() {
        for (int j = 0; j < heap.size(); j++) {
            int left = left(j);
            int right = right(j);
            //System.out.println("-> " +left + ", " + j + ", " + right);
            Entry<K, V> e_left, e_right;
            e_left = left < heap.size() ? heap.get(left) : null;
            e_right = right < heap.size() ? heap.get(right) : null;
            if (left < heap.size() && compare(heap.get(left), heap.get(j)) < 0) {
                System.out.println("Invalid left child relationship");
                System.out.println("=> " + e_left + ", " + heap.get(j) + ", " + e_right);
            }
            if (right < heap.size() && compare(heap.get(right), heap.get(j)) < 0) {
                System.out.println("Invalid right child relationship");
                System.out.println("=> " + e_left + ", " + heap.get(j) + ", " + e_right);
            }
        }
    }

    public void heapAdd(K key, V value)
    {
        PQEntry<K, V> ent = new PQEntry<>(key, value);
        heap.add(ent);
    }

    public void leafToRoot()
    {
        Entry<K, V> ent = heap.getLast();
        heap.addFirst(ent);
        heap.removeLast();
    }

    public static void getPQTimes()
    {
        Random rnd = new Random();
        rnd.setSeed(1024);
        int n_min = 1000, n_max = 1000000, n_samples = 80;
        double alpha = ( (Math.log(n_max) / Math.log(n_min)) - 1) / (n_samples-1);
        for(int i = 0; i < n_samples; ++i) {
            double t = System.currentTimeMillis();
            int n = (int) Math.pow(n_min, (1 + i * alpha));
            Integer[] arr = IntStream.rangeClosed(1, n).boxed().toArray(Integer[]::new);
            Runnable worker = () -> {
                HeapPriorityQueue<Integer, Integer> pq = new HeapPriorityQueue<>(arr, arr);
                for(int k = 0; k < arr.length; ++k) {
                    arr[k] = pq.removeMin().getKey();
                }
            };

            double result = System.currentTimeMillis() - t;

            System.out.println(i + "\t" + n + "\t" + result + "\t" );
        }
    }

    public static void getHeapSortTimes()
    {
        Random rnd = new Random();
        rnd.setSeed(1024);
        int n_min = 1000, n_max = 1000000, n_samples = 80;
        double alpha = ( (Math.log(n_max) / Math.log(n_min)) - 1) / (n_samples-1);
        for(int i = 0; i < n_samples; ++i) {
            double t = System.currentTimeMillis();

            int n = (int) Math.pow(n_min, (1 + i * alpha));
            Integer[] arr = IntStream.rangeClosed(1, n).boxed().toArray(Integer[]::new);
            Runnable worker = () -> {
                HeapPriorityQueue<Integer, Integer> pq = new HeapPriorityQueue<>(arr, arr);
                pq.heapsort();
            };

            double result = System.currentTimeMillis() - t;

            System.out.println(i + "\t" + n + "\t" + result + "\t" );
        }
    }

    public static void main(String[] args) {
        double start = System.currentTimeMillis();
        getPQTimes();
        System.out.println("PQTime = " + (System.currentTimeMillis() - start));
        getHeapSortTimes();

        /*
        Integer[] arr = new Integer[]{2, 5, 16, 4, 10, 23, 39, 18, 26, 15};
        Integer[] arr2 = new Integer[]{1,5,2,7,2,7,9,3,4,8,4,1,4};

        HeapPriorityQueue<Integer, String> pq = new HeapPriorityQueue<>();
        HeapPriorityQueue<Integer, String> pq2 = new HeapPriorityQueue<>();
        for (Integer i : arr) {
            pq.insert(i, Integer.toString(i));
            pq2.heapAdd(i, Integer.toString(i));
        }

        // Show heapify
        System.out.println("Unheaped " + pq2);
        pq2.heapify();
        System.out.println("Heaped " + pq2);

        // Show PQSort
        ArrayList<Integer> toSort = new ArrayList<>();
        Collections.addAll(toSort, arr2);
        //pq.PQSort(toSort);
        //System.out.println(toSort);

        // Show heapsort
        //ArrayList<Entry<Integer, String>> sortedHeap = pq2.heapsort();
        //System.out.println(sortedHeap);
        */
    }
}

class HeapPriorityQueueTest {
    @Test
    void testSize() {
        HeapPriorityQueue<Integer, String> pq = new HeapPriorityQueue<>();
        int n = 10;
        for (int i = 0; i < n; ++i) {
            pq.insert(i, Integer.toString(i));
        }

        System.out.println(pq.heap);
        assertEquals(n, pq.size());
    }

    @Test
    void testMin() {
        Integer[] arr = new Integer[]{35, 26, 15, 24, 33, 4, 12, 1, 23, 21, 2, 5};
        HeapPriorityQueue<Integer, String> pq = new HeapPriorityQueue<>();

        for (Integer i : arr) pq.insert(i, Integer.toString(i));

        assertEquals(1, pq.min().getKey());
        assertEquals("1", pq.min().getValue());
    }

    @Test
    void testInsert() {
        Integer[] arr = new Integer[]{35, 26, 15, 24, 33, 4, 12, 1, 23, 21, 2, 5};
        HeapPriorityQueue<Integer, String> pq = new HeapPriorityQueue<>();

        for (Integer i : arr) pq.insert(i, Integer.toString(i));

        assertEquals(12, pq.size());
        assertEquals("[1, 2, 5, 23, 4, 12, 15, 35, 24, 33, 21, 26]", pq.toString());
    }

    @Test
    void testRemoveMin() {
        Integer[] arr = new Integer[]{35, 26, 15, 24, 33, 4, 12, 1, 23, 21, 2, 5};
        HeapPriorityQueue<Integer, String> pq = new HeapPriorityQueue<>();

        for (Integer i : arr) pq.insert(i, Integer.toString(i));

        assertEquals(1, pq.removeMin().getKey());
        assertEquals(11, pq.size());
        assertEquals(2, pq.min().getKey());
    }

    @Test
    void testToString() {
        Integer[] arr = new Integer[]{35, 26, 15, 24, 33, 4, 12, 1, 23, 21, 2, 5};
        HeapPriorityQueue<Integer, String> pq = new HeapPriorityQueue<>();

        for (Integer i : arr) pq.insert(i, Integer.toString(i));

        assertEquals("[1, 2, 5, 23, 4, 12, 15, 35, 24, 33, 21, 26]", pq.toString());
    }
}
