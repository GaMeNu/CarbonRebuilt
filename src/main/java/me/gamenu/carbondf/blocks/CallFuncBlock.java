package me.gamenu.carbondf.blocks;

import me.gamenu.carbondf.exceptions.ArgsOverflowException;
import me.gamenu.carbondf.values.DFItem;
import me.gamenu.carbondf.values.DFVariable;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CallFuncBlock implements IBlockData, IDataBlock{

    BlockData data;
    ParamsValuesContainer args;
    private final String name;

    public CallFuncBlock(String name) {
        this.data = new BlockData(ActionType.byName("call_func", "dynamic"));
        this.name = name;
        this.args = new ParamsValuesContainer();
    }

    public CallFuncBlock addInput(DFItem input) {
        this.args.addInputValue(input);
        return this;
    }

    public CallFuncBlock addInputs(DFItem... inputs) {
        for (DFItem input : inputs) {
            addInput(input);
        }

        return this;
    }

    public CallFuncBlock addReturnVar(DFVariable var) {
        this.args.addReturnVariable(var);
        return this;
    }

    public CallFuncBlock addReturnVars(DFVariable... vars) {
        for (DFVariable var : vars) {
            addReturnVar(var);
        }
        return this;
    }

    @Override
    public ParamsValuesContainer getArgs() {
        return this.args;
    }

    @Override
    public CallFuncBlock setTagOption(String name, String option) {
        this.args.tags().getTag(name).setOption(option);
        return this;
    }

    @Override
    public CallFuncBlock setTagVariable(String name, DFVariable variable) {
        this.args.tags().getTag(name).setVariable(variable);
        return this;
    }

    @Override
    public Category getCategory() {
        return this.data.getCategory();
    }

    @Override
    public BlockType getBlock() {
        return this.data.getBlock();
    }

    @Override
    public ActionType getAction() {
        return this.data.getAction();
    }

    @Override
    public ActionType getSubAction() {
        return this.data.getSubAction();
    }

    @Override
    public CallFuncBlock setSubAction(ActionType action) {
        this.data.setSubAction(action);
        return this;
    }

    @Override
    public CallFuncBlock setSubAction(String blockID, String actionName) {
        this.data.setSubAction(blockID, actionName);
        return this;
    }

    @Override
    public Target getTarget() {
        return this.data.getTarget();
    }

    @Override
    public CallFuncBlock setTarget(Target target) {
        this.data.setTarget(target);
        return this;
    }

    @Override
    public IBlock.Attribute getAttribute() {
        return this.data.getAttribute();
    }

    @Override
    public CallFuncBlock setAttribute(IBlock.Attribute attribute) {
        this.data.setAttribute(attribute);
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject res = new JSONObject()
                .put("id", getCategory().getId())
                .put("block", getBlock().getId())
                .put("action", getAction().getName())
                .put("args", args.toJSON())
                .put("data", name);

        if (this.getTarget() != null) {
            res.put("target", this.getTarget().getId());
        }

        if (this.getSubAction() != null) {
            res.put("subAction", this.getSubAction().getName());
        }

        if (this.getAttribute() != null) {
            res.put("attribute", this.getAttribute().getId());
        }

        return res;
    }

    public static class ParamsValuesContainer implements IBlockArgs{
        public static Set<DFItem.Type> argTypes = CodeBlock.ArgsContainer.VALID_ARGS_TYPES;

        TagsContainer tags;
        List<DFItem> inputs;
        List<DFVariable> returns;

        final int maxArgsSize;

        public ParamsValuesContainer() {
            this.tags = new TagsContainer(ActionType.byName("call_func", "dynamic"));
            this.maxArgsSize = ARGS_CONTAINER_SIZE - tags.size();

            this.inputs = new ArrayList<>();
            this.returns = new ArrayList<>();
        }

        public List<DFItem> inputs() {
            return inputs;
        }

        public List<DFVariable> returns() {
            return returns;
        }

        private int getCurrentSize() {
            return inputs.size() + returns.size();
        }

        public ParamsValuesContainer addInputValue(DFItem item) {
            if (getCurrentSize() > maxArgsSize)
                throw new ArgsOverflowException("Too many arguments. Chest length exceeded.");

            inputs.add(item);
            return this;
        }

        public ParamsValuesContainer addReturnVariable(DFVariable variable) {
            if (getCurrentSize() > maxArgsSize)
                throw new ArgsOverflowException("Too many arguments. Chest length exceeded.");

            returns.add(variable);
            return this;
        }

        @Override
        public List<DFItem> getItemsList() {
            List<DFItem> res = new ArrayList<>(returns);
            res.addAll(inputs);

            for (int i = res.size(); i < maxArgsSize; i++)
                res.add(null);

            res.addAll(tags.buildList());
            return res;
        }

        @Override
        public Set<DFItem.Type> getValidArgsTypes() {
            return argTypes;
        }

        @Override
        public TagsContainer tags() {
            return tags;
        }
    }

}
