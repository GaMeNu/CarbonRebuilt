package me.gamenu.carbondf.blocks;


public class BlockData implements IBlockData {
    private final TemplateValue.Category category;
    private final BlockType block;
    private final ActionType action;
    private ActionType subAction;
    private Target target;
    private IBlock.Attribute attribute;

    public BlockData(BlockType block, ActionType action) {
        this.category = TemplateValue.Category.BLOCK;
        this.block = block;
        this.action = action;
    }

    public BlockData(ActionType action) {
        this(action.getBlockType(), action);
    }

    public TemplateValue.Category getCategory() {
        return category;
    }

    public BlockType getBlock() {
        return block;
    }

    public ActionType getAction() {
        return action;
    }

    public ActionType getSubAction() {
        return subAction;
    }

    public BlockData setSubAction(ActionType subAction) {
        this.subAction = subAction;
        return this;
    }

    @Override
    public IBlockData setSubAction(String blockID, String actionName) {
        return this.setSubAction(ActionType.byName(blockID, actionName));
    }

    public Target getTarget() {
        return target;
    }

    public BlockData setTarget(Target target) {
        this.target = target;
        return this;
    }

    public IBlock.Attribute getAttribute() {
        return attribute;
    }

    public BlockData setAttribute(IBlock.Attribute attribute) {
        this.attribute = attribute;
        return this;
    }
}
