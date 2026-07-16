package algorithms.dynamic_programming;

import java.util.*;

/**
 * Knapsack, Coin Change and Partition Variants — Java
 */
public class KnapsackCoinChange {

    // 1. 0/1 Knapsack
    public static int zeroOneKnapsack(int[] weights, int[] values, int W) {
        int n = weights.length;
        int[] dp = new int[W + 1];
        for (int i = 0; i < n; i++)
            for (int w = W; w >= weights[i]; w--)
                dp[w] = Math.max(dp[w], dp[w - weights[i]] + values[i]);
        return dp[W];
    }

    // 2. Unbounded Knapsack (can repeat items)
    public static int unboundedKnapsack(int[] weights, int[] values, int W) {
        int[] dp = new int[W + 1];
        for (int w = 1; w <= W; w++)
            for (int i = 0; i < weights.length; i++)
                if (weights[i] <= w) dp[w] = Math.max(dp[w], dp[w - weights[i]] + values[i]);
        return dp[W];
    }

    // 3. Coin Change — min coins
    public static int coinChange(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, amount + 1);
        dp[0] = 0;
        for (int coin : coins)
            for (int a = coin; a <= amount; a++)
                dp[a] = Math.min(dp[a], dp[a - coin] + 1);
        return dp[amount] > amount ? -1 : dp[amount];
    }

    // 4. Coin Change II — number of combinations
    public static int change(int amount, int[] coins) {
        int[] dp = new int[amount + 1];
        dp[0] = 1;
        for (int coin : coins)
            for (int a = coin; a <= amount; a++)
                dp[a] += dp[a - coin];
        return dp[amount];
    }

    // 5. Partition Equal Subset Sum
    public static boolean canPartition(int[] nums) {
        int sum = 0;
        for (int n : nums) sum += n;
        if (sum % 2 != 0) return false;
        int target = sum / 2;
        boolean[] dp = new boolean[target + 1];
        dp[0] = true;
        for (int num : nums)
            for (int j = target; j >= num; j--)
                dp[j] = dp[j] || dp[j - num];
        return dp[target];
    }

    // 6. Target Sum (+/-) — count ways
    public static int findTargetSumWays(int[] nums, int target) {
        int sum = 0;
        for (int n : nums) sum += n;
        if ((sum + target) % 2 != 0 || Math.abs(target) > sum) return 0;
        int positive = (sum + target) / 2;
        int[] dp = new int[positive + 1];
        dp[0] = 1;
        for (int num : nums)
            for (int j = positive; j >= num; j--)
                dp[j] += dp[j - num];
        return dp[positive];
    }

    // 7. Minimum Subset Sum Difference
    public static int minimumDifference(int[] nums) {
        int sum = 0;
        for (int n : nums) sum += n;
        int half = sum / 2;
        boolean[] dp = new boolean[half + 1];
        dp[0] = true;
        for (int num : nums)
            for (int j = half; j >= num; j--)
                dp[j] = dp[j] || dp[j - num];
        for (int j = half; j >= 0; j--)
            if (dp[j]) return sum - 2 * j;
        return sum;
    }

    public static void main(String[] args) {
        System.out.println(zeroOneKnapsack(new int[]{1,3,4,5}, new int[]{1,4,5,7}, 7)); // 9
        System.out.println(coinChange(new int[]{1,5,11}, 15));   // 3
        System.out.println(change(5, new int[]{1,2,5}));          // 4
        System.out.println(canPartition(new int[]{1,5,11,5}));   // true
        System.out.println(findTargetSumWays(new int[]{1,1,1,1,1}, 3)); // 5
        System.out.println(minimumDifference(new int[]{3,1,4,2,2,1})); // 1
    }
}


package algorithms.dynamic_programming;

import java.util.*;

/**
 * Knapsack, Coin Change and Partition Variants — Java
 */
public class KnapsackCoinChange {

    // 1. 0/1 Knapsack
    public static int zeroOneKnapsack(int[] weights, int[] values, int W) {
        int n = weights.length;
        int[] dp = new int[W + 1];
        for (int i = 0; i < n; i++)
            for (int w = W; w >= weights[i]; w--)
                dp[w] = Math.max(dp[w], dp[w - weights[i]] + values[i]);
        return dp[W];
    }

    // 2. Unbounded Knapsack (can repeat items)
    public static int unboundedKnapsack(int[] weights, int[] values, int W) {
        int[] dp = new int[W + 1];
        for (int w = 1; w <= W; w++)
            for (int i = 0; i < weights.length; i++)
                if (weights[i] <= w) dp[w] = Math.max(dp[w], dp[w - weights[i]] + values[i]);
        return dp[W];
    }

    // 3. Coin Change — min coins
    public static int coinChange(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, amount + 1);
        dp[0] = 0;
        for (int coin : coins)
            for (int a = coin; a <= amount; a++)
                dp[a] = Math.min(dp[a], dp[a - coin] + 1);
        return dp[amount] > amount ? -1 : dp[amount];
    }

    // 4. Coin Change II — number of combinations
    public static int change(int amount, int[] coins) {
        int[] dp = new int[amount + 1];
        dp[0] = 1;
        for (int coin : coins)
            for (int a = coin; a <= amount; a++)
                dp[a] += dp[a - coin];
        return dp[amount];
    }

    // 5. Partition Equal Subset Sum
    public static boolean canPartition(int[] nums) {
        int sum = 0;
        for (int n : nums) sum += n;
        if (sum % 2 != 0) return false;
        int target = sum / 2;
        boolean[] dp = new boolean[target + 1];
        dp[0] = true;
        for (int num : nums)
            for (int j = target; j >= num; j--)
                dp[j] = dp[j] || dp[j - num];
        return dp[target];
    }

    // 6. Target Sum (+/-) — count ways
    public static int findTargetSumWays(int[] nums, int target) {
        int sum = 0;
        for (int n : nums) sum += n;
        if ((sum + target) % 2 != 0 || Math.abs(target) > sum) return 0;
        int positive = (sum + target) / 2;
        int[] dp = new int[positive + 1];
        dp[0] = 1;
        for (int num : nums)
            for (int j = positive; j >= num; j--)
                dp[j] += dp[j - num];
        return dp[positive];
    }

    // 7. Minimum Subset Sum Difference
    public static int minimumDifference(int[] nums) {
        int sum = 0;
        for (int n : nums) sum += n;
        int half = sum / 2;
        boolean[] dp = new boolean[half + 1];
        dp[0] = true;
        for (int num : nums)
            for (int j = half; j >= num; j--)
                dp[j] = dp[j] || dp[j - num];
        for (int j = half; j >= 0; j--)
            if (dp[j]) return sum - 2 * j;
        return sum;
    }

    public static void main(String[] args) {
        System.out.println(zeroOneKnapsack(new int[]{1,3,4,5}, new int[]{1,4,5,7}, 7)); // 9
        System.out.println(coinChange(new int[]{1,5,11}, 15));   // 3
        System.out.println(change(5, new int[]{1,2,5}));          // 4
        System.out.println(canPartition(new int[]{1,5,11,5}));   // true
        System.out.println(findTargetSumWays(new int[]{1,1,1,1,1}, 3)); // 5
        System.out.println(minimumDifference(new int[]{3,1,4,2,2,1})); // 1
    }
}
