import java.io.Serializable;

public class Request implements Serializable {
    private static final long serialVersionUID = 4831994753580089564L;
    String command;
    Creature creature;
    String token;
    String login;
    String password;
    boolean success;

    public Request(String result, boolean success){
        this.login = result;
        this.success = success;
    }

    public Request(String command, String token) {
        this.command = command;
        this.token = token;
    }

    public Request(String command, Creature creature, String token) {
        this.command = command;
        this.creature = creature;
        this.token = token;
    }

    public Request(String command, String str, String token) {
        this.command = command;
        this.login = str;
        this.token = token;
    }

    public Request(String command, String login, String password, String token) {
        this.command = command;
        this.login = login;
        this.password = password;
        this.token = token;
    }
}