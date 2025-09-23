package com.algorithms;

import com.algorithms.metrics.CSVWriter;
import com.algorithms.metrics.Metrics;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class MergeSortTest {

    @Test
    void testMergeSortAllCasesInOneCSV() throws IOException {
        // 10-15 разных размеров массивов
        int[] sizes = {1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 10000, 12000, 14000, 16000};

        List<Metrics> allMetrics = new ArrayList<>();

        for (int n : sizes) {
            int[] arr = new Random().ints(n, -1_000_000, 1_000_000).toArray();
            int[] copy = arr.clone();

            Metrics m = MergeSort.sort(arr);

            // Проверка сортировки
            java.util.Arrays.sort(copy);
            assertArrayEquals(copy, arr, "Array must be fully sorted");

            // Проверка метрик
            assertTrue(m.getMaxDepth() > 0, "Recursion depth should be > 0");
            assertTrue(m.getAllocations() >= 1, "There should be at least 1 allocation");
            assertTrue(m.getElapsedMillis() > 0, "Elapsed time should be > 0");
            assertTrue(m.getComparisons() > 0, "There should be some comparisons");

            // Добавляем в общий список
            allMetrics.add(m);
        }

        // Сохраняем все метрики в один CSV
        CSVWriter writer = new CSVWriter();
        String filename = CSVWriter.simpleFilename("mergesort_all_tests");
        writer.writeMetricsList(allMetrics, filename);

        Path csvPath = Path.of("results", filename);
        assertTrue(Files.exists(csvPath), "CSV file must exist after writing");
        assertTrue(Files.size(csvPath) > 0, "CSV file must not be empty");

    }}