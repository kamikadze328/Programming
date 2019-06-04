import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

class CollectionManager {

    private File importFile;
    private CopyOnWriteArrayList<Creature> Creatures;
    private DataBaseManager DBmanager;

    CollectionManager(File file, DataBaseManager DBmanager) {
        importFile = file;
        this.DBmanager = DBmanager;
        Creatures = DBmanager.synchronize();
    }

    String loadFile(File file, String token) throws SQLException{
        try {
            if (file == null || !(file.isFile()) || !(file.exists()) || !file.canRead())
                throw new IOException();
            String JsonString = readFromFile(file);
            int added = load(JsonString, token);
            if (added >= 0)
                return "ADDED: " + added;
            else
                return "JSONError";
        } catch (NullPointerException | IOException ex) {
            return "loadFileError";
        }
    }

    /**
     *
     * @param JsonString строка в формате json
     * @param token token
     * @return количество добавленных существ
     * @throws SQLException
     */
    int load(String JsonString, String token) throws SQLException{
        try {
            return parser(JsonString.split("},\\{"), token);
        } catch (JsonSyntaxException ex) {
            return -1;
        }
    }

    private String readFromFile(File file) throws IOException {
        StringBuilder jsonStr = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(new BufferedInputStream(new FileInputStream(file))));
        String line;
        while ((line = r.readLine()) != null) jsonStr.append(line);
        jsonStr = new StringBuilder(jsonStr.substring(1, jsonStr.length() - 1));
        return jsonStr.toString();
    }

    private int parser(String[] line, String token) throws SQLException, JsonSyntaxException {
        ArrayList<Creature> tempCr = new ArrayList<>();
        boolean oneParse = false;
        if (line.length == 1) oneParse = true;
        int added = 0;
        Gson gson = new Gson();
        for (int i = 0; i < line.length; i++) {
            if (i == 0 && !oneParse) line[i] = line[i] + "}";
            else if (i == line.length - 1 && !oneParse) line[i] = "{" + line[i];
            else if (line.length > 1) line[i] = "{" + line[i] + "}";
            if (!line[i].equals("") && line[i].contains("\"family\"") && (line[i].contains("\"name\""))) {
                Creature forAction = gson.fromJson(line[i], Creature.class);
                tempCr.add(forAction);
            }
        }
        for (Creature creature : tempCr)
            if (add(creature, token)) added++;
        return added;
    }

    String remove(Creature forAction, String token) throws SQLException {
        String answer = DBmanager.removeCreature(forAction, token);
        if (answer.contains("Success")){
            Creatures.remove(forAction);
        }
        return answer;
    }
    String change(Creature forAction, String token)throws SQLException {
        String answer = DBmanager.change(forAction, token);
        if (answer.contains("Success")) {
            Creatures.removeIf(forAction::equals);
            Creatures.add(forAction);
        }
        return answer;
    }

    boolean addIfMax(Creature forAction, String token) throws SQLException {
        if (DBmanager.addIfMax(forAction, token)) {
            Creatures.add(forAction);
            return true;
        } else
            return false;
    }

    boolean add(Creature forAction, String token) throws SQLException{
        if (DBmanager.addCreature(forAction, token)) {
            Creatures.add(forAction);
            return true;
        }
        return false;
    }


    synchronized boolean save() {
        File saveFile = importFile;
        Gson gson = new Gson();
        try (OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(saveFile, false))) {
            osw.write(gson.toJson(Creatures));
            osw.flush();
            return true;
        } catch (IOException | NullPointerException e) {
            Date d = new Date();
            SimpleDateFormat formater = new SimpleDateFormat("MM.dd_hh:mm:ss");
            saveFile = new File("saveFile" + formater.format(d) + ".json");
            try (OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(saveFile, true))) {
                osw.write(gson.toJson(Creatures));
                osw.flush();
                return true;
            } catch (IOException ex) {
                return false;
            }
        }
    }

    String clear(String token) throws SQLException {
        int deleted = DBmanager.clearCreature(token);
        if (deleted> 0)
            Creatures.clear();
        Creatures = DBmanager.synchronize();
        return "DELETED: " + deleted;
    }

    List<Creature> getCreatures() {
        return Creatures;
    }


}