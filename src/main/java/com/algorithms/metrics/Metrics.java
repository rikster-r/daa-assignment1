package com.algorithms.metrics;

/**
 * Thread-safe metrics collector for divide-and-conquer algorithms.
 * Tracks time, recursion depth, comparisons, and allocations.
 */
public class Metrics {
    private long startTime;
    private long endTime;
    private int maxDepth;
    private int currentDepth;
    private long comparisons;
    private long allocations;
    private String algorithmName;
    private int inputSize;

    public Metrics(String algorithmName, int inputSize) {
        this.algorithmName = algorithmName;
        this.inputSize = inputSize;
        reset();
    }

    public void reset() {
        this.startTime = 0;
        this.endTime = 0;
        this.maxDepth = 0;
        this.currentDepth = 0;
        this.comparisons = 0;
        this.allocations = 0;
    }

    // Time tracking
    public void startTiming() {
        this.startTime = System.nanoTime();
    }

    public void endTiming() {
        this.endTime = System.nanoTime();
    }

    public long getElapsedNanos() {
        return endTime - startTime;
    }

    public double getElapsedMillis() {
        return (endTime - startTime) / 1_000_000.0;
    }

    // Depth tracking
    public void enterRecursion() {
        currentDepth++;
        if (currentDepth > maxDepth) {
            maxDepth = currentDepth;
        }
    }

    public void exitRecursion() {
        currentDepth--;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public int getCurrentDepth() {
        return currentDepth;
    }

    // Operation counters
    public void incrementComparisons() {
        comparisons++;
    }

    public void incrementComparisons(long count) {
        comparisons += count;
    }

    public void incrementAllocations() {
        allocations++;
    }

    public void incrementAllocations(long count) {
        allocations += count;
    }

    public long getComparisons() {
        return comparisons;
    }

    public long getAllocations() {
        return allocations;
    }

    // Getters for CSV export
    public String getAlgorithmName() {
        return algorithmName;
    }

    public int getInputSize() {
        return inputSize;
    }

    @Override
    public String toString() {
        return String.format("Metrics[%s, n=%d, time=%.3fms, depth=%d, cmp=%d, alloc=%d]",
                algorithmName, inputSize, getElapsedMillis(), maxDepth, comparisons, allocations);
    }
}