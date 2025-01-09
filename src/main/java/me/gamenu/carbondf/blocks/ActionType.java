package me.gamenu.carbondf.blocks;

import me.gamenu.carbondf.etc.DBCUtils;
import me.gamenu.carbondf.exceptions.InvalidFieldException;
import me.gamenu.carbondf.values.DFItem;
import me.gamenu.carbondf.values.TypeSet;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a DiamondFire Action type
 */
public class ActionType {
    /** This is a map containing each {@link BlockType BlockType}'s matching ActionTypes, by their names*/
    public static Map<BlockType, Map<String, ActionType>> actionTypes;

    /**
     * Get an ActionType by its {@link BlockType BlockType} and name
     * @param blockType BlockType to which the action belongs
     * @param name ActionType's name
     * @return the requested ActionType (null if not found)
     */
    public static ActionType byName(BlockType blockType, String name) {
        Map<String, ActionType> cbActs = actionTypes.get(blockType);
        if (cbActs == null)
            throw new InvalidFieldException("Cannot get ActionType of name \"" + name + "\"");
        ActionType res =  cbActs.get(name);
        if (res == null)
            throw new InvalidFieldException("Cannot get ActionType of name \"" + name + "\"");
        return res;
    }

    /**
     * Get an ActionType by its matching {@link BlockType BlockType}'s ID and name
     * @param blockID Block ID
     * @param name ActionType's name
     * @return the requested ActionType (null if not found)
     */
    public static ActionType byName(String blockID, String name) {
        return byName(BlockType.byID(blockID), name);
    }

    static {
        actionTypes = new HashMap<>();
        for (Map.Entry<String, Map<String, JSONObject>> blockActsEntry : DBCUtils.actionTypes.entrySet()) {
            // Current block type (original map is already sorted)
            BlockType blockType = BlockType.byID(blockActsEntry.getKey());

            actionTypes.put(blockType, new HashMap<>());
            for (Map.Entry<String, JSONObject> actEntry : blockActsEntry.getValue().entrySet()) {
                JSONObject actionJSON = actEntry.getValue();

                ActionType at;
                at = ActionType.fromJSON(actionJSON);
                String name = at.getName();

                actionTypes.get(blockType).put(name, at);
            }
        }
    }

    /** Action's name */
    final String name;
    /** Action's matching {@link BlockType BlockType} */
    final BlockType blockType;

    /*
     * Action's overload-able arguments
     * CANCELLED - The DF ActionDump standard is too complex for my smol brain (and I'm too lazy)
     */
    //List<List<DFParameter>> arguments;

    /** Action's return value's types*/
    TypeSet returnValues;

    // Specific to EVENT actions
    /** Whether the Event action can be cancelled */
    final boolean cancellable;

    // Specific to actions with sub-actions (Repeat-While, SelectObject-Condition)
    /** Action's possible sub-action blocks */
    final List<BlockType> subActionBlocks;

    /** A list of possible tag names for this action type */
    final List<String> tagNames;

    /*
     * Arguments parsing:
     * {"text": ""} - TL_END - marks the end of a shared types between all
     * {"text": "§xOR"} - TL_OR - all items from the start or last TL_OR are one option,
     * and the ones after the TL_OR are a different option. Overloading.
     * {param} - a param
     */

    private static ActionType fromJSON(JSONObject actionJSON) {
        // Set ActionType's name
        String name = DBCUtils.stripColors(actionJSON.getString("name"));

        // Set ActionType's matching codeblock
        String blockName = actionJSON.getString("codeblockName");
        BlockType blockType = BlockType.byName(blockName);

        JSONObject iconObject = actionJSON.getJSONObject("icon");

        // We are not sure whether the json even HAS the cancellable tag,
        // so we check that first
        boolean cancellable =
                actionJSON.has("cancellable")
                && iconObject.getBoolean("cancellable");

        // We are also not sure that sub-action blocks exist for this action
        List<BlockType> subActionBlocks;
        if (actionJSON.has("subActionBlocks")) {
            // Create the sub action blocks array list
            subActionBlocks = new ArrayList<>();

            for (Object oSBB : actionJSON.getJSONArray("subActionBlocks")) {
                String sbbID = (String) oSBB;
                BlockType curBT = BlockType.byID(sbbID);
                subActionBlocks.add(curBT);
            }
        } else
            subActionBlocks = null;

        // Create return value's possibe types
        TypeSet types = actionReturnValuesFromJSON(actionJSON);

        // Create a list of possible tags
        List<String> tagNames = new ArrayList<>();
        if (actionJSON.has("tags")) {
            for (Object oTag : actionJSON.getJSONArray("tags")) {
                JSONObject tagObj = (JSONObject) oTag;
                tagNames.add(tagObj.getString("name"));
            }
        }

        return new ActionType(name, blockType, types, cancellable, subActionBlocks, tagNames);
    }

    private static TypeSet actionReturnValuesFromJSON(JSONObject actionObject) {
        TypeSet types;
        JSONObject iconObject = actionObject.getJSONObject("icon");
        if (!iconObject.has("returnValues") || iconObject.getJSONArray("returnValues").isEmpty()) {
            return null;
        }

        types = new TypeSet();
        for (Object oRet : iconObject.getJSONArray("returnValues")) {
            JSONObject ret = (JSONObject) oRet;
            if (ret.has("type")) {
                // Add a new type
                String typeName = ret.getString("type");
                DFItem.Type type = DFItem.Type.typeNames.get(typeName);
                types.add(type);
            }
        }

        return types;
    }

    private ActionType(String name, BlockType blockType, TypeSet returnValue, boolean cancellable, List<BlockType> subActionBlocks, List<String> tagNames) {
        this.name = name;
        this.blockType = blockType;
        this.returnValues = returnValue;
        this.cancellable = cancellable;
        this.subActionBlocks = subActionBlocks;
        this.tagNames = tagNames;
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
     * Get the Action's return values' types
     * @return the action's possible return values
     */
    public TypeSet getReturnValues() {
        return returnValues;
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

    public List<String> getTagNames() {
        return tagNames;
    }
}
