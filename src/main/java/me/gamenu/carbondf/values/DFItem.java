package me.gamenu.carbondf.values;

import me.gamenu.carbondf.etc.ToJSONObject;
import org.json.JSONObject;

public abstract class DFItem implements ToJSONObject {

    private final Type type;

    public DFItem(Type type){
        this.type = type;
    }

    protected JSONObject createJSONFromData(JSONObject dataObj) {
        return new JSONObject()
                .put("id", type.getId())
                .put("data", dataObj);
    }

    public Type getType() {
        return type;
    }

    public abstract JSONObject toJSON();

    public enum Type {

        NUMBER("num"),
        STRING("str"),
        STYLED_TEXT("comp"),
        LOCATION("loc"),
        VECTOR("vec"),
        SOUND("snd"),
        PARTICLE("part"),
        POTION("pot"),
        ITEM("item"),

        LIST("list"),
        DICT("dict"),

        ANY("any"),
        VARIABLE("var"),
        PARAMETER("pn_el"),
        BLOCK_TAG("bl_tag")
        ;

        private final String id;

        Type(String id) {
            this.id = id;
        }

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
