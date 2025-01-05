package me.gamenu.carbondf.etc;

import org.json.JSONObject;

/**
 * This class is just to make sure DiamondFire objects can be serialized to a JSON format
 * Allows to easily compile the intermediary format down to DiamondFire JSON
 */
public interface DFBuildable {
    /**
     * Returns a JSONObject representation of the object.
     * @return the JSONObject representing the object.
     */
    JSONObject toJSON();

    default JSONObject build() {
        return toJSON();
    }
}
