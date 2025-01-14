package me.gamenu.carbondf.blocks;

public interface IBlockData {
    TemplateValue.Category getCategory();

    BlockType getBlock();

    ActionType getAction();

    ActionType getSubAction();

    IBlockData setSubAction(ActionType action);

    IBlockData setSubAction(String blockID, String actionName);

    Target getTarget();

    IBlockData setTarget(Target target);

    IBlock.Attribute getAttribute();

    IBlockData setAttribute(IBlock.Attribute attribute);
}
