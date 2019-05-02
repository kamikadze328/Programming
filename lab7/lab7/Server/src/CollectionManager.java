import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;


class CollectionManager {

    private File importFile;
    private CopyOnWriteArrayList<Creature> Creatures;
    private String initTime;
    private boolean exit;
    private DataBaseManager DBman;

    CollectionManager(File file, DataBaseManager DBman) {
        importFile = file;
        exit = true;
        Creatures = new CopyOnWriteArrayList<>();
        OffsetDateTime d = OffsetDateTime.now();
        initTime = d.format(DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ssX"));
        this.DBman = DBman;
    }

    boolean loadFile(File file) {
        try {
            if (file == null)
                throw new NullPointerException("Вместо файла передано ничего. Добавьте элементы вручную или импортируйте из другого файла");
            if (!(file.isFile()))
                throw new FileNotFoundException("Ой, а это не файл. Добавьте элементы вручную или импортируйте из другого файла");
            if (!(file.exists()))
                throw new FileNotFoundException("404. Файл нот фаунд. Добавьте элементы вручную или импортируйте из другого файла");
            if (!file.canRead())
                throw new SecurityException("Охраняемая территория!! Вход запрещён! Добавьте элементы вручную или импортируйте из другого файла");
            String JsonString = readFromFile(file);
            Receiver.add("Файл сервера считан");
            return load(JsonString);
        } catch (NullPointerException | FileNotFoundException | SecurityException ex) {
            Receiver.add(ex.getMessage());
            return false;
        } catch (IOException ex) {
            Receiver.add("Произошла ошибка при чтении с файла.");
            return false;
        }
    }

    boolean load(String JsonString) {
        try {
            parser(JsonString.split("},\\{"));
            return true;
        } catch (JsonSyntaxException ex) {
            Receiver.add("JSON строки исписаны неразборчивым подчерком");
            return false;
        }
    }

    private String readFromFile(File file) throws IOException {
        String jsonStr = "";
        BufferedReader r = new BufferedReader(new InputStreamReader(new BufferedInputStream(new FileInputStream(file))));
        String line;
        while ((line = r.readLine()) != null) jsonStr += line;
        jsonStr = jsonStr.substring(1, jsonStr.length() - 1);
        Receiver.add("\nФайл успешно считан");
        return jsonStr;
    }

    private void parser(String[] line) throws JsonSyntaxException {
        boolean oneParse = false;
        if (line.length == 1) oneParse = true;
        int beginCountCreatures = Creatures.size();
        int noInit = 0;
        int count = -1;
        Gson gson = new Gson();
        for (int i = 0; i < line.length; i++) {
            if (i == 0 && !oneParse) line[i] = line[i] + "}";
            else if (i == line.length - 1 && !oneParse) line[i] = "{" + line[i];
            else if (line.length > 1) line[i] = "{" + line[i] + "}";
            if (line[i].equals("")) {
            } else if (line[i].contains("\"family\"") && (line[i].contains("\"name\""))) {
                count++;
                Creature forAction = gson.fromJson(line[i], Creature.class);
                add(forAction);
            } else noInit++;
        }
        int finalCount = ++count;
        int addCountCreature = Creatures.size() - beginCountCreatures;
        Receiver.add(("\nУдачно инициализированно " + finalCount + " существ, неудачно " + noInit +
                "\nВ коллекцию добавлено " + addCountCreature + " из них.\n"));
    }

    void remove(Creature forAction) {
        if (DBman.removeCreature(forAction)) {
            Creatures.remove(forAction);
            Receiver.add(forAction.toString() + " удалён из коллекции.");
        } else Receiver.add(forAction.toString() + " в коллекции не было.");
    }

    void addIfMax(Creature forAction) {
        if (DBman.addIfMax(forAction)) {
            Creatures.add(forAction);
            Receiver.add((forAction.toString() + " добавлен, т.к. является наибольшим"));
        } else
            Receiver.add(forAction.toString() + " не является наибольшим элементом коллекции");
    }

    void add(Creature forAction) {
        if (DBman.addCreature(forAction)) {
            Creatures.add(forAction);
            Receiver.add(forAction.toString() + " добавлен");
        } /*else addReceiver(forAction.toString() + " уже существует\n");*/
    }

    void info() {
        DBman.info();
        Receiver.add("Коллекция типа " + Creatures.getClass().getSimpleName() + " содержит объекты класса Creature" +
                "\nДата инициализации:  " + initTime +
                "\nСейчас содержит " + Creatures.size() + " существ." +
                "\n\nДля помощи введите команду help.");
    }

    void help() {
        Receiver.add("add {element}: добавить новое существо;\n" +
                "remove {element}: удалить существо;\n" +
                "add_if_max {element}: добавить новое существо, если его значение превышает значение наибольшего элемента;\n" +
                "show: вывести в System.out все строки в строковом представлении;\n" +
                "clear: очистить коллекцию;\n" +
                "info: вывести информацию о коллекции (тип, дата инициализации, количество элементов);\n" +
                "load путь_к_файлу: считать коллекцию из файла сервера;\n" +
                "import путь_к_файлу: считать коллекцию из файла клиента;\n" +
                "save: сохранить коллекцию в файл на сервере;\n" +
                "exit: завершает работу клиента;\n" +
                "help: вывести помощь по всем командам.");
    }

    synchronized void save() {
        File saveFile = importFile;
        Gson gson = new Gson();
        try (OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(saveFile, false))) {

            osw.write(gson.toJson(Creatures));
            osw.flush();
            Receiver.add("Коллекция сохранена в файл сервера " + saveFile.getAbsolutePath());
        } catch (IOException | NullPointerException e) {
            Date d = new Date();
            SimpleDateFormat formater = new SimpleDateFormat("MM.dd_hh:mm:ss");
            saveFile = new File("saveFile" + formater.format(d) + ".json");
            try (OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(saveFile, true))) {
                osw.write(gson.toJson(Creatures));
                osw.flush();
                Receiver.add("Коллекция сохранена в файл " + saveFile.getAbsolutePath());
            } catch (IOException ex) {
                Receiver.add("Сохранение коллекции не удалось");
            }
        }
    }

    void show() {
        Receiver.add(Creatures.toString());
    }

    boolean isExit() {
        return exit;
    }

    void trueExit() {
        exit = true;
    }

    void clear() {
        if (DBman.clearCreature())
            Creatures.clear();
    }
}
