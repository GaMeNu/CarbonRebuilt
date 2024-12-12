package me.gamenu.carbondf.code;

import me.gamenu.carbondf.etc.DBCUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a CodeBlock type.
 */
public class BlockType {

    /** The HashMap containing all BLockType IDs and their matching built BlockTypes */
    public static final HashMap<String, BlockType> blockTypes = new HashMap<>();

    /**
     * Return a matching BlockType from a BlockType's ID
     * @param id BlockType's ID
     * @return matching BlockType
     */
    public static BlockType getByID(String id) {
        return blockTypes.get(id);
    }

    /**
     * Return a matching BlockType from a BlockType's name
     * @param name BlockType's name
     * @return matching BlockType
     */
    public static BlockType getByName(String name) {
        String id = DBCUtils.codeBlockTypes.inverseBidiMap().get(name);
        return blockTypes.get(id);
    }

    /** BlockType's ID */
    private final String id;
    /** BLockType's name */
    private final String name;

    /**
     * Create a new BlockType
     * @param id Block's ID
     * @param name Block's name
     */
    private BlockType(String id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Get the BlockType's ID
     * @return the BlockType's ID
     */
    public String getId() {
        return id;
    }

    /**
     * Get the BlockType's name
     * @return the BlockType's name
     */
    public String getName() {
        return name;
    }

    static {

        for (Map.Entry<String, String> block: DBCUtils.codeBlockTypes.entrySet()){

            blockTypes.put(block.getKey(), new BlockType(block.getKey(), block.getValue()));
        }

    }
}
