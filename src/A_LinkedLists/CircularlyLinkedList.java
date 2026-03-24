package A_LinkedLists;

import interfaces.List;
import org.junit.jupiter.api.Test;
import java.util.Iterator;
import java.util.stream.IntStream;
import static org.junit.jupiter.api.Assertions.*;

public class CircularlyLinkedList<E> implements List<E> {
    private class Node<T> {
        private final T data;
        private Node<T> next;

        public Node(T e, Node<T> n) {
            data = e;
            next = n;
        }

        public T getData() {
            return data;
        }

        public void setNext(Node<T> n) {
            next = n;
        }

        public Node<T> getNext() {
            return next;
        }
    }

    private Node<E> tail;
    private int size = 0;

    public CircularlyLinkedList() {
        tail = new Node<>(null, tail);
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public E get(int position) {
        Node<E> curr = tail.getNext();
        for (int i = 0 ; i < position ; i++)
        {
            curr = curr.getNext();
        }
        return curr.getData();
    }


    public Node<E> getNode(int position) {
        Node<E> curr = tail.getNext();
        for (int i = 0 ; i < position ; i++)
        {
            curr = curr.getNext();
        }
        return curr;
    }

    /*
     * Inserts the given element at the specified index of the list, shifting all
     * subsequent elements in the list one position further to make room.
     *
     * @param i the index at which the new element should be stored
     * @param e the new element to be stored
     */
    @Override
    public void add(int i, E e) {
        if (i == 0 || size < 1)
        {
            addFirst(e);
            return;
        }
        else if (size == 1)
        {
            addLast(e);
            return;
        }
        else
        {
            Node<E> curr = this.getNode(i - 1);

            Node<E> newNode = new Node<>(e, curr.getNext());
            curr.setNext(newNode);
            size++;
        }
    }

    @Override
    public E remove(int i) {
        if (i == 0 || size < 1)
        {
            return removeFirst();
        }
        else if (size == 1)
        {
            return removeLast();
        }
        else
        {
            Node<E> curr = this.getNode(i - 1);

            Node<E> next = curr.getNext().getNext();
            E data = curr.getNext().getData();
            curr.setNext(next);
            size--;
            return data;
        }
    }

    public void rotate() {
        tail = tail.getNext();
    }

    private class CircularlyLinkedListIterator<E> implements Iterator<E> {
        Node<E> curr = (Node<E>) tail;

        @Override
        public boolean hasNext() {
            return curr != tail;
        }

        @Override
        public E next() {
            E res = curr.data;
            curr = curr.next;
            return res;
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new CircularlyLinkedListIterator<E>();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public E removeFirst() {
        if (size == 0)
        {
            return null;
        }
        E first = tail.getNext().getData();
        tail.setNext(tail.getNext().getNext());
        size--;
        return first;
    }

    @Override
    public E removeLast() {
        Node<E> last = tail;
        Node<E> second = getNode(size - 2);
        tail = second;

        tail.setNext(last.getNext());
        size--;
        return last.getData();
    }

    @Override
    public void addFirst(E e) {
        if (size == 0)
        {
            tail = new Node<>(e, tail);
            tail.next = tail;
        }
        else
        {
            Node<E> newNode = new Node<E>(e, tail.getNext());
            tail.setNext(newNode);
        }
        size++;
    }

    @Override
    public void addLast(E e) {
        if (size == 0)
        {
            addFirst(e);
        }
        else
        {
            Node<E> last = getNode(size - 1);
            Node<E> newNode = new Node<E>(e, tail.getNext());
            last.setNext(newNode);
            tail = newNode;
            size++;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> curr = tail;
        do {
            curr = curr.next;
            if (curr == null)
            {
                sb.append(" ");
                break;
            }
            else
            {
                sb.append(curr.data);
            }
            if (curr != tail) {
                sb.append(", ");
            }
        } while (curr != tail);
        sb.append("]");
        return sb.toString();
    }


    public static void main(String[] args) {
        CircularlyLinkedList<Integer> ll = new CircularlyLinkedList<>();
        for (int i = 10; i < 20; i++) {
            ll.addLast(i);
        }
        System.out.println(ll);

        ll.removeFirst();
        System.out.println(ll);

        ll.removeLast();
        System.out.println(ll);

        ll.rotate();
        System.out.println(ll);

        ll.removeFirst();
        ll.rotate();
        System.out.println(ll);

        ll.removeLast();
        ll.rotate();
        System.out.println(ll + "\n");


        List<Integer> tll = new CircularlyLinkedList<>();
        //assertNull(tll.removeFirst());
        System.out.println(tll);

        tll.addLast(1);
        tll.addLast(2);
        tll.addLast(3);
        System.out.println(tll);
        Integer r = tll.removeFirst();
        System.out.println(tll + " --> " + r + " Size: " + tll.size());
        //assertEquals(1, r);
        //assertEquals(2, tll.size());
    }
}


class CircularlyLinkedListTest {
    @Test
    void testIsEmpty() {
        List<Integer> ll = new CircularlyLinkedList<Integer>();
        assertTrue(ll.isEmpty());

        ll.addLast(1);
        assertFalse(ll.isEmpty());

        ll.removeLast();
        assertTrue(ll.isEmpty());
    }

    @Test
    void testGet() {
        List<Integer> ll = new CircularlyLinkedList<Integer>();
        ll.addLast(1);
        ll.addLast(2);
        ll.addLast(3);
        assertEquals("[1, 2, 3]", ll.toString());
        assertEquals(1, ll.get(0), "did not get right element(0)");
        assertEquals(2, ll.get(1), "did not get right element(1)");
        assertEquals(3, ll.get(2), "did not get right element(2)");
    }

    @Test
    void testAdd() {
        List<Integer> ll = new CircularlyLinkedList<Integer>();
        ll.addLast(1);
        ll.addLast(2);
        ll.addLast(3);

        ll.add(1, 100);

        assertEquals("[1, 100, 2, 3]", ll.toString(), "item not added correctly");
    }

    @Test
    void testRemove() {
        List<Integer> ll = new CircularlyLinkedList<Integer>();
        ll.addLast(1);
        ll.addLast(2);
        ll.addLast(3);

        assertEquals(2, ll.remove(1), "the removed value should be 1 ");
        assertEquals(2, ll.size(), "the size should be 2");
    }

    @Test
    void testSize() {
        List<Integer> ll = new CircularlyLinkedList<Integer>();
        assertEquals(0, ll.size());

        ll.addFirst(1);
        assertEquals(1, ll.size());

        ll.removeFirst();
        assertEquals(0, ll.size());
    }

    @Test
    void testRemoveFirst() {
        List<Integer> ll = new CircularlyLinkedList<>();
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
        List<Integer> ll = new CircularlyLinkedList<Integer>();
        ll.addLast(1);
        ll.addLast(2);
        ll.addLast(3);
        assertEquals(3, ll.removeLast());
        assertEquals(2, ll.size());
    }

    @Test
    void testAddFirst() {
        List<Integer> ll = new CircularlyLinkedList<Integer>();
        ll.addLast(-1);
        ll.addFirst(1);

        assertEquals(2, ll.size());
        assertEquals("[1, -1]", ll.toString());
    }

    @Test
    void testAddLast() {
        List<Integer> ll = new CircularlyLinkedList<Integer>();
        ll.addFirst(1);
        ll.addLast(-1);

        assertEquals(2, ll.size());
        assertEquals("[1, -1]", ll.toString());
    }

    @Test
    void testRotate() {
        CircularlyLinkedList<Integer> ll = new CircularlyLinkedList<Integer>();
        IntStream.rangeClosed(0, 10).forEach((x) -> ll.addLast(x));
        assertEquals("[0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10]", ll.toString());

        ll.rotate();
        assertEquals("[1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 0]", ll.toString());

    }

    @Test
    void testToString() {
        List<Integer> ll = new CircularlyLinkedList<Integer>();
        ll.addLast(1);
        ll.addLast(2);
        ll.addLast(3);
        assertEquals("[1, 2, 3]", ll.toString());
    }
}
