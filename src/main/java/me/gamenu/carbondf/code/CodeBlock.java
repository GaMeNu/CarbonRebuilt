package me.gamenu.carbondf.code;

import me.gamenu.carbondf.etc.ToJSONObject;
import me.gamenu.carbondf.types.ActionType;
import me.gamenu.carbondf.types.BlockType;
import me.gamenu.carbondf.values.DFItem;
import me.gamenu.carbondf.values.DFVariable;
import org.json.JSONObject;

/**
 * This class represents a single CodeBlock
 */
public class CodeBlock extends Block implements ToJSONObject {
    private ActionType action;
    private ActionType subAction;
    private Target target;

    private CodeBlockArgs args;
    private Attribute attribute;

    public CodeBlock(String blockName, String actionName) {
        this(ActionType.byName(blockName, actionName));
    }

    public CodeBlock(String blockName, String actionName, Target target) {
        this(ActionType.byName(blockName, actionName), target);
    }

    public CodeBlock(ActionType action) {
        this(action.getBlockType(), action);
    }

    public CodeBlock(BlockType block, ActionType action) {
        this(block, action, null);
    }

    public CodeBlock(ActionType action, Target target) {
        this(action.getBlockType(), action, target);
    }

    public CodeBlock(BlockType block, ActionType action, Target target) {
        super(block);
        this.action = action;
        this.target = target;

        this.args = new CodeBlockArgs(this.action);
    }

    public ActionType getAction() {
        return action;
    }

    public CodeBlock setSubAction(ActionType subAction) {
        this.subAction = subAction;
        return this;
    }

    public CodeBlock setSubAction(String blockID, String actionName) {
        this.subAction = ActionType.byName(blockID, actionName);
        return this;
    }

    public ActionType getSubAction() {
        return subAction;
    }

    public Target getTarget() {
        return target;
    }

    public CodeBlock setAttribute(Attribute attribute) {
        this.attribute = attribute;
        return this;
    }

    /**
     * Get the CodeBlock's arguments and tags' container
     * @return the code block's args
     */
    public CodeBlockArgs args() {
        return args;
    }

    /**
     * <p>Get the CodeBlock's item arguments.</p>
     * <p>This is equivalent to <code>this.args().items()</code></p>
     * @return the item arguments' container
     */
    public CodeBlockArgs.ItemsContainer items() {
        return this.args().items();
    }
    /**
     * <p>Get the CodeBlock's tags.</p>
     * <p>This is equivalent to <code>this.args().tags()</code></p>
     * @return the item arguments' container
     */
    public CodeBlockArgs.TagsContainer tags() {
        return this.args().tags();
    }

    public CodeBlock addItem(DFItem item) {
        this.items().add(item);
        return this;
    }

    public CodeBlock setTagOption(String name, String option) {
        this.tags().getTag(name).setOption(option);
        return this;
    }

    public CodeBlock setTagVariable(String name, DFVariable variable) {
        this.tags().getTag(name).setVariable(variable);
        return this;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject res = new JSONObject()
                .put("id", cat.getId())
                .put("block", block.getId())
                .put("action", action.getName())
                .put("args", args.toJSON());

        if (this.target != null) {
            res.put("target", target.getId());
        }

        if (this.subAction != null) {
            res.put("subAction", subAction.getName());
        }

        return res;
    }


    public enum Attribute {
        NOT("NOT"),
        LS_CANCEL("LS-CANCEL");

        final String id;
        Attribute(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }
}
