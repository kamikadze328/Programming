import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    static ConcurrentHashMap<String, ObjectOutputStream> clients = new ConcurrentHashMap<>();
    private static ServerSocket server;

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() ->
                System.out.println("Выход")
        ));
        final int PORT = 5001;
        CollectionManager manager = null;
        DataBaseManager DBman = new DataBaseManager();
        try {
            if (args.length == 0)
                throw new ArrayIndexOutOfBoundsException("Имя файла должно передаваться программе с помощью аргумента командной строки.");
            manager = new CollectionManager(new File(args[0]), DBman);
            manager.loadFile(new File(args[0]), null);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


        try {
            server = new ServerSocket(PORT);
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