package com.legalsearch.modules;

import java.util.Arrays;

public class SuffixIndexEngine {

    // Helper class to represent a suffix entry during sorting
    private static class Suffix implements Comparable<Suffix> {
        int index;
        int rank1;
        int rank2;

        @Override
        public int compareTo(Suffix other) {
            if (this.rank1 != other.rank1) {
                return Integer.compare(this.rank1, other.rank1);
            }
            return Integer.compare(this.rank2, other.rank2);
        }
    }

    /**
     * Builds a Suffix Array for a given text.
     * Returns an array of starting indices of sorted suffixes.
     */
    public int[] buildSuffixArray(String text) {
        int n = text.length();
        Suffix[] suffixes = new Suffix[n];

        for (int i = 0; i < n; i++) {
            suffixes[i] = new Suffix();
            suffixes[i].index = i;
            suffixes[i].rank1 = text.charAt(i);
            suffixes[i].rank2 = (i + 1 < n) ? text.charAt(i + 1) : -1;
        }

        Arrays.sort(suffixes);

        int[] ind = new int[n];
        for (int k = 4; k < 2 * n; k *= 2) {
            int rank = 0;
            int prevRank = suffixes[0].rank1;
            suffixes[0].rank1 = rank;
            ind[suffixes[0].index] = 0;

            for (int i = 1; i < n; i++) {
                if (suffixes[i].rank1 == prevRank && suffixes[i].rank2 == suffixes[i - 1].rank2) {
                    prevRank = suffixes[i].rank1;
                    suffixes[i].rank1 = rank;
                } else {
                    prevRank = suffixes[i].rank1;
                    suffixes[i].rank1 = ++rank;
                }
                ind[suffixes[i].index] = i;
            }

            for (int i = 0; i < n; i++) {
                int nextIndex = suffixes[i].index + k / 2;
                suffixes[i].rank2 = (nextIndex < n) ? suffixes[ind[nextIndex]].rank1 : -1;
            }

            Arrays.sort(suffixes);
        }

        int[] suffixArray = new int[n];
        for (int i = 0; i < n; i++) {
            suffixArray[i] = suffixes[i].index;
        }
        return suffixArray;
    }

    /**
     * Kasai's Algorithm to construct the LCP (Longest Common Prefix) array in O(N).
     * lcp[i] stores the length of the longest common prefix between 
     * suffixArray[i] and suffixArray[i-1].
     */
    public int[] buildLCPArray(String text, int[] suffixArray) {
        int n = text.length();
        int[] lcp = new int[n];
        int[] invSuff = new int[n];

        for (int i = 0; i < n; i++) {
            invSuff[suffixArray[i]] = i;
        }

        int k = 0;
        for (int i = 0; i < n; i++) {
            if (invSuff[i] == n - 1) {
                k = 0;
                continue;
            }

            int j = suffixArray[invSuff[i] + 1];

            while (i + k < n && j + k < n && text.charAt(i + k) == text.charAt(j + k)) {
                k++;
            }

            lcp[invSuff[i] + 1] = k;

            if (k > 0) {
                k--;
            }
        }
        return lcp;
    }

    /**
     * Compares two contract texts and finds the Longest Common Substring (Shared Clause)
     * using Generalized Suffix Array and Kasai's LCP Array.
     */
    public String findLongestSharedClause(String contract1, String contract2) {
        if (contract1 == null || contract2 == null || contract1.isEmpty() || contract2.isEmpty()) {
            return "";
        }

        // Combine contract1 and contract2 with unique sentinel characters
        String combined = contract1 + "#" + contract2 + "$";
        int len1 = contract1.length();
        int totalLen = combined.length();

        int[] sa = buildSuffixArray(combined);
        int[] lcp = buildLCPArray(combined, sa);

        int maxLen = 0;
        int maxIndex = 0;

        for (int i = 1; i < totalLen; i++) {
            // Check if adjacent suffixes belong to different original contracts
            boolean isSuff1InContract1 = sa[i - 1] < len1;
            boolean isSuff2InContract1 = sa[i] < len1;

            if (isSuff1InContract1 != isSuff2InContract1) {
                if (lcp[i] > maxLen) {
                    maxLen = lcp[i];
                    maxIndex = sa[i];
                }
            }
        }

        if (maxLen == 0) {
            return "No common clauses found.";
        }

        return combined.substring(maxIndex, maxIndex + maxLen);
    }
}