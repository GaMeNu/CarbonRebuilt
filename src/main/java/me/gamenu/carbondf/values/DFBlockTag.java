package me.gamenu.carbondf.values;

import me.gamenu.carbondf.etc.DBCUtils;
import me.gamenu.carbondf.exceptions.InvalidFieldException;
import me.gamenu.carbondf.types.ActionType;
import me.gamenu.carbondf.types.BlockType;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DFBlockTag extends DFItem{

    static Map<ActionType, Map<String, DFBlockTag>> tags;

    /** {@link BlockType Block} the BlockTag belongs to */
    BlockType blockType;
    /** {@link ActionType Action} the BlockTag belongs to */
    ActionType actionType;
    /** The BlockTag's name */
    String tagName;
    /** The BlockTag's selected option */
    String option;
    /** Valid Options */
    List<String> validOptions;
    /** The BlockTag's variable (if exists) */
    DFVariable variable;

    public DFBlockTag(ActionType actionType, String tagName) {
        super(Type.BLOCK_TAG);

        Map<String, JSONObject> actionTagsMap = DBCUtils.tagsMap
                .get(actionType.getBlockType().getId())
                .get(actionType.getName());

        if (!actionTagsMap.containsKey(tagName)) {
            throw new InvalidFieldException("Invalid tag name!");
        }

        JSONObject tagObject = actionTagsMap.get(tagName);

        List<String> validOptions = new ArrayList<>();

        for (Object option : tagObject.getJSONArray("options")) {
            JSONObject optionObject = (JSONObject) option;
            validOptions.add(DBCUtils.stripColors(optionObject.getString("name")));
        }

        this.blockType = actionType.getBlockType();
        this.actionType = actionType;
        this.tagName = tagName;
        this.validOptions = validOptions;
        this.option = tagObject.getString("defaultOption");
    }

    public BlockType getBlockType() {
        return blockType;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public String getTagName() {
        return tagName;
    }

    public String getOption() {
        return option;
    }

    public List<String> getValidOptions() {
        return validOptions;
    }

    public DFBlockTag setOption(String option) {
        if (!validOptions.contains(option)) {
            throw new InvalidFieldException("Invalid option \"" + option + "\" for tag \"" + tagName + "\". " +
                    "Valid options: \"" + String.join("\", \"", validOptions) + "\"");
        }
        this.option = option;
        return this;
    }

    private DFBlockTag setOptionVariable(DFVariable variable) {
        this.variable = variable;
        return this;
    }

    public DFBlockTag setVariable(DFVariable variable) {
        if (!variable.getType().contains(Type.ANY) && !variable.getType().contains(Type.STRING)) {
            throw new InvalidFieldException("Variable is not of types " + new TypeSet(Type.ANY, Type.STRING));
        }
        return setOptionVariable(variable);
    }

    @Override
    public JSONObject toJSON() {
        JSONObject data = new JSONObject()
                .put("action", actionType.getName())
                .put("block", actionType.getBlockType().getId())
                .put("tag", tagName)
                .put("option", option);

        if (variable != null) {
            data.put("variable", variable.toJSON());
        }

        return createJSONFromData(data);
    }
}
