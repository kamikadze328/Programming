import java.io.File;
import java.io.Serializable;

class Request implements Serializable {
    private static final long serialVersionUID = 4831994753580089564L;
    String command;
    Creature creature;
    File fileClients;
    String token;
    String fileServer;
    boolean success;

    Request(String result, boolean success){
        this.command = result;
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
        this.fileClients = file;
        this.token = token;
    }

    Request(String command, String fileServer, String token) {
        this.command = command;
        this.fileServer = fileServer;
        this.token = token;
    }
}