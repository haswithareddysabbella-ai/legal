package com.legalsearch.modules;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NPConstraintEngine {

    /**
     * Represents a 3-SAT Literal (e.g., Variable x1 or NOT x1)
     */
    public static class Literal {
        public int varIndex; // 1-based index (1, 2, 3...)
        public boolean isTrue; // true = x, false = NOT x

        public Literal(int varIndex, boolean isTrue) {
            this.varIndex = varIndex;
            this.isTrue = isTrue;
        }

        public boolean evaluate(boolean[] assignment) {
            boolean val = assignment[varIndex];
            return isTrue ? val : !val;
        }
    }

    /**
     * Represents a 3-SAT Clause (Disjunction of 3 Literals: L1 OR L2 OR L3)
     */
    public static class Clause3SAT {
        public Literal l1, l2, l3;

        public Clause3SAT(Literal l1, Literal l2, Literal l3) {
            this.l1 = l1;
            this.l2 = l2;
            this.l3 = l3;
        }

        public boolean evaluate(boolean[] assignment) {
            return l1.evaluate(assignment) || l2.evaluate(assignment) || l3.evaluate(assignment);
        }
    }

    /**
     * 1. 3-SAT Evaluator & Dynamic Verification
     * Checks if a given variable assignment satisfies all legal rule constraints.
     *
     * @param numVars number of boolean legal conditions
     * @param formula list of 3-SAT clauses
     * @param assignment boolean values assigned to variables
     * @return true if all statutory constraints are satisfied
     */
    public boolean evaluate3SAT(int numVars, List<Clause3SAT> formula, boolean[] assignment) {
        for (Clause3SAT clause : formula) {
            if (!clause.evaluate(assignment)) {
                return false; // Found a rule conflict
            }
        }
        return true;
    }

    /**
     * Solves small 3-SAT formulas via backtracking to verify rule consistency.
     */
    public boolean isFormulaSatisfiable(int numVars, List<Clause3SAT> formula, boolean[] assignment, int currentVar) {
        if (currentVar > numVars) {
            return evaluate3SAT(numVars, formula, assignment);
        }

        // Try setting current variable to true
        assignment[currentVar] = true;
        if (isFormulaSatisfiable(numVars, formula, assignment, currentVar + 1)) {
            return true;
        }

        // Try setting current variable to false
        assignment[currentVar] = false;
        if (isFormulaSatisfiable(numVars, formula, assignment, currentVar + 1)) {
            return true;
        }

        return false;
    }

    /**
     * Represents an edge/conflict pair between two contract clauses
     */
    public static class ConflictEdge {
        public int u;
        public int v;

        public ConflictEdge(int u, int v) {
            this.u = u;
            this.v = v;
        }
    }

    /**
     * 2. Vertex Cover 2-Approximation Algorithm
     * Finds a near-optimal set of legal clauses to audit or remove so that
     * all conflicting clause pairs are addressed.
     *
     * @param numClauses total number of clauses (vertices)
     * @param conflicts list of conflicting clause pairs (edges)
     * @return Set of clause indices forming the Vertex Cover
     */
    public Set<Integer> findVertexCover2Approximation(int numClauses, List<ConflictEdge> conflicts) {
        Set<Integer> cover = new HashSet<>();
        boolean[] visitedEdges = new boolean[conflicts.size()];

        for (int i = 0; i < conflicts.size(); i++) {
            if (visitedEdges[i]) continue;

            ConflictEdge edge = conflicts.get(i);
            int u = edge.u;
            int v = edge.v;

            // If neither endpoint is in the cover, add both
            if (!cover.contains(u) && !cover.contains(v)) {
                cover.add(u);
                cover.add(v);

                // Mark all edges incident to u or v as covered
                for (int j = 0; j < conflicts.size(); j++) {
                    ConflictEdge e = conflicts.get(j);
                    if (e.u == u || e.v == u || e.u == v || e.v == v) {
                        visitedEdges[j] = true;
                    }
                }
            }
        }

        return cover;
    }
}