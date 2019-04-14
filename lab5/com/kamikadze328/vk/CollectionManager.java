package com.kamikadze328.vk;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.File;

/**
 * Класс служит для управления коллекцией и обработки команд.
 */
 class CollectionManager {
    /**
     * Хранит импортируемый файл.
     */
    protected File importFile;
    /**
     * Основная коллекция для существ.
     */
    private Set<Creature> Creatures;
    /**
     * Хранит время инициализации коллекции.
     */
    private Date initTime;
    /**
     * Переменная для осуществления одноразового вызова {@link #finishWork()} при стандартном завершении работы.
     */
    private boolean exit;

    /**
     * Создаёт менеджера и его коллекцию, записивает время инициализации.
     * Также поток shutdownHook для отлавливания нестандартного выхода из программы
     * @param file импортируемый файл
     */
    public CollectionManager(File file) {
        importFile = file;
        exit = true;
        Creatures = Collections.synchronizedSet(new HashSet<>());
        initTime = new Date();
        ShutdownHook shutdownHook = new ShutdownHook();
        Runtime.getRuntime().addShutdownHook(shutdownHook);
    }

    /**
     * Класс для создания Shutdown-ловушки.
     */
    public class ShutdownHook extends Thread {
        /**
         * Если этот поток был создан с использованием отдельного объекта runnable, вызывается метод run этого объекта Runnable.
         * Вызывает {@link #finishWork()}
         */
        public void run() {
            finishWork();
        }
    }

    /**
     * Если не поймана ни одна ошибка, то вызывает {@link #readFromFile(File)}.
     * Если успешно, то далее вызывает {@link #parser(String)}.
     * Служит для обработки возможных ошибок.
     * @param file файл с объектами в формате json
     */
    protected void Import(File file) {

        try {
            if (file == null)
                throw new NullPointerException("  Вместо файла передано ничего. Добавьте элементы вручную или импортируйте из другого файла");
            if (!(file.isFile()))
                throw new FileNotFoundException("  Ой, а это не файл. Добавьте элементы вручную или импортируйте из другого файла");
            if (!(file.exists()))
                throw new FileNotFoundException("  404. Файл нот фаунд. Добавьте элементы вручную или импортируйте из другого файла");
            if (!file.canRead())
                throw new SecurityException("  Охраняемая территория!! Вход запрещён! Добавьте элементы вручную или импортируйте из другого файла");
            //if (file.equals(importFile)) file = importFile;
            String JsonString = readFromFile(file);
            if ((Creatures = parser(JsonString)).isEmpty())
                System.out.println("    Ничего не добавлено, возможно импортируемая коллекция пуста, или элементы заданы неверно");
        } catch(NullPointerException | FileNotFoundException | SecurityException ex){
            System.out.println(ex.getMessage());
        } catch (JsonSyntaxException ex) {
            System.out.println("  JSON файл исписан неразборчивым подчерком");
        }catch (IOException ex){
            System.out.println("  Ты что-то ввёл  и всё исчезло. Как!? Как ты это сделал? Большое спасибо!!!!");
        }
    }



    /**
     * Считывает информацию из файла.
     * @param file файл с объектами в формате json
     * @return строка с содержимым файла
     * @throws IOException Если произошла ошибка ввода
     */

    private String readFromFile(File file) throws  IOException{
            String jsonStr = "";
            BufferedInputStream br = new BufferedInputStream(new FileInputStream(file));
/*          int Char;
            while ((Char = br.read()) != -1){
                Character strChar = (char) Char;
                jsonStr = jsonStr + strChar.toString();
            } */
            BufferedReader r = new BufferedReader(new InputStreamReader(br));
            String line;
            while ((line = r.readLine()) != null) jsonStr += line;
            jsonStr = jsonStr.substring(1, jsonStr.length()-1);
            System.out.println("    Файл успешно считан!");
            return jsonStr;
    }

    /**
     * Парсит полученную строку и инициализирует существ.
     * Существо будет инициализированно, если имеет поле name и поле Class, имеющее в значении существующее названия класса Creature или его наследников.
     * Существо может быть добавлено в коллекцию, если в ненулевое поле name.
     * @param jsonStr строка c содержимым файла
     * @return Коллекция с инициализированными существами
     * @throws JsonSyntaxException Если произошла в файле некорректый формат JSON
     */
    private Set<Creature> parser(String jsonStr) throws JsonSyntaxException{
        ArrayList<Creature> BeginCreatures= new ArrayList<>();
        Set<Creature> FinalCreatures = Collections.synchronizedSet(new HashSet<>());
        String[] line = jsonStr.split("\\},\\{");
        boolean oneParse = false;
        if (line.length==1) oneParse = true;
        int beginCountCreature = FinalCreatures.size();
        int noInit=0;
        int count =-1;
        Gson gson = new Gson();
            for (int i = 0; i < line.length; i++) {
                if(i==0&&!oneParse) line[i] = line[i] + "}";
                else if(i==line.length-1&&!oneParse) line[i] = "{" + line[i];
                else if(line.length> 1)line[i] = "{" + line[i] + "}";
                if (line[i].equals("")){
                    continue;
                }else if (line[i].contains("\"Class\":\"CloseFriends\"")) {
                    count++;
                    BeginCreatures.add(gson.fromJson(line[i], CloseFriends.class));
                }else if (line[i].contains("\"Class\":\"MoominFamily\"")) {
                    count++;
                    BeginCreatures.add(gson.fromJson(line[i], MoominFamily.class));
                }else if (line[i].contains("\"Class\":\"Morra\"")){
                    count++;
                    BeginCreatures.add(gson.fromJson(line[i], Morra.class));
                }else if (line[i].contains("\"Class\":\"Hattifatteners\"")){
                    count++;
                    BeginCreatures.add(gson.fromJson(line[i], Hattifatteners.class));
                }else if(line[i].contains("\"Class\":\"Creature\"")){
                    count++;
                    BeginCreatures.add(gson.fromJson(line[i], Creature.class));
                }else {
                    noInit++;
                    continue;
                }
                if (count >= 0 && BeginCreatures.get(count).getName() != null && !BeginCreatures.get(count).getName().equals("")&& !BeginCreatures.get(count).getName().trim().equals(""))
                    FinalCreatures.add(BeginCreatures.get(count));
                }
            int finalCount = ++count;
            int addCountCreature = FinalCreatures.size() - beginCountCreature;
            System.out.println("\n    Успешно инициализированно " + finalCount + " существ, неинициализированно " + noInit +
                    "\n    В коллекцию добавлено " + addCountCreature + " из них.\n");
            return FinalCreatures;
    }

    /**
     * Удаляет существо из коллекции, если таковое присутствует.
     * @param forAction существо инициализированное методом start() класса Reader
     */
    protected void remove(Creature forAction) {
        if(Creatures.remove(forAction))
            System.out.println("    "+forAction.toString() + " удалён из коллекции.");
        else System.out.println("   "+forAction.toString() + " в коллекции не было.");
    }

    /**
     * Добавляет существо в коллекцию, если его имя длиннее каждого имени в коллекции.
     * После успешного сравнения вызывается {@link #add(Creature)}.
     * @param forAction существо инициализированное методом start() класса Reader
     */
    protected void addIfMax(Creature forAction) {
        Iterator<Creature> iterator = Creatures.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            if (forAction.compareTo(iterator.next()) > 0)
                count++;
        }
        if (Creatures.size()==count) {
            System.out.println("    " + forAction.toString() +" является максимальным");
            add(forAction);
        } else System.out.println("    "+forAction.toString()+ " не является наибольшим элементом коллекции");
    }


    /**
     * Добавляет элемент в коллекцию, если такового ранее в коллекции не было.
     * @param forAction существо инициализированное методом start() класса Reader
     */
    protected void add(Creature forAction){
        if(Creatures.add(forAction))
            System.out.println("    "+forAction.toString() + " добавлен в коллекцию");
        else System.out.println("    "+forAction.toString() + " уже есть в коллекции");
    }

    /**
     * Выводит информацию о коллекции.
     */
    protected void info() {
        System.out.println("\n    Коллекция типа HashSet содержит объекты класса Creatures(и подклассов)" +
                "\n    Дата инициализации:  " + initTime +
                "\n    Сейчас содержит " + Creatures.size() + " существ." +
                "\n\n    Для помощи введите команду help.");
    }

    /**
     * Выводит информацию  о всех доступных командах.
     */
    protected void help(){
        System.out.println("    add {element}: добавить новое существо в коллекцию,\n" +
                        "   show: вывести в System.out все элементы коллекции в строковом представлении,\n" +
                        "   clear: очистить коллекцию,\n" +
                        "   info: вывести информацию о коллекции (тип, дата инициализации, количество элементов),\n" +
                        "   load: перечитать коллекцию из файла,\n" +
                        "   remove {element}: удалить существо из коллекции,\n" +
                        "   add_if_max {element}: добавить новое существо в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции),\n" +
                        "   help: вывести все команды.");
    }

    /**
     * Сохраняет текущую коллекцию в начальный файл, если файл отсутствует, то создаёт новый.
     * Название файла будет состоять из времени сохранения.
     */
    protected void finishWork() {
        if(exit){
          //  if ((importFile == null)||(!(importFile.isFile()))||(!(importFile.exists()))||(!importFile.canRead()))
         //       importFile =
            File saveFile = importFile;
            Gson gson = new Gson();
            try(OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(saveFile, false))){
                osw.write(gson.toJson(Creatures));
                osw.flush();
                System.out.println("\n    Коллекция сохранена в файл " + saveFile.getAbsolutePath());
            }catch (IOException | NullPointerException e){
                Date d = new Date();
                SimpleDateFormat formater = new SimpleDateFormat("MM.dd_hh:mm:ss");
                saveFile = new File("saveFile" + formater.format(d) + ".json");
                try(OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(saveFile, true))){
                    osw.write(gson.toJson(Creatures));
                    osw.flush();
                    System.out.println("\n    Коллекция сохранена в файл " + saveFile.getAbsolutePath());
                }catch (IOException ex){
                    System.out.println("  Сохранение коллекции не удалось");
                }
            }
            exit = false;
        }

    }

    /**
     * Выводит всех существ коллекции в строковом представлении.
     */
    protected void show(){
        System.out.println(Creatures.toString());
    }

    /**
     * Очищает коллекцию от всех существ.
     */
    protected void clear(){
        Creatures.clear();
    }
}
