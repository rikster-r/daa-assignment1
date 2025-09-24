package com.algorithms;

import java.util.*;

public class DeterministicSelect {

    public static int select(int[] arr, int k) {
        return select(arr, 0, arr.length - 1, k);
    }

    private static int select(int[] arr, int left, int right, int k) {
        if (left == right) return arr[left];

        int pivotValue = medianOfMedians(arr, left, right);
        int pivotIndex = partition(arr, left, right, pivotValue);

        int rank = pivotIndex - left + 1;

        if (k == rank) {
            return arr[pivotIndex];
        } else if (k < rank) {
            return select(arr, left, pivotIndex - 1, k);
        } else {
            return select(arr, pivotIndex + 1, right, k - rank);
        }
    }

    private static int medianOfMedians(int[] arr, int left, int right) {
        int n = right - left + 1;

        if (n <= 5) {
            Arrays.sort(arr, left, right + 1);
            return arr[left + n / 2];
        }

        int numGroups = (n + 4) / 5;
        int[] medians = new int[numGroups];

        for (int i = 0; i < numGroups; i++) {
            int groupLeft = left + i * 5;
            int groupRight = Math.min(groupLeft + 4, right);

            Arrays.sort(arr, groupLeft, groupRight + 1);
            medians[i] = arr[groupLeft + (groupRight - groupLeft) / 2];
        }

        return select(medians, 0, numGroups - 1, (numGroups + 1) / 2);
    }

    private static int partition(int[] arr, int left, int right, int pivot) {
        int pivotIndex = -1;
        for (int i = left; i <= right; i++) {
            if (arr[i] == pivot) {
                pivotIndex = i;
                break;
            }
        }

        if (pivotIndex == -1) {
            pivotIndex = left;
        }

        swap(arr, pivotIndex, right);

        int storeIndex = left;
        for (int i = left; i < right; i++) {
            if (arr[i] <= pivot) {
                swap(arr, i, storeIndex);
                storeIndex++;
            }
        }

        swap(arr, storeIndex, right);

        return storeIndex;
    }

    private static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }
}