import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;

public class Sender extends Thread {

    private SocketAddress server;
    private Handler handler = new Handler();
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private boolean exit;
    String token;
    private volatile boolean isWorking = false;

    Sender(SocketAddress server, String token, GUI gui) {
        this.token = token;
        this.server = server;
        handler.gui = gui;
        while(!connect()){}
        handler.start();
    }

    /**
     * Trying to connect infinitely to notify user when server is off without refreshing
     */
    @Override
    public void run() {
        while (!exit) {
            try {
                oos.writeObject("");
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            } catch (IOException e) {
                connect();
            }
        }
    }



    void getCollection() {
        try {
            oos.writeObject("get");
        } catch (IOException | NullPointerException e) {
            GUI.setConnectionInfo(false);
        }
    }
    void addCreature(Creature cr){
        try {
            while(!isWorking){}
            oos.writeObject(new Request("add", cr, token));
        }catch (IOException e){
            connect();
        }
    }

    private boolean connect() {
        SocketChannel sc;
        isWorking = false;
        try {
            sc = SocketChannel.open(server);
            oos = new ObjectOutputStream(sc.socket().getOutputStream());
            ois = new ObjectInputStream(sc.socket().getInputStream());
            handler.ois = ois;
            handler.oos = oos;
            GUI.setConnectionInfo(true);
            isWorking = true;
            return true;
        } catch (IOException e) {
            GUI.setConnectionInfo(false);
            handler.ois = null;
            handler.oos = null;
            while (!exit) {
                try {
                    Thread.sleep(1000);
                    sc = SocketChannel.open(server);
                    oos = new ObjectOutputStream(sc.socket().getOutputStream());
                    ois = new ObjectInputStream(sc.socket().getInputStream());
                    handler.ois = ois;
                    handler.oos = oos;
                    GUI.setConnectionInfo(true);
                    isWorking = true;
                    return true;
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                } catch (IOException ignored) {
                }
            }
            return false;
        }
    }
    void exit(){
        exit = true;
        handler.exit = true;
        handler=null;
    }
}
