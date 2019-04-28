import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

public class RequestsHandler extends Thread {
    private Socket client;
    private CollectionManager manager;
    private boolean exit = false;

    RequestsHandler(Socket socket, CollectionManager manager) {
        this.client = socket;
        this.manager = manager;
        manager.trueExit();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {manager.save(); exit();}));
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
                    request = new Request("");
                }
                String command = request.command;
                Creature creature = request.creature;
                File file = request.file;
                CopyOnWriteArrayList<Creature> creatures = request.creatures;

                new Thread(() -> {
                    try {
                        switch (command) {
                            case ("info"):
                                oos.writeObject(manager.info());
                                break;
                            case "help":
                                oos.writeObject(manager.help());
                                break;
                            case "show":
                                oos.writeObject(manager.show());
                                break;
                            case "clear":
                                oos.writeObject(manager.clear());
                                break;
                            case "add":
                                oos.writeObject(manager.add(creature));
                                break;
                            case "remove":
                                oos.writeObject(manager.remove(creature));
                                break;
                            case "add_if_max":
                                oos.writeObject(manager.addIfMax(creature));
                                break;
                            case "import":
                                oos.writeObject(manager.add(creatures));
                                break;
                            case "load":
                                manager.loadFile(file);
                                oos.writeObject(manager.getReceiver());
                                break;
                            case "save":
                                oos.writeObject(manager.save());
                                break;

                            case "exit":
                                oos.writeObject(manager.save());
                                client.close();
                                exit();
                                break;
                        }
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                }).start();
            }
        } catch (IOException e) {
            System.out.println("Клиент отключился");
            System.out.println(e.getCause().toString());
        }
    }
    private void exit(){
        exit = true;
    }
}