package com.cluttered.cryptocurrency;

public class LessThanMinimum extends RuntimeException {

    public LessThanMinimum(final Action action) {
        super("Less than minimum required to perform " + action);
    }
}