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
    private boolean logIn =false;
    private String token;
    int userId;

    RequestsHandler(Socket socket, CollectionManager manager, DataBaseManager DBman) {
        this.client = socket;
        this.manager = manager;
        this.DBman = DBman;
        manager.trueExit();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            manager.save();
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
                        switch (command) {
                            case ("info"):
                                if(DBman.checkToken(token) > 0)
                                    manager.info();
                                else{
                                    oos.writeObject(Receiver.get());
                                    client.close();
                                }
                                break;
                            case "help":
                                if(DBman.checkToken(token) > 0)
                                    manager.help();
                                else{
                                    oos.writeObject(Receiver.get());
                                    client.close();
                                }
                                break;
                            case "show":
                                if(DBman.checkToken(token) > 0)
                                    manager.show();
                                else{
                                    oos.writeObject(Receiver.get());
                                    client.close();
                                }
                                break;
                            case "clear":
                                userId = DBman.checkToken(token);
                                if(userId > 0)
                                    manager.clear();
                                else{
                                    oos.writeObject(Receiver.get());
                                    client.close();
                                }
                                break;
                            case "add":
                                userId = DBman.checkToken(token);
                                if(userId > 0)
                                    manager.add(creature);
                                else{
                                    oos.writeObject(Receiver.get());
                                    client.close();
                                }
                                break;
                            case "remove":
                                userId = DBman.checkToken(token);
                                if(userId > 0)
                                    manager.remove(creature);
                                else{
                                    oos.writeObject(Receiver.get());
                                    client.close();
                                }
                                break;
                            case "add_if_max":
                                userId = DBman.checkToken(token);
                                if(userId > 0)
                                    manager.addIfMax(creature);
                                else{
                                    oos.writeObject(Receiver.get());
                                    client.close();
                                }
                                break;
                            case "import":
                                userId = DBman.checkToken(token);
                                if(userId > 0)
                                    manager.load(str);
                                else{
                                    oos.writeObject(Receiver.get());
                                    client.close();
                                }
                                break;
                            case "load":
                                userId = DBman.checkToken(token);
                                if(userId > 0)
                                    manager.loadFile(file);
                                else{
                                    oos.writeObject(Receiver.get());
                                    client.close();
                                }
                                break;
                            case "save":
                                userId = DBman.checkToken(token);
                                if(userId > 0)
                                    manager.save();
                                else{
                                    oos.writeObject(Receiver.get());
                                    client.close();
                                }
                                break;
                            case "exit":
                                manager.save();
                                oos.writeObject(Receiver.get());
                                client.close();
                                exit();
                                break;

                            case "checkLogin":
                                logIn = true;
                                if(DBman.checkLogin(str)){
                                    Receiver.add("1");
                                }else Receiver.add("Логин занят");
                                break;
                            case "signUp":
                                logIn = true;
                                if(DBman.signUp(str, password)){
                                    Receiver.add("1");
                                }else Receiver.add("Не удалось зарегестрироваться");
                                break;
                            case "logIn":
                                logIn = true;
                                DBman.logIn(str, password);
                        }
                        if (!exit) oos.writeObject(Receiver.get());
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                }).start();
            }
        } catch (IOException e) {
            if(!logIn) {
                System.out.println("Клиент отключился");
                logIn = false;
            }
        }
    }
    private void exit() {
        exit = true;
    }
}