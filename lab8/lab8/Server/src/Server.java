import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    static ConcurrentHashMap<String, ObjectOutputStream> clients = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() ->
                System.out.println("Выход")
        ));
        final int PORT = 5001;
        DataBaseManager DBman = new DataBaseManager();
        CollectionManager manager = new CollectionManager(DBman);
        try {
            ServerSocket server = new ServerSocket(PORT);
            System.out.println("Сервер запущен");
            System.out.println("Сервер работает клиентов(port = " + PORT + ")");
            while (true) {
                Socket client = server.accept();
                new Thread(new RequestsHandler(client, manager, DBman)).start();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    static void add(ObjectOutputStream client, String token) {
        clients.put(token, client);
    }

    static void remove(String token) {
        clients.remove(token);
    }

    static void remove(int i) {
        int count = 0;
        for (String keys : clients.keySet()) {
            if (count == i) {
                clients.remove(keys);
                break;
            }
            count++;
        }
    }
}