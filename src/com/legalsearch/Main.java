package com.legalsearch;

import com.legalsearch.modules.NPConstraintEngine;
import com.legalsearch.modules.NetworkFlowEngine;
import com.legalsearch.modules.ParallelAnalyticsEngine;
import com.legalsearch.modules.QueryDPEngine;
import com.legalsearch.modules.StringSearchEngine;
import com.legalsearch.modules.SuffixIndexEngine;
import com.legalsearch.utils.MenuUI;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StringSearchEngine stringEngine = new StringSearchEngine();
        SuffixIndexEngine suffixEngine = new SuffixIndexEngine();
        QueryDPEngine queryDPEngine = new QueryDPEngine();
        NetworkFlowEngine flowEngine = new NetworkFlowEngine();
        NPConstraintEngine npEngine = new NPConstraintEngine();
        ParallelAnalyticsEngine parallelEngine = new ParallelAnalyticsEngine();
        boolean running = true;

        while (running) {
            MenuUI.printMenu();
            int choice = -1;

            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("\n[!] Invalid input. Please enter a number between 0 and 6.\n");
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.println("\n-------------------------------------------------------------------");
                    System.out.println("                 OPTION 1: SEARCH CONTRACT FOR CLAUSES            ");
                    System.out.println("-------------------------------------------------------------------");
                    System.out.print("Enter Document Content: ");
                    String text = scanner.nextLine();
                    System.out.print("Enter Word/Clause to Find: ");
                    String pattern = scanner.nextLine();

                    if (text.isEmpty() || pattern.isEmpty()) {
                        System.out.println("\n[!] Input text and search pattern cannot be empty.\n");
                        break;
                    }

                    System.out.println("\nSearching...");
                    
                    List<Integer> kmpResults = stringEngine.searchKMP(text, pattern);
                    System.out.println("-> KMP Search: Found at positions " + kmpResults);

                    List<Integer> zResults = stringEngine.searchZ(text, pattern);
                    System.out.println("-> Z-Algorithm: Found at positions " + zResults);

                    List<Integer> rkResults = stringEngine.searchRabinKarp(text, pattern);
                    System.out.println("-> Rabin-Karp: Found at positions " + rkResults);
                    System.out.println();
                    break;

                case 2:
                    System.out.println("\n-------------------------------------------------------------------");
                    System.out.println("          OPTION 2: COMPARE CONTRACTS & FIND DUPLICATES           ");
                    System.out.println("-------------------------------------------------------------------");
                    System.out.print("Enter Contract 1 Text: ");
                    String doc1 = scanner.nextLine();
                    System.out.print("Enter Contract 2 Text: ");
                    String doc2 = scanner.nextLine();

                    if (doc1.isEmpty() || doc2.isEmpty()) {
                        System.out.println("\n[!] Contract texts cannot be empty.\n");
                        break;
                    }

                    System.out.println("\nAnalyzing document structures...");
                    
                    String sharedClause = suffixEngine.findLongestSharedClause(doc1, doc2);
                    
                    System.out.println("\n-> Analysis Complete!");
                    System.out.println("-> Longest Shared Clause / Duplicate Content Found:");
                    System.out.println("   \"" + sharedClause + "\"");
                    int len = sharedClause.equals("No common clauses found.") ? 0 : sharedClause.length();
                    System.out.println("-> Length: " + len + " characters\n");
                    break;
                case 3:
                    System.out.println("\n-------------------------------------------------------------------");
                    System.out.println("          OPTION 3: FIX MISSPELLED SEARCH QUERIES (DP)             ");
                    System.out.println("-------------------------------------------------------------------");
                    System.out.print("Enter Search Query (with potential typos): ");
                    String userQuery = scanner.nextLine().trim();

                    List<String> legalTerms = List.of(
                        "indemnity", "liability", "jurisdiction", "arbitration",
                        "confidentiality", "termination", "severability", "warranty"
                    );

                    String corrected = queryDPEngine.correctQuery(userQuery, legalTerms);
                    int dist = queryDPEngine.computeDamerauLevenshteinDistance(userQuery, corrected);

                    System.out.println("\n-> Correction Result:");
                    System.out.println("   Original Query : " + userQuery);
                    System.out.println("   Suggested Term : " + corrected);
                    System.out.println("   Edit Distance  : " + dist + " operation(s)");

                    System.out.println("\n-> Running Matrix-Chain Pipeline Optimization...");
                    int[] pipelineDims = {10, 30, 5, 60, 10}; // Example transformation dimensions
                    int minOps = queryDPEngine.matrixChainOrder(pipelineDims);
                    System.out.println("   Minimum scalar operations for transformation pipeline: " + minOps + "\n");
                    break;

                case 4:
                    System.out.println("\n-------------------------------------------------------------------");
                    System.out.println("          OPTION 4: ASSIGN LAWYERS TO COURT CASES (NETWORK FLOW)   ");
                    System.out.println("-------------------------------------------------------------------");
                    
                    List<String> lawyers = List.of("Adv. Sharma (IP Law)", "Adv. Verma (Corporate)", "Adv. Rao (Litigation)", "Adv. Kapoor (Tax Law)");
                    List<String> cases = List.of("Case A: Patent Infringement", "Case B: Merger Acquisition", "Case C: Commercial Dispute", "Case D: Corporate Audit");

                    // Eligibility matrix: lawyer i can take case j
                    boolean[][] eligibility = {
                        {true,  false, false, false}, // Sharma -> Patent Infringement
                        {false, true,  true,  true }, // Verma -> Merger, Dispute, Audit
                        {false, false, true,  false}, // Rao -> Dispute
                        {false, false, false, true }  // Kapoor -> Audit
                    };

                    System.out.println("Running Dinic's Algorithm for Maximum Bipartite Matching...\n");
                    List<String> assignments = flowEngine.allocateLawyersToCases(lawyers, cases, eligibility);

                    for (String line : assignments) {
                        System.out.println(line);
                    }
                    System.out.println();
                    break;

                case 5:
                    System.out.println("\n-------------------------------------------------------------------");
                    System.out.println("          OPTION 5: CHECK LEGAL RULES & CONFLICTS (NP-HARD)        ");
                    System.out.println("-------------------------------------------------------------------");
                    
                    // --- 1. 3-SAT Constraint Consistency Evaluation ---
                    System.out.println("1. Evaluating Legal Rule Consistency (3-SAT Reduction)...");
                    int numVars = 3; // Legal Conditions: x1 = Exclusivity, x2 = Non-Compete, x3 = Arbitration
                    List<NPConstraintEngine.Clause3SAT> formula = new ArrayList<>();

                    // Clause 1: (x1 OR x2 OR x3)
                    formula.add(new NPConstraintEngine.Clause3SAT(
                        new NPConstraintEngine.Literal(1, true),
                        new NPConstraintEngine.Literal(2, true),
                        new NPConstraintEngine.Literal(3, true)
                    ));

                    // Clause 2: (NOT x1 OR NOT x2 OR x3)
                    formula.add(new NPConstraintEngine.Clause3SAT(
                        new NPConstraintEngine.Literal(1, false),
                        new NPConstraintEngine.Literal(2, false),
                        new NPConstraintEngine.Literal(3, true)
                    ));

                    boolean[] assignment = new boolean[numVars + 1];
                    boolean isConsistent = npEngine.isFormulaSatisfiable(numVars, formula, assignment, 1);

                    if (isConsistent) {
                        System.out.println("   -> Statutory Formula is SATISFIABLE (No rule conflicts detected).");
                        System.out.println("   -> Valid Assignment: x1=" + assignment[1] + ", x2=" + assignment[2] + ", x3=" + assignment[3]);
                    } else {
                        System.out.println("   -> Statutory Formula is UNSATISFIABLE (Direct legal conflict found!).");
                    }

                    // --- 2. Vertex Cover 2-Approximation for Clause Conflicts ---
                    System.out.println("\n2. Finding Minimum Clause Cover for Conflict Audit (Vertex Cover 2-Approx)...");
                    List<String> clauseNames = List.of(
                        "Clause 0: Indemnity", "Clause 1: Liability Cap", 
                        "Clause 2: Non-Compete", "Clause 3: Governing Law", "Clause 4: Arbitration"
                    );

                    List<NPConstraintEngine.ConflictEdge> conflicts = List.of(
                        new NPConstraintEngine.ConflictEdge(0, 1),
                        new NPConstraintEngine.ConflictEdge(1, 2),
                        new NPConstraintEngine.ConflictEdge(2, 3),
                        new NPConstraintEngine.ConflictEdge(3, 4)
                    );

                    Set<Integer> cover = npEngine.findVertexCover2Approximation(clauseNames.size(), conflicts);

                    System.out.println("   -> Priority Clauses to Audit/Modify (Covers all conflict pairs):");
                    for (int clauseIdx : cover) {
                        System.out.println("      - " + clauseNames.get(clauseIdx));
                    }
                    System.out.println();
                    break;

                case 6:
                    System.out.println("\n-------------------------------------------------------------------");
                    System.out.println("          OPTION 6: STREAMING BIG DATA & CRYPTO ANALYTICS          ");
                    System.out.println("-------------------------------------------------------------------");

                    // 1. Reservoir Sampling Demonstration
                    System.out.println("1. Reservoir Sampling over Legal Stream...");
                    List<String> docStream = new ArrayList<>();
                    for (int i = 1; i <= 1000; i++) {
                        docStream.add("Doc_ID_" + i);
                    }
                    int sampleSize = 5;
                    List<String> sampledDocs = parallelEngine.reservoirSample(docStream, sampleSize);
                    System.out.println("   -> Sampled " + sampleSize + " documents from stream of " + docStream.size() + " items:");
                    System.out.println("      " + sampledDocs);

                    // 2. Parallel Prefix Scan Demonstration
                    System.out.println("\n2. Parallel Prefix Scan (Cumulative Token Counting)...");
                    int[] wordCountsPerDoc = {1200, 850, 2100, 450, 3100, 1750};
                    int[] cumulativeTokens = parallelEngine.computeParallelPrefixScan(wordCountsPerDoc);
                    System.out.print("   -> Word Counts per Chunk : " + java.util.Arrays.toString(wordCountsPerDoc) + "\n");
                    System.out.print("   -> Cumulative Token Scan : " + java.util.Arrays.toString(cumulativeTokens) + "\n");

                    // 3. Miller-Rabin Cryptographic Key Verification
                    System.out.println("\n3. Cryptographic Signature Key Verification (Miller-Rabin Primality)...");
                    java.math.BigInteger cryptoPrime = new java.math.BigInteger("1000000007"); // 10^9 + 7
                    boolean isPrime = parallelEngine.isProbablePrime(cryptoPrime, 10);
                    System.out.println("   -> Testing Key Prime " + cryptoPrime + ": " + (isPrime ? "VALID PROBABLE PRIME" : "INVALID COMPOSITE"));
                    System.out.println();
                    break;

                case 0:
                    running = false;
                    System.out.println("\nThank you for using LegalSearch. Goodbye!");
                    break;

                default:
                    System.out.println("\n[!] Option out of range. Choose between 0 and 6.\n");
            }
        }
        scanner.close();
    }
}