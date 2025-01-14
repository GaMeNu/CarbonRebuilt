package me.gamenu.carbondf.blocks;

import me.gamenu.carbondf.etc.DFBuildable;
import me.gamenu.carbondf.exceptions.ArgsOverflowException;
import me.gamenu.carbondf.exceptions.InvalidItemException;
import me.gamenu.carbondf.values.DFItem;
import me.gamenu.carbondf.values.DFVariable;
import org.json.JSONObject;

import java.util.*;

public class CodeBlock implements IBlockData, IArgsBlock {
    BlockData data;
    ArgsContainer args;

    public CodeBlock(String blockID, String actionName) {
        this(ActionType.byName(blockID, actionName));
        this.args = new ArgsContainer(getAction());
    }

    public CodeBlock(ActionType action) {
        this(action.getBlockType(), action);
        this.args = new ArgsContainer(getAction());
    }

    public CodeBlock(BlockType block, ActionType action) {
        this.data = new BlockData(block, action);
        this.args = new ArgsContainer(getAction());
    }

    @Override
    public CodeBlock setAttribute(IBlock.Attribute attribute) {
        this.data.setAttribute(attribute);
        return this;
    }

    public CodeBlock setTagOption(String name, String option) {
        this.args.tags().getTag(name).setOption(option);
        return this;
    }

    public CodeBlock setTagVariable(String name, DFVariable variable) {
        this.args.tags().getTag(name).setVariable(variable);
        return this;
    }

    @Override
    public CodeBlock setSubAction(ActionType subAction) {
        this.data.setSubAction(subAction);
        return this;
    }

    @Override
    public Target getTarget() {
        return null;
    }

    @Override
    public CodeBlock setTarget(Target target) {
        this.data.setTarget(target);
        return this;
    }

    @Override
    public IBlock.Attribute getAttribute() {
        return null;
    }

    @Override
    public CodeBlock setSubAction(String blockID, String actionName) {
        this.data.setSubAction(ActionType.byName(blockID, actionName));
        return this;
    }

    @Override
    public ArgsContainer getArgs() {
        return this.args;
    }

    public CodeBlock addItem(DFItem item) {
        this.args.addItem(item);
        return this;
    }

    @Override
    public BlockType getBlock() {
        return this.data.getBlock();
    }

    @Override
    public ActionType getAction() {
        return this.data.getAction();
    }

    @Override
    public ActionType getSubAction() {
        return this.data.getSubAction();
    }

    @Override
    public Category getCategory() {
        return this.data.getCategory();
    }

    @Override
    public JSONObject toJSON() {
        JSONObject res = new JSONObject()
                .put("id", getCategory().getId())
                .put("block", getBlock().getId())
                .put("action", getAction().getName())
                .put("args", args.toJSON());

        if (this.getTarget() != null) {
            res.put("target", this.getTarget().getId());
        }

        if (this.getSubAction() != null) {
            res.put("subAction", this.getSubAction().getName());
        }

        if (this.getAttribute() != null) {
            res.put("attribute", this.getAttribute().getId());
        }

        return res;
    }

    public static class ArgsContainer implements IBlockArgs, DFBuildable {
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
                DFItem.Type.VARIABLE
        ));

        List<DFItem> items;
        TagsContainer tags;
        int maxSize;


        /**
         * Create new code block tags.<br/>
         * this automatically includes the action's matching args;
         * @param actionType Action type to generate
         */
        public ArgsContainer(ActionType actionType) {
            this.tags = new TagsContainer(actionType);

            this.maxSize = ARGS_CONTAINER_SIZE - this.tags.size();

            this.items = new ItemsContainer(maxSize);
        }

        public List<DFItem> items() {
            return items;
        }

        public TagsContainer tags() {
            return tags;
        }

        public ArgsContainer addItem(DFItem item) {
            if (items.size() == maxSize)
                throw new ArgsOverflowException("Too many arguments. Chest length exceeded.");

            items.add(item);
            return this;
        }

        @Override
        public List<DFItem> getItemsList() {
            List<DFItem> res = new ArrayList<>(items);

            int firstTagIndex = maxSize;
            for (int i = res.size(); i < firstTagIndex; i++)
                res.add(null);

            res.addAll(tags().buildList());
            return res;
        }

        @Override
        public Set<DFItem.Type> getValidArgsTypes() {
            return VALID_ARGS_TYPES;
        }


        /**
         * Basically a fancy {@link List < DFItem > List<DFItem>} with some extra safety checks
         */
        static class ItemsContainer implements List<DFItem> {
            final int maxSize;
            List<DFItem> items;

            public ItemsContainer(int maxSize) {
                this.maxSize = maxSize;
                this.items = new ArrayList<>();
            }

            public boolean isValidItem(DFItem item) {
                return ArgsContainer.VALID_ARGS_TYPES.contains(item.getRealType());
            }

            public void throwValidItem(DFItem item) {
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


            public boolean add(DFItem item) {
                throwValidItem(item);

                if (items.size() == maxSize)
                    throw new ArgsOverflowException("Too many arguments. Chest length exceeded.");

                return items.add(item);
            }


            public void add(int index, DFItem element) {
                throwValidItem(element);

                if (items.size() == maxSize)
                    throw new ArgsOverflowException("Too many arguments. Chest length exceeded.");

                items.add(index, element);
            }


            public boolean remove(Object o) {
                return items.remove(o);
            }


            public boolean containsAll(Collection<?> c) {
                //noinspection SlowListContainsAll
                return items.containsAll(c);
            }


            public boolean addAll(Collection<? extends DFItem> c) {
                c.forEach(this::throwValidItem);

                if (items.size() + c.size() > maxSize)
                    throw new ArgsOverflowException("Too many arguments. Chest length exceeded.");

                return items.addAll(c);
            }


            public boolean addAll(int index, Collection<? extends DFItem> c) {
                c.forEach(this::throwValidItem);

                if (items.size() + c.size() > maxSize)
                    throw new ArgsOverflowException("Too many arguments. Chest length exceeded.");

                return items.addAll(index, c);
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

    }
}
