package algorithms.math;

import java.util.*;

/**
 * Number Theory Algorithms in Java
 */
public class NumberTheory {

    // 1. GCD and LCM
    public static long gcd(long a, long b) { return b == 0 ? a : gcd(b, a % b); }
    public static long lcm(long a, long b) { return a / gcd(a, b) * b; }

    // 2. Sieve of Eratosthenes
    public static List<Integer> sieve(int n) {
        boolean[] isComposite = new boolean[n + 1];
        List<Integer> primes = new ArrayList<>();
        for (int i = 2; i <= n; i++) {
            if (!isComposite[i]) {
                primes.add(i);
                for (long j = (long) i * i; j <= n; j += i)
                    isComposite[(int) j] = true;
            }
        }
        return primes;
    }

    // 3. Fast Modular Exponentiation
    public static long modPow(long base, long exp, long mod) {
        long result = 1;
        base %= mod;
        while (exp > 0) {
            if ((exp & 1) == 1) result = result * base % mod;
            base = base * base % mod;
            exp >>= 1;
        }
        return result;
    }

    // 4. Count Prime Factors
    public static Map<Integer, Integer> primeFactors(int n) {
        Map<Integer, Integer> factors = new LinkedHashMap<>();
        for (int p = 2; p * p <= n; p++) {
            while (n % p == 0) { factors.merge(p, 1, Integer::sum); n /= p; }
        }
        if (n > 1) factors.put(n, 1);
        return factors;
    }

    // 5. Extended Euclidean (Modular Inverse)
    public static long[] extGcd(long a, long b) {
        if (b == 0) return new long[]{a, 1, 0};
        long[] r = extGcd(b, a % b);
        return new long[]{r[0], r[2], r[1] - (a/b)*r[2]};
    }
    public static long modInverse(long a, long mod) {
        long[] r = extGcd(a, mod);
        if (r[0] != 1) throw new ArithmeticException("No inverse");
        return (r[1] % mod + mod) % mod;
    }

    // 6. Catalan Numbers
    public static long catalan(int n) {
        long c = 1;
        for (int i = 0; i < n; i++) { c = c * 2 * (2*i+1) / (i+2); }
        return c;
    }

    // 7. Pascal's Triangle row
    public static long[] pascalRow(int n) {
        long[] row = new long[n + 1];
        row[0] = 1;
        for (int i = 1; i <= n; i++) row[i] = row[i-1] * (n-i+1) / i;
        return row;
    }

    // 8. Power of Two Check patterns
    public static boolean isPowerOf2(int n) { return n > 0 && (n & (n - 1)) == 0; }
    public static int nextPowerOf2(int n) { return Integer.highestOneBit(n - 1) << 1; }

    public static void main(String[] args) {
        System.out.println(gcd(12, 8));           // 4
        System.out.println(sieve(30));            // [2,3,5,7,11,13,17,19,23,29]
        System.out.println(modPow(2, 10, 1000));  // 24
        System.out.println(primeFactors(360));    // {2=3, 3=2, 5=1}
        System.out.println(catalan(5));           // 42
        System.out.println(Arrays.toString(pascalRow(5))); // [1,5,10,10,5,1]
    }
}
