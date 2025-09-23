package com.algorithms;

public class Main {
    public static void main(String[] args) {
        System.out.println("Divide and Conquer Algorithms - v1.0.0");
        int n = (args.length > 0) ? Integer.parseInt(args[0]) : 100_000;
        System.out.println("Running MergeSort on n=" + n);
        MergeSort.demo(n);
    }
}
