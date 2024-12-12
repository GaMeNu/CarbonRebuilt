package me.gamenu;

import me.gamenu.carbondf.code.ActionType;

public class Main {
    public static void main(String[] args) {

        ActionType at = ActionType.getByName("set_var", "GetSignText");
        System.out.println(at.getBlockType().getName() + "::" + at.getName());
        System.out.println(at.getReturnValues());
    }
}