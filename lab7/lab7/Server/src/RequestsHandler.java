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
    private Receiver receiver;
    private String login;

    RequestsHandler(Socket socket, CollectionManager manager, DataBaseManager DBman, int id) {
        this.client = socket;
        this.manager = manager;
        this.DBman = DBman;
        receiver = new Receiver(id);
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
                    request = new Request("", null);
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
                                exit = true;
                                throw new IOException();
                            } else if (login != null) {
                                oos.writeObject(receiver.get());
                                System.out.println(login + " отключён по таймауту");
                                exit();
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
                                        manager.clear(receiver, token);
                                        break;
                                    case "add":
                                        manager.add(creature, receiver, token);
                                        break;
                                    case "remove":
                                        manager.remove(creature, receiver, token);
                                        break;
                                    case "add_if_max":
                                        manager.addIfMax(creature, receiver, token);
                                        break;
                                    case "import":
                                        manager.load(str, receiver, token);
                                        break;
                                    case "load":
                                        manager.loadFile(file, receiver, token);
                                        break;
                                    case "save":
                                        manager.save(receiver);
                                        break;
                                    case "exit":
                                        String login = DBman.getLogin(token, receiver);
                                        if (login != null) {
                                            System.out.println(login + " отключился");
                                            Server.sendToAll(login + " отключился", receiver);
                                        } else {
                                            Server.sendToAll("Кто-то отключился", receiver);
                                            System.out.println("Кто-то отключился");
                                            receiver.add("Кажется вы замешаны в какой-то подозрительной активности");
                                        }
                                        justExit();
                                        break;
                                }
                                oos.writeObject(receiver.get());
                            }
                        } else {
                            Request answer = null;
                            boolean success;
                            switch (command) {
                                case "checkLogin":
                                    DBman.deleteDeadUsers();
                                    if (DBman.checkLogin(str, receiver)) {
                                        answer = new Request(receiver.get(), true);
                                    } else
                                        answer = new Request(receiver.get(), false);
                                    break;
                                case "logIn":
                                    if (DBman.logIn(str, password, receiver)) {
                                        System.out.println(str + " подключился");
                                        Server.sendToAll(str + " подключился", receiver);
                                        answer = new Request(receiver.get(), true);
                                    } else
                                        answer = new Request(receiver.get(), false);
                                    break;
                                case "checkEmail":
                                    success = DBman.checkEmail(str, receiver);
                                    answer = new Request(receiver.get(), success);
                                    break;
                                case "sendToken":
                                    success = DBman.sendToken(str, receiver);
                                    answer = new Request(receiver.get(), success);
                                    break;
                                case "signUp":
                                    if (DBman.signUp(str, password, receiver)) {
                                        answer = new Request(receiver.get(), true);
                                    } else {
                                        receiver.add("Не удалось зарегестрироваться");
                                        answer = new Request(receiver.get(), false);
                                    }
                                    break;
                            }
                            if (!exit) oos.writeObject(answer);
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
        justExit();
    }

    private void justExit() {
        Server.remove(receiver);
        exit = true;
    }

    Receiver getReceiver() {
        return receiver;
    }
}