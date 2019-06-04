import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Handler extends Thread {
    ObjectInputStream ois;
    ObjectOutputStream oos;
    GUI gui;
    boolean exit = false;

    Handler(Sender sender) {
        ois = sender.ois;
        oos = sender.oos;
    }

    @Override
    public void run() {
        while (!exit) {
            try {
                Object message = ois.readObject();
                new Thread(() -> {
                    if (message instanceof String) {
                        String request = (String) message;

                        switch (request) {
                            case "AddedSuccess":
                            case "RemovedSuccess":
                            case "SavedSuccess":
                                gui.printTextToConsole(request, false);
                                break;
                            case "AddedFailing":
                            case "RemovedFailingDontYours":
                            case "RemovedFailingDontExists":
                            case "SavedFailing":
                            case "loadFileError":
                            case "JSONError":
                                gui.printTextToConsole(request, true);
                                break;
                        }
                        if(request.contains("DELETED: ")){
                            gui.printTextToConsole(request.substring(0,7).toLowerCase(), Integer.parseInt(request.substring(9)));
                        } else if(request.contains("ADDED: ")){
                            gui.printTextToConsole(request.substring(0,5).toLowerCase(), Integer.parseInt(request.substring(7)));

                        }


                    } else if (message instanceof Request) {
                        Request request = (Request) message;
                        gui.refreshCollection(request.creatures);
                    }
                }).start();
            } catch (IOException | NullPointerException | ClassNotFoundException ignored) {
            }
        }
    }
}
