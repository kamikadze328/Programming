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
    private DataBaseManager DBman;
    private String initTime;

    CollectionManager(File file, DataBaseManager DBman, Receiver receiver) {
        importFile = file;
        this.DBman = DBman;
        Creatures = DBman.synchronize(receiver);
        initTime = OffsetDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ssX"));
    }

    boolean loadFile(File file, Receiver receiver, String token) {
        try {
            if (file == null)
                throw new NullPointerException("Вместо файла передано ничего. Добавьте элементы вручную или импортируйте из другого файла");
            if (!(file.isFile()))
                throw new FileNotFoundException("Ой, а это не файл. Добавьте элементы вручную или импортируйте из другого файла");
            if (!(file.exists()))
                throw new FileNotFoundException("404. Файл нот фаунд. Добавьте элементы вручную или импортируйте из другого файла");
            if (!file.canRead())
                throw new SecurityException("Охраняемая территория!! Вход запрещён! Добавьте элементы вручную или импортируйте из другого файла");
            String JsonString = readFromFile(file, receiver);
            return load(JsonString, receiver, token);
        } catch (NullPointerException | FileNotFoundException | SecurityException ex) {
            receiver.add(ex.getMessage());
            return false;
        } catch (IOException ex) {
            receiver.add("Произошла ошибка при чтении с файла.");
            return false;
        }
    }

    boolean load(String JsonString, Receiver receiver, String token) {
        try {
            parser(JsonString.split("},\\{"), receiver, token);
            return true;
        } catch (JsonSyntaxException ex) {
            receiver.add("JSON строки исписаны неразборчивым подчерком");
            return false;
        }
    }

    private String readFromFile(File file, Receiver receiver) throws IOException {
        String jsonStr = "";
        BufferedReader r = new BufferedReader(new InputStreamReader(new BufferedInputStream(new FileInputStream(file))));
        String line;
        while ((line = r.readLine()) != null) jsonStr += line;
        jsonStr = jsonStr.substring(1, jsonStr.length() - 1);
        receiver.add("\nФайл сервера успешно считан");
        return jsonStr;
    }

    private void parser(String[] line, Receiver receiver, String token) throws JsonSyntaxException {
        boolean oneParse = false;
        if (line.length == 1) oneParse = true;
        int noInit = 0;
        int added = 0;
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
                if (add(forAction, receiver, token)) added++;
            } else noInit++;
        }
        int finalCount = ++count;
        receiver.add("\nУдачно инициализированно " + finalCount + " существ, неудачно " + noInit + ",\nДобавлено " + added + ".\n");
    }

    void remove(Creature forAction, Receiver receiver, String token) {
        if (DBman.removeCreature(forAction, receiver, token)) {
            Creatures.remove(forAction);
            receiver.add(forAction.toString() + " удалён.");
        }
    }

    void addIfMax(Creature forAction, Receiver receiver, String token) {
        if (DBman.addIfMax(forAction, receiver, token)) {
            Creatures.add(forAction);
            receiver.add((forAction.toString() + " добавлен, т.к. является наибольшим"));
        } else
            receiver.add(forAction.toString() + " не является наибольшим элементом коллекции");
    }

    boolean add(Creature forAction, Receiver receiver, String token) {
        if (DBman.addCreature(forAction, receiver, token)) {
            Creatures.add(forAction);
            receiver.add(forAction.toString() + " добавлен");
            return true;
        }
        return false;
    }

    void info(Receiver receiver) {
        DBman.info(receiver);
        receiver.add("\nКоллекция типа " + Creatures.getClass().getSimpleName() + " содержит объекты класса Creature" +
                "\nДата инициализации:  " + initTime +
                "\nСейчас содержит " + Creatures.size() + " существ." +
                "\nДля помощи введите команду help.");
    }

    void help(Receiver receiver) {
        receiver.add("add {element}: добавить существо;\n" +
                "remove {element}: удалить существо;\n" +
                "add_if_max {element}: добавить существо, если его имя длинее всех остальных имён;\n" +
                "show: показать текущее содержимое коллекции;\n" +
                "clear: очистить коллекцию;\n" +
                "info: вывести информацию о баззе данных и коллекции;\n" +
                "load путь_к_файлу: считать существ из файла сервера;\n" +
                "import путь_к_файлу: считать существ из файла клиента;\n" +
                "save: сохранить существ в файл на сервере;\n" +
                "exit: завершить работу;\n" +
                "help: вывести помощь по всем командам.");
    }

    synchronized void save(Receiver receiver) {
        File saveFile = importFile;
        Gson gson = new Gson();
        try (OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(saveFile, false))) {

            osw.write(gson.toJson(Creatures));
            osw.flush();
            receiver.add("Коллекция сохранена в файл сервера " + saveFile.getAbsolutePath());
        } catch (IOException | NullPointerException e) {
            Date d = new Date();
            SimpleDateFormat formater = new SimpleDateFormat("MM.dd_hh:mm:ss");
            saveFile = new File("saveFile" + formater.format(d) + ".json");
            try (OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(saveFile, true))) {
                osw.write(gson.toJson(Creatures));
                osw.flush();
                receiver.add("Коллекция сохранена в файл " + saveFile.getAbsolutePath());
            } catch (IOException ex) {
                receiver.add("Сохранение коллекции не удалось");
            }
        }
    }

    void show(Receiver receiver) {
        receiver.add(Creatures.toString());
    }

    void clear(Receiver receiver, String token) {
        if (DBman.clearCreature(receiver, token))
            Creatures.clear();
        Creatures = DBman.synchronize(receiver);
    }
}
