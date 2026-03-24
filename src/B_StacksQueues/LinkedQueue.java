package B_StacksQueues;

import interfaces.Queue;
import A_LinkedLists.DoublyLinkedList;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LinkedQueue<E> implements Queue<E> {
    private DoublyLinkedList<E> ll;

    public static void main(String[] args) {
    }

    public LinkedQueue() {
        ll = new DoublyLinkedList<>();
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
    public void enqueue(E e) {
        ll.addLast(e);
    }

    @Override
    public E first() {
        return ll.get(0);
    }

    @Override
    public E dequeue() {
        return ll.removeFirst();
    }

    public String toString() {
        return ll.toString();
    }
}


class LinkedQueueTest {

    @Test
    void testSize() {
        LinkedQueue<Integer> s = new LinkedQueue<>();
        for (int i = 0; i < 10; ++i)
            s.enqueue(i);
        assertEquals(10, s.size());
    }

    @Test
    void testIsEmpty() {
        LinkedQueue<Integer> s = new LinkedQueue<>();
        for (int i = 0; i < 10; ++i)
            s.enqueue(i);
        for (int i = 0; i < 10; ++i)
            s.dequeue();
        assertTrue(s.isEmpty());
    }

    @Test
    void testEnqueue() {
        LinkedQueue<Integer> s = new LinkedQueue<>();
        for (int i = 0; i < 10; ++i)
            s.enqueue(i);
        assertEquals("[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]", s.toString());
    }

    @Test
    void testFirst() {
        LinkedQueue<Integer> s = new LinkedQueue<>();
        for (int i = 0; i < 10; ++i)
            s.enqueue(i);
        assertEquals(0, s.first());
    }

    @Test
    void testDequeue()
    {
        LinkedQueue<Integer> s = new LinkedQueue<>();
        for (int i = 0; i < 10; ++i)
            s.enqueue(i);

        assertEquals(0, s.dequeue());
        assertEquals(9, s.size());
    }
}
