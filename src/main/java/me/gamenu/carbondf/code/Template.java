package me.gamenu.carbondf.code;

import me.gamenu.carbondf.blocks.Block;
import me.gamenu.carbondf.blocks.CodeBlock;
import me.gamenu.carbondf.blocks.DataBlock;
import me.gamenu.carbondf.blocks.TemplateValue;
import me.gamenu.carbondf.etc.DFBuildable;
import me.gamenu.carbondf.exceptions.CarbonRuntimeException;
import me.gamenu.carbondf.exceptions.InvalidFieldException;
import me.gamenu.carbondf.types.BlockType;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Template extends BlocksList implements DFBuildable {

    // This is here to make it impossible to init templates without the manager
    // Basically hiding the constructor for Template while leaving the constructor for BlocksList intact
    Template() {
        super();
    }

    TemplateMetadata metadata;

    Set<BlockType> validBlockTypes = Arrays.stream(TemplateType.values()).map(TemplateType::getBlockType).collect(Collectors.toSet());

    void setMetadata(TemplateMetadata metadata) {
        this.metadata = metadata;
    }

    public TemplateMetadata getMetadata() {
        return metadata;
    }

    @Override
    public JSONObject toJSON() {
        JSONArray blocksJSON = new JSONArray();

        for (TemplateValue v : this.blocks) {
            blocksJSON.put(v.toJSON());
        }

        return new JSONObject().put("blocks", blocksJSON);
    }

    /**
     * This is the same as toJSON, but performs more finishing operations
     * @return Built JSON object of the template
     */
    @Override
    public JSONObject buildJSON() {
        // Make sure the first block is a CodeBlock
        // (we can't have a bracket as the first Block)
        if (!(blocks.get(0).getCategory() == TemplateValue.Category.BLOCK
                && blocks.get(0) instanceof CodeBlock cb)
        ) {
            throw new InvalidFieldException(String.format("Block category \"%s\" is an invalid template starter type", blocks.get(0).getCategory().getId()));
        }

        // Make sure the first block is also a valid starter type
        // (we must have a valid starter block as a starter type)
        // (most of the code here is honestly just for the error message)
        if (!validBlockTypes.contains(cb.getBlock())) {
            throw new InvalidFieldException(String.format("Block ID \"%s\" is an invalid template starter type. Valid types: \"%s\"",
                    cb.getBlock().getId(), validBlockTypes
                            .stream()
                            .map(BlockType::getId)
                            .collect(Collectors.joining("\", \""))
            ));
        }

        JSONArray blocksJSON = new JSONArray();

        List<TemplateValue> templateValues = this.blocks;

        int i = 0;
        for(TemplateValue v : templateValues) {
            if (v.getCategory() == TemplateValue.Category.BLOCK) {
                i++;
            }
            try {
                blocksJSON.put(v.buildJSON());
            } catch (CarbonRuntimeException e) {
                if (v instanceof Block b)
                    throw new CarbonRuntimeException(e.getMessage(), i, b);
                else
                    throw e;
            }
        }

        return new JSONObject().put("blocks", blocksJSON);
    }

    // Overridden methods that return Templates instead of BlocksList for compat with assigning a stack to a variable
    // Might be REMOVED in a future version!
    @Override
    public Template addBlock(TemplateValue value) {
        super.addBlock(value);
        return this;
    }

    @Override
    public Template addSubList(Block block, BlocksList subList) {
        super.addSubList(block, subList);
        return this;
    }

    @Override
    public BlocksList addSubList(Block block, Supplier<BlocksList> subListSupplier) {
        super.addSubList(block, subListSupplier);
        return this;
    }

    @Override
    public Template elseBlock(BlocksList subList) {
        super.elseBlock(subList);
        return this;
    }

    @Override
    public Template elseBlock(Supplier<BlocksList> subListSupplier) {
        super.elseBlock(subListSupplier);
        return this;
    }

    public static class TemplateMetadata {
        String name;
        TemplateType templateType;

        public static TemplateMetadata fromBlock(CodeBlock block) {
            TemplateType tt = TemplateType.fromID(block.getBlock().getId());
            if (tt == null) {
                throw new InvalidFieldException(String.format("Block ID \"%s\" is an invalid template starter type. Valid types: \"%s\"",
                        block.getBlock().getId(),
                        Arrays.stream(TemplateType.values())
                                .map(type -> type.getBlockType().getId())
                                .collect(Collectors.joining("\", \""))
                ));
            }

            String templateName = (block instanceof DataBlock db)
                    ? db.getName()
                    : block.getAction().getName();

            return new TemplateMetadata(templateName, tt);
        }

        public TemplateMetadata(String name, TemplateType templateType) {
            this.name = name;
            if (templateType == null) {
                throw new InvalidFieldException("Template type cannot be null");
            }
            this.templateType = templateType;
        }

        public String getName() {
            return name;
        }

        public TemplateType getTemplateType() {
            return templateType;
        }
    }

    public enum TemplateType {
        EVENT(BlockType.byID("event")),
        ENTITY_EVENT(BlockType.byID("entity_event")),
        FUNC(BlockType.byID("func")),
        PROCESS(BlockType.byID("process")),
        ;

        final BlockType blockType;

        private static final Map<String, TemplateType> blockIDtoType = Arrays.stream(TemplateType.values())
                .collect(Collectors.toMap(
                        v -> v.getBlockType().getId(),
                        v -> v
                ));

        public static TemplateType fromID(String id) {
            return blockIDtoType.get(id);
        }

        TemplateType(BlockType blockType) {
            this.blockType = blockType;
        }

        public BlockType getBlockType() {
            return blockType;
        }
    }
}
