package me.gamenu.carbondf.blocks;

import me.gamenu.carbondf.values.DFParameter;
import me.gamenu.carbondf.values.DFVariable;

public class FuncBlock extends Block<ParameterContainer> implements IDataBlock {

    private final String name;

    public FuncBlock(String name) {
        super(ActionType.byName("call_func", "dynamic"));
        this.name = name;
        this.args = new ParameterContainer();
    }

    public FuncBlock addInputParam(DFParameter param) {
        args.addInputParam(param);
        return this;
    }

    public FuncBlock addReturnParam(DFParameter param) {
        args.addReturnParam(param);
        return this;
    }

    public String getName() {
        return name;
    }

    @Override
    public FuncBlock setAttribute(Attribute attribute) {
        super.setAttribute(attribute);
        return this;
    }

    @Override
    public FuncBlock setTagOption(String name, String option) {
        super.setTagOption(name, option);
        return this;
    }

    @Override
    public FuncBlock setTagVariable(String name, DFVariable variable) {
        super.setTagVariable(name, variable);
        return this;
    }

    @Override
    public FuncBlock setSubAction(ActionType subAction) {
        super.setSubAction(subAction);
        return this;
    }

    @Override
    public FuncBlock setTarget(Target target) {
        super.setTarget(target);
        return this;
    }

    @Override
    public FuncBlock setSubAction(String blockID, String actionName) {
        super.setSubAction(blockID, actionName);
        return this;
    }


}
