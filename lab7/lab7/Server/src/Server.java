import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {

        final int PORT = 5001;
        CollectionManager manager = null;
        try {
            if (args.length == 0)
                throw new ArrayIndexOutOfBoundsException("\tимя файла должно передаваться программе с помощью аргумента командной строки.");
            manager = new CollectionManager(new File(args[0]));
            if (manager.loadFile(new File(args[0]))) System.out.println(manager.getReceiver());
            else {
                System.out.println(manager.getReceiver());
                System.exit(1);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }

        try {
            try (ServerSocket server = new ServerSocket(PORT)) {
                System.out.println("Сервер запущен");
                while (true) {
                    System.out.println("Сервер ожидает клиентов(port = " + PORT + ")");
                    Socket client = server.accept();
                    System.out.println("Подключён клиент:" + "" +
                            "\n\taddr = " + client.getInetAddress() + "," +
                            "\n\tport = " + client.getPort() + ".");
                    new Thread(new RequestsHandler(client, manager)).start();
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
