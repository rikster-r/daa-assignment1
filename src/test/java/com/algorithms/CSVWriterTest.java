package com.algorithms;

import com.algorithms.metrics.CSVWriter;
import com.algorithms.metrics.Metrics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CSVWriterTest {
    @TempDir
    Path tempDir;

    private CSVWriter csvWriter;
    private Metrics metrics;

    @BeforeEach
    void setUp() {
        csvWriter = new CSVWriter(tempDir.toString());
        metrics = new Metrics("TestAlgo", 100);

        // Set up some test data
        metrics.startTiming();
        metrics.incrementComparisons(50);
        metrics.incrementAllocations(10);
        metrics.enterRecursion();
        metrics.enterRecursion();
        metrics.exitRecursion();
        metrics.exitRecursion();
        metrics.endTiming();
    }

    @Test
    void testWriteSingleMetrics() throws IOException {
        String filename = "test_single.csv";
        csvWriter.writeMetrics(metrics, filename);

        Path resultFile = tempDir.resolve(filename);
        assertTrue(Files.exists(resultFile));

        List<String> lines = Files.readAllLines(resultFile);
        assertEquals(2, lines.size()); // Header + data

        String header = lines.get(0);
        assertTrue(header.contains("algorithm"));
        assertTrue(header.contains("input_size"));
        assertTrue(header.contains("time_nanos"));

        String data = lines.get(1);
        assertTrue(data.startsWith("TestAlgo,100"));
        assertTrue(data.contains(",50,10")); // comparisons,allocations
    }

    @Test
    void testWriteMetricsList() throws IOException {
        Metrics metrics2 = new Metrics("TestAlgo2", 200);
        metrics2.startTiming();
        metrics2.incrementComparisons(25);
        metrics2.endTiming();

        List<Metrics> metricsList = Arrays.asList(metrics, metrics2);
        String filename = "test_list.csv";

        csvWriter.writeMetricsList(metricsList, filename);

        Path resultFile = tempDir.resolve(filename);
        List<String> lines = Files.readAllLines(resultFile);
        assertEquals(3, lines.size()); // Header + 2 data lines
    }

    @Test
    void testFilenameGenerators() {
        String timestamped = CSVWriter.timestampedFilename("mergesort");
        assertTrue(timestamped.startsWith("mergesort_"));
        assertTrue(timestamped.endsWith(".csv"));

        String simple = CSVWriter.simpleFilename("quicksort");
        assertEquals("quicksort_results.csv", simple);
    }
}
