package me.gamenu.carbondf.values;

import me.gamenu.carbondf.etc.DBCUtils;
import me.gamenu.carbondf.exceptions.InvalidFieldException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.util.Arrays;

public class DFParticle extends DFItem {

    // PARTICLE
    String particle;

    private final String[] validFields;

    // cluster
    int amount;
    double horizontalSpread; // V
    double verticalSpread; // V

    DFMinecraftItem material; // V

    // motion
    DFVector motion; // V
    float motionVariation; // V
    double roll;

    // color
    Color color;
    Color fadeColor;
    float colorVariation;

    // size
    double size;
    float sizeVariation;

    public DFParticle(String particle) {
        this(particle, 1, 0, 0);
    }

    public DFParticle(String particle, int amount, double horizontalSpread, double verticalSpread) {
        super(Type.PARTICLE);

        // Make sure particle name is valid, and init valid fields
        JSONObject particleJSON = DBCUtils.particleMap.get(particle);
        if (particleJSON == null)
            throw new InvalidFieldException("Particle name does not exist");
        JSONArray fieldsArr = particleJSON.getJSONArray("fields");

        this.validFields = new String[fieldsArr.length()];
        for (int i = 0; i < validFields.length; i++)
            validFields[i] = fieldsArr.getString(i).trim();

        // Init other attributes
        this.particle = particle;
        this.amount = amount;
        this.horizontalSpread = horizontalSpread;
        this.verticalSpread = verticalSpread;

        // Default values
        if (hasField("Motion")) this.motion = new DFVector(0, 0, 0);
        if (hasField("Motion Variation")) this.motionVariation = 0F;
        this.roll = 0.0;

        if (hasField("Color")) this.color = new Color(0, 0, 0);
        if (hasField("Color Variation")) this.colorVariation = 0F;
        this.fadeColor = new Color(0, 0, 0);

        if (hasField("Size")) this.size = 1.0;
        if (hasField("Size Variation")) this.sizeVariation = 0F;
    }

    private boolean hasField(String field) {
        return Arrays.asList(this.validFields).contains(field);
    }

    private void assertField(String field) {
        if (!hasField(field))
            throw new InvalidFieldException("Particle " + particle + " does not support field " + field);
    }

    public DFParticle setSpread(double horizontal, double vertical) {
        this.horizontalSpread = horizontal;
        this.verticalSpread = vertical;
        return this;
    }

    public DFParticle setMaterial(DFMinecraftItem material) {
        assertField("Material");
        this.material = material;
        return this;
    }

    public DFParticle setMotion(DFVector motion) {
        assertField("Motion");
        this.motion = motion;
        return this;
    }

    public DFParticle setMotionVariation(float motionVariation) {
        assertField("Motion Variation");
        this.motionVariation = motionVariation;
        return this;
    }

    public DFParticle setRoll(double roll) {
        this.roll = roll;
        return this;
    }

    public DFParticle setColor(Color color) {
        assertField("Color");
        this.color = color;
        return this;
    }

    public DFParticle setColorVariation(float colorVariation) {
        assertField("Color Variation");
        this.colorVariation = colorVariation;
        return this;
    }

    public DFParticle setFadeColor(Color fadeColor) {
        this.fadeColor = fadeColor;
        return this;
    }

    public DFParticle setSize(double size) {
        assertField("Size");
        this.size = size;
        return this;
    }

    public DFParticle setSizeVariation(float sizeVariation) {
        assertField("Size Variation");
        this.sizeVariation = sizeVariation;
        return this;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject cluster = new JSONObject()
                .put("amount", amount)
                .put("horizontal", horizontalSpread)
                .put("vertical", verticalSpread);

        JSONObject particleData = new JSONObject();
        if (material != null)
            particleData.put("material", material);

        if (motion != null)
            particleData
                    .put("x", motion.getX())
                    .put("y", motion.getY())
                    .put("z", motion.getZ());

        if (hasField("Motion Variation"))
            particleData.put("motionVariation", motionVariation*100);

        particleData.put("roll", roll);

        if (color != null)
            particleData.put("rgb", color.getRGB());

        if (hasField("Color Variation"))
            particleData.put("colorVariation", colorVariation*100);

        particleData.put("rgb_fade", fadeColor.getRGB());

        if (hasField("Size"))
            particleData.put("size", size);

        if (hasField("Size Variation"))
            particleData.put("sizeVariation", sizeVariation*100);

        JSONObject data = new JSONObject()
                .put("particle", particle)
                .put("cluster", cluster)
                .put("data", particleData);
        return createJSONFromData(data);
    }
}
