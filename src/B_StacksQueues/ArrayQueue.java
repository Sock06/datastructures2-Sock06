package B_StacksQueues;

import interfaces.Queue;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ArrayQueue<E> implements Queue<E> {

    private static int CAPACITY = 1000;
    private E[] data;
    private int front = 0;
    private int size = 0;

    public ArrayQueue(int capacity) {
        data = (E[]) new Object[capacity];
        CAPACITY = capacity;
    }

    public ArrayQueue() {
        this(CAPACITY);
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
    public void enqueue(E e) {
        if (size < CAPACITY)
        {
            data[size] = e;
            size++;
        }
        else
        {
            throw new IllegalStateException("Queue is full");
        }
    }

    @Override
    public E first() {
        return isEmpty() ? null : data[front];
    }

    @Override
    public E dequeue() {
        if (size <= 0)
        {
            throw new IllegalStateException("Queue is empty");
        }
        else {
            E top = data[front];
            for (int i = 0 ; i < size() - 1 ; i++)
            {
                data[i] = data[i+1];
            }
            size--;
            return top;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; ++i) {
            E res = data[(front + i) % CAPACITY];
            sb.append(res);
            if (i != size - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    public static void main(String[] args) {
        Queue<Integer> qq = new ArrayQueue<>();
        System.out.println(qq);

        int N = 10;
        for (int i = 0; i < N; ++i) {
            qq.enqueue(i);
        }
        System.out.println(qq);

        for (int i = 0; i < N / 2; ++i) qq.dequeue();
        System.out.println(qq);
    }
}


class ArrayQueueTest {
    @Test
    void testSize() {
        Queue<Integer> s = new ArrayQueue<>();
        for (int i = 0; i < 10; ++i)
            s.enqueue(i);
        assertEquals(10, s.size());
    }

    @Test
    void testIsEmpty() {
        Queue<Integer> s = new ArrayQueue<>();
        for (int i = 0; i < 10; ++i)
            s.enqueue(i);
        for (int i = 0; i < 10; ++i)
            s.dequeue();
        assertTrue(s.isEmpty());
    }

    @Test
    void testEnqueue() {
        Queue<Integer> s = new ArrayQueue<>();
        for (int i = 0; i < 10; ++i)
            s.enqueue(i);
        assertEquals("[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]", s.toString());
    }

    @Test
    void testFirst() {
        Queue<Integer> s = new ArrayQueue<>();
        for (int i = 0; i < 10; ++i)
            s.enqueue(i);
        assertEquals(0, s.first());
    }

    @Test
    void testDequeue() {
        Queue<Integer> s = new ArrayQueue<>();
        for (int i = 0; i < 10; ++i)
        {
            s.enqueue(i);
        }

        assertEquals(0, s.dequeue());
        assertEquals(9, s.size());
    }
}

