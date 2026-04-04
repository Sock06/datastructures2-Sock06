package C_Trees;

import interfaces.Position;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * Concrete implementation of a binary tree using a node-based, linked
 * structure.
 */
public class LinkedBinaryTree<E> extends AbstractBinaryTree<E> {
    static java.util.Random rnd = new java.util.Random();
    protected Node<E> root = null;

    /**
     * Nested static class for a binary tree node.
     */
    public static class Node<E> implements Position<E>
    {
        private E element;
        private Node<E> left, right, parent;

        public Node(E e, Node<E> p, Node<E> l, Node<E> r) {
            element = e;
            left = l;
            right = r;
            parent = p;
        }

        // accessor
        public E getElement() {
            return element;
        }

        // modifiers
        public void setElement(E e) {
            element = e;
        }

        public Node<E> getLeft() {
            return left;
        }

        public void setLeft(Node<E> n) {
            left = n;
        }

        public Node<E> getRight() {
            return right;
        }

        public void setRight(Node<E> n) {
            right = n;
        }

        public Node<E> getParent() {
            return parent;
        }

        public void setParent(Node<E> n) {
            parent = n;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            if (element == null) {
                sb.append("null");
            } else {
                sb.append(element);
            }
            return sb.toString();
        }
    }

    // LinkedBinaryTree instance variables
    private int size = 0; // number of nodes in the tree

    public LinkedBinaryTree() {
        all_paths = new ArrayList<>();
    } // constructs an empty binary tree


    public static LinkedBinaryTree<Integer> makeRandom(int n) {
        LinkedBinaryTree<Integer> bt = new LinkedBinaryTree<>();
        bt.root = randomTree(null, 1, n);
        return bt;
    }
    // nonpublic utility
    private static <T extends Integer> Node<T> randomTree(Node<T> parent, Integer first, Integer last) {
        if (first > last) return null;
        else {
            int treeSize = last - first + 1;
            int leftCount = rnd.nextInt(treeSize);
            //int rightCount = treeSize - leftCount - 1;
            Node<T> root = new Node<>((T) ((Integer) (first + leftCount)), parent, null, null);
            root.setLeft(randomTree(root, first, first + leftCount - 1));
            root.setRight(randomTree(root, first + leftCount + 1, last));
            return root;
        }
    }


    // accessor methods (not already implemented in AbstractBinaryTree)
    /**
     * Factory function to create a new node storing element e.
     */
    protected Node<E> createNode(E e, Node<E> parent, Node<E> left, Node<E> right) {
        return new Node<>(e, parent, left, right);
    }

    /**
     * Verifies that a Position belongs to the appropriate class, and is not one
     * that has been previously removed. Note that our current implementation does
     * not actually verify that the position belongs to this particular list
     * instance.
     *
     * @param p a Position (that should belong to this tree)
     * @return the underlying Node instance for the position
     * @throws IllegalArgumentException if an invalid position is detected
     */
    protected Node<E> validate(Position<E> p) throws IllegalArgumentException {
        if (!(p instanceof Node)) throw new IllegalArgumentException("Not valid position type");
        Node<E> node = (Node<E>) p; // safe cast
        if (node.getParent() == node) // our convention for defunct node
            throw new IllegalArgumentException("p is no longer in the tree");
        return node;
    }

    /**
     * Returns the number of nodes in the tree.
     *
     * @return number of nodes in the tree
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Returns the root Position of the tree (or null if tree is empty).
     *
     * @return root Position of the tree (or null if tree is empty)
     */
    @Override
    public Position<E> root() {
        return root;
    }

    // update methods supported by this class
    /**
     * Returns the Position of p's parent (or null if p is root).
     *
     * @param p A valid Position within the tree
     * @return Position of p's parent (or null if p is root)
     * @throws IllegalArgumentException if p is not a valid Position for this tree.
     */
    @Override
    public Position<E> parent(Position<E> p) throws IllegalArgumentException {
        validate(p);
        return ((Node<E>) p).getParent();
    }

    /**
     * Returns the Position of p's left child (or null if no child exists).
     *
     * @param p A valid Position within the tree
     * @return the Position of the left child (or null if no child exists)
     * @throws IllegalArgumentException if p is not a valid Position for this tree
     */
    @Override
    public Position<E> left(Position<E> p) throws IllegalArgumentException {
        validate(p);
        return ((Node<E>) p).getLeft();
    }

    /**
     * Returns the Position of p's right child (or null if no child exists).
     *
     * @param p A valid Position within the tree
     * @return the Position of the right child (or null if no child exists)
     * @throws IllegalArgumentException if p is not a valid Position for this tree
     */
    @Override
    public Position<E> right(Position<E> p) throws IllegalArgumentException {
        validate(p);
        return ((Node<E>) p).getRight();
    }

    public int nodeHeight(Node<E> curr_node) {
        if (curr_node == null)
        {
            return 0;
        }

        int l = nodeHeight(curr_node.left);
        int r = nodeHeight(curr_node.right);

        //System.out.println("On Curr Node: " + curr_node + " --> Left side: " + l + ", Right side: " + r);

        if (l > r)
        {
            if (curr_node == root)
            {
                return l;
            }
            return l + 1;
        }
        else
        {
            if (curr_node == root)
            {
                return r;
            }
            return r + 1;
        }
    }

    public int height()
    {
        return nodeHeight(root);
    }

    public ArrayList<ArrayList<E>> getRootLeafPaths()
    {
        return all_paths;
    }

    /**
     * Places element e at the root of an empty tree and returns its new Position.
     *
     * @param e the new element
     * @return the Position of the new element
     * @throws IllegalStateException if the tree is not empty
     */
    public Position<E> addRoot(E e) throws IllegalStateException {
        root = new Node<>(e, null, null, null);
        size++;
        return root;
    }

    /**
     * Creates a new left child of Position p storing element e and returns its
     * Position.
     *
     * @param p the Position to the left of which the new element is inserted
     * @param e the new element
     * @return the Position of the new element
     * @throws IllegalArgumentException if p is not a valid Position for this tree
     * @throws IllegalArgumentException if p already has a left child
     */
    public Position<E> addLeft(Position<E> p, E e) throws IllegalArgumentException {
        validate(p);
        Node<E> new_left = new Node<>(e, ((Node<E>) p), null, null);
        ((Node<E>) p).setLeft(new_left);
        size++;
        return new_left;
    }

    /**
     * Creates a new right child of Position p storing element e and returns its
     * Position.
     *
     * @param p the Position to the right of which the new element is inserted
     * @param e the new element
     * @return the Position of the new element
     * @throws IllegalArgumentException if p is not a valid Position for this tree.
     * @throws IllegalArgumentException if p already has a right child
     */
    public Position<E> addRight(Position<E> p, E e) throws IllegalArgumentException {
        Node<E> new_right = new Node<>(e, ((Node<E>) p), null, null);
        ((Node<E>) p).setRight(new_right);
        size++;
        return new_right;
    }

    /**
     * Replaces the element at Position p with element e and returns the replaced
     * element.
     *
     * @param p the relevant Position
     * @param e the new element
     * @return the replaced element
     * @throws IllegalArgumentException if p is not a valid Position for this tree.
     */
    public E set(Position<E> p, E e) throws IllegalArgumentException {
        E to_replace = p.getElement();
        set(p, e);
        return to_replace;
    }

    /**
     * Attaches trees t1 and t2, respectively, as the left and right subtree of the
     * leaf Position p. As a side effect, t1 and t2 are set to empty trees.
     *
     * @param p  a leaf of the tree
     * @param t1 an independent tree whose structure becomes the left child of p
     * @param t2 an independent tree whose structure becomes the right child of p
     * @throws IllegalArgumentException if p is not a valid Position for this tree
     * @throws IllegalArgumentException if p is not a leaf
     */
    public void attach(Position<E> p, LinkedBinaryTree<E> t1, LinkedBinaryTree<E> t2) throws IllegalArgumentException {
        ((Node<E>) p).setLeft((Node<E>) t1.root());
        ((Node<E>) p).setRight((Node<E>) t2.root());
        size = size + t1.size() + t2.size();
    }

    /**
     * Removes the node at Position p and replaces it with its child, if any.
     *
     * @param p the relevant Position
     * @return element that was removed
     * @throws IllegalArgumentException if p is not a valid Position for this tree.
     * @throws IllegalArgumentException if p has two children.
     */
    public E remove(Position<E> p) throws IllegalArgumentException {
        E removed_elem = p.getElement();

        if (right(p) != null && left(p) != null) {
            throw new IllegalArgumentException();
        }

        if (left(p) != null) {
            Position<E> l = left(p);
            set(parent(l), parent(p).getElement());
        }

        else if (right(p) != null) {
            Position<E> r = right(p);
            set(parent(r), parent(p).getElement());
        }

        size--;
        set(parent(p), p.getElement());
        return removed_elem;
    }

    public String toString() {
        //return positions().toString();
        String los = "[" + StringHelper(this.root) + "]";
        return los.replace("null, ", "");
    }

    private String StringHelper(Node<E> to_print) {
        if (to_print == null) {
            return "null";
        }

        if (to_print.left == null && to_print.right == null)
        {
            if (to_print.getElement() == null) {
                return "null";
            }
            else {
                return to_print.getElement().toString();
            }
        }

        return StringHelper(to_print.left) + ", " + to_print.getElement() + ", " + StringHelper(to_print.right);
    }

    public void createLevelOrder(ArrayList<E> l) {
        root = createLevelOrderHelper(l, root, 0);
    }

    private Node<E> createLevelOrderHelper(java.util.ArrayList<E> l, Node<E> p, int index) {
        if (l.isEmpty()) {
            return p;
        }

        if (index < l.size()) {
            Node<E> n = createNode(l.get(index), p, null, null);
            n.left = createLevelOrderHelper(l, n.left, 2 * index + 1);
            n.right = createLevelOrderHelper(l, n.right, 2 * index + 2);
            ++size;
            return n;
        }

        return p;
    }

    public void createLevelOrder(E[] arr) {
        root = createLevelOrderHelper(arr, root, 0);
    }

    private Node<E> createLevelOrderHelper(E[] arr, Node<E> p, int index) {
        if (arr.length < 1) {
            return p;
        }

        if (index < arr.length) {
            Node<E> n = createNode(arr[index], p, null, null);
            n.left = createLevelOrderHelper(arr, n.left, 2 * index + 1);
            n.right = createLevelOrderHelper(arr, n.right, 2 * index + 2);
            ++size;
            return n;
        }

        return p;
    }

    /*
    public String levelOrderString() {
        StringBuilder sb = new StringBuilder("[");

        sb.append(levelOrderHelper(this.root));
        sb.append("]");

        String los = sb.toString();
        los.replace(",  , ", ",");

        return los;
    }

    private String levelOrderHelper(Node<E> to_print) {
        if (to_print == null) {
            return "null";
        }
        if (to_print.left == null && to_print.right == null)
        {
            if (to_print.getElement() == null) {
                return "null";
            }
            else {
                return to_print.getElement().toString();
            }
        }

        return to_print.getElement() + ", " + StringHelper(to_print.left) + ", " + StringHelper(to_print.right);
    }
     */

    int countExternal(Node<E> curr_node) {
        int l = 0, r = 0;
        if (curr_node.left == null && curr_node.right == null)
        {
            return 1;
        }

        if (curr_node.left != null)
        {
            l = countExternal(curr_node.left);
        }
        if (curr_node.right != null)
        {
            r = countExternal(curr_node.right);
        }

        return l + r;
    }

    public void construct(E[] inorder, E[] preorder)
    {
        root = new Node<>(preorder[0], null, null, null);
        size++;

        int r = 0, p = 0;
        while (r < inorder.length && inorder[r] != preorder[0])
        {
            r++;
        }
        if (r == inorder.length)
        {
            System.out.println("Invalid traversal - Inorder is incorrect");
            return;
        }

        // Set Left and right subtrees
        E[] l_io = (E[]) new Object[r];
        E[] r_io = (E[]) new Object[inorder.length - r - 1];
        System.arraycopy(inorder, 0, l_io, 0, r);
        System.arraycopy(inorder, r + 1, r_io, 0, inorder.length - r - 1);


        preorder = removeFirst(preorder);
        E[] l_pre = (E[]) new Object[l_io.length];
        E[] r_pre = (E[]) new Object[r_io.length];
        for (int i = 0 ; i < l_io.length ; i++)
        {
            for (int j = 0 ; j < preorder.length ; j++)
            {
                if (preorder[j] == l_io[i])
                {
                    l_pre[j] = preorder[j];
                    p++;
                }
            }
        }
        System.arraycopy(preorder, p, r_pre, 0, r_io.length);

        root.setLeft(treeConstructHelper(l_io, l_pre, root));
        root.setRight(treeConstructHelper(r_io, r_pre, root));
    }

    private Node<E> treeConstructHelper(E[] inorder, E[] preorder, Node<E> parent)
    {
        if (inorder.length < 1 || preorder.length < 1) {
            return null;
        }

        int r = 0, p = 0;
        while (r < inorder.length && inorder[r] != preorder[0])
        {
            r++;
        }
        if (r == inorder.length) {
            System.out.println("Invalid traversal - Inorder does not contain element");
            return null;
        }

        Node<E> node = new Node<>(preorder[0], null, null, null);
        size++;

        // Set Left and right subtrees
        E[] l_insub = (E[]) new Object[r];
        E[] r_insub = (E[]) new Object[inorder.length - r - 1];
        System.arraycopy(inorder, 0, l_insub, 0, r);
        System.arraycopy(inorder, r + 1, r_insub, 0, inorder.length - r - 1);


        preorder = removeFirst(preorder);
        E[] l_pre = (E[]) new Object[l_insub.length];
        E[] r_pre = (E[]) new Object[r_insub.length];
        for (int i = 0 ; i < l_insub.length ; i++)
        {
            for (int j = 0 ; j < preorder.length ; j++)
            {
                if (preorder[j] == l_insub[i])
                {
                    l_pre[j] = preorder[j];
                    p++;
                }
            }
        }

        System.arraycopy(preorder, p, r_pre, 0, r_insub.length);

        node.setLeft(treeConstructHelper(l_insub, l_pre, node));
        node.setRight(treeConstructHelper(r_insub, r_pre, node));

        return node;
    }

    private E[] removeFirst(E[] arr) {
        E[] newArr = (E[]) new Object[arr.length - 1];

        for (int i = 1; i < arr.length ; i++)
        {
            newArr[i - 1] = arr[i];
        }

        return newArr;
    }


    private ArrayList<ArrayList<E>> all_paths;
    public void rootToLeafPaths() {
        if (isEmpty())
        {
            System.out.println("Empty BT");
        }
        else
        {
            ArrayList<E> fromRoot = new ArrayList<>(nodeHeight(root));
            rootToLeafPath(root, fromRoot);
        }
    }

    private void rootToLeafPath(Node<E> node, ArrayList<E> curr_path) {
        curr_path.add(node.getElement());
        ArrayList<E> new_path = (ArrayList<E>) curr_path.clone();

        if (node.left == null && node.right == null)
        {
            //System.out.println("Path " + new_path + " added");
            this.all_paths.add(new_path);
        }
        else
        {
            if (node.left != null)
            {
                rootToLeafPath(node.left, new_path);
            }
            if (node.right != null)
            {
                rootToLeafPath(node.right, new_path);
            }
        }

        curr_path.removeLast();
    }

    public int getDiameter() {
        if (isEmpty())
        {
            return 0;
        }
        else
        {
            return nodeHeight(root.left) + nodeHeight(root.right) + 1;
        }
    }

    public void LeafPrinter(Node<E> n) {
        if (n.left == null && n.right == null)
        {
            System.out.print(n + ", ");
        }
        else
        {
            if (n.left != null)
            {
                LeafPrinter(n.getLeft());
            }
            if (n.right != null)
            {
                LeafPrinter(n.getRight());
            }
        }
    }

    public void LeafPrint2()
    {
        this.rootToLeafPaths();
        ArrayList<ArrayList<E>> paths = this.getRootLeafPaths();
        E[] leaves = (E[]) new Object[paths.size()/2];

        for (int i = 0 ; i < paths.size()/2 ; i++)
        {
            ArrayList<E> al = paths.get(i);
            leaves[i] = al.getLast();
            //System.out.println("I = " + i + " up to " + paths.size()/2 + ", Curr Array: " + Arrays.toString(leaves));
        }

        System.out.println(Arrays.toString(leaves));
    }


    public static void main(String [] args) {
        Integer[] inorder = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22};
        Integer[] preorder = {6, 5, 3, 2, 1, 0, 4, 17, 10, 9, 8, 7, 16, 14, 13, 12, 11, 15, 21, 20, 19, 18, 22};
        LinkedBinaryTree<Integer> bt = new LinkedBinaryTree <>();
        bt.construct(inorder, preorder);

        /*
        Character[] prech = {'A', 'B', 'D', 'E', 'G', 'H', 'C', 'F'};
        Character[] inch = {'D', 'B', 'G', 'E', 'H', 'A', 'C', 'F'};

        LinkedBinaryTree<Character> cbt = new LinkedBinaryTree <>();
        cbt.construct(inch, prech);

        cbt.LeafPrinter(cbt.root);
         */

        //System.out.println(bt + "  Diameter = " + bt.getDiameter());
        bt.rootToLeafPaths();
        ArrayList<ArrayList<Integer>> paths = bt.getRootLeafPaths();
        for (ArrayList<Integer> ALI : paths)
        {
            System.out.println(ALI + "   Size = " + ALI.size());
        }

        bt.LeafPrint2();
    }
}

class LinkedBinaryTreeTest {
    @Test
    void testSize() {
        LinkedBinaryTree<Integer> bt = new LinkedBinaryTree<>();
        Position<Integer> root = bt.addRoot(1);
        assertEquals(1, bt.size());

        Position<Integer> l = bt.addLeft(root, 2);

        bt.remove(bt.root());
        assertEquals(1, bt.size());
    }

    @Test
    void testAddRoot() {
        LinkedBinaryTree<Integer> bt = new LinkedBinaryTree<>();

        bt.addRoot(0);
        assertEquals(0, bt.root().getElement(), "root not added correctly");
    }

    @Test
    void testAddLeft() {
        LinkedBinaryTree<Integer> bt = new LinkedBinaryTree<>();

        bt.addRoot(0);
        bt.addLeft(bt.root(), 1);
        assertEquals(1, bt.left(bt.root()).getElement());
    }

    @Test
    void testAddRight() {
        LinkedBinaryTree<Integer> bt = new LinkedBinaryTree<>();

        bt.addRoot(0);
        bt.addRight(bt.root(), 1);
        assertEquals(1, bt.right(bt.root()).getElement());
    }

    @Test
    void testRemove() {
        LinkedBinaryTree<Integer> bt = new LinkedBinaryTree<>();

        bt.addRoot(0);
        bt.addRight(bt.root(), 1);
        Integer old = bt.remove(bt.right(bt.root()));
        assertEquals(old, 1);
        assertEquals(1, bt.size());
    }

    @Test
    void testToString() {
        LinkedBinaryTree<Integer> bt = new LinkedBinaryTree<>();

        Integer[] arr = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        bt.createLevelOrder(arr);
        System.out.println(bt);
        assertEquals("[8, 4, 9, 2, 10, 5, 11, 1, 12, 6, 3, 7]", bt.toString());
    }

    @Test
    void testCreateLevelOrder() {
        LinkedBinaryTree<Integer> bt = new LinkedBinaryTree<>();

        Integer[] arr = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        bt.createLevelOrder(arr);
        System.out.println(bt.toString());
        assertEquals("[8, 4, 9, 2, 10, 5, 11, 1, 12, 6, 3, 7]", bt.toString());
    }

    @Test
    void testDepth() {
        LinkedBinaryTree<Integer> bt = new LinkedBinaryTree<>();

        Integer[] arr = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        bt.createLevelOrder(arr);

        assertEquals(0, bt.depth(bt.root()));
    }

    @Test
    void testInorder() {
        LinkedBinaryTree<Integer> bt = new LinkedBinaryTree<>();

        Integer[] arr = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        bt.createLevelOrder(arr);
        //System.out.println(bt.toString());
        assertEquals("[8, 4, 9, 2, 10, 5, 11, 1, 12, 6, 3, 7]", bt.inorder().toString());
    }


    @Test
    void testHeight() {
        LinkedBinaryTree<Integer> bt = new LinkedBinaryTree<>();

        Integer[] arr = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        bt.createLevelOrder(arr);

        assertEquals(3, bt.height());
    }
}
