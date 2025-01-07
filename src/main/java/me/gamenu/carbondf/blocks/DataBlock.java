package me.gamenu.carbondf.blocks;

import me.gamenu.carbondf.types.ActionType;
import me.gamenu.carbondf.types.BlockType;
import org.json.JSONObject;

public class DataBlock extends CodeBlock {
    String name;

    public DataBlock(String blockID, String name) {
        this(BlockType.byID(blockID), name);
    }

    public DataBlock(BlockType block, String name) {
        super(block, ActionType.byName(block, "dynamic"));
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public JSONObject toJSON() {
        return super.toJSON()
                .put("data", name);
    }

    @Override
    public JSONObject buildJSON() {
        return super.buildJSON().put("data", name);
    }
}
