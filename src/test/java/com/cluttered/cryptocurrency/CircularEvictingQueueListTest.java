package com.cluttered.cryptocurrency;

import mockit.Injectable;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.stream.Collectors;

import static mockit.Deencapsulation.getField;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JMockit.class)
public class CircularEvictingQueueListTest {

    @Tested
    @SuppressWarnings("unused")
    private CircularEvictingQueueList<Integer> queueList;

    @Injectable
    @SuppressWarnings("unused")
    private final int capacity = 3;

    @Test
    public void testConstructor() {
        final int headIndex = getField(queueList, "headIndex");
        final int nextIndex = getField(queueList, "nextIndex");

        assertThat(headIndex).isEqualTo(0);
        assertThat(nextIndex).isEqualTo(0);

        assertThat(queueList.size()).isEqualTo(0);
        assertThat(queueList.isEmpty()).isTrue();
        assertThat(queueList.isFull()).isFalse();
    }

    @Test
    public void testAddFirst() {
        queueList.add(1);

        final int headIndex = getField(queueList, "headIndex");
        final int nextIndex = getField(queueList, "nextIndex");

        assertThat(headIndex).isEqualTo(0);
        assertThat(nextIndex).isEqualTo(1);

        assertThat(queueList.size()).isEqualTo(1);
        assertThat(queueList.isEmpty()).isFalse();
        assertThat(queueList.isFull()).isFalse();
    }

    @Test
    public void testFill() {
        queueList.add(1);
        queueList.add(2);
        queueList.add(3);

        final int headIndex = getField(queueList, "headIndex");
        final int nextIndex = getField(queueList, "nextIndex");

        assertThat(headIndex).isEqualTo(0);
        assertThat(nextIndex).isEqualTo(0);

        assertThat(queueList.size()).isEqualTo(3);
        assertThat(queueList.isEmpty()).isFalse();
        assertThat(queueList.isFull()).isTrue();
    }

    @Test
    public void testEvict() {
        queueList.add(1);
        queueList.add(2);
        queueList.add(3);
        queueList.add(4);

        final int headIndex = getField(queueList, "headIndex");
        final int nextIndex = getField(queueList, "nextIndex");

        assertThat(headIndex).isEqualTo(1);
        assertThat(nextIndex).isEqualTo(1);

        assertThat(queueList.size()).isEqualTo(3);
        assertThat(queueList.isEmpty()).isFalse();
        assertThat(queueList.isFull()).isTrue();
    }

    @Test
    public void testReplaceAllOriginal() {
        queueList.add(1);
        queueList.add(2);
        queueList.add(3);
        queueList.add(4);
        queueList.add(5);
        queueList.add(6);

        final int headIndex = getField(queueList, "headIndex");
        final int nextIndex = getField(queueList, "nextIndex");

        assertThat(headIndex).isEqualTo(0);
        assertThat(nextIndex).isEqualTo(0);

        assertThat(queueList.size()).isEqualTo(3);
        assertThat(queueList.isEmpty()).isFalse();
        assertThat(queueList.isFull()).isTrue();
    }

    @Test
    public void testStream() {
        queueList.add(1);
        queueList.add(2);
        queueList.add(3);
        queueList.add(4);
        queueList.add(5);

        final List<Integer> result = queueList.stream()
                .collect(Collectors.toList());

        assertThat(result).containsExactly(3, 4, 5);
    }
}