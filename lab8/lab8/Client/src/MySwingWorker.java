import javax.swing.*;
import java.net.InetSocketAddress;
import java.util.Locale;

public class MySwingWorker extends SwingWorker<Void, Void> {

    private Locale locale;
    private User user;

    MySwingWorker(Locale locale, User user) {
        this.user = user;
        this.locale = locale;
    }

    @Override
    protected Void doInBackground() {
        try {
            Connector connector = new Connector(new InetSocketAddress("localhost", 5001));
            connector.start();
            new GUI(connector, locale, user);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}