package com.kamikadze328.vk;

import java.util.Date;
import java.util.Objects;

/**
 * Класс для существ
 */
public class Creature extends Actor implements Comparable<Creature>{
    /**
     * Время создания
     */
    private Date CurrentTime;
    /**
     * Фраза
     */
    private String phrase;
    /**
     * Класс существа
     */
    protected String Class;

    /**
     * инициализирует существо по имени и локации
     * @param n имя
     * @param l локация
     */
    Creature(String n, Location l){
        super(n,l);
        CurrentTime = new Date();
    }
    /**
     * инициализирует существо по имени, локации и фразе
     * @param n имя
     * @param l локация
     * @param p фраза
     */
    Creature(String n, Location l, String p){
        super(n,l);
        phrase = p;
        CurrentTime = new Date();
    }

    /**
     * Устанавливает фразу.
     * @param aPhrase фраза
     */
    public void setPhrase(String aPhrase){
        phrase = aPhrase;
    }

    /**
     * Устанавливает в время создания текущее время
     */
    public void setCurrentTime() {
        CurrentTime = new Date();
    }

    /**
     * Возвращает время создания объекта
     * @return время создания
     */
    public Date getCurrentTime() {
        return CurrentTime;
    }

    /**
     * Существо произносит фразу.
     */
    public void sayPhrase(){
        System.out.print(this.getName() + ": " + phrase +"\n");
    }

    /**
     * Возвращает фразу.
     * @return фраза
     */
    public String getPhrase() {
        return phrase;
    }

    /**
     * Проверяет существ на равенство.
     * @param otherObject другое существо
     * @return true если существа имеют одно имя и один класс.
     */
    @Override
    public boolean equals(Object otherObject) {
        if(super.equals(otherObject)) return true;
        Creature other = (Creature) otherObject;
        return super.equals(other);
    }

    /**
     * Сравнивает существ.
     * @param o другое существо
     * @return число положительное, то первое существо больше. Число отрицательно, то второе существо больше. 0 - существа эквивалентны.
     */
    @Override
    public int compareTo(Creature o) {
        return getName().length() - o.getName().length();
    }

    /**
     * Выводит строковое представление существа.
     * @return строкове представление класса и имени
     */
    @Override
    public String toString() {
        return "\n" + Class +" "+ getName();
    }

    /**
     * Определяет hashcode существа.
     * @return hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), Class);
    }
}