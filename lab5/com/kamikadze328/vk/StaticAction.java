package com.kamikadze328.vk;

public class StaticAction extends Action{
    StaticAction(Creature a, String d){
        super(a, d);
    }
    StaticAction(CloseFriends a, String d){
        super(a, d);
    }
    StaticAction(MoominFamily a, String d){
        super(a, d);
    }
    StaticAction(Morra a, String d){
        super(a, d);
    }
    StaticAction(Hattifatteners a, String d){
        super(a, d);
    }
    StaticAction(Decoration a, String d){
        super(a, d);
    }
    StaticAction(Actor a, String d){
        super(a,d);
    }
    public void describe() {
            System.out.println(actor.getName() + " " + description);
        }
    @Override
    public void changeDescription(String s) {
            super.changeDescription(s);
        }

    @Override
    public void changeLocation(Location l) {
        if(actor.getLocation()==l){
            System.out.println(actor.getName() + " " + description + " в " + l);
        }
    }

    @Override
    public void changeLocation() {
        System.out.println(actor.getName() + " " + description + " в " + actor.getLocation());
    }

    @Override
    public void changeLocation(Location l, Actor a) {
        if (actor.getLocation() == l) {
            System.out.println(a.getName() + " " + description + " " + actor.getName() + " в " + l);
        }
    }
}
