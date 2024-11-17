package me.gamenu.carbondf.values;

import me.gamenu.carbondf.etc.DBCUtils;
import me.gamenu.carbondf.exceptions.InvalidFieldException;
import org.json.JSONObject;

/**
 * Represents a DiamondFire Game Value
 */
public class DFGameValue extends DFItem{
    /** Game value */
    String valueType;
    /** Game value's target */
    Target target;
    /** value's return type */
    Type returnType;

    /**
     * Create a new Game Value with a DEFAULT target
     * @param valueType Game Value type
     */
    public DFGameValue(String valueType) {
        this(valueType, Target.DEFAULT);
    }

    /**
     * Create a new Game Value
     * @param valueType Game Value type
     * @param target Game Value target
     */
    public DFGameValue(String valueType, Target target) {
        super(Type.GAME_VALUE);

        // Make sure is a valid game value
        JSONObject gvData = DBCUtils.gameValuesMap.get(valueType);
        if (gvData == null)
            throw new InvalidFieldException("Invalid Game Value type!");

        // Set value
        this.valueType = valueType;
        // Set target
        this.target = target;

        // Get the matching return type and set it
        String returnTypeName = gvData.getJSONObject("icon").getString("returnType");
        returnType = Type.typeNames.get(returnTypeName);
    }

    @Override
    public JSONObject toJSON() {
        JSONObject data = new JSONObject()
                .put("type", valueType)
                .put("target", target.getId());
        return createJSONFromData(data);
    }

    /**
     * Possible Game Value targets
     */
    public enum Target {
        // I generated this with a Python script based on DFOnline's source code :+1:
        SELECTION("Selection"),
        DEFAULT("Default"),
        VICTIM("Victim"),
        KILLER("Killer"),
        DAMAGER("Damager"),
        SHOOTER("Shooter"),
        PROJECTILE("Projectile"),
        LAST_ENTITY("LastEntity");

        final String id;

        Target(String targetId) {
            this.id = targetId;
        }

        public String getId() {
            return id;
        }
    }
}
