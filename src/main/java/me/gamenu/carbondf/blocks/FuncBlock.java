package me.gamenu.carbondf.blocks;

import me.gamenu.carbondf.etc.DFBuildable;
import me.gamenu.carbondf.exceptions.*;
import me.gamenu.carbondf.values.DFItem;
import me.gamenu.carbondf.values.DFParameter;
import me.gamenu.carbondf.values.DFVariable;
import me.gamenu.carbondf.values.TypeSet;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class FuncBlock implements IBlockData, IDataBlock {

    BlockData data;
    ParamsContainer args;
    private final String name;

    public FuncBlock(String name) {
        this.data = new BlockData(ActionType.byName("func", "dynamic"));
        this.name = name;
        this.args = new ParamsContainer();
    }

    public FuncBlock addInputParam(DFParameter param) {
        args.addInputParam(param);
        return this;
    }

    public FuncBlock addReturnParam(DFParameter param) {
        args.addReturnParam(param);
        return this;
    }

    public FuncBlock addInputParams(DFParameter... params) {
        for (DFParameter param : params) {
            args.addInputParam(param);
        }
        return this;
    }

    public FuncBlock addReturnParams(DFParameter... params) {
        for (DFParameter param : params) {
            args.addReturnParam(param);
        }
        return this;
    }

    public String getName() {
        return name;
    }

    @Override
    public FuncBlock setAttribute(IBlock.Attribute attribute) {
        this.data.setAttribute(attribute);
        return this;
    }

    @Override
    public ParamsContainer getArgs() {
        return this.args;
    }

    public FuncBlock setTagOption(String name, String option) {
        this.args.tags().getTag(name).setOption(option);
        return this;
    }

    @Override
    public FuncBlock setTagVariable(String name, DFVariable variable) {
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
    public FuncBlock setSubAction(ActionType subAction) {
        this.data.setSubAction(subAction);
        return this;
    }

    @Override
    public FuncBlock setTarget(Target target) {
        this.data.setTarget(target);
        return this;
    }

    @Override
    public IBlock.Attribute getAttribute() {
        return this.data.getAttribute();
    }

    @Override
    public FuncBlock setSubAction(String blockID, String actionName) {
        this.data.setSubAction(blockID, actionName);
        return this;
    }

    @Override
    public Target getTarget() {
        return this.data.getTarget();
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

    public static class ParamsContainer implements DFBuildable, IBlockArgs {
        TagsContainer tags;
        List<DFParameter> inputs;
        List<DFParameter> returns;
        private final int maxArgsSize;

        public ParamsContainer() {
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

        private DFParameter getLastParam() {
            return inputs.get(inputs.size()-1);
        }

        public ParamsContainer addInputParam(DFParameter param) {
            if (getCurrentSize() >= maxArgsSize)
                throw new ArgsOverflowException("Too many arguments. Chest length exceeded.");

            if (param.isReturned())
                throw new InvalidItemException("Returned parameters cannot be added as input params");
            if (!inputs.isEmpty()) {
                TypeSet sharedTypes = new TypeSet(getLastParam().getParamTypes());
                sharedTypes.retainAll(param.getParamTypes());

                if (!param.isOptional() && getLastParam().isOptional()) {
                    if (!sharedTypes.isEmpty()) {
                        throw new InvalidItemException("Non-optional parameters cannot appear after optional parameters with shared types ( " + sharedTypes + " )");
                    }
                }

                if (getLastParam().isPlural()) {
                    if (!sharedTypes.isEmpty()) {
                        throw new InvalidItemException("Cannot have a parameter after a plural parameter with shared types ( " + sharedTypes + " )");
                    }
                }
            }

            inputs.add(param);
            return this;
        }

        public ParamsContainer addReturnParam(DFParameter param) {
            if (getCurrentSize() >= maxArgsSize)
                throw new ArgsOverflowException("Too many arguments. Chest length exceeded.");

            if (!param.isReturned())
                throw new InvalidItemException("Non-returned parameters cannot be added as return params");

            returns.add(param);
            return this;
        }

        private void verifyReturns(CallFuncBlock.ParamsValuesContainer args) {
            List<DFVariable> returnVars = args.returns();

            if (returnVars.size() != returns.size()) {
                throw new ArgsCountException(String.format(
                        "The amount of return params and variables to accept them is different (params: %s, vars: %s)",
                        returns.size(), returnVars.size()
                ));
            }

            for (int i = 0; i < returns.size(); i++) {
                DFVariable var = returnVars.get(i);

                DFParameter param = this.returns.get(i);
                if (!param.canAcceptItem(var))
                    throw new InvalidItemException(String.format(
                            "Return parameter \"%s\" of type(s) %s cannot accept variable \"%s\" of type(s) %s",
                            param.getName(), param.getParamTypes(), var.getName(), var.getType()
                    ));
            }
        }

        private void verifyInputs(CallFuncBlock.ParamsValuesContainer args) {
            int acceptedAmount = 0;
            int paramsI = 0;
            int argsI = 0;
            List<DFParameter> paramsLs = inputs;
            List<DFItem> argsLs = args.inputs();

            // get current param
            DFParameter currentParam = inputs.get(0);
            DFItem currentArg = null;
            if (!args.inputs().isEmpty())
                currentArg = args.inputs().get(0);

            /*
             * Each iteration will compare the next argument to the current parameter.
             * The current parameter may persist between iterations (if it is plural)
             * The current argument may persist between iterations too
             *  (if it failed a plural parameter)
             */
            while (paramsI < paramsLs.size() && (currentArg != null && argsI < argsLs.size())) {
                currentParam = paramsLs.get(paramsI);
                currentArg = argsLs.get(argsI);

                // check if the parameter cannot accept
                if (!currentParam.canAcceptItem(currentArg)) {
                    // If parameter is not optional and hasn't already accepted anything,
                    // An un-skippable parameter has no value to accept.
                    if (!currentParam.isOptional() && acceptedAmount == 0) {
                        throw new TypeException(String.format(
                                "Parameter %s of types %s cannot accept argument of types %s",
                                currentParam.getName(), currentParam.getParamTypes(), currentArg.getType()
                        ));
                    }

                    // Now, either currentParam is optional, or it's plural that has already accepted a value
                    // in both cases, it can be safely skipped
                    // The currentArg must stay the same, because it "failed" matching.
                    // We must keep it to match against the next param.
                    if ((currentParam.isPlural() && acceptedAmount > 0)
                            || currentParam.isOptional()) {
                        acceptedAmount = 0;
                        paramsI++;
                    }
                    else {
                        throw new CarbonRuntimeException("Um... what. " +
                                "(A parameter was assumed to be safely skippable, but it was not.)"
                        );
                    }

                } else {
                    acceptedAmount++;
                    if (!currentParam.isPlural()) {
                        acceptedAmount = 0;
                        paramsI++;
                    }
                    argsI++;
                }
            }

            if (acceptedAmount > 0) {
                paramsI++;
            }

            if (argsI < argsLs.size()) {
                if (currentParam.isPlural() && currentParam.isTypeChecked())
                    throw new TypeException(String.format(
                            "Parameter %s of types %s cannot accept argument of types %s",
                            currentParam.getName(), currentParam.getParamTypes(), currentArg.getType()
                    ));

                throw new ArgsCountException(String.format(
                        "Too many input arguments (got %s)",
                        args.inputs().size()
                ));
            }

            while (paramsI < paramsLs.size()) {
                currentParam = paramsLs.get(++paramsI);
                if (!currentParam.isOptional()) {
                    throw new ArgsCountException(String.format(
                            "Not enough input arguments (got %s)",
                            args.inputs().size()
                    ));
                }
            }

        }

        public void verifyArgs(CallFuncBlock.ParamsValuesContainer args) {
            verifyReturns(args);
            verifyInputs(args);
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

}
