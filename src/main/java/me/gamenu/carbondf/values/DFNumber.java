package me.gamenu.carbondf.values;

import org.json.JSONObject;

/**
 * This class represents a DiamondFire number value
 */
public class DFNumber extends DFItem {
    double value;

    public DFNumber(double value) {
        super(Type.NUMBER);
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public JSONObject toJSON() {
        return createJSONFromData(new JSONObject().put("name", Double.toString(value)));
    }
}
