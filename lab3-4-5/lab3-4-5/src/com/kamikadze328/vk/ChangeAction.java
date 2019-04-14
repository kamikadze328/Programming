package com.kamikadze328.vk;

public class ChangeAction extends Action implements ChangeFriends, ChangeHattifatteners, ChangeMoomin, ChangeMorra, ChangeDecoration{
    ChangeAction(Creature a, String d){
        super(a, d);
    }
    ChangeAction(CloseFriends a, String d) {
        super(a, d);
    }
    ChangeAction(MoominFamily a, String d){
        super(a, d);
    }
    ChangeAction(Morra a, String d){
        super(a, d);
    }
    ChangeAction(Hattifatteners a, String d){
        super(a, d);
    }
    ChangeAction(Decoration a, String d){
        super(a, d);
    }

    @Override
    public void changeSize(int s) {
        System.out.printf(actor.getName() + " " + description +"  до размера %d \n", s);
        actorD.size = s;
    }

    @Override
    public void changeSize(int s, Actor a) {
        System.out.println(a.getName() + " " + description +"  до размера "+ s + " предмет "+actor.getName());
    }

    @Override
    public void changeDescription(String s) {
        super.changeDescription(s);
    }

    @Override
    public void changeHeight(int h, Actor a) {
        if(h>actorD.height){
            System.out.printf(a.getName() + " поднимает " + actor.getName() + " на %d\n", h-actorD.height);
        }
        if(h<actorD.height&&h==0){
            System.out.println(a.getName() + " кладёт " + actor.getName() + " на землю");
        }
        else{
            System.out.printf(a.getName() + " опускает " + actor.getName() + " на %d\n", -h+actorD.height);
        }
    }

    @Override
    public void changeHeight(int h) {
        if(h>actorD.height){
            System.out.printf(actor.getName() + " поднялся на %d\n", h-actorD.height);
        }
        if(h<actorD.height&&h==0){
            System.out.println(actor.getName() + " упал на землю");
        }
        else{
            System.out.printf(actor.getName() + " опустился на %d\n", -h+actorD.height);
        }
        actorD.height = h;
    }

    @Override
    public void changeTail(boolean t) {
        actorCF.tail = t;
    }

    @Override
    public void changeIntelligence(int i) {
    actorCF.intelligence = i;
    }

    @Override
    public void changeStrength(int s){
        actorCF.strength = s;
    }

    @Override
    public void changeCare(int c) {
        actorMF.care = c;
    }

    @Override
    public void changeWaste(int w) {
        actorMF.waste = w;
    }

    @Override
    public void changeHat(boolean h) {
        actorMF.hat = h;
    }

    @Override
    public void changeCharge(int e) {
        actorH.electricCharge = e;
    }

    @Override
    public void changeTemperature(int t) {
        actorM.temperature = t; }

    @Override
    public void changeLocation(Location l)throws HungryException {
        actor.setLocation(l);
        System.out.print(actor.getName() + " " + description + " в " + l + "\n");
        actor.setHunger(actor.getHunger()-1);
        if(actor.getHunger()< 8) throw new HungryException(actor);
    }

    @Override
    public void changeLocation() {
        System.out.print(actor.getName() + " " + description +"\n");
    }
    @Override
    public void changeLocation(Location l, Actor a)throws HungryException {
        a.inventory.add(actor);
        System.out.print(a.getName() +" " + description + " " + actor.getName() + " в " + l +"\n");
        actor.setLocation(l);
        a.setLocation(l);
        a.inventory.remove(actor);
        a.setHunger(a.getHunger()-1);
        if(a.getHunger()< 8) throw new HungryException(actor);
    }
}
