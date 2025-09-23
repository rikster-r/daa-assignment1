package com.algorithms;
import com.algorithms.metrics.CSVWriter;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
public class DeterministicSelectTest {
    @Test
    void testRandomTrials() throws IOException {
        Random rand = new Random(42);
        CSVWriter csvWriter = new CSVWriter("results/tests");

        String filename = CSVWriter.simpleFilename("deterministic_select_tests");
        String header = "trial,n,k,expected,actual,arr\n";

        java.nio.file.Path filePath = java.nio.file.Paths.get("results/tests", filename);
        java.nio.file.Files.write(filePath, header.getBytes());

        for (int trial = 0; trial < 100; trial++) {
            int n = rand.nextInt(200) + 1;
            int[] arr = new int[n];
            for (int i = 0; i < n; i++) {
                arr[i] = rand.nextInt(1000);
            }

            int[] sorted = arr.clone();
            Arrays.sort(sorted);

            int k = rand.nextInt(n) + 1;
            int expected = sorted[k - 1];
            int actual = DeterministicSelect.select(arr.clone(), k);

            assertEquals(expected, actual,
                "Ошибка в trial=" + trial + " при k=" + k);

            String row = String.format("%d,%d,%d,%d,%d,\"%s\"\n",
                    trial, n, k, expected, actual, Arrays.toString(arr));
            java.nio.file.Files.write(filePath, row.getBytes(), java.nio.file.StandardOpenOption.APPEND);
        }
    }
}
