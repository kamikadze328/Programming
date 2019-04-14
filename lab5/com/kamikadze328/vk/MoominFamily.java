package com.kamikadze328.vk;

import java.util.Objects;

public class MoominFamily extends Creature{
    protected int waste;
    protected int care;
    protected boolean hat;
    MoominFamily(String n, Location l){
        super(n,l);
    }
    MoominFamily(String n, Location l, String p) {
        super(n, l, p);
    }
    MoominFamily(String n, Location l, String p, int w, int c, boolean h){
        super(n,l,p);
        waste = w;
        care = c;
        hat = h;
    }

    public int getCare() {
        return care;
    }
    public int getWaste() {
        return waste;
    }
    public boolean getHat(){
        return hat;
    }

}
