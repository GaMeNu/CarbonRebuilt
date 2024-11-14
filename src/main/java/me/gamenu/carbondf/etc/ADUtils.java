package me.gamenu.carbondf.etc;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class ADUtils {
    public static final JSONObject DBC;
    public static final Map<String, String> CBNameToIdentifier = new HashMap<>();

    static {
        try {
            DBC = new JSONObject(new JSONTokener(new FileReader("src/main/resources/dbc/dbc.json")));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        JSONArray codeBlocks = DBC.getJSONArray("codeblocks");
        for (Object o: codeBlocks){
            JSONObject cb = (JSONObject) o;
            String name = cb.getString("name");
            String identifier = cb.getString("identifier");
            CBNameToIdentifier.put(name, identifier);
        }
    }
}
