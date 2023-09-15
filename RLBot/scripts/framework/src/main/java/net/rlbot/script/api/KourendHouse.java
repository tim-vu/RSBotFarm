package net.rlbot.script.api;

import net.rlbot.api.game.Vars;

public enum KourendHouse {

    ARCEUUS(4896),
    HOSIDIUS(4895),
    LOVAKENGJ(4898),
    PISCARILIUS(4899),
    SHAYZIEN(4894);

    private final int varbit;

    KourendHouse(int varbit) {

        this.varbit = varbit;
    }

    public int getVarbit() {

        return varbit;
    }

    public float getFavour() {

        return Vars.getBit(getVarbit()) / (float) 1000;
    }
}
