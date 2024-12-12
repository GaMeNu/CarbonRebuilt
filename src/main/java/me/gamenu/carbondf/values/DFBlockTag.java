package me.gamenu.carbondf.values;

import me.gamenu.carbondf.code.ActionType;
import me.gamenu.carbondf.code.BlockType;
import org.json.JSONObject;

public class DFBlockTag extends DFItem{

    /** {@link BlockType Block} the BlockTag belongs to */
    BlockType block;
    /** {@link ActionType Action} the BlockTag belongs to */
    ActionType action;
    /** The BlockTag's name */
    String tag;
    /** The BlockTag's selected option */
    String option;
    /** The BlockTag's variable */
    DFVariable variable;

    public DFBlockTag() {
        super(Type.BLOCK_TAG);
    }

    @Override
    public JSONObject toJSON() {
        return null;
    }
}
