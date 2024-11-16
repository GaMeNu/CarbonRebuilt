package me.gamenu.carbondf.values;

import org.json.JSONObject;

public class DFSound extends DFItem {

    String sound;
    float pitch;
    float volume;

    public DFSound(String sound) {
        this(sound, 1, 1);
    }

    public DFSound(String sound, float pitch, float volume) {
        super(Type.SOUND);
        this.sound = sound;
        this.pitch = pitch;
        this.volume = volume;
    }

    public String getSound() {
        return sound;
    }

    public float getPitch() {
        return pitch;
    }

    public float getVolume() {
        return volume;
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
