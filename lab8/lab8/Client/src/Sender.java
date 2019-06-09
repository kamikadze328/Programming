import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;

class Sender {

    private SocketAddress server;
    private Handler handler;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private boolean exit;
    private String token;
    private GUI gui;
    volatile boolean isWorking = false;

    Sender(SocketAddress server, String token, GUI gui) {
        this.token = token;
        this.server = server;
        this.gui = gui;
        handler = new Handler(server, gui, token, this);
        handler.start();
    }

    void getCollection() {
        try {
            oos.writeObject(new Request("get", token));
        } catch (IOException | NullPointerException e) {
            gui.printTextToConsole("tryAgain", true);
            GUI.setConnectionInfo(false);
        }
    }

    void addCreature(Creature cr) {
        try {
            if (isWorking) oos.writeObject(new Request("add", cr, token));
            else gui.printTextToConsole("tryAgain", true);
        } catch (IOException e) {
            gui.printTextToConsole("tryAgain", true);
            connect();
        }
    }

    void removeCreature(Creature cr) {
        try {
            if (isWorking)
                oos.writeObject(new Request("remove", cr, token));
            else gui.printTextToConsole("tryAgain", true);
        } catch (IOException e) {
            gui.printTextToConsole("tryAgain", true);
            connect();
        }
    }

    void changeCreature(Creature cr) {
        try {
            if (isWorking) oos.writeObject(new Request("change", cr, token));
            else gui.printTextToConsole("tryAgain", true);
        } catch (IOException e) {
            gui.printTextToConsole("tryAgain", true);
            connect();
        }
    }

    void connect() {
        SocketChannel sc;
        isWorking = false;
        try {
            if(!exit) {
                sc = SocketChannel.open(server);
                oos = new ObjectOutputStream(sc.socket().getOutputStream());
                ois = new ObjectInputStream(sc.socket().getInputStream());
                GUI.setConnectionInfo(true);
                isWorking = true;
            }
        } catch (IOException e) {
            GUI.setConnectionInfo(false);
            while (!exit) {
                try {
                    Thread.sleep(1000);
                    sc = SocketChannel.open(server);
                    oos = new ObjectOutputStream(sc.socket().getOutputStream());
                    ois = new ObjectInputStream(sc.socket().getInputStream());
                    GUI.setConnectionInfo(true);
                    isWorking = true;
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                } catch (IOException ignored) {
                }
            }
        }
    }

    void exit() {
        exit = true;
        try {
            oos.writeObject(new Request("exit", token));
        } catch (IOException ignored) {
        }
        try {
            handler.exit = true;
            handler.ois.close();
        } catch (IOException ignored) {
        }
        try {
            oos.close();
            ois.close();
        } catch (IOException ignored) {
        }
    }

    void addIfMaxCreature(Creature cr) {
        try {
            if (isWorking) oos.writeObject(new Request("add_if_max", cr, token));
            else gui.printTextToConsole("tryAgain", true);
        } catch (IOException e) {
            gui.printTextToConsole("tryAgain", true);
            connect();
        }
    }
}
