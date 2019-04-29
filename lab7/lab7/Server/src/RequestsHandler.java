import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class RequestsHandler extends Thread {
    private Socket client;
    private CollectionManager manager;
    private boolean exit = false;

    RequestsHandler(Socket socket, CollectionManager manager) {
        this.client = socket;
        this.manager = manager;
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
                    request = new Request("");
                }
                String command = request.command;
                Creature creature = request.creature;
                File file = request.file;
                String jsonStr = request.jsonStr;

                new Thread(() -> {
                    try {
                        switch (command) {
                            case ("info"):
                                manager.info();
                                oos.writeObject(Receiver.get());
                                break;
                            case "help":
                                manager.help();
                                oos.writeObject(Receiver.get());
                                break;
                            case "show":
                                oos.writeObject(manager.show());
                                break;
                            case "clear":
                                manager.clear();
                                oos.writeObject(Receiver.get());
                                break;
                            case "add":
                                manager.add(creature);
                                oos.writeObject(Receiver.get());
                                break;
                            case "remove":
                                manager.remove(creature);
                                oos.writeObject(Receiver.get());
                                break;
                            case "add_if_max":
                                manager.addIfMax(creature);
                                oos.writeObject(Receiver.get());
                                break;
                            case "import":
                                manager.load(jsonStr);
                                oos.writeObject(Receiver.get());
                                break;
                            case "load":
                                manager.loadFile(file);
                                oos.writeObject(Receiver.get());
                                break;
                            case "save":
                                manager.save();
                                oos.writeObject(Receiver.get());
                                break;
                            case "exit":
                                manager.save();
                                oos.writeObject(Receiver.get());
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
            //Надо убрать
            System.out.println(e.getCause().toString());
        }
    }

    private void exit() {
        exit = true;
    }
}