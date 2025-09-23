package com.algorithms;

import com.algorithms.metrics.Metrics;
import java.util.Random;

public class MergeSort {

    private static final int INSERTION_SORT_THRESHOLD = 16;

    /**
     * Сортировка массива и сбор метрик.
     */
    public static Metrics sort(int[] arr) {
        Metrics m = new Metrics("MergeSort", arr.length);
        if (arr == null || arr.length <= 1) return m;

        int[] buffer = new int[arr.length];
        m.incrementAllocations(); // выделили буфер

        m.startTiming();
        mergeSort(arr, buffer, 0, arr.length - 1, 1, m);
        m.endTiming();

        return m;
    }

    private static void mergeSort(int[] arr, int[] buffer, int left, int right, int depth, Metrics m) {
        m.enterRecursion();

        if (depth > m.getMaxDepth()) {
            // глубина рекурсии автоматически обновляется в Metrics
        }

        int size = right - left + 1;
        if (size <= INSERTION_SORT_THRESHOLD) {
            insertionSort(arr, left, right, m);
            m.exitRecursion();
            return;
        }

        int mid = (left + right) >>> 1;
        mergeSort(arr, buffer, left, mid, depth + 1, m);
        mergeSort(arr, buffer, mid + 1, right, depth + 1, m);
        merge(arr, buffer, left, mid, right, m);

        m.exitRecursion();
    }

    private static void merge(int[] arr, int[] buffer, int left, int mid, int right, Metrics m) {
        System.arraycopy(arr, left, buffer, left, right - left + 1);
        m.incrementAllocations(right - left + 1);

        int i = left, j = mid + 1, k = left;
        while (i <= mid && j <= right) {
            m.incrementComparisons();
            if (buffer[i] <= buffer[j]) {
                arr[k++] = buffer[i++];
            } else {
                arr[k++] = buffer[j++];
            }
            m.incrementAllocations();
        }
        while (i <= mid) {
            arr[k++] = buffer[i++];
            m.incrementAllocations();
        }
    }

    private static void insertionSort(int[] arr, int left, int right, Metrics m) {
        for (int i = left + 1; i <= right; i++) {
            int key = arr[i];
            int j = i - 1;
            while (j >= left) {
                m.incrementComparisons();
                if (arr[j] > key) {
                    arr[j + 1] = arr[j];
                    m.incrementAllocations();
                    j--;
                } else break;
            }
            arr[j + 1] = key;
            m.incrementAllocations();
        }
    }

    public static void demo(int n) {
        int[] arr = new Random().ints(n, -1_000_000, 1_000_000).toArray();
        Metrics m = sort(arr);
        System.out.println("Sorted correctly? " + isSorted(arr));
        System.out.println(m.toString());
    }

    private static boolean isSorted(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            if (arr[i - 1] > arr[i]) return false;
        }
        return true;
    }
}
