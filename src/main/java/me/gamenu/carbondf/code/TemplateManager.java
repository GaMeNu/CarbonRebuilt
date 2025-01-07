package me.gamenu.carbondf.code;

import me.gamenu.carbondf.blocks.CodeBlock;
import me.gamenu.carbondf.exceptions.DuplicateEntryException;
import me.gamenu.carbondf.values.VarManager;

import java.util.HashMap;
import java.util.Map;

public class TemplateManager {

    Map<String, Template> templates = new HashMap<>();

    VarManager vm;

    public TemplateManager() {
        this.vm = new VarManager();
    }

    public VarManager vars() {
        return vm;
    }

    public Template get(String name) {
        return templates.get(name);
    }

    public boolean has(String name) {
        return get(name) != null;
    }

    public Template create(CodeBlock starterBlock) {
        Template.TemplateMetadata metadata = Template.TemplateMetadata.fromBlock(starterBlock);
        String tlName = metadata.getName();
        if (has(tlName)) {
            throw new DuplicateEntryException("Template with the name \"" + tlName + "\" already exists. Please use TemplateManager#get() to get the existing instance.");
        }

        // Starting a new template - all LINE scopes are gone
        vm.clearLineScope();
        Template newTemplate = new Template();
        newTemplate.setMetadata(metadata);
        newTemplate.setTemplateManager(this);

        newTemplate.add(starterBlock);

        templates.put(tlName, newTemplate);

        return newTemplate;
    }
}

