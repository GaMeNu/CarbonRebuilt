package me.gamenu.carbondf.values;

import org.json.JSONObject;

public class DFVector extends DFItem {

    private final double x;
    private final double y;
    private final double z;

    public DFVector(double x, double y, double z) {
        super(Type.VECTOR);
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    @Override
    public JSONObject toJSON() {
        return createJSONFromData(
                new JSONObject()
                        .put("x", x)
                        .put("y", y)
                        .put("z", z)
        );
    }
}
