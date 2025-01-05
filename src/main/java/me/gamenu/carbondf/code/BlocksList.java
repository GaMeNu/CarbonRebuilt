package me.gamenu.carbondf.code;

import me.gamenu.carbondf.blocks.Block;
import me.gamenu.carbondf.blocks.Bracket;
import me.gamenu.carbondf.blocks.ElseBlock;
import me.gamenu.carbondf.blocks.TemplateValue;
import me.gamenu.carbondf.exceptions.InvalidFieldException;
import me.gamenu.carbondf.types.BlockType;

import java.util.*;
import java.util.function.Supplier;

public class BlocksList implements List<TemplateValue> {

    static final Set<BlockType> NORMAL_BRACKETS = Set.of(
            BlockType.byID("if_game"),
            BlockType.byID("if_entity"),
            BlockType.byID("if_player"),
            BlockType.byID("if_var"),
            BlockType.byID("else")
    );

    static final Set<BlockType> REPEAT_BRACKETS = Set.of(
            BlockType.byID("repeat")
    );

    public static BlocksList of(TemplateValue... values) {
        BlocksList res = new BlocksList();
        res.addAll(Arrays.stream(values).toList());
        return res;
    }

    List<TemplateValue> blocks;

    public BlocksList() {
        this.blocks = new ArrayList<>();
    }

    public List<TemplateValue> blocks() {
        return blocks;
    }

    public BlocksList elseBlock(Supplier<BlocksList> subListSupplier) {
        addSubList(new ElseBlock(), subListSupplier);
        return this;
    }

    public BlocksList elseBlock(BlocksList subList) {
        addSubList(new ElseBlock(), subList);
        return this;
    }

    public BlocksList addSubList(Block block, Supplier<BlocksList> subListSupplier) {
        addSubList(block, subListSupplier.get());
        return this;
    }

    public BlocksList addSubList(Block block, BlocksList subList) {
        Bracket.Type bType;
        if (NORMAL_BRACKETS.contains(block.getBlock())) {
            bType = Bracket.Type.NORMAL;
        } else if (REPEAT_BRACKETS.contains(block.getBlock())) {
            bType = Bracket.Type.REPEAT;
        } else {
            throw new InvalidFieldException("BlockID \"" + block.getBlock().getId() + "\" does not have brackets");
        }

        blocks.add(block);

        blocks.add(new Bracket(bType, Bracket.Direction.OPEN));
        blocks.addAll(subList.blocks());
        blocks.add(new Bracket(bType, Bracket.Direction.CLOSE));
        return this;
    }

    public BlocksList addBlock(TemplateValue value) {
        add(value);
        return this;
    }

    /*
     * Begin methods overridden from List<TemplateValue>
     */

    @Override
    public int size() {
        return blocks.size();
    }

    @Override
    public boolean isEmpty() {
        return blocks.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return blocks.contains(o);
    }

    @Override
    public Iterator<TemplateValue> iterator() {
        return blocks.iterator();
    }

    @Override
    public Object[] toArray() {
        return blocks.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return blocks.toArray(a);
    }

    @Override
    public boolean add(TemplateValue templateValue) {
        return blocks.add(templateValue);
    }

    @Override
    public boolean remove(Object o) {
        return blocks.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return blocks.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends TemplateValue> c) {
        return blocks.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends TemplateValue> c) {
        return blocks.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return blocks.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return blocks.retainAll(c);
    }

    @Override
    public void clear() {
        blocks.clear();
    }

    @Override
    public TemplateValue get(int index) {
        return blocks.get(index);
    }

    @Override
    public TemplateValue set(int index, TemplateValue element) {
        return blocks.set(index, element);
    }

    @Override
    public void add(int index, TemplateValue element) {
        blocks.add(index, element);
    }

    @Override
    public TemplateValue remove(int index) {
        return blocks.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return blocks.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return blocks.lastIndexOf(o);
    }

    @Override
    public ListIterator<TemplateValue> listIterator() {
        return blocks.listIterator();
    }

    @Override
    public ListIterator<TemplateValue> listIterator(int index) {
        return blocks.listIterator(index);
    }

    @Override
    public List<TemplateValue> subList(int fromIndex, int toIndex) {
        return blocks.subList(fromIndex, toIndex);
    }
}
