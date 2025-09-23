package com.algorithms;

import com.algorithms.metrics.Metrics;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class QuickSortTest {

    @Test
    void testQuickSortMultipleRunsForGraph() throws IOException {
        int runs = 10;      // нужно 10 прогонов
        int n = 10_000;     // размер массива
        Random rng = new Random();

        String filename = "quicksort_runs.csv";
        Path csvPath = Path.of("results", filename);

        // Создаём папку results
        Files.createDirectories(csvPath.getParent());

        // Записываем заголовок в CSV (перезаписывает файл при каждом запуске)
        try (FileWriter writer = new FileWriter(csvPath.toFile(), false)) {
            writer.write("run,n,comparisons,allocations,maxDepth,timeMillis\n");
        }

        // Десять прогонов
        for (int run = 1; run <= runs; run++) {
            int[] arr = rng.ints(n, -1_000_000, 1_000_000).toArray();
            int[] copy = arr.clone();

            Metrics m = QuickSort.sort(arr);

            // Проверяем, что массив отсортирован
            java.util.Arrays.sort(copy);
            assertArrayEquals(copy, arr, "Array must be fully sorted");

            // Записываем строку в CSV
            try (FileWriter writer = new FileWriter(csvPath.toFile(), true)) {
                writer.write(String.format(
                        "%d,%d,%d,%d,%d,%.3f\n",
                        run,
                        n,
                        m.getComparisons(),
                        m.getAllocations(),
                        m.getMaxDepth(),
                        m.getElapsedMillis() // double -> %.3f (3 знака после запятой)
                ));

            }
        }

        // Проверяем, что CSV создан
        assertTrue(Files.exists(csvPath), "CSV file must exist after writing");
        assertTrue(Files.size(csvPath) > 0, "CSV file must not be empty");
    }
}