package me.gamenu.carbondf.values;

import org.json.JSONObject;

public class DFString extends DFItem {
    java.lang.String value;

    public DFString(java.lang.String value) {
        super(Type.STRING);
        this.value = value;
    }


    public String getValue() {
        return value;
    }

    @Override
    public JSONObject toJSON() {
        return createJSONFromData(new JSONObject().put("name", value));
    }
}
