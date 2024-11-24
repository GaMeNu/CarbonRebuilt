package me.gamenu;

import me.gamenu.carbondf.code.ActionType;

public class Main {
    public static void main(String[] args) {

        System.out.println(
                ActionType.fromName("set_var", "=").getBlockType().getName()
        );
    }
}