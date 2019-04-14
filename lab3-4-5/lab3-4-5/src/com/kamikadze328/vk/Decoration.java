package com.kamikadze328.vk;

public class Decoration extends Actor {
    protected int size;
    protected int height;
    Decoration(String n){
        super(n);
    }
    Decoration(String n, Location l){
        super(n,l);
    }
    Decoration(String n, Location l, int s, int h){
        super(n,l);
        size = s;
        height = h;
    }
}
