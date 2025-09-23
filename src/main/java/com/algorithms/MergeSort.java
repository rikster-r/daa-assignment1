package com.algorithms;

import java.util.Arrays;

public class MergeSort {
    private static final int INSERTION_SORT_THRESHOLD = 16;

    public static class Metrics {
        public long comparisons = 0;
        public long assignments = 0;
        public int maxDepth = 0;
        public int allocations = 0;
        public long elapsedNanos = 0;

        @Override
        public String toString() {
            return String.format(
                    "n/a, comparisons=%d, assignments=%d, depth=%d, allocations=%d, time=%.3f ms",
                    comparisons, assignments, maxDepth, allocations, elapsedNanos / 1_000_000.0
            );
        }

        public String toCSV(int n) {
            return String.format(
                    "%d,%d,%d,%d,%d,%.6f",
                    n, comparisons, assignments, maxDepth, allocations, elapsedNanos / 1_000_000.0
            );
        }
    }

    public static Metrics sort(int[] arr) {
        Metrics m = new Metrics();
        if (arr == null || arr.length <= 1) return m;
        int[] buffer = new int[arr.length];
        m.allocations++; // one allocation for buffer

        long start = System.nanoTime();
        mergeSort(arr, buffer, 0, arr.length - 1, 1, m);
        m.elapsedNanos = System.nanoTime() - start;

        return m;
    }

    private static void mergeSort(int[] arr, int[] buffer, int left, int right, int depth, Metrics m) {
        m.maxDepth = Math.max(m.maxDepth, depth);

        int size = right - left + 1;
        if (size <= INSERTION_SORT_THRESHOLD) {
            insertionSort(arr, left, right, m);
            return;
        }
        int mid = (left + right) >>> 1;
        mergeSort(arr, buffer, left, mid, depth + 1, m);
        mergeSort(arr, buffer, mid + 1, right, depth + 1, m);
        merge(arr, buffer, left, mid, right, m);
    }

    private static void merge(int[] arr, int[] buffer, int left, int mid, int right, Metrics m) {
        // copy to buffer
        System.arraycopy(arr, left, buffer, left, right - left + 1);
        m.assignments += (right - left + 1);

        int i = left, j = mid + 1, k = left;
        while (i <= mid && j <= right) {
            m.comparisons++;
            if (buffer[i] <= buffer[j]) {
                arr[k++] = buffer[i++];
            } else {
                arr[k++] = buffer[j++];
            }
            m.assignments++;
        }
        while (i <= mid) {
            arr[k++] = buffer[i++];
            m.assignments++;
        }
    }

    private static void insertionSort(int[] arr, int left, int right, Metrics m) {
        for (int i = left + 1; i <= right; i++) {
            int key = arr[i];
            int j = i - 1;
            while (j >= left) {
                m.comparisons++;
                if (arr[j] > key) {
                    arr[j + 1] = arr[j];
                    m.assignments++;
                    j--;
                } else break;
            }
            arr[j + 1] = key;
            m.assignments++;
        }
    }

    public static void demo(int n) {
        int[] arr = new java.util.Random().ints(n, -1_000_000, 1_000_000).toArray();
        Metrics m = sort(arr);
        System.out.println("Sorted correctly? " + isSorted(arr));
        System.out.println("Metrics CSV: n,comparisons,assignments,depth,allocations,time(ms)");
        System.out.println(m.toCSV(n));
    }

    private static boolean isSorted(int[] arr) {
        for (int i = 1; i < arr.length; i++) if (arr[i - 1] > arr[i]) return false;
        return true;
    }
}
