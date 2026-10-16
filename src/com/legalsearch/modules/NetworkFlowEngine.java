package com.legalsearch.modules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class NetworkFlowEngine {

    // Represents an edge in the flow network
    public static class Edge {
        public int to;
        public int rev;
        public int capacity;
        public int flow;

        public Edge(int to, int rev, int capacity) {
            this.to = to;
            this.rev = rev;
            this.capacity = capacity;
            this.flow = 0;
        }
    }

    private List<List<Edge>> graph;
    private int[] level;
    private int[] ptr;

    /**
     * 1. Dinic's Algorithm for Maximum Network Flow
     */
    public int computeMaxFlow(int numVertices, List<List<Edge>> adjGraph, int source, int sink) {
        this.graph = adjGraph;
        this.level = new int[numVertices];
        this.ptr = new int[numVertices];

        int maxFlow = 0;

        while (bfs(source, sink, numVertices)) {
            Arrays.fill(ptr, 0);
            while (true) {
                int pushed = dfs(source, sink, Integer.MAX_VALUE);
                if (pushed == 0) break;
                maxFlow += pushed;
            }
        }

        return maxFlow;
    }

    private boolean bfs(int source, int sink, int n) {
        Arrays.fill(level, -1);
        level[source] = 0;
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(source);

        while (!queue.isEmpty()) {
            int v = queue.poll();
            for (Edge edge : graph.get(v)) {
                if (edge.capacity - edge.flow > 0 && level[edge.to] == -1) {
                    level[edge.to] = level[v] + 1;
                    queue.offer(edge.to);
                }
            }
        }
        return level[sink] != -1;
    }

    private int dfs(int v, int sink, int pushed) {
        if (pushed == 0) return 0;
        if (v == sink) return pushed;

        for (; ptr[v] < graph.get(v).size(); ptr[v]++) {
            Edge edge = graph.get(v).get(ptr[v]);
            int tr = edge.to;

            if (level[v] + 1 != level[tr] || edge.capacity - edge.flow == 0) {
                continue;
            }

            int trPushed = dfs(tr, sink, Math.min(pushed, edge.capacity - edge.flow));
            if (trPushed == 0) continue;

            edge.flow += trPushed;
            graph.get(tr).get(edge.rev).flow -= trPushed;
            return trPushed;
        }
        return 0;
    }

    /**
     * Helper method to build a directed residual edge pair
     */
    public static void addEdge(List<List<Edge>> adjGraph, int from, int to, int capacity) {
        Edge a = new Edge(to, adjGraph.get(to).size(), capacity);
        Edge b = new Edge(from, adjGraph.get(from).size(), 0);
        adjGraph.get(from).add(a);
        adjGraph.get(to).add(b);
    }

    /**
     * 2. Maximum Bipartite Matching for Lawyer Allocation
     * Maps N lawyers to M court cases based on skill availability.
     *
     * @param lawyers list of lawyer names
     * @param cases list of case names
     * @param eligibilityMatrix boolean matrix where eligibilityMatrix[i][j] is true if lawyer i can take case j
     * @return List of formatted allocation strings
     */
    public List<String> allocateLawyersToCases(List<String> lawyers, List<String> cases, boolean[][] eligibilityMatrix) {
        int numLawyers = lawyers.size();
        int numCases = cases.size();

        // Nodes layout:
        // Source = 0
        // Lawyers = 1 to numLawyers
        // Cases = numLawyers + 1 to numLawyers + numCases
        // Sink = numLawyers + numCases + 1
        int source = 0;
        int sink = numLawyers + numCases + 1;
        int totalNodes = sink + 1;

        List<List<Edge>> adjGraph = new ArrayList<>();
        for (int i = 0; i < totalNodes; i++) {
            adjGraph.add(new ArrayList<>());
        }

        // Source to each lawyer (capacity = 1 case per lawyer)
        for (int i = 0; i < numLawyers; i++) {
            addEdge(adjGraph, source, i + 1, 1);
        }

        // Lawyer to eligible Case (capacity = 1)
        for (int i = 0; i < numLawyers; i++) {
            for (int j = 0; j < numCases; j++) {
                if (eligibilityMatrix[i][j]) {
                    addEdge(adjGraph, i + 1, numLawyers + 1 + j, 1);
                }
            }
        }

        // Case to Sink (capacity = 1 lawyer needed per case)
        for (int j = 0; j < numCases; j++) {
            addEdge(adjGraph, numLawyers + 1 + j, sink, 1);
        }

        // Run Dinic's Algorithm
        int totalAllocations = computeMaxFlow(totalNodes, adjGraph, source, sink);

        List<String> result = new ArrayList<>();
        result.add("Total Cases Successfully Assigned: " + totalAllocations + " / " + numCases);

        // Reconstruct assignments from flow network
        for (int i = 0; i < numLawyers; i++) {
            int lawyerNode = i + 1;
            for (Edge edge : adjGraph.get(lawyerNode)) {
                if (edge.flow == 1 && edge.to > numLawyers && edge.to <= numLawyers + numCases) {
                    int caseIndex = edge.to - numLawyers - 1;
                    result.add("  - " + lawyers.get(i) + "  -->  " + cases.get(caseIndex));
                }
            }
        }

        return result;
    }
}