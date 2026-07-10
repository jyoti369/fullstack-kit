package algorithms.dynamic_programming;

/**
 * Stock Trading DP Problems — Classic Series
 */
public class StockProblems {

    // 1. Best Time to Buy and Sell — one transaction
    public static int maxProfit1(int[] prices) {
        int minPrice = Integer.MAX_VALUE, maxProfit = 0;
        for (int p : prices) {
            minPrice = Math.min(minPrice, p);
            maxProfit = Math.max(maxProfit, p - minPrice);
        }
        return maxProfit;
    }

    // 2. Unlimited transactions
    public static int maxProfit2(int[] prices) {
        int profit = 0;
        for (int i = 1; i < prices.length; i++)
            if (prices[i] > prices[i-1]) profit += prices[i] - prices[i-1];
        return profit;
    }

    // 3. At most 2 transactions
    public static int maxProfit3(int[] prices) {
        int buy1 = Integer.MIN_VALUE, sell1 = 0, buy2 = Integer.MIN_VALUE, sell2 = 0;
        for (int p : prices) {
            buy1  = Math.max(buy1, -p);
            sell1 = Math.max(sell1, buy1 + p);
            buy2  = Math.max(buy2, sell1 - p);
            sell2 = Math.max(sell2, buy2 + p);
        }
        return sell2;
    }

    // 4. At most k transactions
    public static int maxProfitK(int k, int[] prices) {
        int n = prices.length;
        if (k >= n / 2) return maxProfit2(prices); // unlimited
        int[] buy = new int[k], sell = new int[k];
        java.util.Arrays.fill(buy, Integer.MIN_VALUE);
        for (int p : prices) {
            for (int i = 0; i < k; i++) {
                buy[i]  = Math.max(buy[i], (i == 0 ? 0 : sell[i-1]) - p);
                sell[i] = Math.max(sell[i], buy[i] + p);
            }
        }
        return sell[k-1];
    }

    // 5. With cooldown (after sell, wait 1 day)
    public static int maxProfitCooldown(int[] prices) {
        int hold = Integer.MIN_VALUE, sold = 0, rest = 0;
        for (int p : prices) {
            int prevSold = sold;
            hold = Math.max(hold, rest - p);
            sold = hold + p;
            rest = Math.max(rest, prevSold);
        }
        return Math.max(sold, rest);
    }

    // 6. With transaction fee
    public static int maxProfitFee(int[] prices, int fee) {
        int hold = Integer.MIN_VALUE, cash = 0;
        for (int p : prices) {
            hold = Math.max(hold, cash - p);
            cash = Math.max(cash, hold + p - fee);
        }
        return cash;
    }

    public static void main(String[] args) {
        int[] p = {3,3,5,0,0,3,1,4};
        System.out.println(maxProfit1(p));    // 4
        System.out.println(maxProfit2(p));    // 6
        System.out.println(maxProfit3(p));    // 6
        System.out.println(maxProfitK(2,p));  // 6
        System.out.println(maxProfitCooldown(new int[]{1,2,3,0,2})); // 3
        System.out.println(maxProfitFee(new int[]{1,3,2,8,4,9}, 2)); // 8
    }
}


package algorithms.dynamic_programming;

/**
 * Stock Trading DP Problems — Classic Series
 */
public class StockProblems {

    // 1. Best Time to Buy and Sell — one transaction
    public static int maxProfit1(int[] prices) {
        int minPrice = Integer.MAX_VALUE, maxProfit = 0;
        for (int p : prices) {
            minPrice = Math.min(minPrice, p);
            maxProfit = Math.max(maxProfit, p - minPrice);
        }
        return maxProfit;
    }

    // 2. Unlimited transactions
    public static int maxProfit2(int[] prices) {
        int profit = 0;
        for (int i = 1; i < prices.length; i++)
            if (prices[i] > prices[i-1]) profit += prices[i] - prices[i-1];
        return profit;
    }

    // 3. At most 2 transactions
    public static int maxProfit3(int[] prices) {
        int buy1 = Integer.MIN_VALUE, sell1 = 0, buy2 = Integer.MIN_VALUE, sell2 = 0;
        for (int p : prices) {
            buy1  = Math.max(buy1, -p);
            sell1 = Math.max(sell1, buy1 + p);
            buy2  = Math.max(buy2, sell1 - p);
            sell2 = Math.max(sell2, buy2 + p);
        }
        return sell2;
    }

    // 4. At most k transactions
    public static int maxProfitK(int k, int[] prices) {
        int n = prices.length;
        if (k >= n / 2) return maxProfit2(prices); // unlimited
        int[] buy = new int[k], sell = new int[k];
        java.util.Arrays.fill(buy, Integer.MIN_VALUE);
        for (int p : prices) {
            for (int i = 0; i < k; i++) {
                buy[i]  = Math.max(buy[i], (i == 0 ? 0 : sell[i-1]) - p);
                sell[i] = Math.max(sell[i], buy[i] + p);
            }
        }
        return sell[k-1];
    }

    // 5. With cooldown (after sell, wait 1 day)
    public static int maxProfitCooldown(int[] prices) {
        int hold = Integer.MIN_VALUE, sold = 0, rest = 0;
        for (int p : prices) {
            int prevSold = sold;
            hold = Math.max(hold, rest - p);
            sold = hold + p;
            rest = Math.max(rest, prevSold);
        }
        return Math.max(sold, rest);
    }

    // 6. With transaction fee
    public static int maxProfitFee(int[] prices, int fee) {
        int hold = Integer.MIN_VALUE, cash = 0;
        for (int p : prices) {
            hold = Math.max(hold, cash - p);
            cash = Math.max(cash, hold + p - fee);
        }
        return cash;
    }

    public static void main(String[] args) {
        int[] p = {3,3,5,0,0,3,1,4};
        System.out.println(maxProfit1(p));    // 4
        System.out.println(maxProfit2(p));    // 6
        System.out.println(maxProfit3(p));    // 6
        System.out.println(maxProfitK(2,p));  // 6
        System.out.println(maxProfitCooldown(new int[]{1,2,3,0,2})); // 3
        System.out.println(maxProfitFee(new int[]{1,3,2,8,4,9}, 2)); // 8
    }
}
