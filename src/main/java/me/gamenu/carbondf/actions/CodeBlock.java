package me.gamenu.carbondf.actions;

import me.gamenu.carbondf.etc.ToJSONObject;
import me.gamenu.carbondf.values.DFItem;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * This class represents a single CodeBlock
 */
public class CodeBlock implements ToJSONObject {
    private BlockType block;
    private ActionType action;
    private TargetType target;

    private ArrayList<DFItem> args;
    private Attribute attribute;

    public CodeBlock(BlockType block, ActionType action) {
        this(block, action, null);
    }

    public CodeBlock(BlockType block, ActionType action, TargetType target) {

    }





    @Override
    public JSONObject toJSON() {
        JSONObject res = new JSONObject()
                .put("id", "block")
                .put("block", block.getId());

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
