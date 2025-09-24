package com.algorithms;

import com.algorithms.metrics.DepthTracker;
import com.algorithms.metrics.Metrics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MetricsTest {
    private Metrics metrics;

    @BeforeEach
    void setUp() {
        metrics = new Metrics("TestAlgorithm", 1000);
    }

    @Test
    void testInitialState() {
        assertEquals("TestAlgorithm", metrics.getAlgorithmName());
        assertEquals(1000, metrics.getInputSize());
        assertEquals(0, metrics.getMaxDepth());
        assertEquals(0, metrics.getCurrentDepth());
        assertEquals(0, metrics.getComparisons());
        assertEquals(0, metrics.getAllocations());
    }

    @Test
    void testDepthTracking() {
        assertEquals(0, metrics.getCurrentDepth());

        metrics.enterRecursion();
        assertEquals(1, metrics.getCurrentDepth());
        assertEquals(1, metrics.getMaxDepth());

        metrics.enterRecursion();
        assertEquals(2, metrics.getCurrentDepth());
        assertEquals(2, metrics.getMaxDepth());

        metrics.exitRecursion();
        assertEquals(1, metrics.getCurrentDepth());
        assertEquals(2, metrics.getMaxDepth()); // Max should remain

        metrics.exitRecursion();
        assertEquals(0, metrics.getCurrentDepth());
        assertEquals(2, metrics.getMaxDepth());
    }

    @Test
    void testDepthTrackerRAII() {
        assertEquals(0, metrics.getCurrentDepth());

        try (DepthTracker tracker = DepthTracker.enter(metrics)) {
            assertEquals(1, metrics.getCurrentDepth());

            try (DepthTracker tracker2 = DepthTracker.enter(metrics)) {
                assertEquals(2, metrics.getCurrentDepth());
            }
            assertEquals(1, metrics.getCurrentDepth());
        }
        assertEquals(0, metrics.getCurrentDepth());
        assertEquals(2, metrics.getMaxDepth());
    }

    @Test
    void testCounters() {
        metrics.incrementComparisons();
        assertEquals(1, metrics.getComparisons());

        metrics.incrementComparisons(10);
        assertEquals(11, metrics.getComparisons());

        metrics.incrementAllocations();
        assertEquals(1, metrics.getAllocations());

        metrics.incrementAllocations(5);
        assertEquals(6, metrics.getAllocations());
    }

    @Test
    void testTiming() throws InterruptedException {
        metrics.startTiming();
        Thread.sleep(10); // Small delay for measurable time
        metrics.endTiming();

        assertTrue(metrics.getElapsedNanos() > 0);
        assertTrue(metrics.getElapsedMillis() > 0);
    }

    @Test
    void testReset() {
        metrics.incrementComparisons(5);
        metrics.incrementAllocations(3);
        metrics.enterRecursion();

        metrics.reset();

        assertEquals(0, metrics.getMaxDepth());
        assertEquals(0, metrics.getCurrentDepth());
        assertEquals(0, metrics.getComparisons());
        assertEquals(0, metrics.getAllocations());
    }
}
