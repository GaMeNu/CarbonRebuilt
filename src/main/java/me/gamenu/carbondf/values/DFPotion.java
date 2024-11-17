package me.gamenu.carbondf.values;

import me.gamenu.carbondf.etc.DBCUtils;
import me.gamenu.carbondf.exceptions.InvalidFieldException;
import org.json.JSONObject;

/**
 * This class represents a DiamondFire potion value
 */
public class DFPotion extends DFItem{

    String potion;
    int duration;
    int amplifier;

    /**
     * Create a new DiamondFire potion value
     * @param potion potion type
     */
    public DFPotion(String potion) {
        this(potion, 1000000, 0);
    }

    /**
     * Create a new DiamondFire potion value
     * @param potion potion type
     * @param duration potion duration (in ticks)
     * @param amplifier potion amplifier
     */
    public DFPotion(String potion, int duration, int amplifier) {
        super(Type.POTION);

        if (!DBCUtils.potionTypes.contains(potion))
            throw new InvalidFieldException("Invalid potion type");

        this.potion = potion;
        this.duration = duration;
        this.amplifier = amplifier;
    }

    /**
     * Set the potion's duration
     * @param duration potion's duration (in ticks)
     * @return this
     */
    public DFPotion setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    /**
     * Set the potion's amplifier
     * @param amplifier potion's amplifier
     * @return this
     */
    public DFPotion setAmplifier(int amplifier) {
        this.amplifier = amplifier;
        return this;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject data = new JSONObject()
                .put("pot", potion)
                .put("amp", amplifier)
                .put("dur", duration);
        return createJSONFromData(data);
    }
}
