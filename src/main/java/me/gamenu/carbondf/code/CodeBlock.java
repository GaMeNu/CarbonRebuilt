package me.gamenu.carbondf.code;

import me.gamenu.carbondf.etc.ToJSONObject;
import org.json.JSONObject;

/**
 * This class represents a single CodeBlock
 */
public class CodeBlock implements ToJSONObject {
    private BlockType block;
    private ActionType action;
    private ActionType subAction;
    private TargetType selection;

    private CodeBlockArgs args;
    private Attribute attribute;

    public CodeBlock(BlockType block, ActionType action) {
        this(block, action, null);
    }

    public CodeBlock(BlockType block, ActionType action, TargetType target) {
        this.args = new CodeBlockArgs();
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
