package me.gamenu.carbondf.code;

import org.json.JSONObject;

public class Bracket extends TemplateValue {

    Type type;
    Direction direction;

    public Bracket(Type type, Direction direction) {
        super(Category.BRACKET);
        this.type = type;
        this.direction = direction;
    }

    @Override
    public JSONObject toJSON() {
        return super.toJSON()
                .put("type", type.getId())
                .put("direct", direction.getId());
    }

    public enum Type {
        NORMAL("norm"),
        REPEAT("repeat")
        ;
        final String id;

        Type(String id){
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }

    public enum Direction {
        OPEN("open"),
        CLOSE("close")
        ;
        final String id;

        Direction(String id){
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }
}
