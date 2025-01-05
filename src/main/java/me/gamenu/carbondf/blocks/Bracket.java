package me.gamenu.carbondf.blocks;

import org.json.JSONObject;

/**
 * Represents a DiamondFire Bracket.
 * Brackets wrap a smaller SubList of blocks, usually after IF or REPEAT blocks.
 */
public class Bracket extends TemplateValue {

    Type type;
    Direction direction;

    /**
     * Instantiate a new {@link Bracket}
     * @param type {@link Bracket.Type Type} of the bracket
     * @param direction {@link Bracket.Direction Direction} of the bracket
     */
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

    /**
     * The type of Bracket
     * <table>
     *     <tr>
     *         <td>{@link Bracket.Type#NORMAL}</td>
     *         <td>A normal Bracket, after IF blocks</td>
     *     </tr>
     *     <tr>
     *         <td>{@link Bracket.Type#REPEAT}</td>
     *         <td>A repeat Bracket, after REPEAT blocks</td>
     *     </tr>
     * </table>
     */
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

    /**
     * Direction of the bracket.
     * <table>
     *     <tr>
     *         <td>{@link Direction#OPEN}</td>
     *         <td>An open bracket, at the start of a SubList</td>
     *     </tr>
     *     <tr>
     *         <td>{@link Direction#CLOSE}</td>
     *         <td>A closed bracket, at the end of a SubList</td>
     *     </tr>
     * </table>
     */
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
