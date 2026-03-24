package B_StacksQueues;

import A_LinkedLists.DoublyLinkedList;
import interfaces.Stack;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LinkedStack<E> implements Stack<E> {
    DoublyLinkedList<E> ll;

    public static void main(String[] args) {
    }

    public LinkedStack() {
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
    public void push(E e) {
        ll.addFirst(e);
    }

    @Override
    public E top() {
        return ll.get(0);
    }

    @Override
    public E pop() {
        return ll.removeFirst();
    }

    public String toString() {
        return ll.toString();
    }
}


class LinkedStackTest {

    @Test
    void testSize() {
        LinkedStack<Integer> s = new LinkedStack<>();
        for (int i = 0; i < 10; ++i)
            s.push(i);
        assertEquals(10, s.size());
    }

    @Test
    void testIsEmpty() {
        LinkedStack<Integer> s = new LinkedStack<>();
        for (int i = 0; i < 10; ++i)
            s.push(i);
        for (int i = 0; i < 10; ++i) {
            s.pop();
        }
        assertTrue(s.isEmpty());
    }

    @Test
    void testPush() {
        LinkedStack<Integer> s = new LinkedStack<>();
        for (int i = 0; i < 10; ++i)
            s.push(i);
        assertEquals(10, s.size());
        assertEquals("[9, 8, 7, 6, 5, 4, 3, 2, 1, 0]", s.toString());
    }

    @Test
    void testTop() {
        LinkedStack<Integer> s = new LinkedStack<>();
        for (int i = 0; i < 10; ++i)
            s.push(i);
        assertEquals(9, s.top());
    }

    @Test
    void testPop() {
        LinkedStack<Integer> s = new LinkedStack<>();
        for (int i = 0; i < 10; ++i)
            s.push(i);
        assertEquals(9, s.pop());
        assertEquals(9, s.size());
    }

    @Test
    void testToString() {
        LinkedStack<Integer> s = new LinkedStack<>();
        for (int i = 0; i < 10; ++i)
            s.push(i);
        assertEquals("[9, 8, 7, 6, 5, 4, 3, 2, 1, 0]", s.toString());
    }
}