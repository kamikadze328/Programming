package com.kamikadze328.vk;

import java.util.Objects;

public class Hattifatteners extends Creature {
    protected int electricCharge;

    Hattifatteners(String n, Location l) {
        super(n, l);
    }

    Hattifatteners(String n, Location l, String p) {
        super(n, l, p);
    }

    Hattifatteners(String n, Location l, String p, int e) {
        super(n, l, p);
        electricCharge = e;
    }


    public int getElectricCharge() {
        return electricCharge;
    }
}

