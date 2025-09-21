# Divide-and-Conquer Algorithms

Implementation of classic divide-and-conquer algorithms with performance analysis.

## Algorithms Implemented

1. **MergeSort** - O(n log n) stable sorting with optimizations
2. **QuickSort** - Randomized pivot with bounded recursion depth
3. **Deterministic Select** - Median-of-medians O(n) selection
4. **Closest Pair of Points** - O(n log n) 2D geometric algorithm

## Build & Run
```bash
# Compile
mvn compile

# Run tests
mvn test

# Run benchmarks
mvn exec:java -Dexec.args="benchmark"

# Generate performance report
mvn exec:java -Dexec.args="report output.csv"