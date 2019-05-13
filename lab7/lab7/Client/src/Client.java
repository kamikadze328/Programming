import java.io.IOException;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            while (true) {
                try {
                    System.out.println("Первый раз здесь?(Y/N/exit)");
                    Scanner scanner = new Scanner(System.in);
                    String answer = scanner.nextLine();
                    Auth auth = new Auth();
                    switch (answer) {
                        case "Y":
                            auth.signUp();
                            continue;
                        case "N":
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
            System.out.println("Произошла ошибка, выход");
        }
    }
}
