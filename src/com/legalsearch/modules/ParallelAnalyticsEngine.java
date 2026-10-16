package com.legalsearch.modules;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class ParallelAnalyticsEngine {

    private final Random random = new Random();

    /**
     * 1. Reservoir Sampling Algorithm
     * Selects k items uniformly at random from a stream of unknown/unbounded size.
     * 
     * @param stream list or sequence representing streaming document IDs or records
     * @param k size of the sample reservoir
     * @return List containing k sampled items
     */
    public <T> List<T> reservoirSample(List<T> stream, int k) {
        List<T> reservoir = new ArrayList<>(k);
        if (stream == null || stream.isEmpty() || k <= 0) {
            return reservoir;
        }

        int n = stream.size();
        // Fill reservoir with first k items
        for (int i = 0; i < Math.min(k, n); i++) {
            reservoir.add(stream.get(i));
        }

        // Process remaining items in stream
        for (int i = k; i < n; i++) {
            int j = random.nextInt(i + 1);
            if (j < k) {
                reservoir.set(j, stream.get(i));
            }
        }

        return reservoir;
    }

    /**
     * 2. Parallel Prefix Scan
     * Computes inclusive prefix sums of token counts across document chunks in parallel.
     * 
     * @param counts array of word/token counts per document
     * @return array of cumulative token totals
     */
    public int[] computeParallelPrefixScan(int[] counts) {
        if (counts == null || counts.length == 0) return new int[0];

        int[] result = new int[counts.length];
        
        // Compute parallel prefix sum using parallel stream operations
        IntStream.range(0, counts.length)
                 .parallel()
                 .forEach(i -> {
                     int sum = 0;
                     for (int j = 0; j <= i; j++) {
                         sum += counts[j];
                     }
                     result[i] = sum;
                 });

        return result;
    }

    /**
     * 3. Miller-Rabin Primality Test
     * Probabilistic primality test for cryptographic signature key validation.
     * 
     * @param n number to test for primality
     * @param iterations number of testing rounds (higher = greater confidence)
     * @return true if probably prime, false if composite
     */
    public boolean isProbablePrime(BigInteger n, int iterations) {
        if (n.equals(BigInteger.TWO) || n.equals(BigInteger.valueOf(3))) {
            return true;
        }
        if (n.compareTo(BigInteger.TWO) < 0 || n.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
            return false;
        }

        // Write n - 1 as 2^s * d
        BigInteger d = n.subtract(BigInteger.ONE);
        int s = 0;
        while (d.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
            d = d.divide(BigInteger.TWO);
            s++;
        }

        // Perform k rounds of Miller-Rabin testing
        for (int i = 0; i < iterations; i++) {
            BigInteger a = getRandomBase(n);
            BigInteger x = a.modPow(d, n);

            if (x.equals(BigInteger.ONE) || x.equals(n.subtract(BigInteger.ONE))) {
                continue;
            }

            boolean composite = true;
            for (int r = 1; r < s; r++) {
                x = x.modPow(BigInteger.TWO, n);
                if (x.equals(n.subtract(BigInteger.ONE))) {
                    composite = false;
                    break;
                }
            }

            if (composite) {
                return false;
            }
        }

        return true;
    }

    private BigInteger getRandomBase(BigInteger n) {
        BigInteger min = BigInteger.TWO;
        BigInteger max = n.subtract(BigInteger.TWO);
        BigInteger range = max.subtract(min).add(BigInteger.ONE);

        BigInteger a;
        do {
            a = new BigInteger(n.bitLength(), random);
        } while (a.compareTo(min) < 0 || a.compareTo(max) > 0);

        return a;
    }
}