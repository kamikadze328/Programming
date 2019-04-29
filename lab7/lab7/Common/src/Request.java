import java.io.File;
import java.io.Serializable;

public class Request implements Serializable {
    private static final long serialVersionUID = 4831994753580089564L;
    public String command;
    public Creature creature;
    public File file;
    public String jsonStr;

    public Request(String command) {
        this.command = command;
    }

    public Request(String command, Creature creature) {
        this.command = command;
        this.creature = creature;
    }

    public Request(String command, File file) {
        this.command = command;
        this.file = file;
    }

    public Request(String command, String jsonStr) {
        this.command = command;
        this.jsonStr = jsonStr;
    }
}