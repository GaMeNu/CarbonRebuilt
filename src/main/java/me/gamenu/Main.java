package me.gamenu;

import me.gamenu.carbondf.blocks.CodeBlock;
import me.gamenu.carbondf.code.BlocksList;
import me.gamenu.carbondf.blocks.Target;
import me.gamenu.carbondf.code.Template;
import me.gamenu.carbondf.values.*;

public class Main {
    public static void main(String[] args) {
        Template template = new Template();

        template
                .addBlock(new CodeBlock("player_action", "SendMessage"))
                .addBlock(new CodeBlock("set_var", "=")
                        .addItem(DFVariable.typed("foo", DFVariable.Scope.GLOBAL, new TypeSet(DFItem.Type.NUMBER)))
                        .addItem(new DFNumber(100))
                )

                .addSubList(new CodeBlock("if_var", "=")
                                .addItem(DFVariable.get("foo"))
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
                                        .addItem(DFVariable.get("foo"))
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
                                                .addItem(DFVariable.get("foo"))
                                                .addItem(new DFNumber(1))
                                        )
                        )
                );

        System.out.println(template.build().toString(0));
    }

}