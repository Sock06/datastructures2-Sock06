package C_Trees;

import interfaces.Entry;
import interfaces.Position;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

/**
 * An implementation of a sorted map using a binary search tree.
 */

public class TreeMap<K, V> extends AbstractSortedMap<K, V> {

    // ---------------- nested BalanceableBinaryTree class ----------------

    /**
     * A specialised version of the LinkedBinaryTree class with additional mutators
     * to support binary search tree operations, and a specialized node class that
     * includes an auxiliary instance variable for balancing data.
     */
    protected static class BalanceableBinaryTree<K, V> extends LinkedBinaryTree<Entry<K, V>> {
        // -------------- nested BSTNode class --------------
        // this extends the inherited LinkedBinaryTree.Node class
        protected static class BSTNode<E> extends Node<E> {
            int aux = 0;

            BSTNode(E e, Node<E> parent, Node<E> leftChild, Node<E> rightChild) {
                super(e, parent, leftChild, rightChild);
            }

            public int getAux() {
                return aux;
            }

            public void setAux(int value) {
                aux = value;
            }
        } // --------- end of nested BSTNode class ---------

        // positional-based methods related to aux field
        public int getAux(Position<Entry<K, V>> p) {
            return ((BSTNode<Entry<K, V>>) p).getAux();
        }

        public void setAux(Position<Entry<K, V>> p, int value) {
            ((BSTNode<Entry<K, V>>) p).setAux(value);
        }

        // Override node factory function to produce a BSTNode (rather than a Node)
        @Override
        protected Node<Entry<K, V>> createNode(Entry<K, V> e, Node<Entry<K, V>> parent, Node<Entry<K, V>> left,
                                               Node<Entry<K, V>> right) {
            return new BSTNode<>(e, parent, left, right);
        }

        /**
         * Relinks a parent node with its oriented child node.
         */
        private void relink(Node<Entry<K, V>> parent, Node<Entry<K, V>> child, boolean makeLeftChild) {
            child.setParent(parent);
            if (makeLeftChild) {
                parent.setLeft(child);
            }
            else {
                parent.setRight(child);
            }
        }

        /**
         * Rotates Position p above its parent. Switches between these configurations,
         * depending on whether p is a or p is b.
         *
         * <pre>
         *          b                  a
         *         / \                / \
         *        a  t2             t0   b
         *       / \                    / \
         *      t0  t1                 t1  t2
         * </pre>
         * <p>
         * Caller should ensure that p is not the root.
         */
        public void rotate(Position<Entry<K, V>> p) {
            if (p == root()) {
                return;
            }

            Node<Entry<K, V>> parent = ((Node<Entry<K,V>>) p).getParent();
            Node<Entry<K, V>> grandparent = parent.getParent();
            Node<Entry<K, V>> child;

            if (grandparent == null) {
                root = (Node<Entry<K, V>>) p;
                ((Node<Entry<K,V>>) p).setParent(null);
            }
            else {
                ((Node<Entry<K,V>>) p).setParent(grandparent);
                if (parent == grandparent.getLeft()) {
                    grandparent.setLeft(((Node<Entry<K,V>>) p));
                }
                else {
                    grandparent.setRight(((Node<Entry<K,V>>) p));
                }
            }

            parent.setParent(((Node<Entry<K,V>>) p));
            if (parent.getLeft() == p) {
                child = ((Node<Entry<K,V>>) p).getRight();
                ((Node<Entry<K,V>>) p).setRight(parent);
                parent.setLeft(child);
            }
            else {
                child = ((Node<Entry<K,V>>) p).getRight();
                ((Node<Entry<K,V>>) p).setLeft(parent);
                parent.setRight(child);
            }
        }

        /**
         * Returns the Position that becomes the root of the restructured subtree.
         * <p>
         * Assumes the nodes are in one of the following configurations:
         *
         * <pre>
         *     z=a                 z=c           z=a               z=c
         *    /  \                /  \          /  \              /  \
         *   t0  y=b             y=b  t3       t0   y=c          y=a  t3
         *      /  \            /  \               /  \         /  \
         *     t1  x=c         x=a  t2            x=b  t3      t0   x=b
         *        /  \        /  \               /  \              /  \
         *       t2  t3      t0  t1             t1  t2            t1  t2
         * </pre>
         * <p>
         * The subtree will be restructured so that the node with key b becomes its
         * root.
         *
         * <pre>
         *           b
         *         /   \
         *       a       c
         *      / \     / \
         *     t0  t1  t2  t3
         * </pre>
         * <p>
         * Caller should ensure that x has a grandparent.
         */
        public Position<Entry<K, V>> restructure(Position<Entry<K, V>> x) {
            if (x == root()) {
                return null;
            }

            Node<Entry<K, V>> child = ((Node<Entry<K, V>>) x);
            while (child.getParent() != null) {
                rotate(child);
            }

            return x;
        }
    } // ----------- end of nested BalanceableBinaryTree class -----------

    // We reuse the LinkedBinaryTree class. A limitation here is that we only use the key.
    // protected LinkedBinaryTree<Entry<K, V>> tree = new LinkedBinaryTree<Entry<K, V>>();
    protected BalanceableBinaryTree<K, V> tree = new BalanceableBinaryTree<>();

    /**
     * Constructs an empty map using the natural ordering of keys.
     */
    public TreeMap() {
        super(); // the AbstractSortedMap constructor
        tree.addRoot(null); // create a sentinel leaf as root
    }

    /**
     * Constructs an empty map using the given comparator to order keys.
     *
     * @param comp comparator defining the order of keys in the map
     */
    public TreeMap(Comparator<K> comp) {
        super(comp); // the AbstractSortedMap constructor
        tree.addRoot(null); // create a sentinel leaf as root
    }

    /**
     * Returns the number of entries in the map.
     *
     * @return number of entries in the map
     */
    @Override
    public int size() {
        return (tree.size() - 1) / 2; // only internal nodes have entries
    }

    protected Position<Entry<K, V>> restructure(Position<Entry<K, V>> x) {
        return tree.restructure(x);
    }

    /**
     * Rebalances the tree after an insertion of specified position. This version of
     * the method does not do anything, but it can be overridden by subclasses.
     *
     * @param p the position which was recently inserted
     */
    protected void rebalanceInsert(Position<Entry<K, V>> p) {
        // LEAVE EMPTY
    }

    /**
     * Rebalances the tree after a child of specified position has been removed.
     * This version of the method does not do anything, but it can be overridden by
     * subclasses.
     *
     * @param p the position of the sibling of the removed leaf
     */
    protected void rebalanceDelete(Position<Entry<K, V>> p) {
        // LEAVE EMPTY
    }

    /**
     * Rebalances the tree after an access of specified position. This version of
     * the method does not do anything, but it can be overridden by a subclasses.
     *
     * @param p the Position which was recently accessed (possibly a leaf)
     */
    protected void rebalanceAccess(Position<Entry<K, V>> p) {
        // LEAVE EMPTY
    }

    /**
     * Utility used when inserting a new entry at a leaf of the tree
     */
    private void expandExternal(Position<Entry<K, V>> p, Entry<K, V> entry) {
        tree.set(p, entry);
        tree.addLeft(p, null);
        tree.addRight(p, null);
    }

    // Some notational shorthands for brevity (yet not efficiency)
    protected Position<Entry<K, V>> root() {
        return tree.root();
    }

    protected Position<Entry<K, V>> parent(Position<Entry<K, V>> p) {
        return tree.parent(p);
    }

    protected Position<Entry<K, V>> left(Position<Entry<K, V>> p) {
        return tree.left(p);
    }

    protected Position<Entry<K, V>> right(Position<Entry<K, V>> p) {
        return tree.right(p);
    }

    protected Position<Entry<K, V>> sibling(Position<Entry<K, V>> p) {
        return tree.sibling(p);
    }

    protected boolean isRoot(Position<Entry<K, V>> p) {
        return tree.isRoot(p);
    }

    protected boolean isExternal(Position<Entry<K, V>> p) {
        return tree.isExternal(p);
    }

    protected boolean isInternal(Position<Entry<K, V>> p) {
        return tree.isInternal(p);
    }

    protected void set(Position<Entry<K, V>> p, Entry<K, V> e) {
        tree.set(p, e);
    }

    protected Entry<K, V> remove(Position<Entry<K, V>> p) {
        return tree.remove(p);
    }

    /**
     * Returns the position in p's subtree having the given key (or else the
     * terminal leaf).
     *
     * @param key a target key
     * @param p   a position of the tree serving as root of a subtree
     * @return Position holding key, or last node reached during search
     */
    private Position<Entry<K, V>> treeSearch(Position<Entry<K, V>> p, K key) {
        //System.out.println("ON: " + p);

        if (isExternal(p)) {
            //System.out.println("Extern node, ignoring...");
            return p;
        }

        else {
            if (compare(p.getElement(), key) == 0) {
                //System.out.println("Key found");
                return p;
            }
            if (compare(key, p.getElement()) < 0) {
                return treeSearch(left(p), key);
            }
            else {
                return treeSearch(right(p), key);
            }
        }
    }

    /**
     * Returns position with the minimal key in the subtree rooted at Position p.
     *
     * @param p a Position of the tree serving as root of a subtree
     * @return Position with minimal key in subtree
     */
    protected Position<Entry<K, V>> treeMin(Position<Entry<K, V>> p) {
        Position<Entry<K, V>> min = p;
        while (isInternal(left(min))) {
            min = left(min);
        }
        return min;
    }

    /**
     * Returns the position with the maximum key in the subtree rooted at p.
     *
     * @param p a Position of the tree serving as root of a subtree
     * @return Position with maximum key in subtree
     */
    protected Position<Entry<K, V>> treeMax(Position<Entry<K, V>> p) {
        Position<Entry<K, V>> max = p;
        while (isInternal(right(max))) {
            max = right(max);
        }
        return max;
    }

    /**
     * Returns the value associated with the specified key, or null if no such entry
     * exists.
     *
     * @param key the key whose associated value is to be returned
     * @return the associated value, or null if no such entry exists
     */
    @Override
    public V get(K key) throws IllegalArgumentException {
        LinkedBinaryTree.Node<Entry<K,V>> inode = (LinkedBinaryTree.Node<Entry<K,V>>) treeSearch(root(), key);
        rebalanceAccess(inode);
        if (isExternal(inode)) {
            return null;
        }
        return inode.getElement().getValue();
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
    public V put(K key, V value) throws IllegalArgumentException {
        Position<Entry<K, V>> p = treeSearch(root(), key);

        if (isExternal(p)) {
            expandExternal(p, new MapEntry<>(key, value));
            rebalanceInsert(p);
            return null;
        }
        else {
            V old = p.getElement().getValue();
            set(p, new MapEntry<>(key, value));
            return old;
        }
    }

    /**
     * Removes the entry with the specified key, if present, and returns its
     * associated value. Otherwise does nothing and returns null.
     *
     * @param key the key whose entry is to be removed from the map
     * @return the previous value associated with the removed key, or null if no
     * such entry exists
     */
    @Override
    public V remove(K key) throws IllegalArgumentException {
        Position<Entry<K, V>> inode = treeSearch(root(), key);
        if (isExternal(inode)) {
            rebalanceAccess(inode);
            return null;
        }

        V old = inode.getElement().getValue();
        if (isInternal(left(inode)) && isInternal(right(inode))) {
            Position<Entry<K, V>> sub = treeMax(left(inode));

            set(inode, sub.getElement());
            inode = sub;
        }
        else {
            Position<Entry<K, V>> child;
            if (isInternal(left(inode))) {
                child = left(inode);
            }
            else {
                child = right(inode);
            }

            remove(sibling(child));
        }

        remove(inode);
        rebalanceDelete(inode);
        return old;
    }

    // additional behaviours of the SortedMap interface
    /**
     * Returns the entry having the least key (or null if map is empty).
     *
     * @return entry with least key (or null if map is empty)
     */
    @Override
    public Entry<K, V> firstEntry() {
        if (isEmpty())
            return null;
        return treeMin(root()).getElement();
    }

    /**
     * Returns the entry having the greatest key (or null if map is empty).
     *
     * @return entry with greatest key (or null if map is empty)
     */
    @Override
    public Entry<K, V> lastEntry() {
        if (isEmpty())
            return null;
        return treeMax(root()).getElement();
    }

    /**
     * Returns the entry with least key greater than or equal to given key (or null
     * if no such key exists).
     *
     * @return entry with least key greater than or equal to given (or null if no
     * such entry)
     * @throws IllegalArgumentException if the key is not compatible with the map
     */
    @Override
    public Entry<K, V> ceilingEntry(K key) throws IllegalArgumentException {
        Position<Entry<K, V>> keyNode = treeSearch(root(), key);

        if (isExternal(keyNode)) {
            keyNode = parent(keyNode);
            while (compare(key, parent(keyNode).getElement()) < 0) {
                keyNode = parent(keyNode);
            }
            return keyNode.getElement();
        }

        if (right(keyNode) == null || isExternal(right(keyNode))) {
            return keyNode.getElement();
        }

        return right(keyNode).getElement();
    }

    /**
     * Returns the entry with greatest key less than or equal to given key (or null
     * if no such key exists).
     *
     * @return entry with greatest key less than or equal to given (or null if no
     * such entry)
     * @throws IllegalArgumentException if the key is not compatible with the map
     */
    @Override
    public Entry<K, V> floorEntry(K key) throws IllegalArgumentException {
        Position<Entry<K, V>> keyNode = treeSearch(root(), key);
        System.out.println("KeyNode = " + keyNode + " for key " + key);

        if (isExternal(keyNode)) {
            keyNode = parent(keyNode);
            while (compare(key, parent(keyNode).getElement()) > 0) {
                keyNode = parent(keyNode);
            }
            return keyNode.getElement();
        }

        if (left(keyNode) == null || isExternal(left(keyNode))) {
            return keyNode.getElement();
        }

        return left(keyNode).getElement();
    }

    /**
     * Returns the entry with greatest key strictly less than given key (or null if
     * no such key exists).
     *
     * @return entry with greatest key strictly less than given (or null if no such
     * entry)
     * @throws IllegalArgumentException if the key is not compatible with the map
     */
    @Override
    public Entry<K, V> lowerEntry(K key) throws IllegalArgumentException {
        Position<Entry<K, V>> keyNode = treeSearch(root(), key);
        System.out.println("KeyNode = " + keyNode + " for key " + key);

        if (isExternal(keyNode)) {
            keyNode = lowSearch(root(), key);
        }

        if (compare(floorEntry(key), key) != 0) {
            return floorEntry(key);
        }

        return keyNode.getElement();
    }

    /**
     * Returns the entry with least key strictly greater than given key (or null if
     * no such key exists).
     *
     * @return entry with least key strictly greater than given (or null if no such
     * entry)
     * @throws IllegalArgumentException if the key is not compatible with the map
     */
    @Override
    public Entry<K, V> higherEntry(K key) throws IllegalArgumentException {
        Position<Entry<K, V>> keyNode = treeSearch(root(), key);

        if (isExternal(keyNode)) {
            keyNode = highSearch(root(), key);
        }
        System.out.println("KeyNode = " + keyNode + " for key " + key);

        if (compare(ceilingEntry(key), key) != 0) {
            return ceilingEntry(key);
        }

        return keyNode.getElement();
    }

    private Position<Entry<K, V>> highSearch(Position<Entry<K, V>> pos, K key) {
        System.out.println("Compare: " + pos + ", with key " + key);
        if (compare(key, pos.getElement()) < 0) {
            if (left(pos) != null && compare(key, left(pos).getElement()) < 0) {
                return highSearch(left(pos), key);
            }
            return pos;
        }
        return highSearch(right(pos), key);
    }

    private Position<Entry<K, V>> lowSearch(Position<Entry<K, V>> pos, K key) {
        System.out.println("Compare: " + pos + ", with key " + key);
        if (compare(key, pos.getElement()) > 0) {
            if (right(pos) != null && compare(key, right(pos).getElement()) > 0) {
                return lowSearch(right(pos), key);
            }
            return pos;
        }
        return lowSearch(left(pos), key);
    }


    // Support for iteration
    /**
     * Returns an iterable collection of all key-value entries of the map.
     *
     * @return iterable collection of the map's entries
     */
    @Override
    public Iterable<Entry<K, V>> entrySet() {
        ArrayList<Entry<K, V>> buffer = new ArrayList<>(size());
        for (Position<Entry<K, V>> p : tree.inorder()) {
            if (isInternal(p)) {
                buffer.add(p.getElement());
            }
        }
        return buffer;
    }

    public String toString() {
        //return "";
        return tree.toString();
    }

    public String printKeys() {
        String s = "[";
        for (K key : this.keySet()) {
            if (key == null) {
                break;
            }
            System.out.println(key);
            s = s + key + ", ";
        }

        //s = s.replace(s.charAt(s.length() - 2), ']');
        return s.substring(0, s.length() - 2) + "]";
    }

    public void printLR() {
        Position<Entry<K, V>> p = root();
        System.out.println("Root: " + root());
        if (left(p) != null) {
            System.out.print("L:");
            printLRHelp(left(p));
        }
        if (right(p) != null) {
            System.out.print("R:");
            printLRHelp(right(p));
        }
    }

    public void printLRHelp(Position<Entry<K, V>> p) {
        System.out.println(p);
        if (left(p) != null) {
            System.out.print("L:");
            printLRHelp(left(p));
        }
        if (right(p) != null) {
            System.out.print("R of " + p + ": ");
            printLRHelp(right(p));
        }
    }

    /**
     * Returns an iterable containing all entries with keys in the range from
     * <code>fromKey</code> inclusive to <code>toKey</code> exclusive.
     *
     * @return iterable with keys in desired range
     * @throws IllegalArgumentException if <code>fromKey</code> or
     *                                  <code>toKey</code> is not compatible with
     *                                  the map
     */
    @Override
    public Iterable<Entry<K, V>> subMap(K fromKey, K toKey) throws IllegalArgumentException {
        ArrayList<Entry<K, V>> buffer = new ArrayList<>(size());

        for (Entry<K, V> p : entrySet()) {
            if (p.getKey() != null) {
                if (compare(p, fromKey) >= 0 && compare(p, toKey) < 0) {
                    buffer.add(p);
                }
            }
        }
        return buffer;
    }

    public String subMapKeys(K fromKey, K toKey) {
        String s = "[";
        for (Entry<K, V> e : subMap(fromKey, toKey)) {
            s = s + e.getKey() + ", ";
        }
        return s.substring(0, s.length() - 2) + "]";
    }


    protected void rotate(Position<Entry<K, V>> p) {
        tree.rotate(p);
    }

    // remainder of class is for debug purposes only
    /**
     * Prints textual representation of tree structure (for debug purpose only).
     */
    protected void dump() {
        dumpRecurse(root(), 0);
    }

    /**
     * This exists for debugging only
     */
    private void dumpRecurse(Position<Entry<K, V>> p, int depth) {
        String indent = (depth == 0 ? "" : String.format("%" + (2 * depth) + "s", ""));
        if (isExternal(p))
            System.out.println(indent + "leaf");
        else {
            System.out.println(indent + p.getElement());
            dumpRecurse(left(p), depth + 1);
            dumpRecurse(right(p), depth + 1);
        }
    }

    public String toBinaryTreeString() {
        BinaryTreePrinter<Entry<K, V>> btp = new BinaryTreePrinter<>(this.tree);
        return btp.print();
    }

    public static void main(String[] args) {
        TreeMap<Integer, String> map = new TreeMap<>();
        Integer[] arr = new Integer[]{35, 26, 15, 24, 33, 4, 12, 1, 23, 21, 2, 5};

        for (Integer i : arr) {
            map.put(i, Integer.toString(i));
        }

        //map.printLR();
        System.out.println("LE 24 (23) => " + map.lowerEntry(24).getKey());
        System.out.println("LE 31 (26) => " + map.lowerEntry(31).getKey());
    }
}


class TreeMapTest {
    @Test
    void testSize() {
        TreeMap<Integer, String> map = new TreeMap<>();
        assertEquals(0, map.size());
        map.put(1, "one");
        map.put(2, "two");
        assertEquals(2, map.size());
    }

    @Test
    void testRoot() {
        TreeMap<Integer, String> map = new TreeMap<>();
        Integer[] arr = new Integer[]{35, 26, 15, 24, 33, 4, 12, 1, 23, 21, 2, 5};

        for (Integer i : arr) {
            map.put(i, Integer.toString(i));
        }

        assertEquals(35, map.root().getElement().getKey());
    }

    @Test
    void testGet() {
        TreeMap<Integer, String> map = new TreeMap<>();
        Integer[] arr = new Integer[]{35, 26, 15, 24, 33, 4, 12, 1, 23, 21, 2, 5};

        for (Integer i : arr) {
            map.put(i, Integer.toString(i));
        }
        assertEquals("15", map.get(15));
        assertEquals("24", map.get(24));
        assertNull(map.get(-1));

    }

    @Test
    void testPut() {
        TreeMap<Integer, String> map = new TreeMap<>();
        Integer[] arr = new Integer[]{35, 26, 15, 24, 33, 4, 12, 1, 23, 21, 2, 5};

        for (Integer i : arr) {
            map.put(i, Integer.toString(i));
        }

        assertEquals("[1, 2, 4, 5, 12, 15, 21, 23, 24, 26, 33, 35]", map.printKeys());
        //assertEquals("[1, 2, 4, 5, 12, 15, 21, 23, 24, 26, 33, 35]", map.keySet().toString());
    }

    @Test
    void testRemoveK() {
        TreeMap<Integer, String> map = new TreeMap<>();
        Integer[] arr = new Integer[]{35, 26, 15, 24, 33, 4, 12, 1, 23, 21, 2, 5};

        for (Integer i : arr) {
            map.put(i, Integer.toString(i));
        }

        assertEquals(12, map.size());
        assertEquals("26", map.remove(26));
        assertEquals(11, map.size());
    }

    @Test
    void testFirstEntry() {
        //TreeMap<Integer, String> map = new TreeMap<>();
        TreeMap<Integer, String> map = new TreeMap<>();
        Integer[] arr = new Integer[]{35, 26, 15, 24, 33, 4, 12, 1, 23, 21, 2, 5};

        for (Integer i : arr) {
            map.put(i, Integer.toString(i));
        }

        assertEquals(1, map.firstEntry().getKey());
    }

    @Test
    void testLastEntry() {
        TreeMap<Integer, String> map = new TreeMap<>();
        //java.util.TreeMap<Integer, String> map = new java.util.TreeMap<>();
        Integer[] arr = new Integer[]{35, 26, 15, 24, 33, 4, 12, 1, 23, 21, 2, 5};

        for (Integer i : arr) {
            map.put(i, Integer.toString(i));
        }

        assertEquals(35, map.lastEntry().getKey());
    }

    @Test
    void testCeilingEntry() {
        TreeMap<Integer, String> map = new TreeMap<>();
        //java.util.TreeMap<Integer, String> map = new java.util.TreeMap<>();
        Integer[] arr = new Integer[]{35, 26, 15, 24, 33, 4, 12, 1, 23, 21, 2, 5};

        for (Integer i : arr) {
            map.put(i, Integer.toString(i));
        }

        assertEquals(12, map.ceilingEntry(11).getKey());

        assertEquals(2, map.ceilingEntry(2).getKey());
    }

    @Test
    void testFloorEntry() {
        TreeMap<Integer, String> map = new TreeMap<>();
        //java.util.TreeMap<Integer, String> map = new java.util.TreeMap<>();
        Integer[] arr = new Integer[]{35, 26, 15, 24, 33, 4, 12, 1, 23, 21, 2, 5};

        for (Integer i : arr) {
            map.put(i, Integer.toString(i));
        }

        assertEquals(5, map.floorEntry(11).getKey());
        assertEquals(5, map.floorEntry(5).getKey());

    }

    @Test
    void testLowerEntry() {
        TreeMap<Integer, String> map = new TreeMap<>();
        //java.util.TreeMap<Integer, String> map = new java.util.TreeMap<>();
        Integer[] arr = new Integer[]{35, 26, 15, 24, 33, 4, 12, 1, 23, 21, 2, 5};

        for (Integer i : arr) {
            map.put(i, Integer.toString(i));
        }

        assertEquals(23, map.lowerEntry(24).getKey());
        assertEquals(26, map.lowerEntry(31).getKey());
    }

    @Test
    void testHigherEntry() {
        TreeMap<Integer, String> map = new TreeMap<>();
        //java.util.TreeMap<Integer, String> map = new java.util.TreeMap<>();
        Integer[] arr = new Integer[]{35, 26, 15, 24, 33, 4, 12, 1, 23, 21, 2, 5};

        for (Integer i : arr) {
            map.put(i, Integer.toString(i));
        }

        assertEquals(12, map.higherEntry(11).getKey());
    }

    @Test
    void testToString() {
        TreeMap<Integer, String> map = new TreeMap<>();
        //java.util.TreeMap<Integer, String> map = new java.util.TreeMap<>();
        Integer[] arr = new Integer[]{35, 26, 15, 24, 33, 4, 12, 1, 23, 21, 2, 5};

        for (Integer i : arr) {
            map.put(i, Integer.toString(i));
        }

        //assertEquals("", map.toString());
        assertEquals("[1, 2, 4, 5, 12, 15, 21, 23, 24, 26, 33, 35]", map.printKeys());
        //assertEquals("[<1, 1>, <2, 2>, <4, 4>, <5, 5>, <12, 12>, <15, 15>, <21, 21>, <23, 23>, <24, 24>, <26, 26>, <33, 33>, <35, 35>]", map.toString());
    }

    @Test
    void testSubMap() {
        TreeMap<Integer, String> map = new TreeMap<>();
        //java.util.TreeMap<Integer, String> map = new java.util.TreeMap<>();
        Integer[] arr = new Integer[]{35, 26, 15, 24, 33, 4, 12, 1, 23, 21, 2, 5};

        for (Integer i : arr) {
            map.put(i, Integer.toString(i));
        }

        //assertEquals("[12, 15, 21, 23, 24, 26, 33]", map.subMap(12, 34).toString());
        assertEquals("[12, 15, 21, 23, 24, 26, 33]", map.subMapKeys(12, 34));
    }
}