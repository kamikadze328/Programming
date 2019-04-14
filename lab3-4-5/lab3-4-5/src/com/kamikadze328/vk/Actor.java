package com.kamikadze328.vk;

import java.util.LinkedList;

public class Actor {
    private String name;
    private int fun;
    private boolean isMainCharacter;
    protected Location location;
    private int hunger;

    class Inventory {
        LinkedList<Actor> inventory = new LinkedList<>();

        protected void add(Actor a) {
            inventory.add(a);
            System.out.println(Actor.this.getName() + " поднимает " + a.getName());
        }

        protected void remove(Actor a) {
            inventory.remove(a);
            System.out.println(Actor.this.getName() + " опускает " + a.getName());
        }
    }

    Inventory inventory = new Inventory();

    Actor(String n) {
        name = n;
        fun = 1;
    }

    Actor(String n, Location l) {
        name = n;
        location = l;
        fun = 1;
    }

    public String getName() {

        return name;
    }

    public int getHunger() {

        return hunger;
    }

    public int getFun() {
        return fun;
    }

    public boolean getIsMainCharacter() {
        return isMainCharacter;
    }

    public Location getLocation() {
        return location;
    }

    public void setHunger(int h) {
        hunger = h;
    }


    public void setLocation(Location l) {
        location = l;
    }

    public void setMainCharacter(boolean is) {
        isMainCharacter = is;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setFun(int fun) {
        this.fun = fun;
    }

    public boolean equals(Object otherObject) {
        if (this == otherObject) return true;
        if (otherObject == null) return false;
        if (getClass() != otherObject.getClass()) return false;
        Actor other = (Actor) otherObject;
        return getName().equals(other.getName());
        //        && getFun() == other.getFun()
        //        && getIsMainCharacter() == other.getIsMainCharacter()
          //      && getHunger() == other.getHunger()
            //    && getLocation() == other.getLocation()
              //  && inventory.equals(other.inventory);
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }
}
