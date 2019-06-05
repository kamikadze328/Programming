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
    private ObjectOutputStream oos;

    RequestsHandler(Socket socket, CollectionManager manager, DataBaseManager DBman) {
        this.client = socket;
        this.manager = manager;
        this.DBman = DBman;
    }

    @Override
    public void run() {
        try (ObjectInputStream ois = new ObjectInputStream(client.getInputStream())) {
            oos = new ObjectOutputStream(client.getOutputStream());
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
                                boolean changed = false;
                                boolean ok = false;
                                String answer;
                                answer = DBman.checkToken(token);
                                if (answer != null) {
                                    oos.writeObject(answer);
                                    justExit();
                                } else {
                                    for(ObjectOutputStream oos1 : Server.clients){
                                        if(oos.equals(oos1))
                                            ok=true;
                                    }
                                    if (!ok)
                                        Server.clients.add(oos);
                                    switch (command) {
                                        case "get":
                                            oos.writeObject(new Request(manager.getCreatures()));
                                            get = true;
                                            break;

                                        case "clear":
                                            answer = manager.clear(token);
                                            if(Integer.parseInt(answer.substring(9))>0)
                                                changed = true;
                                            break;

                                        case "add":
                                            if (manager.add(creature, token)) {
                                                answer = "AddedSuccess";
                                                changed = true;
                                                oos.writeObject(new Request(manager.getCreatures()));
                                            } else
                                                answer = "AddedFailing";
                                            break;

                                        case "change":
                                            answer = manager.change(creature, token);
                                            if(answer.contains("Success"))
                                                changed=true;
                                            break;
                                        case "remove":
                                            answer = manager.remove(creature, token);
                                            if(answer.contains("Success"))
                                                changed=true;
                                            break;

                                        case "add_if_max":
                                            if (manager.addIfMax(creature, token)){
                                                answer = "AddedSuccess";
                                                changed = true;
                                            }
                                            else
                                                answer = "AddedFailing";
                                            break;

                                        case "import":
                                            int added = manager.load(fileServer, token);
                                            if (added >= 0){
                                                answer = "ADDED: " + added;
                                                if(added>0) changed=true;
                                            }
                                            else
                                                answer = "JSONError";
                                            break;

                                        case "load":
                                            answer = manager.loadFile(fileClients, token);
                                            if(Integer.parseInt(answer.substring(7))>0)
                                                changed = true;
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
                                    if (!exit && !get) {
                                        oos.writeObject(answer);
                                        if (changed) {
                                            int count = 0;
                                            try {
                                                for (ObjectOutputStream sender : Server.clients) {
                                                    sender.writeObject(new Request(manager.getCreatures()));
                                                    count++;
                                                }
                                            }catch (IOException e){
                                                Server.clients.remove(count);
                                                if(Server.clients.size()>= count)
                                                sendToAll(count);
                                            }
                                        }
                                    }
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
                                            if (!answer.equals("WrongPassword"))
                                                Server.add(oos);
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
                                        if (answer.contains("TOKEN: ")) {
                                            String color = "COLOR: " + DBman.getColor(login);
                                            oos.writeObject(color);
                                            Server.add(oos);
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
                    justExit();
                }
            }
            client.close();
        } catch (IOException ignored) {
        }
    }

    private void sendToAll(int count) {
        try {
            for (int i = count; i <= Server.clients.size(); i++) {
                Server.clients.get(i).writeObject(new Request(manager.getCreatures()));
                count++;
            }
        } catch (IOException e) {
            Server.clients.remove(count);
            if (Server.clients.size() >= count)
                sendToAll(count);
        }
    }

    private void justExit() throws IOException {
        Server.remove(oos);
        oos.close();
        exit = true;
    }
}