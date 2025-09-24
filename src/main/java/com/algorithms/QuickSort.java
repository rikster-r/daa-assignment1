package com.algorithms;

import com.algorithms.metrics.Metrics;

import java.util.Random;

public class QuickSort {

    private static final int INSERTION_SORT_THRESHOLD = 16;

    /** Сортировка массива с метриками */
    public static Metrics sort(int[] arr) {
        Metrics m = new Metrics("QuickSort", arr.length);
        if (arr == null || arr.length <= 1) return m;

        m.startTiming();
        quickSort(arr, 0, arr.length - 1, 1, m);
        m.endTiming();

        return m;
    }

    private static void quickSort(int[] arr, int left, int right, int depth, Metrics m) {
        m.enterRecursion();

        int size = right - left + 1;
        if (size <= INSERTION_SORT_THRESHOLD) {
            insertionSort(arr, left, right, m);
            m.exitRecursion();
            return;
        }

        int pivotIndex = partition(arr, left, right, m);
        quickSort(arr, left, pivotIndex - 1, depth + 1, m);
        quickSort(arr, pivotIndex + 1, right, depth + 1, m);

        m.exitRecursion();
    }

    private static int partition(int[] arr, int left, int right, Metrics m) {
        int pivot = arr[right]; // последний элемент как pivot
        int i = left - 1;

        for (int j = left; j < right; j++) {
            m.incrementComparisons();
            if (arr[j] <= pivot) {
                i++;
                swap(arr, i, j, m);
            }
        }
        swap(arr, i + 1, right, m);
        return i + 1;
    }

    private static void swap(int[] arr, int i, int j, Metrics m) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
        m.incrementAllocations(3); // 3 операции присваивания
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