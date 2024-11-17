package me.gamenu.carbondf.values;

import me.gamenu.carbondf.etc.DBCUtils;
import me.gamenu.carbondf.exceptions.InvalidFieldException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.util.HashSet;

/**
 * This class represents a DiamondFire particle value
 */
public class DFParticle extends DFItem {

    // PARTICLE
    String particle;

    // This is a HashSet to improve performance ( O(1) instead of O(n) iteration)
    // Yes I KNOW this is premat. optim. but it's also for the sake fo consistency
    // (am plannning to use HashSets for more things like potions and sounds)
    private final HashSet<String> validFields;

    // cluster
    int amount;
    double horizontalSpread;
    double verticalSpread;

    DFMinecraftItem material;

    // motion
    DFVector motion;
    float motionVariation;
    double roll;

    // color
    Color color;
    Color fadeColor;
    float colorVariation;

    // size
    double size;
    float sizeVariation;

    /**
     * Create a basic DiamondFire particle
     * @param particle particle's type
     */
    public DFParticle(String particle) {
        this(particle, 1, 0, 0);
    }

    /**
     * Create a DiamondFire Particle
     * @param particle particle's type
     * @param amount amount of particles
     * @param horizontalSpread horizontal spread of particles
     * @param verticalSpread vertical spread of particles
     */
    public DFParticle(String particle, int amount, double horizontalSpread, double verticalSpread) {
        super(Type.PARTICLE);

        // Make sure particle name is valid, and init valid fields
        JSONObject particleJSON = DBCUtils.particleMap.get(particle);
        if (particleJSON == null)
            throw new InvalidFieldException("Particle name does not exist");
        JSONArray fieldsArr = particleJSON.getJSONArray("fields");

        this.validFields = new HashSet<>();
        for (int i = 0; i < fieldsArr.length(); i++)
            validFields.add(fieldsArr.getString(i).trim());

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

    /**
     * Check if the field is valid for the current particle
     * @param field field name to check for
     * @return whether the field is valid
     */
    private boolean hasField(String field) {
        return this.validFields.contains(field);
    }

    /**
     * Same as {@link #hasField(String field) hasField},
     * but throws an {@link InvalidFieldException} if the field is invalid
     * @param field field to check for
     */
    private void assertField(String field) {
        if (!hasField(field))
            throw new InvalidFieldException("Particle " + particle + " does not support field " + field);
    }

    private void assertVariationRange(float variation) {
        if (variation < 0 || variation > 1)
            throw new IllegalArgumentException("Variation must be between 0 and 1");
    }
    /**
     * Sets the particle's spread
     * @param horizontal horizontal spread
     * @param vertical vertical spread
     * @return this
     */
    public DFParticle setSpread(double horizontal, double vertical) {
        this.horizontalSpread = horizontal;
        this.verticalSpread = vertical;
        return this;
    }

    /**
     * Sets the particle's material.<br/>
     * Required particle field: <code>"Material"</code>
     * @param material Material to set
     * @return this
     * @throws InvalidFieldException if the particle does not support the required field
     */
    public DFParticle setMaterial(DFMinecraftItem material) {
        assertField("Material");
        this.material = material;
        return this;
    }

    /**
     * Sets the particle's motion direction.<br/>
     * Required particle field: <code>"Motion"</code>
     * @param motion Motion vector
     * @return this
     * @throws InvalidFieldException if the particle does not support the required field
     */
    public DFParticle setMotion(DFVector motion) {
        assertField("Motion");
        this.motion = motion;
        return this;
    }

    /**
     * Sets the particle's motion variation.<br/>
     * Required particle field: <code>"Motion Variation"</code>
     * @param variation Motion variation, between 0 and 1.
     * @return this
     * @throws InvalidFieldException if the particle does not support the required field
     */
    public DFParticle setMotionVariation(float variation) {
        assertField("Motion Variation");
        assertVariationRange(variation);
        this.motionVariation = variation;
        return this;
    }

    /**
     * Sets the particle's roll.
     * @param roll roll
     * @return this
     */
    public DFParticle setRoll(double roll) {
        this.roll = roll;
        return this;
    }

    /**
     * Sets the particle's color.<br/>
     * Required particle field: <code>"Color"</code>
     * @param color Particle's color
     * @return this
     * @throws InvalidFieldException if the particle does not support the required field
     */
    public DFParticle setColor(Color color) {
        assertField("Color");
        this.color = color;
        return this;
    }

    /**
     * Sets the particle's variation.<br/>
     * Required particle field: <code>"Color Variation"</code>
     * @param variation Color variation, between 0 and 1
     * @return this
     * @throws InvalidFieldException if the particle does not support the required field
     */
    public DFParticle setColorVariation(float variation) {
        assertField("Color Variation");
        assertVariationRange(variation);
        this.colorVariation = variation;
        return this;
    }

    /**
     * Sets the particle's fade color.
     * @param fadeColor Fade color
     * @return this
     */
    public DFParticle setFadeColor(Color fadeColor) {
        this.fadeColor = fadeColor;
        return this;
    }

    /**
     * Sets the particle's size.<br/>
     * Required particle field: <code>"Size"</code>
     * @param size Particle's size
     * @return this
     * @throws InvalidFieldException if the particle does not support the required field
     */
    public DFParticle setSize(double size) {
        assertField("Size");
        this.size = size;
        return this;
    }

    /**
     * Sets the particle's size variation.<br/>
     * Required particle field: <code>"Size Variation"</code>
     * @param variation Size variation, between 0 and 1
     * @return this
     * @throws InvalidFieldException if the particle does not support the required field
     */
    public DFParticle setSizeVariation(float variation) {
        assertField("Size Variation");
        assertVariationRange(variation);
        this.sizeVariation = variation;
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
