package me.gamenu;

import me.gamenu.carbondf.blocks.CodeBlock;
import me.gamenu.carbondf.blocks.Target;
import me.gamenu.carbondf.code.BlocksList;
import me.gamenu.carbondf.code.TemplateManager;
import me.gamenu.carbondf.values.*;

public class Main {
    public static void main(String[] args) {
        TemplateManager tm = new TemplateManager();
        VarManager vm = tm.vars();

        tm
                .create(new CodeBlock("event", "Join"))
                .addBlock(new CodeBlock("player_action", "SendMessage"))
                .addBlock(new CodeBlock("set_var", "=")
                        .addItem(vm.typed("foo", DFVariable.Scope.GLOBAL, DFItem.Type.NUMBER))
                        .addItem(new DFNumber(100))
                )
                .addSubList(new CodeBlock("if_var", "=")
                                .addItem(vm.get("foo"))
                                .addItem(new DFNumber(1)),
                        new BlocksList()
                                .addBlock(
                                        new CodeBlock("player_action", "SendMessage")
                                                .addItem(new DFStyledText("TRUE"))
                                )
                )
                .elseBlock(new BlocksList()
                        .addBlock(
                                new CodeBlock("player_action", "SendMessage")
                                        .addItem(new DFStyledText("FALSE!!! Prepare for your death >:)"))
                        )
                        .addSubList(new CodeBlock("repeat", "While")
                                        .setSubAction("if_var", ">")
                                        .addItem(vm.get("foo"))
                                        .addItem(new DFNumber(1)),
                                new BlocksList()
                                        .addBlock(new CodeBlock("game_action", "Lightning")
                                                .addItem(new DFGameValue("Location", Target.DEFAULT))
                                        )
                                        .addBlock(new CodeBlock("player_action", "SendMessage")
                                                .addItem(new DFStyledText("Thou hast been <b>smitten</b>!"))
                                                .setTagOption("Alignment Mode", "Centered")
                                        )
                                        .addBlock(new CodeBlock("set_var", "-=")
                                                .addItem(vm.get("foo"))
                                                .addItem(new DFNumber(1))
                                        )
                        )
                )
                .addBlock(
                        new CodeBlock("set_var", "AlignLoc")
                                .setTagOption("Rotation", "Remove rotation")
                                .addItem(vm.typed("loc", DFVariable.Scope.LINE, DFItem.Type.LOCATION))
                                .addItem(new DFGameValue("Location", Target.DEFAULT))
                );

        System.out.println(tm.get("Join").buildJSON().toString(0));
    }

}