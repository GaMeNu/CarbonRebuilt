package me.gamenu.carbondf.code;

import me.gamenu.carbondf.blocks.TemplateValue;
import me.gamenu.carbondf.etc.DFBuildable;
import me.gamenu.carbondf.values.DFVariable;
import org.json.JSONArray;
import org.json.JSONObject;

public class Template extends BlocksList implements DFBuildable {
    @Override
    public JSONObject toJSON() {
        JSONArray blocksJSON = new JSONArray();

        for (TemplateValue v : this.blocks) {
            blocksJSON.put(v.toJSON());
        }

        return new JSONObject().put("blocks", blocksJSON);
    }

    /**
     * This is the same as toJSON, but performs more finishing operations
     * @return Built JSON object of the template
     */
    @Override
    public JSONObject build() {
        DFVariable.clearLineScope();
        return DFBuildable.super.build();
    }
}
