package me.gamenu.carbondf.blocks;

import me.gamenu.carbondf.etc.DBCUtils;
import me.gamenu.carbondf.exceptions.InvalidFieldException;

import java.util.*;

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
    public static BlockType byID(String id) {
        BlockType res = blockTypes.get(id);
        if (res == null) {
            throw new InvalidFieldException("Cannot get BlockType of ID \"" + id + "\"");
        }
        return res;
    }

    /**
     * Return a matching BlockType from a BlockType's name
     * @param name BlockType's name
     * @return matching BlockType
     */
    public static BlockType byName(String name) {
        String id = DBCUtils.codeBlockTypes.inverseBidiMap().get(name);
        if (id == null) {
            throw new InvalidFieldException("Cannot get BlockType of name \"" + name + "\"");
        }
        return blockTypes.get(id);
    }

    /** BlockType's ID */
    private final String id;
    /** BLockType's name */
    private final String name;

    private final Set<Target> validTargets;

    /**
     * Create a new BlockType
     *
     * @param id           Block's ID
     * @param name         Block's name
     * @param validTargets Valid BlockType targets (may be null)
     */
    private BlockType(String id, String name, Set<Target> validTargets) {
        this.id = id;
        this.name = name;
        this.validTargets = validTargets;
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

    public boolean equals(BlockType obj) {
        return getId().equals(obj.getId());
    }

    static {
        Set<Target> validTargets = Set.of(Target.values());
        Map<String, Set<Target>> blockSelectionsMap = new HashMap<>();
        blockSelectionsMap.put("player_action", validTargets);
        blockSelectionsMap.put("entity_event",  validTargets);
        blockSelectionsMap.put("entity_action", validTargets);
        blockSelectionsMap.put("if_entity",     validTargets);
        blockSelectionsMap.put("if_player",     validTargets);


        for (Map.Entry<String, String> block: DBCUtils.codeBlockTypes.entrySet()){
            blockTypes.put(block.getKey(),
                    new BlockType(block.getKey(), block.getValue(), blockSelectionsMap.get(block.getKey()))
            );
        }

    }
}
