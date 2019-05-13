import java.io.File;
import java.io.Serializable;

class Request implements Serializable {
    private static final long serialVersionUID = 4831994753580089564L;
    String command;
    Creature creature;
    File file;
    String token;
    //Для файла сервера или логина
    String str;
    String password;
    boolean success;

    Request(String result, boolean success){
        this.str = result;
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

    Request(String command, File file, String token) {
        this.command = command;
        this.file = file;
        this.token = token;
    }

    Request(String command, String str, String token) {
        this.command = command;
        this.str = str;
        this.token = token;
    }
    Request(String command, String login, String password, String token) {
        this.command = command;
        this.str = login;
        this.password = password;
        this.token = token;
    }
}