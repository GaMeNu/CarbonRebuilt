package me.gamenu.carbondf.code;

import me.gamenu.carbondf.blocks.*;
import me.gamenu.carbondf.etc.DFBuildable;
import me.gamenu.carbondf.exceptions.CarbonRuntimeException;
import me.gamenu.carbondf.exceptions.InvalidBlockException;
import me.gamenu.carbondf.exceptions.InvalidFieldException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Template extends BlocksList implements DFBuildable {

    // This is here to make it impossible to init templates without the manager
    // Basically hiding the constructor for Template while leaving the constructor for BlocksList intact
    Template() {
        super();
    }

    TemplateManager tm;
    TemplateMetadata metadata;

    /**
     * Template metadata of the template, including its type, name, etc.
     * @param metadata TemplateMetadata to set
     */
    void setMetadata(TemplateMetadata metadata) {
        this.metadata = metadata;
    }

    /**
     * Used for checking func calls and their existence
     * @param tm TemplateManager that owns this template
     */
    void setTemplateManager(TemplateManager tm) {
        this.tm = tm;
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
        // TODO: This is bad -- verification and building the JSON itself are done in the same pass.
        verifyStarterBlock();

        JSONArray blocksJSON = new JSONArray();

        List<TemplateValue> templateValues = this.blocks;

        int i = 0;
        for(TemplateValue v : templateValues) {
            if (v.getCategory() == TemplateValue.Category.BLOCK) {
                i++;
            }

            if (v instanceof IDataBlock cb) {
                verifyTemplateCalls(cb, i);
            }

            try {
                blocksJSON.put(v.buildJSON());
            } catch (CarbonRuntimeException e) {
                if (v instanceof IBlock b)
                    throw new CarbonRuntimeException(e.getMessage(), i, b, e);
                else
                    throw e;
            }
        }

        return new JSONObject().put("blocks", blocksJSON);
    }

    private void verifyStarterBlock() {
        // Make sure the first block is a CodeBlock
        // (we can't have a bracket as the first Block)
        if (
                !(blocks.get(0).getCategory() == TemplateValue.Category.BLOCK
                        && blocks.get(0) instanceof IBlock cb)
        ) {
            throw new InvalidBlockException(String.format("Block category \"%s\" is an invalid template starter type", blocks.get(0).getCategory().getId()));
        }
        // Make sure the first block is also a valid starter type
        // (we must have a valid starter block as a starter type)
        // (most of the code here is honestly just for the error message)
        if (!templateStarters.contains(cb.getBlock())) {
            throw new InvalidBlockException(String.format("Block ID \"%s\" is an invalid template starter type. Valid types: \"%s\"",
                    cb.getBlock().getId(), templateStarters
                            .stream()
                            .map(BlockType::getId)
                            .collect(Collectors.joining("\", \""))
            ));
        }
    }


    private void verifyTemplateCalls(IBlock block, int index) {
        // Make sure the block is a data block
        // (and thus is a caller. We already verify for starters in addBlock)
        if (!(block instanceof IDataBlock db)) return;

        // Make sure called template exists
        if (!tm.has(db.getName()))
            throw new InvalidBlockException(String.format("Block attempts to call non-existent template \"%s\"", db.getName()), index, db);

        // Make sure called template is of the matching type
        TemplateMetadata metadata = tm.get(db.getName()).getMetadata();
        BlockType templateBT = metadata.getTemplateType().getBlockType();
        BlockType callerBT = db.getBlock();
        if (!templateBT.equals(callToTemplates.get(callerBT)))
            throw new InvalidBlockException(String.format("Block attempts to call template \"%s\" of type %s, but is of type %s (which calls type %s)",
                    db.getName(), templateBT.getId(), callerBT.getId(), callToTemplates.get(callerBT).getId())
            );

        if (templateBT.equals(TemplateType.FUNC.getBlockType())) {
            if (!(block instanceof CallFuncBlock caller))
                throw new InvalidBlockException("CallFunc block isn't a DataBlock");
            metadata.getParamsContainer().verifyArgs(caller.getArgs());
        }


    }

    // Overridden methods that return Templates instead of BlocksList for compat with assigning a stack to a variable
    // Might be REMOVED in a future version!
    @Override
    public Template addBlock(TemplateValue value) {
        super.addBlock(value);
        return this;
    }

    @Override
    public Template addSubList(IBlock block, BlocksList subList) {
        super.addSubList(block, subList);
        return this;
    }

    @Override
    public BlocksList addSubList(IBlock block, Supplier<BlocksList> subListSupplier) {
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
        FuncBlock.ParamsContainer paramsContainer;

        public static TemplateMetadata fromBlock(FuncBlock block) {
            TemplateType tt = TemplateType.fromID(block.getBlock().getId());
            if (tt == null) {
                throw new InvalidBlockException(String.format("Block ID \"%s\" is an invalid template starter type. Valid types: \"%s\"",
                        block.getBlock().getId(),
                        Arrays.stream(TemplateType.values())
                                .map(type -> type.getBlockType().getId())
                                .collect(Collectors.joining("\", \""))
                ));
            }
            if (tt != TemplateType.FUNC) {
                throw new InvalidBlockException("FuncBlocks must be of type FUNC");
            }

            String templateName = block.getName();


            TemplateMetadata res = new TemplateMetadata(templateName, tt);
            res.setParamsContainer(block.getArgs());
            return res;

        }

        public static TemplateMetadata fromBlock(CodeBlock block) {
            TemplateType tt = TemplateType.fromID(block.getBlock().getId());
            if (tt == null) {
                throw new InvalidBlockException(String.format("Block ID \"%s\" is an invalid template starter type. Valid types: \"%s\"",
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

        public void setParamsContainer(FuncBlock.ParamsContainer paramsContainer) {
            this.paramsContainer = paramsContainer;
        }

        public String getName() {
            return name;
        }

        public TemplateType getTemplateType() {
            return templateType;
        }

        public FuncBlock.ParamsContainer getParamsContainer() {
            return paramsContainer;
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
