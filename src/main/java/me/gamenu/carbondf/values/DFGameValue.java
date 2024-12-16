package me.gamenu.carbondf.values;

import me.gamenu.carbondf.code.Target;
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
    public TypeSet getType() {
        return new TypeSet(returnType);
    }

    /**
     * Get the current Game Value type
     * @return the current Game Value type
     */
    public String getValueType() {
        return valueType;
    }

    /**
     * Get the Game Value's target
     * @return the current Game Value target
     */
    public Target getTarget() {
        return target;
    }

    /**
     * Get the Game Value's return type
     * @return the Game Value's type
     */
    public Type getReturnType() {
        return returnType;
    }


    @Override
    public JSONObject toJSON() {
        JSONObject data = new JSONObject()
                .put("type", valueType)
                .put("target", target.getId());
        return createJSONFromData(data);
    }

}
