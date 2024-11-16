package me.gamenu;

import me.gamenu.carbondf.values.*;

public class Main {
    public static void main(String[] args) {

        System.out.println(
                new DFParticle("SPIT")
                        .setMotion(new DFVector(1, 2, 3))
                        .setMotionVariation(0)
                        .toJSON()
                        .toString()
        );
    }
}