package com.cluttered.cryptocurrency;

import java.util.*;

import static java.lang.Math.min;

public class CircularEvictingQueueList<E> implements Queue<E>, List<E> {

    private final int capacity;
    private final List<E> delegateList;

    private int size;
    private int headIndex;
    private int nextIndex;

    public CircularEvictingQueueList(final int capacity) {
        this.capacity = capacity;
        delegateList = new ArrayList<>(capacity);
        headIndex = nextIndex = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    public boolean isFull() {
        return size == capacity;
    }

    @Override
    public boolean add(final E e) {
        try {
            delegateList.set(nextIndex, e);
        } catch (final Exception ex) {
            delegateList.add(e);
        }
        if (!isEmpty() && nextIndex == headIndex)
            headIndex = (headIndex + 1) % capacity;
        nextIndex = (nextIndex + 1) % capacity;
        size = min(capacity, size + 1);
        return true;
    }

    @Override
    public void clear() {
        headIndex = nextIndex = size = 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new CircularEvictingQueueListIterator();
    }

    @Override
    public boolean contains(final Object object) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(final Object object) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(final Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(final Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(final Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(final Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public E get(final int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public E set(final int index, final E element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(final int index, final E element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public E remove(final int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOf(final Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int lastIndexOf(final Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<E> listIterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<E> listIterator(final int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<E> subList(final int fromIndex, final int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean offer(final E e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public E remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public E poll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public E element() {
        throw new UnsupportedOperationException();
    }

    @Override
    public E peek() {
        throw new UnsupportedOperationException();
    }

    private class CircularEvictingQueueListIterator implements Iterator<E> {

        private boolean first;
        private int cursor;

        private CircularEvictingQueueListIterator() {
            first = true;
            cursor = headIndex;
        }

        @Override
        public boolean hasNext() {
            return (first && !isEmpty()) || cursor != nextIndex;
        }

        @Override
        public E next() {
            final E element = delegateList.get(cursor);
            cursor = (cursor + 1) % capacity;
            first = false;
            return element;
        }
    }
}