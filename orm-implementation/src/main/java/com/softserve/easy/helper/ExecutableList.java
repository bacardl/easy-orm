package com.softserve.easy.helper;


import com.softserve.easy.action.EntityInsertAction;

import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

public class ExecutableList<T extends Executable> implements Iterable<T>{

    private final List<T> executables;
    private final Sorter<T> sorter;

    public ExecutableList(List<T> executables) {
        this(executables, null);
    }

    public ExecutableList(List<T> executables, Sorter<T> sorter) {
        this.executables = executables;
        this.sorter = sorter;
    }

    @Override
    public Iterator<T> iterator() {
        return null;
    }

    @Override
    public void forEach(Consumer<? super T> action) {

    }

    @Override
    public Spliterator<T> spliterator() {
        return null;
    }

    public static interface Sorter<T extends Executable> {
        void sort(List<T> list);
    }

    private static class InsertActionSorter<T> implements ExecutableList.Sorter<EntityInsertAction> {
        @Override
        public void sort(List<EntityInsertAction> list) {

        }
    }
}
