import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    static int countUsers;
    public static void main(String[] args) {

        final int PORT = 5001;
        CollectionManager manager = null;
        DataBaseManager DBman = new DataBaseManager();
        try {
            if (args.length == 0)
                throw new ArrayIndexOutOfBoundsException("\tимя файла должно передаваться программе с помощью аргумента командной строки.");
            manager = new CollectionManager(new File(args[0]), DBman);
            if (manager.loadFile(new File(args[0]))) System.out.println(Receiver.get());
            else {
                System.out.println(Receiver.get());
                System.exit(1);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }

        try {
            try (ServerSocket server = new ServerSocket(PORT)) {
                System.out.println("Сервер запущен");
                System.out.println("Сервер работает клиентов(port = " + PORT + ")");
                while (true) {
                    Socket client = server.accept();
                    countUsers++;
                    /*System.out.println("Подключён клиент:" + "" +
                            "\n\taddr = " + client.getInetAddress() + "," +
                            "\n\tport = " + client.getPort() + ".");*/
                    new Thread(new RequestsHandler(client, manager, DBman)).start();
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
