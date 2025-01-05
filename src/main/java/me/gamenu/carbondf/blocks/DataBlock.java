package me.gamenu.carbondf.blocks;

import me.gamenu.carbondf.types.ActionType;
import me.gamenu.carbondf.types.BlockType;
import org.json.JSONObject;

public class DataBlock extends CodeBlock {
    String name;

    public DataBlock(String blockID, String name) {
        this(BlockType.byID(blockID), name);
    }

    public DataBlock(BlockType blockType, String name) {
        super(blockType, ActionType.byName(blockType, "dynamic"));
        this.name = name;
    }

    @Override
    public JSONObject toJSON() {
        return super.toJSON()
                .put("data", name);
    }
}
