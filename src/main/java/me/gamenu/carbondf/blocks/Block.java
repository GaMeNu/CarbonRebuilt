package me.gamenu.carbondf.blocks;

import me.gamenu.carbondf.types.BlockType;
import org.json.JSONObject;

/**
 * Represents a single DiamondFire Block of any type.
 */
public abstract class Block extends TemplateValue {
    BlockType block;
    public Block(BlockType block) {
        super(Category.BLOCK);
        this.block = block;
    }

    public BlockType getBlock() {
        return block;
    }

    @Override
    public JSONObject toJSON() {
        return super.toJSON().put("block", block.getId());
    }
}
