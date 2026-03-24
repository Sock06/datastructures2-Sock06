package D_PriorityQueues;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class LeetSolutions
{
    public static int findKthLargest(int[] nums, int k) {
        int[] heapq = new int[k];
        int temp;
        boolean inserted;

        for (int i = 0 ; i < k ; i++)
        {
            int j = 0;
            heapq[i] = nums[i];
            while (j < i)
            {
                if (heapq[j] > heapq[j + 1])
                {
                    temp = heapq[j];
                    heapq[j] = heapq[j + 1];
                    heapq[j + 1] = temp;
                    if (j > 0)
                    {
                        j--;
                    }
                }
                else
                {
                    j++;
                }
            }
        }

        for (int i = k ; i < nums.length ; i++)
        {
            inserted = false;
            if (nums[i] > heapq[0])
            {
                for (int j = 0; j < k - 1 ; j++)
                {
                    heapq[j] = heapq[j + 1];
                    if (nums[i] < heapq[j] && !inserted)
                    {
                        inserted = true;
                        heapq[j] = nums[i];
                        break;
                    }
                    //System.out.println(Arrays.toString(heapq));
                }
                if (!inserted)
                {
                    heapq[k - 1] = nums[i];
                }
            }
        }

        return heapq[0];
    }

    public static List<Integer> findClosestElements(int[] arr, int k, int x) {
        List<Integer> closestElem = new ArrayList<>();
        PriorityQueue<Integer> pq = new PriorityQueue<>();

        for (int i = 0 ; i < k ; i++)
        {
            pq.add(arr[i]);
        }

        System.out.println(pq);

        for (int i = k ; i < arr.length ; i++)
        {
            if (Math.abs(arr[i] - x) < Math.abs(pq.peek() - x))
            {
                pq.poll();
                pq.add(arr[i]);
            }
            //System.out.println("Call " + (i-k) + ": " + pq);
        }

        for (int i = 0 ; i < k ; i++)
        {
            closestElem.add(pq.poll());
        }

        return closestElem;
    }

    public static void main(String[] args)
    {
        int[] nums = new int[]{1,2,3,4,5};
        System.out.println(findClosestElements(nums, 4, 3));
    }
}