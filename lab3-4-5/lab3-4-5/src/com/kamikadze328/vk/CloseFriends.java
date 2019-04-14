package com.kamikadze328.vk;

import java.util.Objects;

public class CloseFriends  extends Creature {
    protected int intelligence;
    protected int strength;
    protected boolean tail;
    CloseFriends(String n, Location l){
        super(n,l);
    }
    CloseFriends(String n, Location l, String p) {
        super(n, l, p);
    }
    CloseFriends(String n, Location l, String p, int i, int s, boolean t){
        super(n,l,p);
        intelligence = i;
        strength = s;
        tail = t;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public int getStrength() {
        return strength;
    }
    public boolean getTail(){
        return tail;
    }

}
