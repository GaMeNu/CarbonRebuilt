package me.gamenu;

import me.gamenu.carbondf.blocks.BlockFactory;
import me.gamenu.carbondf.blocks.Target;
import me.gamenu.carbondf.code.BlocksList;
import me.gamenu.carbondf.code.TemplateManager;
import me.gamenu.carbondf.values.*;

public class Main {
    public static void main(String[] args) {
        TemplateManager tm = new TemplateManager();
        VarManager vm = tm.vars();

        tm
                .create(BlockFactory.codeBlock("event", "Join"))
                .addBlock(BlockFactory.codeBlock("player_action", "SendMessage"))
                .addBlock(BlockFactory.codeBlock("set_var", "=")
                        .addItem(vm.typed("foo", DFVariable.Scope.GLOBAL, DFItem.Type.NUMBER))
                        .addItem(new DFNumber(100))
                )
                .addSubList(BlockFactory.codeBlock("if_var", "=")
                                .addItem(vm.get("foo"))
                                .addItem(new DFNumber(1)),
                        new BlocksList()
                                .addBlock(
                                        BlockFactory.codeBlock("player_action", "SendMessage")
                                                .addItem(new DFStyledText("TRUE"))
                                )
                )
                .elseBlock(new BlocksList()
                        .addBlock(
                                BlockFactory.codeBlock("player_action", "SendMessage")
                                        .addItem(new DFStyledText("FALSE!!! Prepare for your death >:)"))
                        )
                        .addSubList(BlockFactory.codeBlock("repeat", "While")
                                        .setSubAction("if_var", ">")
                                        .addItem(vm.get("foo"))
                                        .addItem(new DFNumber(1)),
                                new BlocksList()
                                        .addBlock(BlockFactory.codeBlock("game_action", "Lightning")
                                                .addItem(new DFGameValue("Location", Target.DEFAULT))
                                        )
                                        .addBlock(BlockFactory.codeBlock("player_action", "SendMessage")
                                                .addItem(new DFStyledText("Thou hast been <b>smitten</b>!"))
                                                .setTagOption("Alignment Mode", "Centered")
                                        )
                                        .addBlock(BlockFactory.codeBlock("set_var", "-=")
                                                .addItem(vm.get("foo"))
                                                .addItem(new DFNumber(1))
                                        )
                        )
                )
                .addBlock(
                        BlockFactory.codeBlock("set_var", "AlignLoc")
                                .setTagOption("Rotation", "Remove rotation")
                                .addItem(vm.typed("loc", DFVariable.Scope.LINE, DFItem.Type.LOCATION))
                                .addItem(new DFGameValue("Location", Target.DEFAULT))
                )
                .addBlock(BlockFactory.dataBlock("start_process", "womp"));

        tm.create(BlockFactory.dataBlock("process", "womp"));
        System.out.println(tm.get("Join").buildJSON().toString(0));
    }

}