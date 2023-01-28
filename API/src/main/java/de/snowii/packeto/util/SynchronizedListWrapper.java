package de.snowii.packeto.util;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public final class SynchronizedListWrapper<E> implements List<E> {
    private final List<E> synList;
    private final Consumer<E> addHandler;

    public SynchronizedListWrapper(final List<E> inputList, final Consumer<E> addHandler) {
        this.synList = Collections.synchronizedList(inputList);
        this.addHandler = addHandler;
    }

    public List<E> originalList() {
        return synList;
    }

    private void handleAdd(E o) {
        addHandler.accept(o);
    }

    @Override
    public int size() {
        return this.synList.size();
    }

    @Override
    public boolean isEmpty() {
        return this.synList.isEmpty();
    }

    @Override
    public boolean contains(final Object o) {
        return this.synList.contains(o);
    }

    @Override
    public @NotNull Iterator<E> iterator() {
        // Has to be manually synched
        return listIterator();
    }

    @Override
    public Object @NotNull [] toArray() {
        return this.synList.toArray();
    }

    @Override
    public boolean add(final E o) {
        handleAdd(o);
        return this.synList.add(o);
    }

    @Override
    public boolean remove(final Object o) {
        return this.synList.remove(o);
    }

    @Override
    public boolean addAll(final Collection<? extends E> c) {
        for (final E o : c) {
            handleAdd(o);
        }
        return this.synList.addAll(c);
    }

    @Override
    public boolean addAll(final int index, final Collection<? extends E> c) {
        for (final E o : c) {
            handleAdd(o);
        }
        return this.synList.addAll(index, c);
    }

    @Override
    public void clear() {
        this.synList.clear();
    }

    @Override
    public E get(final int index) {
        return this.synList.get(index);
    }

    @Override
    public E set(final int index, final E element) {
        return this.synList.set(index, element);
    }

    @Override
    public void add(final int index, final E element) {
        this.synList.add(index, element);
    }

    @Override
    public E remove(final int index) {
        return this.synList.remove(index);
    }

    @Override
    public int indexOf(final Object o) {
        return this.synList.indexOf(o);
    }

    @Override
    public int lastIndexOf(final Object o) {
        return this.synList.lastIndexOf(o);
    }

    @Override
    public @NotNull ListIterator<E> listIterator() {
        // Has to be manually synched
        return this.synList.listIterator();
    }

    @Override
    public @NotNull ListIterator<E> listIterator(final int index) {
        // Has to be manually synched
        return this.synList.listIterator(index);
    }

    @Override
    public @NotNull List<E> subList(final int fromIndex, final int toIndex) {
        return this.synList.subList(fromIndex, toIndex);
    }

    @Override
    public boolean retainAll(final @NotNull Collection<?> c) {
        return this.synList.retainAll(c);
    }

    @Override
    public boolean removeAll(final @NotNull Collection<?> c) {
        return this.synList.removeAll(c);
    }

    @Override
    public boolean containsAll(final @NotNull Collection<?> c) {
        return new HashSet<>(this.synList).containsAll(c);
    }

    @Override
    public <T> T @NotNull [] toArray(final T @NotNull [] a) {
        return this.synList.toArray(a);
    }

    @Override
    public void sort(final Comparator<? super E> c) {
        synList.sort(c);
    }

    @Override
    public void forEach(Consumer<? super E> consumer) {
        synList.forEach(consumer);
    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        return synList.removeIf(filter);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return synList.equals(obj);
    }

    @Override
    public int hashCode() {
        return synList.hashCode();
    }

    @Override
    public String toString() {
        return synList.toString();
    }
}