package me.gamenu.carbondf.values;

import org.json.JSONObject;

public class DFStyledText extends DFItem {
    String value;

    public DFStyledText(String value) {
        super(Type.STYLED_TEXT);
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
