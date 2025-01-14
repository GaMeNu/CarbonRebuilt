package me.gamenu;

import me.gamenu.carbondf.blocks.BlockFactory;
import me.gamenu.carbondf.code.TemplateManager;
import me.gamenu.carbondf.values.*;

public class Main {
    public static void main(String[] args) {
        TemplateManager tm = new TemplateManager();
        VarManager vm = tm.vars();

        tm
                .create(
                        BlockFactory.funcBlock("foo")
                                .addInputParams(
                                        new DFParameter.Builder(vm, "num")
                                                .setPlural(true)
                                                .build()
                                )
                                .addReturnParams(
                                        new DFParameter.Builder(vm, "res", DFItem.Type.NUMBER)
                                                .setReturned(true)
                                                .build()
                                )
                );

        tm
                .create(
                        BlockFactory.codeBlock("event", "Join")
                )
                .addBlock(
                        BlockFactory.callFuncBlock("foo")
                                .addInputs(
                                        new DFNumber(1),
                                        new DFNumber(2),
                                        new DFString("3"),
                                        new DFString("4")
                                )
                                .addReturnVars(
                                        vm.typed("res", DFVariable.Scope.LINE, DFItem.Type.NUMBER)
                                )
                );


        System.out.println(tm.get("Join").buildJSON().toString(0));
    }

}