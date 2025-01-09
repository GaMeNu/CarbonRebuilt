package me.gamenu.carbondf.blocks;

import me.gamenu.carbondf.etc.DFBuildable;
import org.json.JSONObject;

public interface TemplateValue extends DFBuildable {


    Category getCategory();

    @Override
    JSONObject toJSON();

    enum Category {
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
