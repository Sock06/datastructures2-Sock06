package C_Trees;

public class Recursion {
    public static long fibonacci(int n) {
        if(n <= 1) {
            return n;
        }
        return fibonacci(n - 1) + fibonacci(n - 2);
    }

    public static int tribonacci(int n) {
        if (n == 2) {
            return 1;
        }
        if (n < 2) {
            return 0;
        }

        return tribonacci(n - 1) + tribonacci(n - 2) + tribonacci(n - 3);
    }

    public static int mcCarthy91(int n) {
        if (n > 100)
        {
            return n - 10;
        }
        else
        {
            return mcCarthy91(mcCarthy91(n + 11));
        }
    }

    public static void foo(int x) {
        if (x/2 == 0)
        {
            System.out.print(x);
        }
        else
        {
            foo(x/2);
            System.out.print(x%2);
        }
    }

    public static void main(String[] args)
    {
        System.out.println(fibonacci(5));
        System.out.println(tribonacci(9));
        System.out.println(mcCarthy91(87));
        foo(2468);

        //double t = System.currentTimeMillis();
        //System.out.println(fibonacci(50));
        //System.out.println("Time is " + (System.currentTimeMillis() - t));
    }
}

