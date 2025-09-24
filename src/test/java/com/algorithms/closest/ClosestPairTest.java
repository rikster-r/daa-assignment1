package com.algorithms.closest;

import com.algorithms.metrics.Metrics;
import com.algorithms.metrics.CSVWriter;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClosestPairTest {

    @Test
    public void testSmallValidation() throws IOException {
        ClosestPair.Point[] points = {
                new ClosestPair.Point(0, 0),
                new ClosestPair.Point(3, 4),
                new ClosestPair.Point(7, 7),
                new ClosestPair.Point(1, 1)
        };

        Metrics metrics = new Metrics("ClosestPair", points.length);
        double result = ClosestPair.validateClosestPair(points, metrics);

        assertEquals(Math.sqrt(2), result, 1e-6);

        // Write metrics to CSV
        CSVWriter writer = new CSVWriter();
        writer.writeMetrics(metrics, CSVWriter.simpleFilename("closestpair"));
    }

    @Test
    public void testLargeOnlyFast() throws IOException {
        int n = 5000;
        Random rand = new Random(42);
        ClosestPair.Point[] points = new ClosestPair.Point[n];
        for (int i = 0; i < n; i++) {
            points[i] = new ClosestPair.Point(rand.nextDouble() * 1000, rand.nextDouble() * 1000);
        }

        Metrics metrics = new Metrics("ClosestPair", n);
        double result = ClosestPair.validateClosestPair(points, metrics);

        System.out.println("Fast result (n=5000): " + result);

        // Write metrics to CSV
        CSVWriter writer = new CSVWriter();
        writer.writeMetrics(metrics, CSVWriter.simpleFilename("closestpair"));
    }

    @Test
    public void testVariousSizes() throws IOException {
        int[] sizes = {2, 4, 8, 10, 50, 100, 200, 500, 1000, 2000, 3000, 4000, 5000};
        Random rand = new Random(42);
        CSVWriter writer = new CSVWriter();

        for (int n : sizes) {
            ClosestPair.Point[] points = new ClosestPair.Point[n];
            for (int i = 0; i < n; i++) {
                points[i] = new ClosestPair.Point(rand.nextDouble() * 1000, rand.nextDouble() * 1000);
            }

            Metrics metrics = new Metrics("ClosestPair", n);
            double result = ClosestPair.validateClosestPair(points, metrics);

            System.out.println("n=" + n + " → closest=" + result);

            // Append metrics to CSV
            writer.writeMetrics(metrics, CSVWriter.simpleFilename("closestpair"));
        }
    }
}
