package me.gamenu.carbondf.code;

import me.gamenu.carbondf.values.VarManager;

public class TemplateManager {
    VarManager vm;

    public TemplateManager() {
        this.vm = new VarManager();
    }

    public VarManager vars() {
        return vm;
    }

    public Template create() {
        // Starting a new template - all LINE scopes are gone
        vm.clearLineScope();
        return new Template();
    }
}

