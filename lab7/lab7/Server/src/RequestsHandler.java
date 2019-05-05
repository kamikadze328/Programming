import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class RequestsHandler extends Thread {
    private Socket client;
    private CollectionManager manager;
    private DataBaseManager DBman;
    private boolean exit = false;
    private boolean logIn = false;
    private Receiver receiver;
    private String login;

    RequestsHandler(Socket socket, CollectionManager manager, DataBaseManager DBman, int id) {
        this.client = socket;
        this.manager = manager;
        this.DBman = DBman;
        receiver = new Receiver(id);
        manager.trueExit();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            manager.save(receiver);
            exit();
        }));
    }

    @Override
    public void run() {
        try (ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
             ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream())) {
            Request request;
            while (!exit) {
                try {
                    request = (Request) ois.readObject();
                } catch (ClassNotFoundException e) {
                    System.out.println(e.getMessage());
                    request = new Request("", "0");
                }

                String command = request.command;
                Creature creature = request.creature;
                File file = request.file;
                String str = request.str;
                String password = request.password;
                String token = request.token;

                new Thread(() -> {

                    try {
                        if (token != null) {
                            login = DBman.checkToken(token, receiver);
                            if (login != null && login.equals("-1")) {
                                oos.writeObject(receiver.get());
                                exit();
//                                client.close();
                                logIn = true;
                                throw new IOException();
                            } else if (login != null) {
                                oos.writeObject(receiver.get());
                                System.out.println(login + " отключён по таймауту");

                                exit();
//                                client.close();
                                logIn = true;
                                throw new IOException();
                            } else {
                                switch (command) {
                                    case ("info"):
                                        manager.info(receiver);
                                        break;
                                    case "help":
                                        manager.help(receiver);
                                        break;
                                    case "show":
                                        manager.show(receiver);
                                        break;
                                    case "clear":
                                        manager.clear(receiver);
                                        break;
                                    case "add":
                                        manager.add(creature, receiver);
                                        break;
                                    case "remove":
                                        manager.remove(creature, receiver);
                                        break;
                                    case "add_if_max":
                                        manager.addIfMax(creature, receiver);
                                        break;
                                    case "import":
                                        manager.load(str, receiver);
                                        break;
                                    case "load":
                                        manager.loadFile(file, receiver);
                                        break;
                                    case "save":
                                        manager.save(receiver);
                                        break;
                                    case "exit":
                                        logIn();
                                        String login = DBman.getLogin(token, receiver);
                                        if (login != null) {
                                            System.out.println(login + " отключился");
                                            Server.sendToAll(str + " подключился", receiver);
                                            receiver.add("0");
                                        } else {
                                            Server.sendToAll("Кто-то подключился", receiver);
                                            System.out.println("Кто-то отключился");
                                        }
                                        oos.writeObject(receiver.get());
//                                        client.close();
                                        exit();
                                        break;
                                }
                                if (!exit) oos.writeObject(receiver.get());
                            }
                        } else {
                            switch (command) {
                                case "checkLogin":
                                    logIn();
                                    if (DBman.checkLogin(str, receiver)) {
                                        receiver.add("1");
                                    } else receiver.add("Логин занят");
                                    break;
                                case "signUp":
                                    logIn();
                                    if (DBman.signUp(str, password, receiver)) {
                                        receiver.add("1");
                                    } else receiver.add("Не удалось зарегестрироваться");
                                    break;
                                case "logIn":
                                    logIn();
                                    if (DBman.logIn(str, password, receiver)) {
                                        System.out.println(str + " подключился");
                                        Server.sendToAll(str + " подключился", receiver);
                                        Server.add(receiver);
                                    }
                            }
                            if (!exit) oos.writeObject(receiver.get());
                        }
                    } catch (IOException ignored) {
                    }
                }).start();
            }
            client.close();
        } catch (IOException ignored) {
        }
    }

    private void exit() {
        Server.sendToAll(login + " отключился по таймауту", receiver);
        Server.remove(receiver);
        exit = true;
    }

    private void logIn() {
        logIn = true;
    }
}