package me.gamenu.carbondf.blocks;

import me.gamenu.carbondf.etc.DFBuildable;
import me.gamenu.carbondf.exceptions.ArgsOverflowException;
import me.gamenu.carbondf.exceptions.InvalidItemException;
import me.gamenu.carbondf.values.DFItem;
import me.gamenu.carbondf.values.DFParameter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ParameterContainer implements DFBuildable, IBlockArgs {
    TagsContainer tags;
    List<DFParameter> inputs;
    List<DFParameter> returns;
    private final int maxArgsSize;

    public ParameterContainer() {
        this.tags = new TagsContainer(ActionType.byName("func", "dynamic"));
        this.maxArgsSize = ARGS_CONTAINER_SIZE - tags.size();

        this.inputs = new ArrayList<>();
        this.returns = new ArrayList<>();
    }

    @Override
    public TagsContainer tags() {
        return this.tags;
    }

    private int getCurrentSize() {
        return inputs.size() + returns.size();
    }

    public ParameterContainer addInputParam(DFParameter param) {
        if (getCurrentSize() >= maxArgsSize)
            throw new ArgsOverflowException("Too many arguments. Chest length exceeded.");

        if (param.isReturned())
            throw new InvalidItemException("Returned parameters cannot be added is input params");

        inputs.add(param);
        return this;
    }

    public ParameterContainer addReturnParam(DFParameter param) {
        if (getCurrentSize() >= maxArgsSize)
            throw new ArgsOverflowException("Too many arguments. Chest length exceeded.");

        if (!param.isReturned())
            throw new InvalidItemException("Non-returned parameters cannot be added as return params");

        returns.add(param);
        return this;
    }

    @Override
    public List<DFItem> getItemsList() {
        List<DFItem> res = new ArrayList<>();
        res.addAll(returns);
        res.addAll(inputs);

        for (int i = res.size(); i < maxArgsSize; i++)
            res.add(null);

        res.addAll(tags.buildList());
        return res;
    }

    @Override
    public Set<DFItem.Type> getValidArgsTypes() {
        return Set.of();
    }
}
