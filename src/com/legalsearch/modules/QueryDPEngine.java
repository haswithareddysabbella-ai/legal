package com.legalsearch.modules;

import java.util.Arrays;
import java.util.List;

public class QueryDPEngine {

    /**
     * 1. Damerau-Levenshtein Distance
     * Calculates edit distance supporting:
     * - Insertion
     * - Deletion
     * - Substitution
     * - Transposition (swapping two adjacent characters)
     */
    public int computeDamerauLevenshteinDistance(String source, String target) {
        if (source == null || target == null) return 0;
        int m = source.length();
        int n = target.length();

        int[][] dp = new int[m + 1][n + 1];

        for (int i = 0; i <= m; i++) dp[i][0] = i;
        for (int j = 0; j <= n; j++) dp[0][j] = j;

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                int cost = (source.charAt(i - 1) == target.charAt(j - 1)) ? 0 : 1;

                int insertion = dp[i][j - 1] + 1;
                int deletion = dp[i - 1][j] + 1;
                int substitution = dp[i - 1][j - 1] + cost;

                int minDistance = Math.min(insertion, Math.min(deletion, substitution));

                // Transposition check
                if (i > 1 && j > 1 &&
                    source.charAt(i - 1) == target.charAt(j - 2) &&
                    source.charAt(i - 2) == target.charAt(j - 1)) {
                    minDistance = Math.min(minDistance, dp[i - 2][j - 2] + cost);
                }

                dp[i][j] = minDistance;
            }
        }
        return dp[m][n];
    }

    /**
     * Finds the closest valid legal term from a dictionary for a misspelled user query.
     */
    public String correctQuery(String userQuery, List<String> legalDictionary) {
        if (userQuery == null || userQuery.trim().isEmpty() || legalDictionary == null || legalDictionary.isEmpty()) {
            return userQuery;
        }

        String bestMatch = userQuery;
        int minDistance = Integer.MAX_VALUE;

        for (String term : legalDictionary) {
            int dist = computeDamerauLevenshteinDistance(userQuery.toLowerCase(), term.toLowerCase());
            if (dist < minDistance) {
                minDistance = dist;
                bestMatch = term;
            }
        }

        // Return correction if edit distance is within reasonable threshold (<= 3)
        return (minDistance <= 3) ? bestMatch : userQuery;
    }

    /**
     * 2. Bitmask Dynamic Programming
     * Solves optimal clause processing order / Traveling Salesperson-style evaluation
     * over a set of N legal conditions with transition costs.
     * 
     * @param costMatrix transition cost matrix between legal conditions
     * @return minimum total cost to satisfy/process all conditions starting from condition 0
     */
    public int solveBitmaskDP(int[][] costMatrix) {
        int n = costMatrix.length;
        if (n == 0) return 0;

        int totalStates = 1 << n;
        int[][] dp = new int[totalStates][n];

        for (int[] row : dp) {
            Arrays.fill(row, 1_000_000);
        }

        // Base case: starting at condition 0 with mask (1 << 0)
        dp[1][0] = 0;

        for (int mask = 1; mask < totalStates; mask++) {
            for (int u = 0; u < n; u++) {
                if ((mask & (1 << u)) == 0) continue;
                if (dp[mask][u] >= 1_000_000) continue;

                for (int v = 0; v < n; v++) {
                    if ((mask & (1 << v)) != 0) continue; // v is already visited

                    int nextMask = mask | (1 << v);
                    int newCost = dp[mask][u] + costMatrix[u][v];
                    if (newCost < dp[nextMask][v]) {
                        dp[nextMask][v] = newCost;
                    }
                }
            }
        }

        int minCost = 1_000_000;
        int finalMask = totalStates - 1;
        for (int u = 0; u < n; u++) {
            minCost = Math.min(minCost, dp[finalMask][u]);
        }

        return minCost;
    }

    /**
     * 3. Matrix-Chain Multiplication
     * Finds the minimum scalar multiplications needed to chain legal processing operations.
     * 
     * @param p array of dimensions, where matrix i has dimensions p[i-1] x p[i]
     * @return minimum number of scalar multiplications required
     */
    public int matrixChainOrder(int[] p) {
        int n = p.length - 1;
        int[][] m = new int[n + 1][n + 1];

        for (int i = 1; i <= n; i++) {
            m[i][i] = 0;
        }

        for (int len = 2; len <= n; len++) {
            for (int i = 1; i <= n - len + 1; i++) {
                int j = i + len - 1;
                m[i][j] = Integer.MAX_VALUE;

                for (int k = i; k <= j - 1; k++) {
                    int q = m[i][k] + m[k + 1][j] + p[i - 1] * p[k] * p[j];
                    if (q < m[i][j]) {
                        m[i][j] = q;
                    }
                }
            }
        }
        return m[1][n];
    }
}