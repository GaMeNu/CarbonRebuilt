package me.gamenu.carbondf.values;

import me.gamenu.carbondf.etc.ToJSONObject;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * The base for all DiamondFire Items and Values
 */
public abstract class DFItem implements ToJSONObject {

    /** The type for the current item */
    private final Type type;

    public DFItem(Type type){
        this.type = type;
    }

    /**
     * Helper method for creating an item JSON for {@link #toJSON} faster
     * @param dataObj data object to wrap in an Item format
     * @return wrapped data object
     */
    protected JSONObject createJSONFromData(JSONObject dataObj) {
        return new JSONObject()
                .put("id", type.getId())
                .put("data", dataObj);
    }

    /**
     * Get the current item's {@link Type Type}
     * @return the current item's type
     */
    public Type getType() {
        return type;
    }

    public abstract JSONObject toJSON();

    /**
     * This enum contains all possible DiamondFire value types
     */
    public enum Type {
        /** Number value */
        NUMBER("num"),
        /** Styled Text value */
        STYLED_TEXT("comp"),
        /** String value */
        STRING("str"),
        /** Location value */
        LOCATION("loc"),
        /** Vector value */
        VECTOR("vec"),
        /** Sound value */
        SOUND("snd"),
        /** Particle value */
        PARTICLE("part"),
        /** Potion value */
        POTION("pot"),
        /** Item value (Normal Minecraft Item) */
        ITEM("item"),

        /**
         * List value<br/>
         * (This is not a physical value, but still exists for parameters and such)
         */
        LIST("list"),
        /**
         * Dict value<br/>
         * (This is not a physical value, but still exists for parameters and such)
         */
        DICT("dict"),

        /**
         * Any value<br/>
         * (This is not a physical value, but still exists for parameters and such)
         */
        ANY("any"),
        /**
         * Variable value<br/>
         * Used for variables
         */
        VARIABLE("var"),
        /**
         * Parameter value<br/>
         * Used for parameters
         */
        PARAMETER("pn_el"),
        /** Game value */
        GAME_VALUE("g_val"),
        /**
         * Block tags<br/>
         * Used for block tags
         */
        BLOCK_TAG("bl_tag"),
        NONE("none")
        ;

        public static final HashMap<String, Type> typeNames = new HashMap<>();
        public static final HashMap<String, Type> typeIDs = new HashMap<>();

        static {
            initTypeNames();
            initTypeIDs();
        }

        private static void initTypeNames() {
            typeNames.put("NUMBER", NUMBER);
            typeNames.put("LOCATION", LOCATION);
            typeNames.put("VECTOR", VECTOR);
            typeNames.put("ITEM", ITEM);
            typeNames.put("LIST", LIST);
            typeNames.put("COMPONENT", STYLED_TEXT);
            typeNames.put("TEXT", STRING);
            typeNames.put("POTION", POTION);
            typeNames.put("SOUND", SOUND);
            typeNames.put("PARTICLE", PARTICLE);
            typeNames.put("VARIABLE", VARIABLE);
            typeNames.put("ANY_TYPE", ANY);
            // This is a fake type, which will be checked for later. Can accept no value.
            typeNames.put("NONE", NONE);
            // This is practically the same as an item
            typeNames.put("BLOCK", ITEM);
            // This is practically the same as a string
            typeNames.put("BLOCK_TAG", STRING);
            typeNames.put("PROJECTILE", ITEM);
            typeNames.put("SPAWN_EGG", ITEM);
            typeNames.put("VEHICLE", ITEM);
            typeNames.put("DICT", DICT);
            typeNames.put("ENTITY_TYPE", ITEM);
        }

        private static void initTypeIDs() {
            for (Type type : Type.values()) {
                typeIDs.put(type.getId(), type);
            }
        }

        /** DiamondFire ID for the Type */
        private final String id;

        Type(String id) {
            this.id = id;
        }

        /**
         * Gets the type's DiamondFire ID
         * @return Type's ID
         */
        public String getId() {
            return id;
        }

        /**
         * Check whether this Type can accept another Type
         * @param other other type to check for
         * @return type
         */
        public boolean canAcceptType(Type other) {
            if (this == ANY) return true;
            return this == other;
        }
    }
}
