import javax.swing.*;
import java.awt.*;
import java.net.InetSocketAddress;
import java.util.Locale;

public class MySwingWorker extends SwingWorker<Void, Void> {

    private Locale locale;
    private Color color;
    private String login;
    private String token;

    MySwingWorker(Locale locale, Color color, String login, String token) {
        this.locale = locale;
        this.color = color;
        this.login = login;
        this.token = token;
    }

    @Override
    protected Void doInBackground() {
        try {
            GUI gui = new GUI(locale, color, login);
            Sender sender = new Sender(new InetSocketAddress("localhost", 5001), token, gui);
            gui.sender = sender;
//            long time = System.currentTimeMillis();
            /*while(System.currentTimeMillis() - time < 1000){}
            sender.getCollection();*/
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}