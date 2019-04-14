package com.kamikadze328.vk;

public abstract class Action {
    String description;
    Actor actor;
    Creature actorC;
    Decoration actorD;
    CloseFriends actorCF;
    MoominFamily actorMF;
    Morra actorM;
    Hattifatteners actorH;

    Action(CloseFriends a, String d) {
        actorCF =a;
        actor = a;
        description = d;
    }
    Action(MoominFamily a, String d){
        actorMF = a;
        actor = a;
        description = d;
    }
    Action(Morra a, String d){
        actorM = a;
        actor = a;
        description = d;
    }
    Action(Hattifatteners a, String d){
        actorH = a;
        actor = a;
        description = d;
    }
    Action(Creature a, String d){
        actorC = a;
        actor = a;
        description = d;
    }
    Action(Decoration a, String d){
        actorD = a;
        actor = a;
        description = d;
    }
    Action(Actor a, String d){
        actor = a;
        description =d;
    }
    public void changeDescription(String s){
        description = s;
    }
    public abstract void changeLocation();
    public abstract void changeLocation(Location l)throws HungryException;
    public abstract void changeLocation(Location l, Actor a)throws HungryException;
}
