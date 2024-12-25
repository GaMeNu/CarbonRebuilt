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
        int rndVal = (int) value;
        return createJSONFromData(new JSONObject().put("name", (rndVal == value) ? Integer.toString(rndVal) : Double.toString(value)));
    }
}
