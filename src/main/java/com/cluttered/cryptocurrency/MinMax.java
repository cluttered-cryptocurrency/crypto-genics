package com.cluttered.cryptocurrency;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class MinMax {

    private double min;
    private double max;

    public MinMax() {
        // Morphia Constructor
    }

    private MinMax(final double min, final double max) {
        this.min = min;
        this.max = max;
    }

    public static MinMax process(final List<MarketSummary> list) {
        final DoubleSummaryStatistics stats = list.parallelStream()
                .map(MarketSummary::getLast)
                .map(Double::valueOf)
                .collect(Collectors.summarizingDouble(Double::doubleValue));
        return new MinMax(stats.getMin(), stats.getMax());
    }

    public Double normalize(final Long input) {
        return (input - min) / (max - min);
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }
}
