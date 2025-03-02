


/*
 * Problem: Finding  the critical temperature 'f' with the minimum number of measurements.
  
  Given:
  - 'k': Number of identical samples.
  - 'n': Number of temperature levels.
  - If a sample breaks at temperature 'x', it cannot be reused.
  - If a sample does not break, it can be reused for further testing.
 
  Goal:
  - Minimize the number of measurements required to determine 'f'.
 
 * Approach:
  - If k = 1, perform a linear search (worst case: 'n' measurements).
  - If k > 1, use dynamic programming:
    - Define dp[k][m] as the maximum number of temperature levels we can check.
    - Use the recurrence relation:
        dp[k][m] = dp[k-1][m-1] + dp[k][m-1] + 1
    - Increase 'm' until dp[k][m] >= n.
 
  
 */





public class CriticalTemperature1a {
    public static int minMeasurement(int k , int n){//Given that k = sample of material and n=temperature level of mateial
        int [][] dp = new int[k+1][n+1];//int [][]is take for 2darray which stores elements of k and n and k+1  for rows and n+1  for column is taken to  handle base case
        
        //base case
        for(int i = 0; i<k;i++){
            dp[i][0]=0;//at o0 temperature levelthe material will not be measuired
        }

        for(int j=0; j<n;j++){
            dp[1][j]=j;// if we have one sample than we have to measure or test the temperature level of material 1 to n linearly

        }
        for (int i = 2;i<=k;i++){//loop iterates over no of samples of material
            for(int j = 1;j<=n;j++){//Loop iterates over no of temp levels.Here j=0 has alredy been taken in base case so we staterd form j =1
                dp[i][j] = Integer.MAX_VALUE;// no  measurements have been done yet,so it started with the highest value(infinity) and it gets updated as we find min.no.of measurements 
                for(int x= 1;x<=j;x++){//loop iterates over each possible temperature level to test each one by one
                    int worstCase = Math.max(dp[i-1][x-1],dp[i][j-x]);// to calculate the worst case scenario
                    dp[i][j]=Math.min(dp[i][j],1+worstCase);//update the value in d[i][j] with min of previous result
                }
            }
 }
 return dp[k][n];//return result for sample k and temperature level n 

 
 }

 public static void main (String[] args){
    System.out.println("Minimum measurements (k=1, n=2):"+ minMeasurement(1, 2));
    System.out.println("Minimum measurements (k=1, n=2):"+ minMeasurement(2, 6));
    System.out.println("Minimum measurements (k=1, n=2):"+ minMeasurement(3, 14));
    

 }

        }

/*Output */
/*Minimum measurements (k=1, n=2):0
Minimum measurements (k=1, n=2):3
Minimum measurements (k=1, n=2):4 */
