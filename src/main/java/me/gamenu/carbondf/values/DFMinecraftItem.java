package me.gamenu.carbondf.values;

import org.json.JSONObject;

public class DFMinecraftItem extends DFItem{
    String id;

    public DFMinecraftItem() {
        super(Type.ITEM);
    }

    public String getId() {
        return id;
    }

    @Override
    public JSONObject toJSON() {
        return null;
    }
}
