package me.gamenu.carbondf.blocks;

import me.gamenu.carbondf.etc.DFBuildable;
import me.gamenu.carbondf.values.DFItem;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Set;

interface IBlockArgs extends DFBuildable {
    int ARGS_CONTAINER_SIZE = 27;

    /**
     * Constructs a {@link List<DFItem>} which has all items, including tags.
     * @return the new list
     */
    List<DFItem> getItemsList();

    /**
     * Gets all valid arg types for this BlockArgs object
     * @return valid arg types for this BlockArgs object
     */
    Set<DFItem.Type> getValidArgsTypes();

    TagsContainer tags();


    default JSONObject toJSON() {

        JSONArray items = new JSONArray();
        List<DFItem> itemsList = this.getItemsList();

        // Add all args
        for (int i = 0; i < itemsList.size(); i++) {
            DFItem arg = itemsList.get(i);
            if (arg != null) {
                JSONObject item = new JSONObject()
                        .put("item", arg.toJSON())
                        .put("slot", i);
                items.put(item);
            }
        }

        return new JSONObject()
                .put("items", items);
    }
}
