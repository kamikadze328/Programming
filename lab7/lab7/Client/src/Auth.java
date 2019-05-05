import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

class Auth {
    private SocketChannel server;
    static ObjectOutputStream oos;
    static ObjectInputStream ois;
    private String login;
    private String password;
    private Scanner scanner;
    private String token;

    Auth() {
        scanner = new Scanner(System.in);
    }

    void signUp() throws IOException {
        System.out.println("Введите почту:");
        login = scanner.nextLine();
        if (checkLogin(login, 1)) {
            System.out.println("Придумайте и введите пароль:");
            password = scanner.nextLine();
            if (signUp(login, password))
                System.out.println("Вы удачно зарегестированны!");
        }
    }

    void logIn() throws IOException {
        System.out.println("Введите почту:");
        login = scanner.nextLine();
        if (!checkLogin(login, 2)) {
            System.out.println("Введите пароль:");
            password = scanner.nextLine();
            if (logIn(login, password)) {
                RequestsSender sender = new RequestsSender(token);
                System.out.println("Добро пожаловать " + login);
                sender.work();
            }
        }
    }

    private boolean checkLogin(String login, int code) throws IOException {
        try {
            connect();
            oos.writeObject(new Request("checkLogin", login, null));
            String input = (String) ois.readObject();
            if (input.equals("1")) {
                if (code == 1)
                    return true;
                else {
                    System.out.println(input);
                    return true;
                }
            } else if (code == 1) {
                System.out.println(input);
                return false;
            } else
                return false;
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private boolean signUp(String login, String password) {
        try {
            oos.writeObject(new Request("signUp", login, password, null));
            String input = (String) ois.readObject();
            if (input.equals("1")) {
                return true;
            } else {
                System.out.println(input);
                return false;
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }


    private boolean logIn(String login, String password) {
        String input;
        try {
            oos.writeObject(new Request("logIn", login, password, null));
            input = (String) ois.readObject();
            if (input.length() == 30) {
                this.login = login;
                token = input;
                return true;
            } else {
                System.out.println(input);
                return false;
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }


    private void connect() throws IOException {
        server = SocketChannel.open(new InetSocketAddress("localhost", 5001));
        oos = new ObjectOutputStream(server.socket().getOutputStream());
        ois = new ObjectInputStream(server.socket().getInputStream());
    }

    void disconnect() {
        try {
            oos.close();
            ois.close();
            server.close();
        } catch (IOException ignored) {

        }
    }
}
