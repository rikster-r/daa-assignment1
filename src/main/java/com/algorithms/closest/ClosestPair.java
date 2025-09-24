package com.algorithms.closest;

import com.algorithms.metrics.DepthTracker;
import com.algorithms.metrics.Metrics;

import java.util.Arrays;
import java.util.Comparator;

public class ClosestPair {

    public static class Point {
        public final double x, y;
        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    /**
     * Public entrypoint with validation:
     * - For n <= 2000, compare against O(n^2) brute force.
     * - For n > 2000, run only divide & conquer.
     */
    public static double validateClosestPair(Point[] points, Metrics metrics) {
        double result = closestPair(points, metrics);

        if (points.length <= 2000) {
            double brute = bruteForce(points, metrics);
            if (Math.abs(result - brute) > 1e-6) {
                throw new AssertionError(
                        "Validation failed: D&C=" + result + " vs Brute=" + brute);
            }
        }

        return result;
    }

    /**
     * Fast divide-and-conquer algorithm O(n log n)
     */
    public static double closestPair(Point[] points, Metrics metrics) {
        metrics.startTiming();

        Point[] pointsSortedByX = Arrays.copyOf(points, points.length);
        Arrays.sort(pointsSortedByX, Comparator.comparingDouble(p -> p.x));
        metrics.incrementAllocations(points.length);

        double result;
        try (DepthTracker ignored = DepthTracker.enter(metrics)) {
            result = closestRecursive(pointsSortedByX, metrics);
        }

        metrics.endTiming();
        return result;
    }

    private static double closestRecursive(Point[] pointsSortedByX, Metrics metrics) {
        int n = pointsSortedByX.length;

        if (n <= 3) {
            return bruteForce(pointsSortedByX, metrics);
        }

        int mid = n / 2;
        Point midPoint = pointsSortedByX[mid];

        try (DepthTracker ignored = DepthTracker.enter(metrics)) {
            double dl = closestRecursive(Arrays.copyOfRange(pointsSortedByX, 0, mid), metrics);
            double dr = closestRecursive(Arrays.copyOfRange(pointsSortedByX, mid, n), metrics);
            double d = Math.min(dl, dr);

            // Build strip
            Point[] strip = Arrays.stream(pointsSortedByX)
                    .filter(p -> Math.abs(p.x - midPoint.x) < d)
                    .toArray(Point[]::new);
            metrics.incrementAllocations(strip.length);

            return Math.min(d, stripClosest(strip, d, metrics));
        }
    }

    private static double bruteForce(Point[] points, Metrics metrics) {
        double min = Double.POSITIVE_INFINITY;
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                metrics.incrementComparisons();
                double dist = distance(points[i], points[j]);
                if (dist < min) {
                    min = dist;
                }
            }
        }
        return min;
    }

    private static double stripClosest(Point[] strip, double d, Metrics metrics) {
        Arrays.sort(strip, Comparator.comparingDouble(p -> p.y));
        metrics.incrementComparisons(strip.length);

        double min = d;
        for (int i = 0; i < strip.length; ++i) {
            for (int j = i + 1; j < strip.length && (strip[j].y - strip[i].y) < min; ++j) {
                metrics.incrementComparisons();
                double dist = distance(strip[i], strip[j]);
                if (dist < min) {
                    min = dist;
                }
            }
        }
        return min;
    }

    private static double distance(Point p1, Point p2) {
        double dx = p1.x - p2.x;
        double dy = p1.y - p2.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
}
