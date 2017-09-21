package com.cluttered.cryptocurrency;

import java.util.List;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class MinMax {

    private double min;
    private double max;

    private MinMax(final double min, final double max) {
        this.min = min;
        this.max = max;
    }

    public static MinMax process(final List<Long> list) {
        double min = Double.POSITIVE_INFINITY;
        double max = Double.NEGATIVE_INFINITY;
        for(final Long value : list) {
            min = min(min, value);
            max = max(max, value);
        }
        return new MinMax(min, max);
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }
}
