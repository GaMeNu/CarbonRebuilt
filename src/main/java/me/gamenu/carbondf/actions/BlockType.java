package me.gamenu.carbondf.actions;

import me.gamenu.carbondf.etc.DBCUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class BlockType {

    private static final HashMap<String, BlockType> blockTypes = new HashMap<>();

    public static HashMap<String, BlockType> getBlockTypes() {
        return blockTypes;
    }

    public static BlockType fromID(String id){
        return blockTypes.get(id);
    }


    private final String id;
    private final String name;

    private BlockType(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    static {

        JSONArray codeblocks;

        codeblocks = DBCUtils.DBC.getJSONArray("codeblocks");

        for (Object o: codeblocks){
            JSONObject codeblock = (JSONObject) o;
            String name = codeblock.getString("name");
            String id = codeblock.getString("identifier");

            blockTypes.put(id, new BlockType(id, name));
        }

    }
}
