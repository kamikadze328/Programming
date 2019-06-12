import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.concurrent.CopyOnWriteArrayList;

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
        try (ObjectInputStream ois = new ObjectInputStream(client.getInputStream())) {
            ObjectOutputStream oos1 = new ObjectOutputStream(client.getOutputStream());
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
                        CopyOnWriteArrayList<Creature> mbNewCreature = request.creatures;
                        ObjectOutputStream oos = Server.clients.get(token);
                        new Thread(() -> {
                            try {
                                boolean get = false;
                                boolean changed = false;
                                String answer;
                                answer = DBman.checkToken(token);
                                if (answer != null) {
                                    try {
                                        oos.writeObject(answer);
                                        Server.remove(token);
                                        oos.close();
                                    }catch (NullPointerException ignored){
                                        Server.remove(token);
                                    }
                                    justExit(token);
                                } else {
                                    switch (command) {
                                        case "get":
                                            CopyOnWriteArrayList<Creature> cr = manager.getCreatures();
                                            Request r = new Request(cr);
                                            oos.writeObject(r);
                                            get = true;
                                            break;

                                        case "clear":
                                            answer = manager.clear(token);
                                            if (Integer.parseInt(answer.substring(9)) > 0)
                                                changed = true;
                                            break;

                                        case "add":
                                            if (manager.add(creature, token)) {
                                                answer = "AddedSuccess";
                                                changed = true;
                                            } else
                                                answer = "AddedFailing";
                                            break;

                                        case "change":
                                            if (mbNewCreature == null)
                                                answer = manager.change(creature, token);
                                            else for (Creature creat : mbNewCreature) {
                                                if (!manager.change(creat, token).contains("Success"))
                                                    break;
                                                else answer = "ChangedSuccess";
                                            }

                                            if (answer.contains("Success"))
                                                changed = true;
                                            break;
                                        case "remove":
                                            answer = manager.remove(creature, token);
                                            if (answer.contains("Success"))
                                                changed = true;
                                            break;

                                        case "add_if_max":
                                            if (manager.addIfMax(creature, token)) {
                                                answer = "AddedSuccess";
                                                changed = true;
                                            } else
                                                answer = "AddedFailing";
                                            break;

                                        case "import":
                                            int added = manager.load(fileServer, token);
                                            if (added >= 0) {
                                                answer = "ADDED: " + added;
                                                if (added > 0) changed = true;
                                            } else
                                                answer = "JSONError";
                                            break;

                                        case "load":
                                            answer = manager.loadFile(fileClients, token);
                                            if (Integer.parseInt(answer.substring(7)) > 0)
                                                changed = true;
                                            break;

                                        case "save":
                                            if (manager.save())
                                                answer = "SavedSuccess";
                                            else
                                                answer = "SavedFailing";
                                            break;

                                        case "exit":
                                            justExit(token);
                                            break;

                                        case "addUser":
                                            get = true;
                                            if (Server.clients.containsKey(token))
                                                Server.remove(token);
                                            Server.add(oos1, token);
                                    }
                                    if (!exit && !get) {
                                        oos.writeObject(answer);
                                        if (changed) {
                                            int count = 0;
                                            try {
                                                CopyOnWriteArrayList<Creature> cr = manager.getCreatures();
                                                Request r = new Request(cr);
                                                for (ObjectOutputStream oos2 : Server.clients.values()) {
                                                    oos2.writeObject(r);
                                                    count++;
                                                }

                                            } catch (IOException e) {
                                                Server.remove(count);
                                                if (Server.clients.size() >= count)
                                                    sendToAll(count);
                                            }
                                        }
                                    }
                                }
                            } catch (IOException ignored) {
                            } catch (SQLException e) {
                                try {
                                    oos.writeObject("SQLException");
                                    e.printStackTrace();
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
                                            String color = "COLOR: " + DBman.getColor(login);
                                            oos1.writeObject(color);
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
                                            oos1.writeObject(color);
                                        }
                                        break;

                                    default:
                                        answer = "IncorrectCommand";
                                        break;
                                }
                                oos1.writeObject(answer);
                            } catch (SQLException e) {
                                try {
                                    oos1.writeObject("SQLException");
                                } catch (IOException ignored) {
                                }
                            } catch (IOException ignored) {
                            }
                        }).start();
                    }
                } catch (ClassNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            }
            client.close();
        } catch (IOException ignored) {
        }
    }

    private void sendToAll(int i) {
        int count = 0;
        try {
            CopyOnWriteArrayList<Creature> cr = manager.getCreatures();
            Request r = new Request(cr);
            for (ObjectOutputStream oos2 : Server.clients.values()) {
                if (count == i)
                    oos2.writeObject(r);
                count++;
            }
        } catch (IOException e) {
            Server.remove(count);
            if (Server.clients.size() >= count)
                sendToAll(count);
        }
    }

    private void justExit(String token) throws IOException{
        if(Server.clients.containsKey(token)) {
            Server.clients.get(token).writeObject("exit");
            Server.remove(token);
        }
        exit = true;
    }
}