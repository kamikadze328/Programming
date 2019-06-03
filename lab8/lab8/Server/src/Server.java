import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

public class Server {

    private static ArrayList<Socket> clients = new ArrayList<>();
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
                System.out.println(manager.loadFile(new File(args[0]), null));
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


        try{
            server = new ServerSocket(PORT);
            System.out.println("Сервер запущен");
            System.out.println("Сервер работает клиентов(port = " + PORT + ")");
            while (true) {
                Socket client = server.accept();
                RequestsHandler kek = new RequestsHandler(client, manager, DBman);
                add(client);
                new Thread(kek).start();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    static void add(Socket client) {
        clients.add(client);
    }

    static void remove(Socket client) {
        clients.remove(client);
    }

    static void sendToAll(String message, Socket sender) {
        try {
//            clients.stream().forEach(p -> ser);

        } catch (ConcurrentModificationException ignored) {
        }
    }
}