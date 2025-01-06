package me.gamenu.carbondf.blocks;

import me.gamenu.carbondf.exceptions.InvalidFieldException;
import me.gamenu.carbondf.types.ActionType;
import me.gamenu.carbondf.types.BlockType;
import me.gamenu.carbondf.values.DFBlockTag;
import me.gamenu.carbondf.values.DFItem;
import me.gamenu.carbondf.values.DFVariable;
import org.json.JSONObject;

/**
 * This class represents a single CodeBlock
 */
public class CodeBlock extends Block {
    private final ActionType action;
    private ActionType subAction;
    private Target target;

    private final CodeBlockArgs args;
    private Attribute attribute;

    /**
     * Constructs a new CodeBlock based on the block ID and the action's name
     * @param blockID Block ID to get action by
     * @param actionName Action Name to get
     */
    public CodeBlock(String blockID, String actionName) {
        this(ActionType.byName(blockID, actionName));
    }

    /**
     * Constructs a new CodeBlock using an ActionType. BlockType will be stated inside the ActionType.
     * @param action ActionType to use
     */
    public CodeBlock(ActionType action) {
        this(action.getBlockType(), action);
    }

    /**
     * Constructs a new CodeBlock using an ActionType and a Block Type
     * @param block BlockType to get Action with
     * @param action ActionType to get
     */
    public CodeBlock(BlockType block, ActionType action) {
        super(block);
        this.action = action;

        this.args = new CodeBlockArgs(this.action);
    }

    /**
     * Gets the block's ActionType
     * @return the block's ActionType
     */
    public ActionType getAction() {
        return action;
    }

    /**
     * Set the block's Sub-ActionType
     * @param subAction ActionType to set as sub action
     * @return the block's Sub-Action
     */
    public CodeBlock setSubAction(ActionType subAction) {
        this.subAction = subAction;
        return this;
    }

    /**
     * Set the block's Sub-ActionType by the ActionType's Block ID and Action Name
     * @param blockID Block ID to get action by
     * @param actionName Action Name to get
     * @return the Action Type
     */
    public CodeBlock setSubAction(String blockID, String actionName) {
        this.subAction = ActionType.byName(blockID, actionName);
        return this;
    }

    /**
     * Set the block's Target
     * @param target The block's new target
     * @return this
     */
    public CodeBlock setTarget(Target target) {
        this.target = target;
        return this;
    }

    public ActionType getSubAction() {
        return subAction;
    }

    public Target getTarget() {
        return target;
    }

    /**
     * Set the block's Attribute.
     * @param attribute The block's attribute to set
     * @return this
     */
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
        DFBlockTag tag = this.tags().getTag(name);
        if (tag == null) {
            throw new InvalidFieldException(String.format("Invalid tag name \"%s\" for action name \"%s\". Valid tags: %s",
                    name, action.getName(), "\"" + String.join("\", \"", this.tags().orderedTagNames) + "\""));
        }
        tag.setOption(option);
        return this;
    }

    public CodeBlock setTagVariable(String name, DFVariable variable) {
        this.tags().getTag(name).setVariable(variable);
        return this;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject res = new JSONObject()
                .put("id", category.getId())
                .put("block", block.getId())
                .put("action", action.getName())
                .put("args", args.toJSON());

        if (this.target != null) {
            res.put("target", target.getId());
        }

        if (this.subAction != null) {
            res.put("subAction", subAction.getName());
        }

        if (this.attribute != null) {
            res.put("attribute", attribute.getId());
        }

        return res;
    }

    @Override
    public JSONObject buildJSON() {
        // Confirm return values' var getter
        if (action.getReturnValues() != null) {
            DFItem returned = items().get(0);
            if (returned == null
                    || returned.getRealType() != DFItem.Type.VARIABLE
                    || !(returned instanceof DFVariable)
            ) {
                throw new InvalidFieldException("First argument of a block with return values must be a variable");
            }

            // Set the values of the variable for the action
            DFVariable var = (DFVariable) items().get(0);
            var.setValueType(action.getReturnValues());
        }
        return super.buildJSON();
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