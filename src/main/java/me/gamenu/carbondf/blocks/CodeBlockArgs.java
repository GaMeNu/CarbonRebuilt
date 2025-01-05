package me.gamenu.carbondf.blocks;

import me.gamenu.carbondf.etc.DFBuildable;
import me.gamenu.carbondf.exceptions.ArgsOverflowException;
import me.gamenu.carbondf.exceptions.InvalidItemException;
import me.gamenu.carbondf.types.ActionType;
import me.gamenu.carbondf.values.DFBlockTag;
import me.gamenu.carbondf.values.DFItem;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;


public class CodeBlockArgs implements DFBuildable {
    public final int ARGS_CONTAINER_SIZE = 27;
    // TODO: This is not good enough for functions (DFParameter),
    //       will have to add check for FUNCTION mode (only DFParameters)
    public static Set<DFItem.Type> VALID_ARGS_TYPES = new HashSet<>(List.of(
            DFItem.Type.NUMBER,
            DFItem.Type.STYLED_TEXT,
            DFItem.Type.STRING,
            DFItem.Type.LOCATION,
            DFItem.Type.VECTOR,
            DFItem.Type.SOUND,
            DFItem.Type.PARTICLE,
            DFItem.Type.POTION,
            DFItem.Type.ITEM,
            DFItem.Type.GAME_VALUE,
            DFItem.Type.VARIABLE,

            DFItem.Type.PARAMETER
    ));

    ItemsContainer items;
    TagsContainer tags;


    /**
     * Create new code block tags.<br/>
     * this automatically includes the action's matching args;
     * @param actionType Action type to generate
     */
    public CodeBlockArgs(ActionType actionType) {
        // TODO: paramsMode
        this.tags = new TagsContainer(actionType);

        int max_args_size = ARGS_CONTAINER_SIZE - this.tags.size();

        this.items = new ItemsContainer(max_args_size);
    }

    public ItemsContainer items() {
        return items;
    }

    public TagsContainer tags() {
        return tags;
    }

    @Override
    public JSONObject toJSON() {
        JSONArray items = new JSONArray();
        // Add all args
        for (int i = 0; i < this.items.size(); i++) {
            DFItem arg = this.items.get(i);
            if (arg != null) {
                JSONObject item = new JSONObject()
                        .put("item", arg.toJSON())
                        .put("slot", i);
                items.put(item);
            }
        }
        // Add tags at the end of the barrel (by order)
        for (int i = this.items.getMaxSize(); i < ARGS_CONTAINER_SIZE; i++) {
            DFBlockTag tag = tags.buildList().get(i - this.items.getMaxSize());
            if (tag != null) {
                JSONObject item = new JSONObject()
                        .put("item", tag.toJSON())
                        .put("slot", i);
                items.put(item);
            }
        }

        return new JSONObject()
                .put("items", items);
    }

    /**
     * Basically a fancy {@link List<DFItem> List<DFItem>} with some extra safety checks
     */
    public static class ItemsContainer {
        final int maxSize;
        List<DFItem> items;

        public ItemsContainer(int max_size) {
            this.maxSize = max_size;
            this.items = new ArrayList<>();
        }

        private boolean isValidItem(DFItem item) {
            return CodeBlockArgs.VALID_ARGS_TYPES.contains(item.getRealType());
        }

        private void throwValidItem(DFItem item) {
            if (!isValidItem(item)) {
                throw new InvalidItemException("Item of type [" + item.getRealType() + "] cannot be used as an argument");
            }
        }

        public int getMaxSize() {
            return maxSize;
        }

        public int size() {
            return items.size();
        }


        public boolean isEmpty() {
            return items.isEmpty();
        }


        public boolean contains(Object o) {
            return items.contains(o);
        }


        public Iterator<DFItem> iterator() {
            return items.iterator();
        }


        public Object[] toArray() {
            return items.toArray();
        }


        public <T> T[] toArray(T[] a) {
            return items.toArray(a);
        }


        public ItemsContainer add(DFItem item) {
            throwValidItem(item);

            if (items.size() == maxSize)
                throw new ArgsOverflowException("Too many arguments. Chest length exceeded.");

            items.add(item);
            return this;
        }


        public ItemsContainer add(int index, DFItem element) {
            throwValidItem(element);

            if (items.size() == maxSize)
                throw new ArgsOverflowException("Too many arguments. Chest length exceeded.");

            items.add(index, element);
            return this;
        }


        public ItemsContainer remove(Object o) {
            items.remove(o);
            return this;
        }


        public boolean containsAll(Collection<?> c) {
            //noinspection SlowListContainsAll
            return items.containsAll(c);
        }


        public ItemsContainer addAll(Collection<? extends DFItem> c) {
            c.forEach(this::throwValidItem);

            if (items.size() + c.size() > maxSize)
                throw new ArgsOverflowException("Too many arguments. Chest length exceeded.");

            items.addAll(c);
            return this;
        }


        public ItemsContainer addAll(int index, Collection<? extends DFItem> c) {
            c.forEach(this::throwValidItem);

            if (items.size() + c.size() > maxSize)
                throw new ArgsOverflowException("Too many arguments. Chest length exceeded.");

            items.addAll(index, c);
            return this;
        }


        public boolean removeAll(Collection<?> c) {
            return items.removeAll(c);
        }


        public boolean retainAll(Collection<?> c) {
            return items.retainAll(c);
        }


        public void clear() {
            items.clear();
        }


        public DFItem get(int index) {
            return items.get(index);
        }


        public DFItem set(int index, DFItem element) {
            if (!isValidItem(element)) {
                return null;
            }

            return items.set(index, element);
        }


        public DFItem remove(int index) {
            return items.remove(index);
        }


        public int indexOf(Object o) {
            return items.indexOf(o);
        }


        public int lastIndexOf(Object o) {
            return items.lastIndexOf(o);
        }


        public ListIterator<DFItem> listIterator() {
            return items.listIterator();
        }


        public ListIterator<DFItem> listIterator(int index) {
            return items.listIterator(index);
        }


        public List<DFItem> subList(int fromIndex, int toIndex) {
            return items.subList(fromIndex, toIndex);
        }
    }

    public static class TagsContainer {
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
         * @param name name of the tag to get
         * @return the {@link DFBlockTag} contained in the container
         */
        public DFBlockTag getTag(String name) {
            return tags.get(name);
        }

        /**
         * Get the amount of tags
         * @return the amount of tags in the container
         */
        public int size() {
            return tags.size();
        }

        /**
         * Get an ordered list of all item tags
         * @return the list of all tags
         */
        public List<DFBlockTag> buildList() {
            return orderedTagNames
                    .stream()
                    .map(name -> tags.get(name))
                    .toList();
        }

    }

}