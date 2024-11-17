package me.gamenu.carbondf.etc;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class DBCUtils {
    public static final JSONObject DBC;
    public static final Map<String, String> CBNameToIdentifier = new HashMap<>();
    public static final Map<String, JSONObject> particleMap = new HashMap<>();
    public static final HashSet<String> potionTypes = new HashSet<>();
    public static final HashSet<String> soundTypes = new HashSet<>();
    public static final HashMap<String, JSONObject> gameValuesMap = new HashMap<>();

    /**
     * Remove colors from a DBC name that may contain colors
     * @param text colored text
     * @return text without colors
     */
    public static String stripColors(String text) {
        // Thanks DFOnline for the RegEx :+1:
        return text.replaceAll("(?i)[&§][\\dA-FK-ORX]", "");
    }

    static {
        try {
            DBC = new JSONObject(new JSONTokener(new FileReader("src/main/resources/dbc/dbc.json")));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        initCBMap();
        initParticleMap();
        initPotionsSet();
        initSoundsSet();
        initGameValuesMap();
    }

    private static void initCBMap() {
        JSONArray codeBlocks = DBC.getJSONArray("codeblocks");
        for (Object o: codeBlocks){
            JSONObject cb = (JSONObject) o;
            String name = cb.getString("name");
            String identifier = cb.getString("identifier");
            CBNameToIdentifier.put(name, identifier);
        }
    }

    private static void initParticleMap() {
        for (Object oPart : DBC.getJSONArray("particles")) {
            JSONObject part = (JSONObject) oPart;
            particleMap.put(part.getString("particle"), part);
         }
    }

    private static void initPotionsSet() {
        for (Object oPot : DBC.getJSONArray("potions")) {
            JSONObject pot = (JSONObject) oPot;
            potionTypes.add(pot.getString("potion"));
        }
    }

    private static void initSoundsSet() {
        for (Object oSnd : DBC.getJSONArray("sounds")) {
            JSONObject snd = (JSONObject) oSnd;
            soundTypes.add(snd.getString("sound"));
        }
    }

    private static void initGameValuesMap() {
        for (Object oGV : DBC.getJSONArray("gameValues")) {
            JSONObject gv = (JSONObject) oGV;
            gameValuesMap.put(stripColors(gv.getJSONObject("icon").getString("name")), gv);
        }
    }
}
