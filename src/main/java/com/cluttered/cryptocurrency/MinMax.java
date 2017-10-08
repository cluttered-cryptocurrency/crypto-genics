package com.cluttered.cryptocurrency;

import java.util.Collection;

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

    public static MinMax process(final Collection<MarketTick> inputTicks) {
        Double min = Double.POSITIVE_INFINITY;
        Double max = Double.NEGATIVE_INFINITY;

        MarketTick previous = null;
        for(final MarketTick tick : inputTicks) {
            if(previous != null) {
                final Double percentDelta = tick.getLast() / ((double) previous.getLast());
                final Double logarithm = Math.log(percentDelta);
                min = Math.min(min, logarithm);
                max = Math.max(max, logarithm);
            }
            previous = tick;
        }

        return new MinMax(min, max);
    }

    public Double normalize(final double input) {
        return (input - min) / (max - min);
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }
}