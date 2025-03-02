/*
Algorithm: Find the Kth Smallest Product in Two Sorted Arrays

1. Initialize a Min-Heap(Priority Queue) to store product values along with their indices.
2. Insert Initial Elements: Push the product of each element in `returns1` with the first element in `returns2` into the heap.
   - Store triplets `{product, index in returns1, index in returns2}`.
3. Extracting  the K Smallest Products:
   - Pop the smallest product from the heap `k` times.
   - Store the extracted product as the latest result.
   - If the extracted element's index from `returns2` has a next element, push the new product into the heap.
4. Returning  the Kth Smallest Extracted Product.

Time Complexity: O(k log k)** (Efficient compared to brute-force `O(m * n log(m * n))`)
Space Complexity: O(min(m, n))** (Heap stores at most `min(m, n)` elements)
*/


import java.util.PriorityQueue;

public class KthSmallestProduct1b { 
    public static int findKthSmallestProduct(int[] returns1, int[] returns2, int k) {
        // Min-Heap (Priority Queue) to store product values along with indices from returns1 and returns2.
        PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> 
            Integer.compare(a[0], b[0]) // Sorting by product value (smallest first)  
        );

        // Initializing the heap with the first element from returns1 and first element from returns2
        for (int i = 0; i < returns1.length; i++) {
            // Store {product, index of returns1, index of returns2}
            minHeap.offer(new int[]{returns1[i] * returns2[0], i, 0});
        }

        int result = 0; // Variable to store the kth smallest product

        // Process k smallest elements from the heap
        while (k-- > 0) {
            int[] top = minHeap.poll(); // Extract the smallest product from the heap
            result = top[0]; // Store the current smallest product as the result
            int i = top[1]; // Index from returns1
            int j = top[2]; // Index from returns2

            // If there is a next element in returns2, add the new product to the heap
            if (j + 1 < returns2.length) {
                minHeap.offer(new int[]{returns1[i] * returns2[j + 1], i, j + 1});
            }
        }

        return result; // Return the kth smallest product
    }

    public static void main(String[] args) {
        // Example test case 1
        int[] returns1 = {-4, -2, 0, 3}; // First sorted array
        int[] returns2 = {2, 4}; // Second sorted array
        int k = 6; // Target kth smallest product

        // Call the function and print the result
        System.out.println("The " + k + "th smallest investment return is: " + 
                            findKthSmallestProduct(returns1, returns2, k));
    }
}
/*
Example 1:

Input: 
returns1 = [2,5]
returns2 = [3,4]
k = 2

Heap Processing:
1st smallest product: 2 × 3 = 6
2nd smallest product: 2 × 4 = 8

Output:
The 2nd smallest investment return is: 8

Example 2:

Input: 
returns1 = [-4,-2,0,3]
returns2 = [2,4]
k = 6

Heap Processing:
1st smallest product: -4 × 4 = -16
2nd smallest product: -4 × 2 = -8
3rd smallest product: -2 × 4 = -8
4th smallest product: -2 × 2 = -4
5th smallest product:  0 × 2 = 0
6th smallest product:  0 × 4 = 0

Output:
The 6th smallest investment return is: 0
*/
