package A_LinkedLists;

import interfaces.List;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class DoublyLinkedList<E> implements List<E> {

    private static class Node<E> {
        private final E data;
        private Node<E> next;
        private Node<E> prev;

        public Node(E e, Node<E> p, Node<E> n) {
            data = e;
            prev = p;
            next = n;
        }

        public E getData() {
            return data;
        }

        public Node<E> getNext() {
            return next;
        }

        public Node<E> getPrev() {
            return prev;
        }

        public void setPrev(Node<E> n) {
            prev = n;
        }

        public void setNext(Node<E> n) {
            next = n;
        }
    }

    private Node<E> head;
    private Node<E> tail;
    private int size = 0;

    public DoublyLinkedList() {
        head = new Node<>(null, null, null);
        tail = new Node<>(null, head, null);
        head.next = tail;
    }

    private void addBetween(E e, Node<E> pred, Node<E> succ) {
        Node<E> newNode = new Node<>(e, pred, succ);
        pred.setNext(newNode);
        succ.setPrev(newNode);
        size = size + 1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public E get(int position) {
        Node<E> curr = head.getNext();
        for (int i = 0 ; i < position ; i++)
        {
            curr = curr.getNext();
        }
        return curr.getData();
    }

    @Override
    public void add(int position, E e) {
        Node<E> curr = head.getNext();

        for (int i = 0 ; i < position - 1 ; i++)
        {
            curr = curr.getNext();
        }
        Node<E> shift = curr.getNext();
        addBetween(e, curr, shift);
    }

    @Override
    public E remove(int position) {
        Node<E> curr = head.getNext();

        for (int i = 0 ; i < position - 1 ; i++)
        {
            curr = curr.getNext();
        }
        Node<E> to_drop = curr.getNext();
        curr.setNext(to_drop.getNext());
        to_drop.getNext().setPrev(curr);
        size = size - 1;
        return to_drop.getData();
    }

    private class DoublyLinkedListIterator<E> implements Iterator<E> {
        Node<E> curr = (Node<E>) head.next;

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
        return new DoublyLinkedListIterator<>();
    }

    /*
    private E remove(Node<E> n) {
        // TODO
        return null;
    }
     */

    public E first() {
        if (isEmpty()) {
            return null;
        }
        return head.next.getData();
    }

    public E last() {
        if (isEmpty()) {
            return null;
        }
        return tail.prev.getData();
    }

    @Override
    public E removeFirst() {
        if (size < 1)
        {
            return null;
        }

        Node<E> first = head.getNext();
        Node<E> second = first.getNext();
        head.setNext(second);
        second.setPrev(head);
        size--;

        return first.getData();
    }

    @Override
    public E removeLast() {
        if (size < 1)
        {
            return null;
        }

        Node<E> last = tail.getPrev();
        Node<E> second_last = last.getPrev();
        second_last.setNext(tail);
        tail.setPrev(second_last);
        size--;

        return last.getData();
    }

    @Override
    public void addLast(E e) {
        addBetween(e, tail.getPrev(), tail);
    }

    @Override
    public void addFirst(E e) {
        addBetween(e, head, head.getNext());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> curr = head.next;
        while (curr != tail) {
            sb.append(curr.data);
            curr = curr.next;
            if (curr != tail) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public static void main(String[] args) {
        DoublyLinkedList<Integer> ll = new DoublyLinkedList<>();

        ll.addFirst(0);
        ll.addFirst(1);
        ll.addFirst(2);
        ll.addLast(-1);
        System.out.println(ll);

        ll.removeFirst();
        System.out.println(ll);

        ll.removeLast();
        System.out.println(ll);

        for (Integer e : ll) {
            System.out.println("value: " + e);
        }
    }
}

class DoublyLinkedListTest {

    @Test
    void testSize() {
        DoublyLinkedList<Integer> ll = new DoublyLinkedList<>();
        assertEquals(0, ll.size());
        ll.addFirst(0);
        assertEquals(1, ll.size());
    }

    @Test
    void testIsEmpty() {
        DoublyLinkedList<Integer> ll = new DoublyLinkedList<>();
        assertTrue(ll.isEmpty());
        ll.addFirst(0);
        assertFalse(ll.isEmpty());
        ll.removeFirst();
        assertTrue(ll.isEmpty());
    }

    @Test
    void testFirst() {
        DoublyLinkedList<Integer> ll = new DoublyLinkedList<>();
        ll.addFirst(-1);
        assertEquals(-1, ll.first());

        ll.removeFirst();
        assertNull(ll.first());
    }

    @Test
    void testLast() {
        DoublyLinkedList<Integer> ll = new DoublyLinkedList<>();
        ll.addFirst(-1);

        assertEquals(-1, ll.last());

        ll.addFirst(-2);
        assertEquals(-1, ll.last());

        ll.addLast(-3);
        assertEquals(-3, ll.last());
    }


    @Test
    void testRemoveLast() {
        DoublyLinkedList<Integer> ll = new DoublyLinkedList<>();
        ll.addFirst(-1);
        ll.addFirst(-2);
        assertEquals(-1, ll.removeLast());
    }

    @Test
    void testGet() {
        DoublyLinkedList<Integer> ll = new DoublyLinkedList<>();
        for (int i = 0; i < 5; ++i) ll.addLast(i);

        assertEquals(1, ll.get(1));
    }

    @Test
    void testRemove() {
        DoublyLinkedList<Integer> ll = new DoublyLinkedList<>();
        for (int i = 0; i < 5; ++i) ll.addLast(i);

        ll.remove(1);
        assertEquals("[0, 2, 3, 4]", ll.toString());
    }

    @Test
    void testAdd() {
        DoublyLinkedList<Integer> ll = new DoublyLinkedList<>();
        for (int i = 0; i < 5; ++i) ll.addLast(i);

        ll.add(2, -1);
        assertEquals("[0, 1, -1, 2, 3, 4]", ll.toString());
    }

    @Test
    void testToString() {
        DoublyLinkedList<Integer> ll = new DoublyLinkedList<>();
        for (int i = 0; i < 5; ++i)
        {
            ll.addLast(i);
        }

        assertEquals("[0, 1, 2, 3, 4]", ll.toString());
    }

    @Test
    void testIterator() {
        DoublyLinkedList<Integer> ll = new DoublyLinkedList<>();
        for (int i = 0; i < 5; ++i) ll.addLast(i);

        ArrayList<Integer> buf = new ArrayList<>();
        for (Integer i : ll) {
            buf.add(i);
        }
        assertEquals("[0, 1, 2, 3, 4]", buf.toString());
    }
}