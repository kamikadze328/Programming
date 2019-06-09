import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;

public class Handler extends Thread {
    ObjectInputStream ois;
    private GUI gui;
    private SocketAddress server;
    boolean exit = false;
    private boolean isWorking = false;
    private String token;
    private Sender sender;


    Handler(SocketAddress server, GUI gui, String token, Sender sender) {
        this.server = server;
        this.gui = gui;
        this.token = token;
        this.sender = sender;
    }

    @Override
    public void run() {
        connect();
        while (!sender.isWorking){}
        new Thread(sender::getCollection).start();
        while (!exit) {
            if (isWorking) {
                try {
                    Object message = ois.readObject();
                    new Thread(() -> {
                        if (message instanceof String) {
                            String request = (String) message;

                            switch (request) {
                                case "AddedSuccess":
                                case "RemovedSuccess":
                                case "SavedSuccess":
                                case "ChangedSuccess":
                                    gui.printTextToConsole(request, false);
                                    break;
                                case "AddedFailing":
                                case "RemovedFailingDontYours":
                                case "RemovedFailingDontExists":
                                case "ChangedFailed":
                                case "ChangedFailingDontYours":
                                case "SavedFailing":
                                case "loadFileError":
                                case "JSONError":
                                    gui.printTextToConsole(request, true);
                                    break;
                                case "exit":
                                    exit = true;
                                    break;
                                case "DeadToken":
                                    gui.printTextToConsole("windowWillBeClosed", true);
                                    try {
                                        Thread.sleep(5000);
                                    }catch (InterruptedException ignored){}
                                    gui.exit();
                            }
                            if (request.contains("DELETED: ")) {
                                gui.printTextToConsole(request.substring(0, 7).toLowerCase(), Integer.parseInt(request.substring(9)));
                            } else if (request.contains("ADDED: ")) {
                                gui.printTextToConsole(request.substring(0, 5).toLowerCase(), Integer.parseInt(request.substring(7)));
                            }
                        } else if (message instanceof Request) {
                            Request request = (Request) message;
                            gui.refreshCollection(request.creatures);
                        }
                    }).start();
                } catch (IOException | NullPointerException | ClassNotFoundException e) {
                    if (!exit)
                        connect();
                }
            } else connect();
        }
    }

    private void connect() {
        SocketChannel sc;
        isWorking = false;
        ObjectOutputStream oos;
        try {
            sc = SocketChannel.open(server);
            oos = new ObjectOutputStream(sc.socket().getOutputStream());
            ois = new ObjectInputStream(sc.socket().getInputStream());
            oos.writeObject(new Request("addUser", token));
            GUI.setConnectionInfo(true);
            isWorking = true;
            new Thread(sender::connect).start();
            Thread.sleep(500);
        } catch (IOException e) {
            GUI.setConnectionInfo(false);
            while (!isWorking&&!exit) {
                try {
                    Thread.sleep(1000);
                    sc = SocketChannel.open(server);
                    oos = new ObjectOutputStream(sc.socket().getOutputStream());
                    ois = new ObjectInputStream(sc.socket().getInputStream());
                    oos.writeObject(new Request("addUser", token));
                    GUI.setConnectionInfo(true);
                    new Thread(sender::connect).start();
                    Thread.sleep(500);
                    isWorking = true;
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                } catch (IOException ignored) {
                }
            }
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }
}
