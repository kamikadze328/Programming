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
    private Request request;

    Auth() {
        scanner = new Scanner(System.in);
    }

    void signUp() throws IOException {
        System.out.println("Введите почту:");
        login = scanner.nextLine();
        System.out.println("Ожидайте\nКормим почтовую сову");
        if (checkLogin(1)) {
            System.out.println("Даём сове последние указания");
            if(sendToken()) {
                System.out.println("Сова отправлена\nВведите токен, отправленный на " + login + ":");
                String mailToken = scanner.nextLine();
                if (checkToken(mailToken)) {
                    System.out.println("С вас пароль, с нас доступ к секретным хранилищам:");
                    password = scanner.nextLine();
                    if (signUp(login, password))
                        System.out.println("Теперь у вас есть доступ к важнейшим знаниям человечества!");
                }
            }
        }
    }


    void logIn() throws IOException {
        System.out.println("Введите почту:");
        login = scanner.nextLine();
        System.out.println("Наши гномы ищут вас в своих списках");
        if (!checkLogin(2)) {
            System.out.println("Отлично!");
            System.out.println("Введите пароль:");
            password = scanner.nextLine();
            System.out.println("Звездосчёты проводят сложные вычисления");
            if (logIn(login, password)) {
                RequestsSender sender = new RequestsSender(token);
                System.out.println("Добро пожаловать " + login + ", чувствуйте себя как дома");
                sender.work();
            }
        }
    }

    private boolean checkToken(String mailToken) throws IOException {
        try {
            oos.writeObject(new Request("checkEmail", mailToken, null));
            request = (Request) ois.readObject();
            String input = request.str;
            if (input.length() > 0) {
                System.out.println(input);
                return false;
            }
            if (!request.success) {
                System.out.println("Токен не соответствует нашим ожиданиям, старайтесь больше");
                return false;
            } else return true;
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private boolean sendToken() throws IOException {
        try {
            oos.writeObject(new Request("sendToken", login, null));
            request = (Request) ois.readObject();
            String input = request.str;
            if (!request.success) System.out.println(input);
            return request.success;
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private boolean checkLogin(int code) throws IOException {
        try {
            connect();
            oos.writeObject(new Request("checkLogin", login, null));
            request = (Request) ois.readObject();
            String input = request.str;
            if (input.length() > 0) {
                System.out.println(input);
                return code != 1;
            } else if (code == 2 && request.success) {
                System.out.println("Хорошее имя! Но видимо от какого-то другого сайта");
            } else if (code == 1 && !request.success)
                System.out.println("Хорошее имя! Но у нас уже один такой.");
            return request.success;
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private boolean signUp(String login, String password) throws IOException {
        try {
            oos.writeObject(new Request("signUp", login, password, null));
            request = (Request) ois.readObject();
            String input = request.str;
            if (input.length() > 0 || !request.success) {
                System.out.println(input);
                return false;
            } else return true;
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private boolean logIn(String login, String password) throws IOException {
        try {
            oos.writeObject(new Request("logIn", login, password, null));
            request = (Request) ois.readObject();
            String input = request.str;
            if (request.success) {
                this.login = login;
                token = input;
                return true;
            } else {
                System.out.println(input);
                return false;
            }
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
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
