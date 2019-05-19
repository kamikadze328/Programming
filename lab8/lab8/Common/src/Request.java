import java.io.Serializable;

class Request implements Serializable {
    private static final long serialVersionUID = 4831994753580089564L;
    String command;
    Creature creature;
    String token;
    //Для файла сервера или логина
    String login;
    String password;
    boolean success;

    Request(String result, boolean success){
        this.login = result;
        this.success = success;
    }

    Request(String command, String token) {
        this.command = command;
        this.token = token;
    }

    Request(String command, Creature creature, String token) {
        this.command = command;
        this.creature = creature;
        this.token = token;
    }

    Request(String command, String str, String token) {
        this.command = command;
        this.login = str;
        this.token = token;
    }

    Request(String command, String login, String password, String token) {
        this.command = command;
        this.login = login;
        this.password = password;
        this.token = token;
    }
}