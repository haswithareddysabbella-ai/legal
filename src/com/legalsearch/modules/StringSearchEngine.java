package com.legalsearch.modules;

import java.util.*;

public class StringSearchEngine {

    // 1. KMP Algorithm for Exact Pattern Matching
    public List<Integer> searchKMP(String text, String pattern) {
        List<Integer> matches = new ArrayList<>();
        if (pattern.isEmpty() || text.isEmpty()) return matches;

        int[] lps = computeLPSArray(pattern);
        int i = 0, j = 0;

        while (i < text.length()) {
            if (pattern.charAt(j) == text.charAt(i)) {
                i++;
                j++;
            }
            if (j == pattern.length()) {
                matches.add(i - j);
                j = lps[j - 1];
            } else if (i < text.length() && pattern.charAt(j) != text.charAt(i)) {
                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    i++;
                }
            }
        }
        return matches;
    }

    private int[] computeLPSArray(String pattern) {
        int[] lps = new int[pattern.length()];
        int len = 0, i = 1;
        while (i < pattern.length()) {
            if (pattern.charAt(i) == pattern.charAt(len)) {
                len++;
                lps[i] = len;
                i++;
            } else {
                if (len != 0) {
                    len = lps[len - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }
        return lps;
    }

    // 2. Z-Algorithm for Pattern & Substring Matching
    public List<Integer> searchZ(String text, String pattern) {
        List<Integer> matches = new ArrayList<>();
        String concat = pattern + "$" + text;
        int[] Z = computeZArray(concat);

        int patLen = pattern.length();
        for (int i = 0; i < Z.length; i++) {
            if (Z[i] == patLen) {
                matches.add(i - patLen - 1);
            }
        }
        return matches;
    }

    private int[] computeZArray(String str) {
        int n = str.length();
        int[] Z = new int[n];
        int L = 0, R = 0;

        for (int i = 1; i < n; i++) {
            if (i > R) {
                L = R = i;
                while (R < n && str.charAt(R - L) == str.charAt(R)) R++;
                Z[i] = R - L;
                R--;
            } else {
                int k = i - L;
                if (Z[k] < R - i + 1) {
                    Z[i] = Z[k];
                } else {
                    L = i;
                    while (R < n && str.charAt(R - L) == str.charAt(R)) R++;
                    Z[i] = R - L;
                    R--;
                }
            }
        }
        return Z;
    }

    // 3. Rabin-Karp Algorithm
    public List<Integer> searchRabinKarp(String text, String pattern) {
        List<Integer> matches = new ArrayList<>();
        int d = 256;
        int q = 101;
        int m = pattern.length();
        int n = text.length();
        if (m > n) return matches;

        int p = 0;
        int t = 0;
        int h = 1;

        for (int i = 0; i < m - 1; i++) h = (h * d) % q;
        for (int i = 0; i < m; i++) {
            p = (d * p + pattern.charAt(i)) % q;
            t = (d * t + text.charAt(i)) % q;
        }

        for (int i = 0; i <= n - m; i++) {
            if (p == t) {
                boolean match = true;
                for (int j = 0; j < m; j++) {
                    if (text.charAt(i + j) != pattern.charAt(j)) {
                        match = false;
                        break;
                    }
                }
                if (match) matches.add(i);
            }
            if (i < n - m) {
                t = (d * (t - text.charAt(i) * h) + text.charAt(i + m)) % q;
                if (t < 0) t = (t + q);
            }
        }
        return matches;
    }
}