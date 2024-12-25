package me.gamenu.carbondf.code;

import me.gamenu.carbondf.etc.ToJSONObject;
import org.json.JSONArray;
import org.json.JSONObject;

public class Template extends BlocksList implements ToJSONObject {
    @Override
    public JSONObject toJSON() {
        JSONArray blocksJSON = new JSONArray();

        for (TemplateValue v : this.blocks) {
            blocksJSON.put(v.toJSON());
        }

        return new JSONObject().put("blocks", blocksJSON);
    }
}
