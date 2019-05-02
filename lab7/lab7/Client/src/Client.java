import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        String login;
        String password;
        while (true){
            System.out.println("Первый раз здесь?(Y/N/exit)");
            Scanner scanner = new Scanner(System.in);
            String answer = scanner.nextLine();
            RequestsSender sender = new RequestsSender();
            switch (answer) {
                case "Y":
                    System.out.println("Введите почту:");
                    login = scanner.nextLine();
                    if(sender.checkLogin(login, 1)){
                        System.out.println("Придумайте и введите пароль:");
                        password = scanner.nextLine();
                        if(sender.signUp(login, password))
                        System.out.println("Вы удачно зарегестированны!");
                    }
                    continue;
                case "N":
                    System.out.println("Введите почту:");
                    login = scanner.nextLine();
                        if(!sender.checkLogin(login, 2)){
                            System.out.println("Введите пароль:");
                            password = scanner.nextLine();
                            if(sender.logIn(login, password))
                                sender.work();
                        }
                    continue;
                case "exit":
                    System.exit(1);
                    break;

            }
        }

    }
}
