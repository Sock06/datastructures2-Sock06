//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main
{
    public static void main(String[] args)
    {
        int[] arr = new int[]{12, 44, 13, 88, 23, 94, 11, 39, 20, 16, 5};

        for (int i : arr)
        {
            int hi =  (3*i + 5) % 11;
            System.out.println(i + " --> " + hi);
        }
    }
}