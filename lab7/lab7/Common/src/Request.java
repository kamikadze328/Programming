import java.io.File;
import java.io.Serializable;

public class Request implements Serializable {
    private static final long serialVersionUID = 4831994753580089564L;
    public String command;
    public Creature creature;
    public File file;
    public String token;
    //Для файла сервера или логина
    public String str;
    public String password;
    boolean sucsess;

    public Request(String command, String token) {
        this.command = command;
        this.token = token;
    }

    public Request(String command, Creature creature, String token) {
        this.command = command;
        this.creature = creature;
        this.token = token;
    }

    public Request(String command, File file, String token) {
        this.command = command;
        this.file = file;
        this.token = token;
    }

    public Request(String command, String str, String token) {
        this.command = command;
        this.str = str;
        this.token = token;
    }
    public Request(String command, String login, String password, String token) {
        this.command = command;
        this.str = login;
        this.password = password;
        this.token = token;
    }
}