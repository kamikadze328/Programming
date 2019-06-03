import javax.print.DocFlavor;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;

public class RequestsHandler extends Thread {
    private Socket client;
    private CollectionManager manager;
    private DataBaseManager DBman;
    private boolean exit = false;

    RequestsHandler(Socket socket, CollectionManager manager, DataBaseManager DBman) {
        this.client = socket;
        this.manager = manager;
        this.DBman = DBman;
    }

    @Override
    public void run() {
        try (ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
             ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream())) {
            while (!exit) {
                try {
                    Object message = ois.readObject();
                    if (message instanceof Request) {
                        Request request = (Request) message;
                        String command = request.command;
                        Creature creature = request.creature;
                        File fileClients = request.fileClients;
                        String fileServer = request.fileServer;
                        String token = request.token;
                        new Thread(() -> {
                            try {
                                boolean get = false;
                                String answer;
                                answer = DBman.checkToken(token);
                                if (answer != null) {
                                    oos.writeObject(answer);
                                    justExit();
                                } else {
                                    switch (command) {
                                        case "get":
                                            oos.writeObject(manager.getCreatures());
                                            get = true;
                                            break;

                                        case "clear":
                                            answer = manager.clear(token);
                                            break;

                                        case "add":
                                            if (manager.add(creature, token))
                                                answer = "AddedSuccess";
                                            else
                                                answer = "AddedFailing";
                                            break;

                                        case "remove":
                                            answer = manager.remove(creature, token);
                                            break;

                                        case "add_if_max":
                                            if (manager.addIfMax(creature, token))
                                                answer = "AddedSuccess";
                                            else
                                                answer = "AddedFailing";
                                            break;

                                        case "import":
                                            int added = manager.load(fileServer, token);
                                            answer = "ADDED: " + added;
                                            break;

                                        case "load":
                                            answer = manager.loadFile(fileClients, token);
                                            break;

                                        case "save":
                                            if (manager.save())
                                                answer = "SavedSuccess";
                                            else
                                                answer = "SavedFailing";
                                            break;

                                        case "exit":
                                            justExit();
                                            break;
                                    }
                                    if (!exit && !get) oos.writeObject(answer);
                                }

                            } catch (IOException ignored) {
                            } catch (SQLException e) {
                                try {
                                    oos.writeObject("SQLException");
                                } catch (IOException ignored) {
                                }
                            }
                        }).start();
                    } else if (message instanceof User) {
                        User user;
                        user = (User) message;
                        String command = user.command;
                        String login = user.login;
                        String password = user.password;
                        String token = user.token;
                        new Thread(() -> {
                            try {
                                String answer;
                                switch (command) {
                                    case "logIn":
                                        DBman.deleteDeadUsers();
                                        if (DBman.checkLogin(login)) {
                                            answer = DBman.logIn(login, password);
                                            /*if (!answer.equals("WrongPassword"))
                                                Server.add(client);*/
                                        } else
                                            answer = "LoginDoesntExist";
                                        break;

                                    case "signUp":
                                        DBman.deleteDeadUsers();
                                        if (!DBman.checkLogin(login)) {
                                            if (DBman.sendToken(login))
                                                answer = "TokenSent";
                                            else
                                                answer = "WrongEmailAddress";
                                            break;
                                        } else
                                            answer = "LoginExists";
                                        break;

                                    case "checkEmail":
                                        answer = DBman.checkEmail(token);
                                        if (answer.equals("EmailCorrect"))
                                            answer = DBman.signUp(login, password);
                                        if(answer.contains("TOKEN: ")){
                                            String color = "COLOR: " + DBman.getColor(login);
                                            oos.writeObject(color);
                                        }
                                        break;

                                    default:
                                        answer = "IncorrectCommand";
                                        break;
                                }
                                oos.writeObject(answer);
                            } catch (SQLException e) {
                                try {
                                    oos.writeObject("SQLException");
                                } catch (IOException ignored) {
                                }
                            } catch (IOException ignored) {
                            }
                        }).start();
                    }
                } catch (ClassNotFoundException e) {
                    System.out.println(e.getMessage());
                    try {
                        oos.writeObject("WTF");
                    } catch (IOException ignored) {
                    }
                } catch (IOException ignored) {

                }
            }
            client.close();
        } catch (IOException ignored) {
        }
    }

    private void justExit() {
        Server.remove(client);
        exit = true;
    }
}