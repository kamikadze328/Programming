package com.kamikadze328.vk;

import java.util.Objects;

public class Morra extends Creature {
    protected int temperature;
    Morra(String n, Location l){
        super(n,l);
    }
    Morra(String n, Location l, String p) {
        super(n, l, p);
    }
    Morra(String n, Location l, String p, int t){
        super(n,l,p);
        temperature = t;
    }

    public int getTemperature() {
        return temperature;
    }

}
