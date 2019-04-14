package com.kamikadze328.vk;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.File;
import java.util.NoSuchElementException;
import java.util.Scanner;

/** Класс служит для считывания данных со стандартного потока ввода и обработки команд
 *
 */
public class Reader {
    /**
     * Экземпляр обработчика команд
     */
    private CollectionManager collectionManager;
    /**
     * Существо для сравнением с существами в коллекции
     */
    private Creature forAction;
    /**
     * Параметр, останавливающий чтение со потока ввода
     */
    private boolean Exit;
    private File file;
    /**
     * Конструктор, принимающий менеджера коллекции. Начинает импорт коллекции из файла. Обычно после создания объекта вызывается метов {@link #start()}
     * @param manager Менеджер коллекции
     */
        public Reader(CollectionManager manager){
            collectionManager = manager;
            manager.Import(manager.importFile);
            Exit = false;
        }

    /**
     * Метод начинает работу программы. Обрабатывает полученные с {@link #readAndParseCommand()} команды, осуществляет выборку и передаёт управление менеджеру коллекции.
     * При получении команды с аргументом инициализирует объект и передаёт менеджеру
     */
    protected void start(){
            collectionManager.info();
            while(!Exit){
                String[] fullCommand = readAndParseCommand();
                if ((fullCommand[0].equals("add_if_max") || fullCommand[0].equals("add") || fullCommand[0].equals("remove") || fullCommand[0].equals("load"))) {
                    if(fullCommand.length == 1) {
                        System.out.println("  Ошибка, " + fullCommand[0] + " должка иметь аргумент.");
                        continue;
                    }else if (fullCommand[0].equals("add_if_max") || fullCommand[0].equals("add") || fullCommand[0].equals("remove")) {
                        try {
                            Gson gson = new Gson();
                            String jsonStr = fullCommand[1];
                            jsonStr = jsonStr.replace(" ", "");
                            if (jsonStr.contains("\"Class\":\"CloseFriends\""))
                                forAction = gson.fromJson(jsonStr, CloseFriends.class);
                            else if (jsonStr.contains("\"Class\":\"MoominFamily\""))
                                forAction = gson.fromJson(jsonStr, MoominFamily.class);
                            else if (jsonStr.contains("\"Class\":\"Morra\""))
                                forAction = gson.fromJson(jsonStr, Morra.class);
                            else if (jsonStr.contains("\"Class\":\"Hattifatteners\""))
                                forAction = gson.fromJson(jsonStr, Hattifatteners.class);
                            else if(jsonStr.contains("\"Class\":\"Creature\""))
                                forAction = gson.fromJson(jsonStr, Creature.class);
                            else forAction = null;
                            if ((forAction == null)  || (forAction.getName() == null)) {
                                System.out.println("  Ошибка, элемент задан неверно, возможно вы указали не все значения.");
                                continue;
                            }
                    }catch (JsonSyntaxException e){
                    System.out.println("  Ошибка в формате аргумента");
                    continue;
                    }
                    } else {
                        file = new File(fullCommand[1].replace(" ", ""));
                    }
                }
                switch (fullCommand[0]){
                    case "info":
                        collectionManager.info();
                        break;
                    case "help":
                        collectionManager.help();
                        break;
                    case "add":
                        collectionManager.add(forAction);
                        break;
                    case "remove":
                        collectionManager.remove(forAction);
                        break;
                    case "add_if_max":
                        collectionManager.addIfMax(forAction);
                        break;
                    case "load":
                        collectionManager.Import(file);
                        break;
                    case "show":
                        collectionManager.show();
                        break;
                    case "clear":
                        collectionManager.clear();
                        break;
                    case "exit":
                        Exit = true;
                        collectionManager.finishWork();
                        break;
                    default:
                        System.out.println("  Ошибка, Неизвестная команда\n" +
                                "  Для помощи введите команду help.");
                }
            }
        }

    /**
     * Считывает команду из System.in и разбивает на самоу команду и аргумент.
     * Позволяет считывать многострочные аргументы.
     *
     * @return массив из двух элементов. Первый содержит название команды. Второй - аргумент, если команда имеет его
     */
    private String[] readAndParseCommand(){
            Scanner consoleScanner = new Scanner(System.in);
            String command;
            String[] fullCommand;
            int count = 0;
            try {
                System.out.print("\nВведите команду:\n>");
                command = consoleScanner.nextLine();
                fullCommand = command.trim().split(" ",2);
                if(fullCommand.length ==1) return fullCommand;
                else if ((fullCommand[0].equals("add_if_max") || fullCommand[0].equals("add") || fullCommand[0].equals("remove"))) {
                    fullCommand[1] = fullCommand[1].trim();
                    command = fullCommand[1];
                    fullCommand[1] = "";
                    while(!command.contains("{")){
                        fullCommand[1] += command;
                        command = consoleScanner.nextLine().trim();
                    }
                    count += command.replace("{", "").length() - command.replace("}", "").length();
                    fullCommand[1] += command;
                    while (count != 0){
                        command = consoleScanner.nextLine();
                        fullCommand[1] += command;
                        count += command.replace("{", "").length() - command.replace("}", "").length();
                    }
                }else return fullCommand;
            }catch(NoSuchElementException ex) {
               fullCommand = new String[1];
               fullCommand[0] = "exit";
            }
            return fullCommand;
        }
}
