package me.gamenu.carbondf.blocks;

import me.gamenu.carbondf.etc.DFBuildable;
import org.json.JSONObject;

public abstract class TemplateValue implements DFBuildable {

    Category cat;

    public TemplateValue(Category cat) {
        this.cat = cat;
    }

    @Override
    public JSONObject toJSON() {
        return new JSONObject().put("id", cat.getId());
    }

    public enum Category {
        BLOCK("block"),
        BRACKET("bracket")
        ;
        final String id;

        Category(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }
}
