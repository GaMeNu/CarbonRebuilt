package me.gamenu.carbondf.blocks;

import me.gamenu.carbondf.exceptions.InvalidFieldException;
import me.gamenu.carbondf.exceptions.InvalidItemException;
import me.gamenu.carbondf.values.DFBlockTag;
import me.gamenu.carbondf.values.DFItem;
import me.gamenu.carbondf.values.DFVariable;
import org.json.JSONObject;

import java.util.List;

/**
 * This class represents a single CodeBlock
 */
abstract class Block<T extends IBlockArgs> implements IBlock {

    private final Category category;

    private final BlockType block;
    private final ActionType action;
    private ActionType subAction;
    private Target target;

    private Attribute attribute;
    T args;


    /**
     * Constructs a new CodeBlock based on the block ID and the action's name
     * @param blockID Block ID to get action by
     * @param actionName Action Name to get
     */
    public Block(String blockID, String actionName) {
        this(ActionType.byName(blockID, actionName));
    }

    /**
     * Constructs a new CodeBlock using an ActionType. BlockType will be stated inside the ActionType.
     * @param action ActionType to use
     */
    public Block(ActionType action) {
        this(action.getBlockType(), action);
    }

    /**
     * Constructs a new CodeBlock using an ActionType and a Block Type
     * @param block BlockType to get Action with
     * @param action ActionType to get
     */
    public Block(BlockType block, ActionType action) {
        this.category = Category.BLOCK;
        this.block = block;
        this.action = action;

    }

    @Override
    public BlockType getBlock() {
        return block;
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
    public Block<T> setSubAction(ActionType subAction) {
        this.subAction = subAction;
        return this;
    }

    /**
     * Set the block's Sub-ActionType by the ActionType's Block ID and Action Name
     * @param blockID Block ID to get action by
     * @param actionName Action Name to get
     * @return the Action Type
     */
    public Block<T> setSubAction(String blockID, String actionName) {
        this.subAction = ActionType.byName(blockID, actionName);
        return this;
    }

    /**
     * Set the block's Target
     * @param target The block's new target
     * @return this
     */
    public Block<T> setTarget(Target target) {
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
    public Block<T> setAttribute(Attribute attribute) {
        this.attribute = attribute;
        return this;
    }

    @Override
    public Category getCategory() {
        return category;
    }

    public T args() {
        return this.args;
    }

    public TagsContainer tags() {
        return args().tags();
    }

    public Block<T> setTagOption(String name, String option) {
        DFBlockTag tag = this.tags().getTag(name);
        if (tag == null) {
            throw new InvalidFieldException(String.format("Invalid tag name \"%s\" for action name \"%s\". Valid tags: %s",
                    name, action.getName(), "\"" + String.join("\", \"", this.tags().orderedTagNames) + "\""));
        }
        tag.setOption(option);
        return this;
    }

    public Block<T> setTagVariable(String name, DFVariable variable) {
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

        List<DFItem> itemsList = args().getItemsList();
        // Confirm return values' var getter
        if (action.getReturnValues() != null) {
            DFItem returned = itemsList.get(0);
            if (returned == null
                    || returned.getRealType() != DFItem.Type.VARIABLE
                    || !(returned instanceof DFVariable)
            ) {
                throw new InvalidItemException("First argument of a block with return values must be a variable");
            }

            // Set the values of the variable for the action
            DFVariable var = (DFVariable) itemsList.get(0);
            var.setValueType(action.getReturnValues());
        }

        return toJSON();
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
