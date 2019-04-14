package com.kamikadze328.vk;

public class HungryException extends Throwable {
    private String name;
    private Actor actor;

    HungryException(Actor a) {
        super(a.getName());
        name = a.getName();
        actor = a;
    }

    @Override
    public String getMessage() {
        return super.getMessage()+ " голодный";
    }

    public String correction(){
        actor.setHunger(actor.getHunger() + 1);
        return name + " достаёт из кормана булочку и пожирает её";
    }
}