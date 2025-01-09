package me.gamenu.carbondf.blocks;

import me.gamenu.carbondf.values.DFItem;
import me.gamenu.carbondf.values.DFVariable;

public class CodeBlock extends Block<ArgsContainer>{
    public CodeBlock(String blockID, String actionName) {
        super(blockID, actionName);
        this.args = new ArgsContainer(getAction());
    }

    public CodeBlock(ActionType action) {
        super(action);
        this.args = new ArgsContainer(getAction());
    }

    public CodeBlock(BlockType block, ActionType action) {
        super(block, action);
        this.args = new ArgsContainer(getAction());
    }

    @Override
    public CodeBlock setAttribute(Attribute attribute) {
        super.setAttribute(attribute);
        return this;
    }

    @Override
    public CodeBlock setTagOption(String name, String option) {
        super.setTagOption(name, option);
        return this;
    }

    @Override
    public CodeBlock setTagVariable(String name, DFVariable variable) {
        super.setTagVariable(name, variable);
        return this;
    }

    @Override
    public CodeBlock setSubAction(ActionType subAction) {
        super.setSubAction(subAction);
        return this;
    }

    @Override
    public CodeBlock setTarget(Target target) {
        super.setTarget(target);
        return this;
    }

    @Override
    public CodeBlock setSubAction(String blockID, String actionName) {
        super.setSubAction(blockID, actionName);
        return this;
    }

    public CodeBlock addItem(DFItem item) {
        args().addItem(item);
        return this;
    }
}
