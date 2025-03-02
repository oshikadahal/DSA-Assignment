/*Method cloasePair (int[] x_cords,int[]y_cords),
 * Using nested loop for loop iteration over all pairs of pints (i,j)
 * For each pair of points (i,j) we calculate Manhattan distance  
 * 
 */

package Question2;
public class ClosestPair { 

    // Function to find the lexicographically smallest pair of points with the smallest distance
    public static int[] findClosestPair(int[] x_coords, int[] y_coords) {
        int n = x_coords.length;
        int minDistance = Integer.MAX_VALUE; // Initialize the minimum distance to a large value
        int[] result = new int[2]; // Array to store the result indices

        // Iterate through all pairs of points
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                // Calculate the Manhattan distance between points (i, j)
                int distance = Math.abs(x_coords[i] - x_coords[j]) + Math.abs(y_coords[i] - y_coords[j]);

                // Check if the current distance is smaller than the minimum distance found so far
                if (distance < minDistance || (distance == minDistance && (i < result[0] || (i == result[0] && j < result[1])))) {
                    minDistance = distance; // Update the minimum distance
                    result[0] = i; // Update the first index of the result
                    result[1] = j; // Update the second index of the result
                }
            }
        }

        return result; // Return the result indices
    }

    public static void main(String[] args) {
        // Example input
        int[] x_coords = {1, 2, 3, 2, 4};
        int[] y_coords = {2, 3, 1, 2, 3};

        // Find the closest pair of points
        int[] result = findClosestPair(x_coords, y_coords);

        // Print the result
        System.out.println("The indices of the closest pair of points are: [" + result[0] + ", " + result[1] + "]");
    }
}
//Output:
//Closest pair: [0, 3]
