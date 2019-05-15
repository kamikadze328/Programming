import java.io.IOException;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() ->
                System.out.println("\nУходим")
        ));
        try {
            while (true) {
                try {
                    Scanner scanner = new Scanner(System.in);
                    System.out.println("Первый раз здесь?(Y/N/exit)");
                    String answer = scanner.nextLine();
                    Auth auth = new Auth();
                    switch (answer.toLowerCase()) {
                        case "y":
                            auth.signUp();
                            continue;
                        case "n":
                            auth.logIn();
                            auth.disconnect();
                            continue;
                        case "exit":
                            System.exit(1);
                            break;
                    }
                } catch (IOException e) {
                    System.out.println("Сервер недоступен");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Произошла ошибка, ухожу");
        }
    }
}
