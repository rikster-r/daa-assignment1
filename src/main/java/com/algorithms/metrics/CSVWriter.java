package com.algorithms.metrics;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * CSV writer for metrics data export and analysis.
 */
public class CSVWriter {
    private static final String DEFAULT_OUTPUT_DIR = "results";
    private static final String CSV_HEADER = "algorithm,input_size,time_nanos,time_millis,max_depth,comparisons,allocations\n";

    private final String outputDir;

    public CSVWriter() {
        this(DEFAULT_OUTPUT_DIR);
    }

    public CSVWriter(String outputDir) {
        this.outputDir = outputDir;
        createOutputDirectory();
    }

    private void createOutputDirectory() {
        try {
            Path dir = Paths.get(outputDir);
            if (!Files.exists(dir)) {
                Files.createDirectories(dir);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to create output directory: " + outputDir, e);
        }
    }

    /**
     * Write a single metrics entry to CSV file
     */
    public void writeMetrics(Metrics metrics, String filename) throws IOException {
        Path filePath = Paths.get(outputDir, filename);
        boolean writeHeader = !Files.exists(filePath);

        try (FileWriter writer = new FileWriter(filePath.toFile(), true)) {
            if (writeHeader) {
                writer.write(CSV_HEADER);
            }
            writer.write(formatMetricsAsCSV(metrics));
        }
    }

    /**
     * Write multiple metrics entries to CSV file
     */
    public void writeMetricsList(List<Metrics> metricsList, String filename) throws IOException {
        Path filePath = Paths.get(outputDir, filename);

        try (FileWriter writer = new FileWriter(filePath.toFile(), false)) {
            writer.write(CSV_HEADER);
            for (Metrics metrics : metricsList) {
                writer.write(formatMetricsAsCSV(metrics));
            }
        }
    }

    private String formatMetricsAsCSV(Metrics metrics) {
        return String.format("%s,%d,%d,%.6f,%d,%d,%d\n",
                metrics.getAlgorithmName(),
                metrics.getInputSize(),
                metrics.getElapsedNanos(),
                metrics.getElapsedMillis(),
                metrics.getMaxDepth(),
                metrics.getComparisons(),
                metrics.getAllocations()
        );
    }

    /**
     * Create a timestamp-based filename for results
     */
    public static String timestampedFilename(String algorithm) {
        return String.format("%s_%d.csv", algorithm, System.currentTimeMillis());
    }

    /**
     * Create a simple filename for results
     */
    public static String simpleFilename(String algorithm) {
        return algorithm + "_results.csv";
    }
}