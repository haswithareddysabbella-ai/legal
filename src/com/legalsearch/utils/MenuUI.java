package com.legalsearch.utils;

public class MenuUI {

    public static void printHeader() {
        System.out.println("===================================================================");
        System.out.println("              LEGALSEARCH - LEGAL ANALYTICS PLATFORM               ");
        System.out.println("===================================================================");
    }

    public static void printMenu() {
        printHeader();
        System.out.println("[1]  Find Keyword or Clause          (Module 1: String Search)");
        System.out.println("[2]  Compare Contracts & Find Duplicates (Module 2: Suffix Indexing)");
        System.out.println("[3]  Fix Misspelled Search Queries    (Module 3: Fuzzy Matching DP)");
        System.out.println("[4]  Assign Lawyers to Court Cases    (Module 4: Network Flow)");
        System.out.println("[5]  Check Legal Rules & Conflicts    (Module 5: Constraint Verification)");
        System.out.println("[6]  Process Streaming Live Documents (Module 6: Big Data Analytics)");
        System.out.println("[0]  Exit Application");
        System.out.println("===================================================================");
        System.out.print("Enter your choice [0-6]: ");
    }
}