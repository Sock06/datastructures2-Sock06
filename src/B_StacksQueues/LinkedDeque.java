package B_StacksQueues;

import interfaces.Deque;
import A_LinkedLists.DoublyLinkedList;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LinkedDeque<E> implements Deque<E> {

    DoublyLinkedList<E> ll;

    public LinkedDeque() {
        ll = new DoublyLinkedList<>();
    }

    public static void main(String[] args) {
        LinkedDeque<Integer> ldq = new LinkedDeque<>();

        System.out.println("IsEmpty: " + ldq.isEmpty());
        for (int i = 0 ; i < 20; i++)
        {
            if (i % 2 == 0)
            {
                ldq.addLast(i);
            }
            else
            {
                ldq.addFirst(i);
            }
        }

        System.out.println(ldq);
        System.out.println("IsEmpty: " + ldq.isEmpty() + ", Size = " + ldq.size());
        System.out.println("First = " + ldq.first() + ", Last = " + ldq.last());
        ldq.removeFirst();
        ldq.removeLast();
        System.out.println("First = " + ldq.first() + ", Last = " + ldq.last());
    }

    @Override
    public int size() {
        return ll.size();
    }

    @Override
    public boolean isEmpty() {
        return ll.isEmpty();
    }

    @Override
    public E first() {
        return ll.first();
    }

    @Override
    public E last() {
        return ll.last();
    }

    @Override
    public void addFirst(E e) {
        ll.addFirst(e);
    }

    @Override
    public void addLast(E e) {
        ll.addLast(e);
    }

    @Override
    public E removeFirst() {
        return ll.removeFirst();
    }

    @Override
    public E removeLast() {
        return ll.removeLast();
    }

    public String toString() {
        return ll.toString();
    }
}


class LinkedDequeTest {
    @Test
    void testSize() {
        Deque<Integer> s = new LinkedDeque<>();
        for (int i = 0; i < 10; ++i)
            s.addFirst(i);
        assertEquals(10, s.size());
    }

    @Test
    void testIsEmpty() {
        Deque<Integer> s = new LinkedDeque<>();

        for (int i = 0; i < 10; ++i)
            s.addFirst(i);
        for (int i = 0; i < 10; ++i)
            s.removeFirst();
        assertTrue(s.isEmpty());
    }

    @Test
    void testAddFirst() {
        Deque<Integer> s = new LinkedDeque<>();

        for (int i = 0; i < 10; ++i)
            s.addFirst(i);
        assertEquals("[9, 8, 7, 6, 5, 4, 3, 2, 1, 0]", s.toString());
    }

    @Test
    void testAddLast() {
        Deque<Integer> s = new LinkedDeque<>();

        for (int i = 0; i < 10; ++i)
            s.addLast(i);
        assertEquals("[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]", s.toString());
    }

    @Test
    void testRemoveFirst() {
        Deque<Integer> s = new LinkedDeque<>();

        for (int i = 0; i < 10; ++i)
            s.addFirst(i);
        s.removeFirst();
        assertEquals("[8, 7, 6, 5, 4, 3, 2, 1, 0]", s.toString());
    }

    @Test
    void testRemoveLast() {
        Deque<Integer> s = new LinkedDeque<>();

        for (int i = 0; i < 10; ++i)
            s.addFirst(i);
        s.removeLast();
        assertEquals("[9, 8, 7, 6, 5, 4, 3, 2, 1]", s.toString());
    }
}
