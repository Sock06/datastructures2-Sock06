package B_StacksQueues;

import interfaces.Stack;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ArrayStack<E> implements Stack<E> {

    public static int CAPACITY = 100;   // default array capacity
    private E[] data;                        // generic array used for storage
    private int t = 0;                // index of the top element in stack

    public ArrayStack() {
        this(CAPACITY);
    }  // constructs stack with default capacity

    @SuppressWarnings({"unchecked"})
    public ArrayStack(int capacity) {        // constructs stack with given capacity
        data = (E[]) new Object[capacity + 1];
        CAPACITY = capacity;
    }


    @Override
    public int size() {
        return t;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Inserts an element at the top of the stack.
     *
     * @param e the element to be inserted
     * @throws IllegalStateException if the array storing the elements is full
     */
    @Override
    public void push(E e) {
        if (t + 1 == CAPACITY)
        {
            throw new IllegalStateException();
        }
        else
        {
            data[t + 1] = e;
            t++;
        }
    }

    /**
     * Returns, but does not remove, the element at the top of the stack.
     *
     * @return top element in the stack (or null if empty)
     */
    @Override
    public E top() {
        return data[t];
    }

    /**
     * Removes and returns the top element from the stack.
     *
     * @return element removed (or null if empty)
     */
    @Override
    public E pop() {
        E top = data[t];
        if (top != null)
        {
            t--;
        }
        return top;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        if (!isEmpty()) {
            for (int i = t; i >= 0; --i) {
                if (data[i] == null)
                {
                    break;
                }
                else
                {
                    sb.append(data[i]);
                    if (i != 1) sb.append(", ");
                }
            }
        }
        sb.append("]");
        return sb.toString();
    }


    public static void main(String[] args) {
        Stack<Integer> S = new ArrayStack<>();  // contents: ()
        System.out.println(S.top());
        S.push(5);                              // contents: (5)
        S.push(3);                              // contents: (5, 3)
        System.out.println(S.size());           // contents: (5, 3)     outputs 2
        System.out.println(S.pop());            // contents: (5)        outputs 3
        System.out.println(S.isEmpty());        // contents: (5)        outputs false
        System.out.println(S.pop());            // contents: ()         outputs 5
        System.out.println(S.isEmpty());        // contents: ()         outputs true
        System.out.println(S.pop());            // contents: ()         outputs null
        S.push(7);                              // contents: (7)
        S.push(9);                              // contents: (7, 9)
        System.out.println(S.top());            // contents: (7, 9)     outputs 9
        S.push(4);                              // contents: (7, 9, 4)
        System.out.println(S.size());           // contents: (7, 9, 4)  outputs 3
        System.out.println(S.pop());            // contents: (7, 9)     outputs 4
        S.push(6);                              // contents: (7, 9, 6)
        S.push(8);                              // contents: (7, 9, 6, 8)
        System.out.println(S.pop());            // contents: (7, 9, 6)  outputs 8
        System.out.println(S);
    }
}

class DoubleStackQueue<E> {
    Stack<E> S1 = new ArrayStack<>();
    Stack<E> S2 = new ArrayStack<>();

    private void DSQEnqueue(E e)
    {
        S1.push(e);
    }

    private E DSQdequeue()
    {
        while (!S1.isEmpty())
        {
            E data = S1.pop();
            S2.push(data);
        }

        E to_deq = S2.pop();

        while (!S2.isEmpty())
        {
            E data = S1.pop();
            S2.push(data);
        }

        return to_deq;
    }

    public static String adjustDigits(ArrayStack<Integer> s)
    {
        String arr = s.toString();
        System.out.println("From: " + s + " to " + arr);

        arr.replace(",","");
        System.out.println("Now: "+ arr);
        arr.replace("[","");
        System.out.println("Now: "+ arr);
        arr.replace("]","");
        System.out.println("Now: "+ arr);
        return arr;
    }

    static String convertToNewBase(int dec_num, int base) {
        ArrayStack<Integer> digits = new ArrayStack<>();
        int original = dec_num;

        Stack<Integer> powers = new ArrayStack<>();
        int exp = base;
        int i = 0;

        powers.push(0);
        while (i < 10)
        {
            powers.push(exp);
            exp = exp*base;
            i++;
        }

        System.out.println(powers + "\n");

        while (dec_num > 0)
        {
            System.out.println("On:" + dec_num);
            digits.push((dec_num % base));
            dec_num = dec_num/base;
        }

        System.out.println(original + " = " + adjustDigits(digits));

        return "";
    }

    public static void main(String[] args)
    {
        convertToNewBase(23, 2);
        System.out.println("\n");
        convertToNewBase(32, 3);
        //convertToNewBase(42, 4);
    }
}

class BracketChecker {
    private String input;

    public BracketChecker(String in) {
        input = in;
    }

    public void check() {
        Stack<Character> bc = new ArrayStack<>();
        for (int i = 0 ; i < input.length() ; i++)
        {
            char c = input.charAt(i);
            if (c == '{' || c == '(' || c == '[')
            {
                bc.push(c);
            }
            if (c == '}' || c == ')' || c == ']')
            {
                if (bc.isEmpty())
                {
                    System.out.println("String Is NOT Balanced");
                    return;
                }
                if ((c == '}' && bc.pop() != '{') || (c == ')' && bc.pop() != '(') || (c == ']' && bc.pop() != '['))
                {
                    System.out.println("String Is NOT Balanced");
                    return;
                }
            }

            //System.out.println(bc);
        }

        if (!bc.isEmpty())
        {
            System.out.println("String Is NOT Balanced");
            return;
        }

        System.out.println("String is Balanced");
    }

    public static void main(String[] args) {
        String[] inputs = {
                "[]]()()", // not correct
                "c[d]", // correct\n" +
                "a{b[c]d}e", // correct\n" +
                "a{b(c]d}e", // not correct; ] doesn't match (\n" +
                "a[b{c}d]e}", // not correct; nothing matches final }\n" +
                "a{b(c) ", // // not correct; Nothing matches opening {
        };

        for (String input : inputs) {
            BracketChecker checker = new BracketChecker(input);
            System.out.println("\nchecking: " + input);
            checker.check();
        }
    }
}


class ArrayStackTest {
    @Test
    void testSize() {
        Stack<Integer> s = new ArrayStack<>();

        assertEquals(0, s.size());

        int N = 16;
        for (int i = 0; i < N; ++i) s.push(i);
        assertEquals(N, s.size());
    }

    @Test
    void testIsEmpty() {
        Stack<Integer> s = new ArrayStack<>();
        for (int i = 0; i < 10; ++i)
            s.push(i);
        for (int i = 0; i < 10; ++i) {
            s.pop();
        }
        assertTrue(s.isEmpty());
    }

    @Test
    void testPush() {
        Stack<Integer> s = new ArrayStack<>();
        for (int i = 0; i < 10; ++i)
            s.push(i);
        assertEquals(10, s.size());
        assertEquals("[9, 8, 7, 6, 5, 4, 3, 2, 1, 0]", s.toString());
    }

    @Test
    void testTop() {
        Stack<Integer> s = new ArrayStack<>();
        for (int i = 0; i < 10; ++i)
            s.push(i);
        assertEquals(9, s.top());
    }

    @Test
    void testPop() {
        Stack<Integer> s = new ArrayStack<>();
        for (int i = 0; i < 10; ++i)
            s.push(i);
        assertEquals(9, s.pop());
        assertEquals(9, s.size());
    }

    @Test
    void testToString() {
        Stack<Integer> s = new ArrayStack<>();
        for (int i = 0; i < 10; ++i)
            s.push(i);
        assertEquals("[9, 8, 7, 6, 5, 4, 3, 2, 1, 0]", s.toString());
    }

    @Test
    void testConvertToBinary()
    {
        //assertEquals("10111", convertToNewBase(23));
        //assertEquals("←-111001000000101011000010011101010110110001100010000000000000", convertToNewBase(1027010000000000000L));
    }
}

