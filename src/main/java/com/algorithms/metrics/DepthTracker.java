package com.algorithms.metrics;

/**
 * RAII-style depth tracker for automatic recursion depth management.
 * Use with try-with-resources to ensure proper cleanup.
 */
public class DepthTracker implements AutoCloseable {
    private final Metrics metrics;

    public DepthTracker(Metrics metrics) {
        this.metrics = metrics;
        metrics.enterRecursion();
    }

    @Override
    public void close() {
        metrics.exitRecursion();
    }

    public static DepthTracker enter(Metrics metrics) {
        return new DepthTracker(metrics);
    }
}