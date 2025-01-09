package me.gamenu.carbondf.blocks;

import org.json.JSONObject;

public class ElseBlock implements IBlock {
    BlockType block;
    Category category;


    public ElseBlock() {
        this.category = Category.BLOCK;
        this.block = BlockType.byID("else");
    }

    @Override
    public BlockType getBlock() {
        return block;
    }

    @Override
    public Category getCategory() {
        return category;
    }

    @Override
    public JSONObject toJSON() {
        return new JSONObject()
                .put("id", category.getId())
                .put("block", block.getId());
    }
}
