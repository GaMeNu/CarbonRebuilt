package me.gamenu;

import me.gamenu.carbondf.code.CodeBlock;
import me.gamenu.carbondf.values.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        CodeBlock cb = new CodeBlock("player_action", "SendMessage");
        cb.items().addAll(List.of(new DFStyledText("<b>Hello world!</b> This is a number:"), new DFNumber(1)));
        cb.tags().getTag("Alignment Mode")
                .setOption("Centered")
                .setVariable(
                        DFVariable.dynamic("womp", DFVariable.Scope.SAVED)
                );

        System.out.println(cb.toJSON().toString(2));
    }
}