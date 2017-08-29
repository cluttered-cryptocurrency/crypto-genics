package com.cluttered.cryptocurrency;

import java.util.List;

public enum Action {
    BUY,
    HOLD,
    SELL;

    private static final Action[] values = values();

    public static Action determine(final List<Double> outputs) {
        int index = 0;
        double max = Double.MIN_VALUE;
        for (int i = 0; i < outputs.size(); i++) {
            if (outputs.get(i) > max) {
                max = outputs.get(i);
                index = i;
            }
        }
        return values[index];
    }
}