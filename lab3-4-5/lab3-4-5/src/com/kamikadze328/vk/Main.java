package com.kamikadze328.vk;

import java.io.*;

/**
 * Главный класс.
 */
public class Main {
    public static void main(String[] args) {

/*      CloseFriends Khemul = new CloseFriends("Хемуль", Location.Hill, "*вздох*", 499, 50, false);
        Story moomin = new Story("kek", Khemul, Territory.Moominhouse, 18);
        moomin.TotalBegin();*/

        String importFile;
        try {
            importFile = args[0];
            CollectionManager manager = new CollectionManager(new File(importFile));
            Reader reader = new Reader(manager);
            reader.start();
        }catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("  имя файла должно передаваться программе с помощью аргумента командной строки.");
        }
        int a=5;
        double b = a;
        System.out.println(b);
        double c[] = new double[2];
        c[0] = a;
        c[1]=b;

    }
}