package com.algorithms;

import com.algorithms.metrics.CSVWriter;
import com.algorithms.metrics.Metrics;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class MergeSortTest {

    @Test
    void testMergeSortCorrectnessAndMetrics() throws IOException {
        int n = 10_000;
        int[] arr = new Random().ints(n, -1_000_000, 1_000_000).toArray();
        int[] copy = arr.clone();

        // Сортировка и сбор метрик
        Metrics m = MergeSort.sort(arr);

        // 1) Проверка корректности сортировки
        java.util.Arrays.sort(copy);
        assertArrayEquals(copy, arr, "Array must be fully sorted");

        // 2) Проверка метрик
        assertTrue(m.getMaxDepth() > 0, "Recursion depth should be > 0");
        assertTrue(m.getAllocations() >= 1, "There should be at least 1 allocation");
        assertTrue(m.getElapsedMillis() > 0, "Elapsed time should be > 0");
        assertTrue(m.getComparisons() > 0, "There should be some comparisons");

        // 3) Проверка записи CSV
        CSVWriter writer = new CSVWriter();
        String filename = CSVWriter.simpleFilename("mergesort");
        writer.writeMetrics(m, filename); // используем твой Metrics напрямую

        Path csvPath = Path.of("results", filename);
        assertTrue(Files.exists(csvPath), "CSV file must exist after writing");
        assertTrue(Files.size(csvPath) > 0, "CSV file must not be empty");
    }
}
