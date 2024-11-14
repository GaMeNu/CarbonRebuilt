package me.gamenu.carbondf.values;

import org.json.JSONObject;

/**
 * This class represents a DiamondFire Location value
 */
public class DFLocation extends DFItem {
    double x;
    double y;
    double z;
    double pitch;
    double yaw;

    public DFLocation(double x, double y, double z, double pitch, double yaw) {
        super(Type.LOCATION);
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public DFLocation(double x, double y, double z) {
        this(x, y, z, 0, 0);
    }

    @Override
    public JSONObject toJSON() {

        // Why tf is DF like this.
        JSONObject locObj = new JSONObject()
                .put("x", x)
                .put("y", y)
                .put("z", z)
                .put("pitch", pitch)
                .put("yaw", yaw);

        return createJSONFromData(
                new JSONObject()
                        .put("isBlock", false)
                        .put("loc", locObj)
        );
    }
}
