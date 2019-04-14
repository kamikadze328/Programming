package com.kamikadze328.vk;

import java.util.ArrayList;

public class Story {
    private String name;
    private ArrayList<Creature> characters = new ArrayList<Creature>();
    public Creature mainCharacter;
    private Territory territory;
    private int currentTime;

    Story(String n, Creature M) {
        try {
            name = n;
            mainCharacter = M;
            M.setMainCharacter(true);
            M.setHunger(10);
            characters.add(M);
            territory = Territory.Moominhouse;
            currentTime = 12;
        } catch (NullPointerException e) {
            throw new MyNullPointerException("Похоже вы не задали главного героя!!!");
        }
    }



    Story(String n, Creature M, Territory t) {
        try {
            name = n;
            M.setMainCharacter(true);
            M.setHunger(10);
            mainCharacter = M;
            characters.add(M);
            territory = t;
            currentTime = 12;
        } catch (NullPointerException e) {
        throw new MyNullPointerException("Похоже вы не задали главного героя!!!");
        }
    }

    Story(String n, Creature M, Territory t, int ct) {
        try {
            name = n;
            M.setMainCharacter(true);
            M.setHunger(10);
            mainCharacter = M;
            characters.add(M);
            territory = t;
            currentTime = ct;
        } catch (NullPointerException e) {
            throw new MyNullPointerException("Похоже вы не задали главного героя!!!");
        }
    }

    public void setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
        System.out.println(currentTime + " часов");
    }

    public void addCharacter(Creature creature) {
        characters.add(creature);
    }

    public ArrayList<Creature> getCharacters() {
        return characters;
    }

    public String getName() {
        return name;
    }

    public Creature getMainCharacter() {
        return mainCharacter;
    }

    public int getCurrentTime() {
        return currentTime;
    }

    public Territory getTerritory() {
        return territory;
    }

    public void begin() {
        System.out.println("\nВ далёкой-далёкой галактике жил-был " + mainCharacter.getName());
        System.out.println("Было у него много друзей");
        for (int i = 0; i < characters.size(); i++) {
            if (characters.get(i).getName() != mainCharacter.getName()) {
                System.out.println(characters.get(i).getName());
            }
        }
        System.out.println("и вот однажды в " + currentTime + " часов" + " в " + territory + "...\n");
    }
    public void TotalBegin(){
        CloseFriends Khemul = (CloseFriends) mainCharacter;
        CloseFriends SnorkMaiden = new CloseFriends("фрекен Снорк", Location.TopFloor);
        CloseFriends Sniff = new CloseFriends("Снифф", Location.TopFloor);
        CloseFriends Snusmumrik = new CloseFriends("Снусмумрик", Location.TopFloor);
        MoominFamily Moomintroll = new MoominFamily("Мумми-тролль", Location.TopFloor);
        Game game = new Game(){
            ArrayList<String> gamers1Name = new ArrayList<String>();
            ArrayList<Actor> gamers1  = new ArrayList<Actor>();
            ArrayList<Actor> gamers2 = new ArrayList<Actor>();
            ArrayList<String> gamers2Name= new ArrayList<String>();
            public void addGamer1(CloseFriends a, String name){
                gamers1Name.add(name);
                gamers1.add(a);
            }
            public void addGamer1(MoominFamily a, String name){
                gamers1Name.add(name);
                gamers1.add(a);
            }
            public void addGamer2(CloseFriends a, String name){
                gamers2Name.add(name);
                gamers2.add(a);
            }
            public void addGamer2(MoominFamily a, String name){
                gamers2Name.add(name);
                gamers2.add(a);
            }
            public void begin(){
                for(int i=0; i<gamers1.size(); i++)
                    System.out.print(gamers1Name.get(i) + ", ");
                System.out.print("начинают сражение против ");
                for(int i=0; i<gamers2.size(); i++)
                    System.out.print(gamers2Name.get(i) + ", ");
                System.out.print("\nПобеждают ");
                if(Math.random()>0.5) {
                    for(int i=0; i<gamers1.size(); i++) {
                        System.out.print(gamers1.get(i).getName() + ", ");
                        gamers1.get(i).setFun(gamers1.get(i).getFun()+10);
                    }
                }
                else{
                    for(int i=0; i<gamers2.size(); i++) {
                        System.out.print(gamers2.get(i).getName() + ", ");
                        gamers2.get(i).setFun(gamers1.get(i).getFun()+10);
                    }
                }
                System.out.print("\n\n");
            }
        };
        ((Game) game).addGamer1(SnorkMaiden, "Джейн");
        ((Game) game).addGamer1(Moomintroll, "Тарзан");
        ((Game) game).addGamer2(Sniff, "сын Тарзана");
        ((Game) game).addGamer2(Snusmumrik, "шимпанзе Чита");


        StaticAction karaul = new StaticAction(Khemul, "караулит Панталошку");
        StaticAction yabloko = new StaticAction(Khemul, "ест яблоко");
        StaticAction schitat = new StaticAction(Khemul, "cчитает тычинки в цветке джунглей");
        StaticAction smotret = new StaticAction(Khemul, "смотрит");
        StaticAction osenit = new StaticAction(Khemul, "выполняет мыслительные процессы");
        StaticAction dozhd = new StaticAction(new Decoration("Дождь"), "перестал");
        ChangeAction sun = new ChangeAction(new Decoration("Солнце"), "зашло");
        Story moomin = new Story("kek", Khemul, Territory.Moominhouse, 18);
        ChangeAction holm = new ChangeAction(new Decoration("холм", Location.Hill, 10, 0), "увядает");
        ChangeAction plod = new ChangeAction(new Decoration("плод", Location.Hill, 3, 7), "сморщивается");
        ChangeAction tsvetok = new ChangeAction(new Decoration("цветок", Location.Hill, 2, 2), "поник");
        StaticAction list = new StaticAction(new Decoration("лист"), "cворачивается трубочкой");
        Creature dom = new Creature("дом", Location.Hill, "*шорох и потрескивание*");
        ChangeAction hodit = new ChangeAction(Khemul, "подходит к кусту");
        ChangeAction skhodit = new ChangeAction(Khemul, "идёт");
        Decoration vetka = new Decoration("ветка", Location.Hill, 1, 7);
        ChangeAction vetkaC = new ChangeAction(vetka, "");
        StaticAction vetkaS = new StaticAction(vetka, "ломается");
        Decoration vetki = new Decoration("ветки");
        ChangeAction sobrat = new ChangeAction(vetki, "собирает");
        StaticAction brat = new StaticAction(Khemul, "берёт спички");
        StaticAction koster = new StaticAction(new Decoration("костёр", Location.FootPath, 6, 0), "пылает");
        Decoration Khvost = new Decoration("хвост Панталошки", Location.FootPath, 5, 0);
        ChangeAction sila = new ChangeAction(Khemul, "увеличивает");
        ChangeAction tashit = new ChangeAction(Khvost, "тащит");
        try{
            moomin.begin();
            ((Game) game).begin();
            Khemul.sayPhrase();
            if (Math.random() > 0.5) {
                yabloko.describe();
            }
            schitat.describe();
            moomin.setCurrentTime(19);
            dozhd.describe();
            sun.changeLocation();
            holm.changeSize(5);
            plod.changeSize(2);
            plod.changeHeight(0);
            tsvetok.changeSize(1);
            list.describe();
            dom.sayPhrase();
            smotret.describe();
            hodit.changeLocation();
            vetkaC.changeHeight(5, Khemul);
            vetkaS.describe();
            osenit.describe();
            sobrat.changeLocation(Location.Yard, Khemul);
            skhodit.changeLocation(Location.Hangar);
            brat.describe();
            skhodit.changeLocation(Location.FootPath);
            koster.describe();
            sila.changeStrength(10000);
            tashit.changeLocation(Location.Yard, Khemul);
        }catch(HungryException e){
            System.out.println(e.getMessage());
            System.out.println(e.correction());
        }
        finally{
            System.out.println("Плотно покушав, " + moomin.mainCharacter.getName() + " лёг спать");
            System.out.println("Вот и сказочке конец, а кто слушал молодец");
        }
    }
}


