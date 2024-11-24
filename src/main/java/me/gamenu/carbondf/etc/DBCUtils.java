package me.gamenu.carbondf.etc;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This class contains utilities for interacting with data from the ActionDump.
 * The class mostly consists of useful public maps and sets, that contain useful data for different objects and the such.
 * Usually this data will be wrapped further behind a matching class or another map
 */
public class DBCUtils {
    /** This is the ActionDump, loaded into memory and converted to a Java JSON Format*/
    public static final JSONObject DBC;
    /** This is a Bi-Directional HashMap, containing all BlockTypes' names and identifiers, being able to switch between them. */
    public static final BidiMap<String, String> codeBlockTypes = new DualHashBidiMap<>();
    /** This is a Map containing all codeBlock IDs, and their matching Action types, by name.<br/>
     * <code>Map< String codeBlockID, Map< String actionName, JSONObject actionData > ></code>
     */
    public static final Map<String, Map<String, JSONObject>> actionTypes = new HashMap<>();
    /** This Map contains each particle's ID, and its associated data (in JSON format) */
    public static final Map<String, JSONObject> particleMap = new HashMap<>();
    /** This Set contains all valid Potion types */
    public static final Set<String> potionTypes = new HashSet<>();
    /** This Set contains all valid Sound types*/
    public static final Set<String> soundTypes = new HashSet<>();
    /** This Map contains each valid Game Value's name, and its associated data (in JSON format) */
    public static final Map<String, JSONObject> gameValuesMap = new HashMap<>();


    /**
     * Remove colors from a DBC name that may contain colors
     * @param text colored text
     * @return text without colors
     */
    public static String stripColors(String text) {
        // Thanks DFOnline for the RegEx :thumbsup:
        return text.replaceAll("(?i)[&§][\\dA-FK-ORX]", "");
    }

    static {
        try {
            DBC = new JSONObject(new JSONTokener(new FileReader("src/main/resources/dbc/dbc.json")));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        initParticleMap();
        initPotionsSet();
        initSoundsSet();
        initGameValuesMap();

        initCodeBlockTypes();
        initActionTypes();
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

    private static void initCodeBlockTypes() {
        for (Object oCB : DBC.getJSONArray("codeblocks")) {
            JSONObject cb = (JSONObject) oCB;
            codeBlockTypes.put(cb.getString("identifier"), cb.getString("name"));
        }
    }

    private static void initActionTypes() {
        for (Object oAT : DBC.getJSONArray("actions")) {
            JSONObject at = (JSONObject) oAT;
            // Prep codeBlock ID
            String cbID = codeBlockTypes.inverseBidiMap().get(at.getString("codeblockName"));
            // Prep action name)]
            String actionName = at.getString("name");

            // Create key if it does not exist
            if (!actionTypes.containsKey(cbID))
                actionTypes.put(cbID, new HashMap<>());

            // Place the action data
            actionTypes.get(cbID).put(actionName, at);
        }
    }
}
