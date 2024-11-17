package me.gamenu;

import me.gamenu.carbondf.values.*;

public class Main {
    public static void main(String[] args) {

        System.out.println(
                new DFGameValue("CPU Usage")
                        .toJSON()
                        .toString()
        );
    }
}