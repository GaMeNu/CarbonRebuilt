package me.gamenu;

import me.gamenu.carbondf.values.DFItem;
import me.gamenu.carbondf.values.DFLocation;
import me.gamenu.carbondf.values.DFParameter;

public class Main {
    public static void main(String[] args) {
        System.out.println(
                new DFParameter.Builder("dingus", DFItem.Type.LOCATION)
                        .optional()
                        .setDefaultValue(new DFLocation(5, 2, 3))
                        .build()
                        .toJSON()
                        .toString()
        );
    }
}