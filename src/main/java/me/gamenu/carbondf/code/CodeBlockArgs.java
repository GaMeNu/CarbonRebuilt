package me.gamenu.carbondf.code;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class CodeBlockArgs<DFItem> implements List<DFItem> {
    public final int MAX_ARGS_SIZE = 27;

    List<DFItem> args;

    @Override
    public int size() {
        return args.size();
    }

    @Override
    public boolean isEmpty() {
        return args.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return args.contains(o);
    }

    @Override
    public Iterator<DFItem> iterator() {
        return args.iterator();
    }

    @Override
    public Object[] toArray() {
        return args.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return args.toArray(a);
    }

    @Override
    public boolean add(DFItem item) {
        return args.add(item);
    }

    @Override
    public boolean remove(Object o) {
        return args.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return args.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends DFItem> c) {
        return args.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends DFItem> c) {
        return args.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return args.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return args.retainAll(c);
    }

    @Override
    public void clear() {
        args.clear();
    }

    @Override
    public DFItem get(int index) {
        return args.get(index);
    }

    @Override
    public DFItem set(int index, DFItem element) {
        return args.set(index, element);
    }

    @Override
    public void add(int index, DFItem element) {
        args.add(index, element);
    }

    @Override
    public DFItem remove(int index) {
        return args.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return args.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return args.lastIndexOf(o);
    }

    @Override
    public ListIterator<DFItem> listIterator() {
        return args.listIterator();
    }

    @Override
    public ListIterator<DFItem> listIterator(int index) {
        return args.listIterator(index);
    }

    @Override
    public List<DFItem> subList(int fromIndex, int toIndex) {
        return args.subList(fromIndex, toIndex);
    }
}