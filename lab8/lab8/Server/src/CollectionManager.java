import java.io.File;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CopyOnWriteArrayList;

class CollectionManager {

    private CopyOnWriteArrayList<Creature> Creatures;
    private DataBaseManager DBman;
    private String initTime;

    CollectionManager(DataBaseManager DBman, Receiver receiver) {
        this.DBman = DBman;
        Creatures = DBman.synchronize(receiver);
        initTime = OffsetDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ssX"));
    }

    void remove(Creature forAction, Receiver receiver, String token) {
        if (DBman.removeCreature(forAction, receiver, token)) {
            Creatures.remove(forAction);
            receiver.add(forAction.toString().replace("\n", "") + " удалён.");
        }
    }

    void addIfMax(Creature forAction, Receiver receiver, String token) {
        if (DBman.addIfMax(forAction, receiver, token)) {
            Creatures.add(forAction);
            receiver.add((forAction.toString().replace("\n", "") + " добавлен, т.к. является наибольшим"));
        } else
            receiver.add(forAction.toString().replace("\n", "") + " не является наибольшим");
    }

    boolean add(Creature forAction, Receiver receiver, String token) {
        if (DBman.addCreature(forAction, receiver, token)) {
            Creatures.add(forAction);
            receiver.add(forAction.toString().replace("\n", "") + " добавлен");
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
                "show: показать текущих существ;\n" +
                "clear: удалить всех существ(или нет);\n" +
                "info: вывести информацию о базе данных и коллекции;\n" +
                "load путь_к_файлу: загрузить существ из файла сервера;\n" +
                "import путь_к_файлу: загрузить существ из файла клиента;\n" +
                "save: сохранить существ в файл на сервере;\n" +
                "exit: завершить работу;\n" +
                "help: вывести помощь по всем командам.");
    }

    synchronized void save(Receiver receiver) {

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
