package me.gamenu.carbondf.blocks;

import me.gamenu.carbondf.values.DFBlockTag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TagsContainer {
    List<String> orderedTagNames;

    Map<String, DFBlockTag> tags;

    public TagsContainer(ActionType actionType) {
        this.orderedTagNames = new ArrayList<>();
        this.tags = new HashMap<>();

        for (String tagName : actionType.getTagNames()) {
            this.orderedTagNames.add(tagName);
            this.tags.put(tagName, new DFBlockTag(actionType, tagName));
        }
    }

    /**
     * Get a specific tag by its name
     *
     * @param name name of the tag to get
     * @return the {@link DFBlockTag} contained in the container
     */
    public DFBlockTag getTag(String name) {
        return tags.get(name);
    }

    /**
     * Get the amount of tags
     *
     * @return the amount of tags in the container
     */
    public int size() {
        return tags.size();
    }

    /**
     * Get an ordered list of all item tags
     *
     * @return the list of all tags
     */
    public List<DFBlockTag> buildList() {
        return orderedTagNames
                .stream()
                .map(name -> tags.get(name))
                .toList();
    }


}
