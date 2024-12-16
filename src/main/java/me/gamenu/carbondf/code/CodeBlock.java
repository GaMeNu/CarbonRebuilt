package me.gamenu.carbondf.code;

import me.gamenu.carbondf.etc.ToJSONObject;
import me.gamenu.carbondf.types.ActionType;
import me.gamenu.carbondf.types.BlockType;
import org.json.JSONObject;

/**
 * This class represents a single CodeBlock
 */
public class CodeBlock implements ToJSONObject {
    // TODO: split to two subclasses - normal codeblocks, and dynamic codeblocks (funcs, procs, callfuncs, callprocs)
    private BlockType block;
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
        this.block = block;
        this.action = action;
        this.target = target;
        this.args = new CodeBlockArgs(this.action);
    }

    public BlockType getBlockType() {
        return block;
    }

    public ActionType getActionType() {
        return action;
    }

    public ActionType getSubActionType() {
        return subAction;
    }

    public Target getTarget() {
        return target;
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
        return args().items();
    }
    /**
     * <p>Get the CodeBlock's tags.</p>
     * <p>This is equivalent to <code>this.args().tags()</code></p>
     * @return the item arguments' container
     */
    public CodeBlockArgs.TagsContainer tags() {
        return args().tags();
    }

    @Override
    public JSONObject toJSON() {
        JSONObject res = new JSONObject()
                .put("id", "block")
                .put("block", block.getId())
                .put("action", action.getName())
                .put("args", args.toJSON());

        if (target != null) {
            res.put("target", target.getId());
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
