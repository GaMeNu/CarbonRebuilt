package me.gamenu.carbondf.code;

import me.gamenu.carbondf.etc.DBCUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActionType {
    /** This is a map containing each {@link BlockType BlockType}'s matching ActionTypes, by their names*/
    public static Map<BlockType, Map<String, ActionType>> actionTypes;

    /**
     * Get an ActionType by its {@link BlockType BlockType} and name
     * @param blockType BlockType to which the action belongs
     * @param name ActionType's name
     * @return the requested ActionType (null if not found)
     */
    public static ActionType fromName(BlockType blockType, String name) {
        Map<String, ActionType> cbActs = actionTypes.get(blockType);
        if (cbActs == null) return null;
        return cbActs.get(name);
    }

    /**
     * Get an ActionType by its matching {@link BlockType BlockType}'s ID and name
     * @param blockID Block ID
     * @param name ActionType's name
     * @return the requested ActionType (null if not found)
     */
    public static ActionType fromName(String blockID, String name) {
        return fromName(BlockType.fromID(blockID), name);
    }

    static {
        actionTypes = new HashMap<>();
        for (Map.Entry<String, Map<String, JSONObject>> blockActsEntry : DBCUtils.actionTypes.entrySet()) {
            // Current block type (original map is already sorted)
            BlockType blockType = BlockType.fromID(blockActsEntry.getKey());

            actionTypes.put(blockType, new HashMap<>());
            for (Map.Entry<String, JSONObject> actEntry : blockActsEntry.getValue().entrySet()) {
                JSONObject actionJSON = actEntry.getValue();

                // Set ActionType's name
                String name = actionJSON.getString("name");

                // Set ActionType's matching codeblock
                String blockName = actionJSON.getString("codeblockName");


                // We are not sure whether the json even HAS the cancellable tag,
                // so we check that first
                boolean cancellable =
                        actionJSON.has("cancellable")
                        && actionJSON.getJSONObject("icon").getBoolean("cancellable");

                // We are also not sure that sub-action blocks exist for this action
                List<BlockType> subActionBlocks;
                if (actionJSON.has("subActionBlocks")) {
                    // Create the sub action blocks array list
                    subActionBlocks = new ArrayList<>();

                    for (Object oSBB : actEntry.getValue().getJSONArray("subActionBlocks")) {
                        String sbbID = (String) oSBB;
                        BlockType curBT = BlockType.fromID(sbbID);
                        subActionBlocks.add(curBT);
                    }
                } else
                    subActionBlocks = null;

                ActionType at = new ActionType(name, blockType, cancellable, subActionBlocks);

                actionTypes.get(blockType).put(name, at);
            }
        }
    }

    /** Action's name */
    final String name;
    /** Action's matching {@link BlockType BlockType} */
    final BlockType blockType;

    // Specific to EVENT actions
    /** Whether the Event action can be cancelled */
    final boolean cancellable;

    // Specific to actions with sub-actions (Repeat-While, SelectObject-Condition)
    /** Action's possible sub-action blocks */
    final List<BlockType> subActionBlocks;

    private ActionType(String name, BlockType blockType, boolean cancellable, List<BlockType> subActionBlocks) {
        this.name = name;
        this.blockType = blockType;
        this.cancellable = cancellable;
        this.subActionBlocks = subActionBlocks;
    }

    /**
     * Get the ActionType's name
     * @return the ActionType's name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the ActionType's associated {@link BlockType BlockType}
     * @return the ActionType's BlockType
     */
    public BlockType getBlockType() {
        return blockType;
    }

    /**
     * Get whether this ActionType is cancellable or not<br/>
     * This may be true only for some Event-type ActionTypes
     * @return whether this action is cancellable
     */
    public boolean isCancellable() {
        return cancellable;
    }

    /**
     * Get the List of Sub-Action blocks
     * @return the list of the ActionType's sub-action blocks
     */
    public List<BlockType> getSubActionBlocks() {
        return subActionBlocks;
    }
}
