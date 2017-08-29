package com.cluttered.cryptocurrency;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.cluttered.cryptocurrency.Action.*;
import static org.assertj.core.api.Assertions.assertThat;

public class ActionTest {

    @Test
    public void testDetermineAction_BUY() {
        final List<Double> outputs = Arrays.asList(1.0, 0.0, 0.5);
        final Action result = Action.determine(outputs);

        assertThat(result).isEqualTo(BUY);
    }

    @Test
    public void testDetermineAction_HOLD() {
        final List<Double> outputs = Arrays.asList(0.5, 1.0, 0.0);
        final Action result = Action.determine(outputs);

        assertThat(result).isEqualTo(HOLD);
    }

    @Test
    public void testDetermineAction_SELL() {
        final List<Double> outputs = Arrays.asList(0.0, 0.5, 1.0);
        final Action result = Action.determine(outputs);

        assertThat(result).isEqualTo(SELL);
    }
}