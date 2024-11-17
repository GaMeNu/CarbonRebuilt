package me.gamenu.carbondf.values;

import me.gamenu.carbondf.etc.DBCUtils;
import me.gamenu.carbondf.exceptions.InvalidFieldException;
import org.json.JSONObject;

public class DFSound extends DFItem {

    String sound;
    double pitch;
    double volume;

    public DFSound(String sound) {
        this(sound, 1, 1);
    }

    public DFSound(String sound, double pitch, double volume) {
        super(Type.SOUND);
        if (!DBCUtils.soundTypes.contains(sound))
            throw new InvalidFieldException("Invalid sound type");
        this.sound = sound;
        this.pitch = pitch;
        this.volume = volume;
    }

    public String getSound() {
        return sound;
    }

    public double getPitch() {
        return pitch;
    }

    public double getVolume() {
        return volume;
    }

    public DFSound setPitch(double pitch) {
        this.pitch = pitch;
        return this;
    }

    public DFSound setVolume(double volume) {
        this.volume = volume;
        return this;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject data = new JSONObject()
                .put("sound", sound)
                .put("pitch", pitch)
                .put("volume", volume);
        return createJSONFromData(data);
    }
}
