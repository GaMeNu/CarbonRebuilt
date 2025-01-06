package me.gamenu.carbondf.values;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class represents a type that consists of multiple possible types.
 */
public class TypeSet implements Set<DFItem.Type> {
    Set<DFItem.Type> types;

    public TypeSet() {
        types = new HashSet<>();
    }

    /**
     * Create a new TypeSet consisting of the given Types
     *
     * @param types types to add to the TypeSet
     */
    public TypeSet(DFItem.Type... types) {
        this.types = new HashSet<>(Arrays.stream(types).toList());
    }

    public TypeSet(TypeSet other) {
        this.types = new HashSet<>(other.types);
    }

    @Override
    public int size() {
        return types.size();
    }

    @Override
    public boolean isEmpty() {
        return types.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return types.contains(o);
    }

    @Override
    public Iterator<DFItem.Type> iterator() {
        return types.iterator();
    }

    @Override
    public Object[] toArray() {
        return types.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return types.toArray(a);
    }

    @Override
    public boolean add(DFItem.Type type) {
        return types.add(type);
    }

    @Override
    public boolean remove(Object o) {
        return types.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return types.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends DFItem.Type> c) {
        return types.addAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return types.retainAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return types.removeAll(c);
    }

    @Override
    public void clear() {
        types.clear();
    }

    public boolean canAcceptType(DFItem.Type type) {
        if (type == null && contains(DFItem.Type.NONE)) return true;
        if (type == DFItem.Type.ANY) return true;
        return contains(DFItem.Type.ANY) || contains(type);
    }

    public boolean canAcceptType(TypeSet other) {
        if (contains(DFItem.Type.ANY) || other.contains(DFItem.Type.ANY)) return true;

        // Create common set.
        TypeSet common = new TypeSet(this);
        common.retainAll(other);

        // If the size is 0, it means there are no shared elements and the type is invalid
        return common.size() > 0;
    }

    @Override
    public String toString() {
        return "[" + types.stream().map(value -> {
            if (value == null) return "NULL";
            else return value.toString();
        }).collect(Collectors.joining(", ")) + "]";
    }

    @Override
    public boolean equals(Object obj) {
        return types.equals(obj) && obj instanceof TypeSet;
    }
}
