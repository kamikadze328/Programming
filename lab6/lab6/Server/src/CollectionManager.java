import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;


class CollectionManager {

    private File importFile;
    private CopyOnWriteArrayList<Creature> Creatures;
    private Date initTime;
    private boolean exit;
    private String receiver = "";

    CollectionManager(File file) {
        importFile = file;
        exit = true;
        Creatures = new CopyOnWriteArrayList<>();
        initTime = new Date();
    }
    boolean loadFile(File file){
        try {
            if (file == null)
                throw new NullPointerException("\tВместо файла передано ничего. Добавьте элементы вручную или импортируйте из другого файла");
            if (!(file.isFile()))
                throw new FileNotFoundException("\tОй, а это не файл. Добавьте элементы вручную или импортируйте из другого файла");
            if (!(file.exists()))
                throw new FileNotFoundException("\t404. Файл нот фаунд. Добавьте элементы вручную или импортируйте из другого файла");
            if (!file.canRead())
                throw new SecurityException("\tОхраняемая территория!! Вход запрещён! Добавьте элементы вручную или импортируйте из другого файла");
            String JsonString = readFromFile(file);
            receiver = "\tФайл сервера считан\n";
            return load(JsonString);
        }catch(NullPointerException | FileNotFoundException | SecurityException ex){
            receiver = ex.getMessage();
            return false;
        } catch (IOException ex){
            receiver = "\tПроизошла ошибка при чтении с файла.";
            return false;
        }
    }

     boolean load(String JsonString){
        try {
            Creatures = parser(JsonString.split("},\\{"));
            return true;
        }catch (JsonSyntaxException ex) {
            receiver = "\tJSON строки исписаны неразборчивым подчерком";
            return false;
        }
     }

    private String readFromFile(File file) throws  IOException{
            String jsonStr = "";
            BufferedReader r = new BufferedReader(new InputStreamReader(new BufferedInputStream(new FileInputStream(file))));
            String line;
            while ((line = r.readLine()) != null) jsonStr += line;
            jsonStr = jsonStr.substring(1, jsonStr.length()-1);
            receiver += "\n\tФайл успешно считан";
            return jsonStr;
    }

     private CopyOnWriteArrayList<Creature> parser(String[] line) throws JsonSyntaxException{
        ArrayList<Creature> BeginCreatures= new ArrayList<>();
        CopyOnWriteArrayList<Creature> FinalCreatures = new CopyOnWriteArrayList<>();
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
            receiver += ("\tУдачно инициализированно " + finalCount + " существ, неудачно " + noInit +
                    "\n\tВ коллекцию добавлено " + addCountCreature + " из них.\n");
            return FinalCreatures;
    }

    String remove(Creature forAction) {
        if(Creatures.remove(forAction))
            return "\t"+forAction.toString() + " удалён из коллекции.";
        else return "\t"+forAction.toString() + " в коллекции не было.";
    }

    String addIfMax(Creature forAction) {
        if (forAction.compareTo(Creatures.stream().max(Creature::compareTo).get()) > 0)
            return (add(forAction) + " т.к. является наибольшим");
        else
            return ("\t" + forAction.toString() + " не является наибольшим элементом коллекции");
    }
//        Creatures.stream().allMatch(((Creature2) -> (forAction.compareTo(Creature2) > 0)));

    String add(Creature forAction){
        if(Creatures.add(forAction))
            return "\t"+forAction.toString() + " добавлен в коллекцию";
        else return "\t"+forAction.toString() + " уже есть в коллекции";
    }
    String add(CopyOnWriteArrayList<Creature> Creatures){
         return "\tВ коллекцию добалено " + this.Creatures.addAllAbsent(Creatures) + " существ";
    }

    String info() {
        return("\tКоллекция типа " + Creatures.getClass().getSimpleName() + " содержит объекты класса Creature(и подклассов)" +
                "\n\tДата инициализации:  " + initTime +
                "\n\tСейчас содержит " + Creatures.size() + " существ." +
                "\n\n\tДля помощи введите команду help.");
    }

    String help(){
        return("\tadd {element}: добавить новое существо в коллекцию;\n" +
                "\tremove {element}: удалить существо из коллекции;\n" +
                "\tadd_if_max {element}: добавить новое существо в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции);\n" +
                "\tshow: вывести в System.out все элементы коллекции в строковом представлении;\n" +
                "\tclear: очистить коллекцию;\n" +
                "\tinfo: вывести информацию о коллекции (тип, дата инициализации, количество элементов);\n" +
                "\tload путь_к_файлу: считать коллекцию из файла сервера;\n" +
                "\timport путь_к_файлу: считать коллекцию из файла клиента;\n" +
                "\tsave: сохранить коллекцию в файл на сервере;\n" +
                "\texit: завершает работу клиента;\n" +
                "\thelp: вывести помощь по всем командам.");
    }

    synchronized String save() {
        File saveFile = importFile;
        Gson gson = new Gson();
        try (OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(saveFile, false))) {
            osw.write(gson.toJson(Creatures));
            osw.flush();
            return ("\tКоллекция сохранена в файл сервера " + saveFile.getAbsolutePath());
        } catch (IOException | NullPointerException e) {
            Date d = new Date();
            SimpleDateFormat formater = new SimpleDateFormat("MM.dd_hh:mm:ss");
            saveFile = new File("saveFile" + formater.format(d) + ".json");
            try (OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(saveFile, true))) {
                osw.write(gson.toJson(Creatures));
                osw.flush();
                return ("\tКоллекция сохранена в файл " + saveFile.getAbsolutePath());
            } catch (IOException ex) {
                return ("\tСохранение коллекции не удалось");
            }
        }
    }

     String getReceiver() {
        String str = receiver;
        receiver = "";
         return str;
     }

     String show() {
         return "\t" + Creatures.toString();
     }

     boolean isExit() {
         return exit;
     }
     void trueExit(){
        exit = true;
     }
     String clear(){
        Creatures.clear();
        return "\tКоллекция очищена";
    }
}
