package A_LinkedLists;

import interfaces.List;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class SinglyLinkedList<E> implements List<E> {

    private static class Node<E> {

        private final E element;
        private Node<E> next;

        public Node(E e, Node<E> n) {
            element = e;
            next = n;
        }

        public E getElement() {
            return element;
        }

        public Node<E> getNext() {
            return next;
        }

        // Modifier methods
        public void setNext(Node<E> n) {
            next = n;
        }
    } //----------- end of nested Node class -----------

    private Node<E> head; // head node of the list (or null if empty)
    private int size = 0;        // number of nodes in the list

    public SinglyLinkedList() {

    }              // constructs an initially empty list

    //@Override
    public int size() {
        return size;
    }

    //@Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public E get(int position) {
        Node<E> curr = head;
        for (int i = 0 ; i < position ; i++)
        {
            curr = curr.getNext();
        }
        return curr.getElement();
    }

    public Node<E> getNode(int position) {
        Node<E> curr = head;
        for (int i = 0 ; i < position ; i++)
        {
            curr = curr.getNext();
        }
        return curr;
    }

    @Override
    public void add(int position, E e) {
        if (position == 0)
        {
            addFirst(e);
        }
        else
        {
            Node<E> curr = head;

            for (int i = 0; i < position - 1; i++)
            {
                curr = curr.getNext();
            }
            Node<E> shift = curr.getNext();

            Node<E> newN = new Node<>(e, shift);
            curr.setNext(newN);
            size = size + 1;
        }
    }


    @Override
    public void addFirst(E e) {
        Node<E> curr = head;
        head = new Node<>(e, curr);
        size = size + 1;
    }

    @Override
    public void addLast(E e) {
        if (size == 0)
        {
            addFirst(e);
        }
        else
        {
            Node<E> curr = head;
            for (int i = 0; i < size - 1; i++)
            {
                curr = curr.getNext();
            }

            Node<E> newN = new Node<>(e, null);
            curr.setNext(newN);
            size = size + 1;
        }
    }

    @Override
    public E remove(int position) {
        if (position == 0)
        {
            return removeFirst();
        }

        Node<E> curr = head;
        for (int i = 0 ; i < position - 1 ; i++)
        {
            curr = curr.getNext();
        }
        Node<E> to_drop = curr.getNext();
        curr.setNext(to_drop.getNext());
        size = size - 1;
        return to_drop.getElement();
    }

    @Override
    public E removeFirst() {
        if (size < 1)
        {
            return null;
        }
        Node<E> curr = head;
        head = head.getNext();
        size--;
        return curr.getElement();
    }

    @Override
    public E removeLast() {
        if (size == 1)
        {
            return removeFirst();
        }
        else
        {
            Node<E> curr = head;
            for (int i = 0; i < size - 2; i++)
            {
                curr = curr.getNext();
            }
            Node<E> last = curr.getNext();
            curr.setNext(null);
            size--;
            return last.getElement();
        }
    }

    //@Override
    public Iterator<E> iterator() {
        return new SinglyLinkedListIterator<>();
    }

    private class SinglyLinkedListIterator<E> implements Iterator<E> {
        Node<E> curr = (Node<E>) head;

        @Override
        public boolean hasNext() {
            return curr != null;
        }

        @Override
        public E next() {
            E res = curr.getElement();
            curr = curr.next;
            return res;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> curr = head;
        while (curr != null) {
            if (curr.getElement() == null)
            {
                sb.append(" ");
            }
            else
            {
                sb.append(curr.getElement());
            }
            if (curr.getNext() != null)
                sb.append(", ");
            curr = curr.getNext();
        }
        sb.append("]");
        return sb.toString();
    }

    public SinglyLinkedList<E> sortedMerge(SinglyLinkedList<E> ll)
    {
        SinglyLinkedList<E> sl = new SinglyLinkedList<>();
        int i = 0, j = 0, k = 0;
        E high = null;

        while (i < this.size() + ll.size() - 1)
        {
            if (this.get(j) == null || ll.get(k) == null)
            {
                break;
            }

            if (this.get(j) instanceof Comparable<?> && ll.get(k) instanceof Comparable<?>)
            {
                if (((Comparable<E>) this.get(j)).compareTo(ll.get(k)) == 1)
                {
                    sl.add(i, ll.get(k));
                    k++;
                    high = this.get(j);
                }
                else
                {
                    sl.add(i, this.get(j));
                    j++;
                    high = ll.get(k);
                }
            }
            i++;
        }
        sl.addLast(high);
        return sl;
    }

    public void reverseList()
    {
        if (size < 3)
        {
            if (size == 2)
            {

                Node<E> last = head.getNext();
                last.setNext(head);
                head.setNext(null);
                head = last;
            }
            else
            {
                return;
            }
        }

        Node<E> finalNode = getNode(size - 1);

        for (int i = size() - 2 ; i >= 0 ; i--)
        {
            Node<E> next = getNode(i);
            Node<E> curr = next.getNext();
            curr.setNext(next);
        }

        head = finalNode;
        getNode(size - 1).setNext(null);
    }

    public SinglyLinkedList<E> copy()
    {
        SinglyLinkedList<E> twin = new SinglyLinkedList<>();
        Node<E> curr = head;

        for (int i = 0 ; i < this.size() ; i++)
        {
            twin.addLast(curr.getElement());
            curr = curr.getNext();
        }

        return twin;
    }


    // Recursively reverse the list
    public void reverseRecursive()
    {
        revRecHelper(this.head, getNode(0));
    }

    public Node<E> revRecHelper(Node<E> n, Node<E> first)
    {
        if (n.next == null)
        {
            this.head = n;
        }
        if (n.next != null)
        {
            Node<E> nextNode = revRecHelper(n.next, first);
            nextNode.setNext(n);
            if (n == first)
            {
                n.setNext(null);
            }
        }
        return n;
    }

    // Uses Recursion to duplicate the list
    public SinglyLinkedList<E> recursiveCopy()
    {
        SinglyLinkedList<E> dupe = new SinglyLinkedList<>();
        dupe.recCopyHelper(this.head);
        return dupe;
    }

    public void recCopyHelper(Node<E> n)
    {
        if (n.next != null)
        {
            recCopyHelper(n.next);
        }

        this.addFirst(n.getElement());
    }


    public static void main(String[] args) {
        SinglyLinkedList<Integer> ll1 = new SinglyLinkedList<>();
        SinglyLinkedList<Integer> ll2 = new SinglyLinkedList<>();

        ll1.addLast(0);
        ll2.addLast(1);
        ll1.addLast(2);
        ll2.addLast(3);
        ll1.addLast(4);
        ll2.addFirst(-1);

        //System.out.println(ll1 + "  Size " + ll1.size() + "\n" + ll2 + "  Size " + ll2.size() + "\n");

        SinglyLinkedList<Integer> msl = ll1.sortedMerge(ll2);
        System.out.println("Linked List: " + msl);

        msl.reverseRecursive();
        System.out.println("Reversed: " + msl);

        SinglyLinkedList<Integer> copy = msl.recursiveCopy();
        System.out.println("Copy of reverse: " + copy);
    }
}

class SinglyLinkedListTest {

    @Test
    void testIsEmpty() {
        List<Integer> ll = new SinglyLinkedList<>();
        System.out.println(ll);
        assertTrue(ll.isEmpty());
        assertEquals("[]", ll.toString());

        ll.addLast(1);
        assertFalse(ll.isEmpty());
        System.out.println(ll);

        ll.removeLast();
        assertTrue(ll.isEmpty());
        System.out.println(ll);
    }

    @Test
    void testGet() {
        List<Integer> ll = new SinglyLinkedList<>();
        ll.addLast(1);
        ll.addLast(2);
        ll.addLast(3);

        Integer r = ll.get(2);
        assertEquals(3, r, "did not get right element" + r);
    }

    @Test
    void testAdd() {
        List<Integer> ll = new SinglyLinkedList<>();
        ll.addLast(1);
        ll.addLast(2);
        ll.addLast(3);

        ll.add(1, 100);

        assertEquals("[1, 100, 2, 3]", ll.toString(), "item not added correctly");
    }

    @Test
    void testRemove() {
        List<Integer> ll = new SinglyLinkedList<>();
        ll.addLast(1);
        ll.addLast(2);
        ll.addLast(3);

        assertEquals(3, ll.remove(2), "the removed value should be 3");
        assertEquals(2, ll.size(), "the size should be 2");
    }

    @Test
    void testSize() {
        List<Integer> ll = new SinglyLinkedList<>();
        assertEquals(0, ll.size());

        ll.addFirst(1);
        assertEquals(1, ll.size());

        ll.removeFirst();
        assertEquals(0, ll.size());
    }

    @Test
    void testRemoveFirst() {
        List<Integer> ll = new SinglyLinkedList<>();
        assertNull(ll.removeFirst());

        ll.addLast(1);
        ll.addLast(2);
        ll.addLast(3);
        Integer r = ll.removeFirst();
        assertEquals(1, r);
        assertEquals(2, ll.size());
    }

    @Test
    void testRemoveLast() {
        List<Integer> ll = new SinglyLinkedList<>();
        ll.addLast(1);
        ll.addLast(2);
        ll.addLast(3);
        assertEquals(3, ll.removeLast());
        assertEquals(2, ll.size());
    }

    @Test
    void testAddFirst() {
        List<Integer> ll = new SinglyLinkedList<>();
        ll.addLast(-1);
        ll.addFirst(1);

        assertEquals(2, ll.size());
        assertEquals("[1, -1]", ll.toString());
    }

    @Test
    void testAddLast() {
        List<Integer> ll = new SinglyLinkedList<>();
        ll.addFirst(1);
        ll.addLast(-1);

        assertEquals(2, ll.size());
        assertEquals("[1, -1]", ll.toString());
    }

    @Test
    void testToString() {
        List<Integer> ll = new SinglyLinkedList<>();
        ll.addLast(1);
        ll.addLast(2);
        ll.addLast(3);
        System.out.println(ll);
        //assertEquals("[1, 2, 3]", ll.toString());
    }
}
