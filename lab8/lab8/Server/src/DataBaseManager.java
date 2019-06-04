import org.bouncycastle.crypto.generators.SCrypt;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.awt.*;
import java.sql.*;
import java.time.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedList;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;


class DataBaseManager {

    private final String URL = "jdbc:postgresql://localhost:5432/test";
    private final String LOGIN = "postgres";
    private final String PASSWORD = "postgresql";
    private TokenFactory tokenFactory;
    private ArrayList<Color> colors = new ArrayList<>();

    DataBaseManager() {
        tokenFactory = new TokenFactory();
    }

    boolean addIfMax(Creature forAction, String token) throws SQLException {
        int lengthName = forAction.getName().length();
        boolean add = false;
        String query = "Select name from Creatures";
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             PreparedStatement pst = connection.prepareStatement(query)) {
            boolean isResult;
            pst.execute();
            do {
                try (ResultSet rs = pst.getResultSet()) {
                    if (rs.isBeforeFirst()) {
                        while (rs.next()) {
                            if (rs.getString(1).length() < lengthName)
                                add = true;
                        }
                        isResult = pst.getMoreResults();
                    } else {
                        isResult = false;
                    }
                }
            } while (isResult && add);
            if (add) addCreature(forAction, token);
            return add;
        }
    }

    boolean addCreature(Creature forAction, String token) throws SQLException {
        String query = "INSERT INTO Creatures(name, hunger, location, creation_time, family, user_id, x, y, size, color) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             PreparedStatement pst = connection.prepareStatement(query)) {
            String name = forAction.getName();
            String family = forAction.getFamily();
            pst.setString(1, name);
            pst.setInt(2, forAction.getHunger());
            pst.setObject(3, forAction.getLocation().toString(), Types.OTHER);
            OffsetDateTime time = forAction.getCreationTime();
            pst.setTimestamp(4, Timestamp.valueOf(LocalDateTime.ofInstant(time.toInstant(), ZoneOffset.UTC)));
            pst.setString(5, family);
            Long userId = getUserId(token);
            if (userId == null) pst.setLong(6, 1);
            else pst.setLong(6, userId);
            pst.setInt(7, forAction.getX());
            pst.setInt(8, forAction.getY());
            pst.setInt(9, forAction.getSize());
            pst.setString(10, forAction.getColor().toString());
            int row = pst.executeUpdate();
            LinkedList<String> inventory = forAction.getInventory();
            for (String s : inventory) addInventory(s, name, family);
            return row > 0;
        }
    }

    private void addInventory(String inventory, String name, String family) throws SQLException {
        String query = "INSERT INTO Inventory(creature_id, inventory) VALUES(?, ?)";
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setLong(1, getCreatureId(name, family));
            pst.setString(2, inventory);
            pst.executeUpdate();
        }
    }

    private Long getCreatureId(String name, String family) throws SQLException {
        String query = "SELECT creature_id from creatures where name = ? AND family = ?";
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, name);
            pst.setString(2, family);
            pst.execute();
            try (ResultSet rs = pst.getResultSet()) {
                rs.next();
                return rs.getLong(1);
            }
        }
    }

    String change(Creature newCreature, String token) throws SQLException {
        Creature oldCreature;
        String query = "SELECT * from creatures where creature_id = ?";
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, newCreature.getId());
            pst.execute();
            try (ResultSet rs = pst.getResultSet()) {
                if (rs.isBeforeFirst()) {
                    while (rs.next()) {
                        String name = rs.getString(1);
                        int hunger = rs.getInt(2);
                        String location = rs.getString(3);
                        Timestamp time = rs.getTimestamp(4);
                        String family = rs.getString(5);
                        Long creatureId = rs.getLong(7);
                        int x = rs.getInt(8);
                        int y = rs.getInt(9);
                        int size = rs.getInt(10);
                        String color = rs.getString(11);
                        oldCreature = initFromDataBase(name, hunger, location, time, family, creatureId, x, y, size, color);
                        String answer = removeCreature(oldCreature, token);
                        if (answer.contains("Success")) {
                            newCreature.setCreationTime(OffsetDateTime.ofInstant(Instant.ofEpochMilli(time.getTime()), ZoneId.of("UTC")));
                            if (addCreature(newCreature, token))
                                return "ChangedSuccess";
                            else while (!addCreature(oldCreature, token)) {
                            }
                            return "ChangedFailed";
                        } else if(answer.contains("DontYours"))
                            return "ChangedFailingDontYours";
                    }
                }
            }
            return "ChangedFailedDontExist";
        }

    }

    String removeCreature(Creature forAction, String token) throws SQLException {
        String query = "SELECT user_id from Creatures where name = ? AND family = ?";
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             PreparedStatement pst = connection.prepareStatement(query)) {
            String name = forAction.getName();
            String family = forAction.getFamily();
            pst.setString(1, name);
            pst.setString(2, family);
            pst.execute();
            try (ResultSet rs = pst.getResultSet()) {
                if (rs.isBeforeFirst()) {
                    rs.next();
                    if (getUserId(token) == rs.getLong(1)) {
                        try (PreparedStatement pst1 = connection.prepareStatement("DELETE FROM Creatures cascade where name = ? AND family = ?")) {
                            pst1.setString(1, name);
                            pst1.setString(2, family);
                            pst1.executeUpdate();
                            return "RemovedSuccess";
                        }
                    } else {
                        return "RemovedFailingDontYours";
                    }
                } else {
                    return "RemovedFailingDontExists";
                }
            }
        }
    }


    int clearCreature(String token) throws SQLException {
        String query = "DELETE FROM Creatures where user_id = ?";
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setLong(1, getUserId(token));
            return pst.executeUpdate();
        }
    }

    /**
     * @param login login
     * @return true, if login exists
     */
    boolean checkLogin(String login) throws SQLException {
        String query = "SELECT count(*) from users where login = ?";
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, login);
            pst.execute();
            try (ResultSet rs = pst.getResultSet()) {
                rs.next();
                int count = rs.getInt(1);
                return count > 0;
            }
        }
    }


    String signUp(String login, String password) throws SQLException {
        String query = "UPDATE Users SET password = ?, color = ? WHERE login = ?";
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             PreparedStatement pst = connection.prepareStatement(query)) {
            Color color = newColor();
            pst.setString(1, generate(password));
            pst.setString(2, Integer.toString(color.getRGB()));
            pst.setString(3, login);
            int row = pst.executeUpdate();
            if (row > 0)
                return "TOKEN: " + getToken(login);
            else
                return "SignUpError";
        }
    }

    private Color newColor() {
        switch (colors.size()) {
            case 0:
                colors.add(Colors.Purple.getRgbColor());
                return Colors.Purple.getRgbColor();
            case 1:
                colors.add(Colors.FernGreen.getRgbColor());
                return Colors.FernGreen.getRgbColor();
            case 2:
                colors.add(Colors.PareGold.getRgbColor());
                return Colors.PareGold.getRgbColor();
            case 3:
                colors.add(Colors.DeepRed.getRgbColor());
                return Colors.DeepRed.getRgbColor();
            case 4:
                colors.add(Colors.Black.getRgbColor());
                return Colors.Black.getRgbColor();
            case 5:
                colors.add(Colors.White.getRgbColor());
                return Colors.White.getRgbColor();

            default:
                while (true) {
                    Color color = new Color((int) (Math.random() * 0x1000000));
                    if (colors.stream().noneMatch(color::equals)) {
                        colors.add(color);
                        return color;
                    }
                }
        }
    }

    /**
     * @param login    login
     * @param password password
     * @return "WrongPassword" or token
     * @throws SQLException SQlEx
     */
    String logIn(String login, String password) throws SQLException {
        String query = "SELECT password from users where login = ?";
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, login);
            pst.execute();
            try (ResultSet rs = pst.getResultSet()) {
                rs.next();
                if (generate(password).equals(rs.getString(1)))
                    return "TOKEN: " + getToken(login);
                else
                    return "WrongPassword";
            }
        }
    }

    boolean sendToken(String login) throws SQLException {
        String token = tokenFactory.nextString();
        if (sendMessage(login, token)) {
            String query = "INSERT INTO Users(login, token, time) VALUES(?, ?, ?)";
            try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
                 PreparedStatement pst = connection.prepareStatement(query)) {
                pst.setString(1, login);
                pst.setString(2, generate(token));
                pst.setLong(3, System.currentTimeMillis());
                pst.executeUpdate();
                return true;
            }
        } else
            return false;
    }

    private boolean sendMessage(String login, String token) {
        String from = "your.father@99cows.com";
        String host = "mail.99cows.com";
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", host);
        Session session = Session.getDefaultInstance(properties);
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(login));
            message.setSubject("New mems");
            message.setText("Blin-blinskiy, new mems are over, zato tyt est` kakoy-to Token:   " + token);
            Transport.send(message);
            return true;
        } catch (MessagingException mex) {
            return false;
        }
    }

    /**
     * Проверяет токен и, если верный, то обновляет время его жизни.
     *
     * @param token токен
     * @return "DeadToken" - если таймаут кончился. "WrongToken" - если токен неверный. null - всё хорошо
     * @throws SQLException SQLEx
     */
    String checkToken(String token) throws SQLException {
        String query = "SELECT time, login from users where token = ?";
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, generate(token));
            pst.execute();
            try (ResultSet rs = pst.getResultSet()) {
                if (rs.isBeforeFirst()) {
                    rs.next();
                    long time = rs.getLong(1);
                    String login = rs.getString(2);
                    if (System.currentTimeMillis() - time < 90000) {
                        updateTime(login);
                        return null;
                    } else
                        return "DeadToken";
                } else
                    return "WrongToken";
            }
        }
    }

    void deleteDeadUsers() {
        String query = "Delete from users where password is null";
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             PreparedStatement pst = connection.prepareStatement(query)) {
            pst.execute();
        } catch (SQLException ignored) {
        }
    }

    /**
     * Проверяет токен, отправленный на почту. Если не подтверждён, то удаляет строку из таблицы.
     *
     * @param token token
     * @return если почта подтверждена - "EmailCorrect" . Иначе состояние токена из checkToken
     * @throws SQLException SQLEx
     */
    String checkEmail(String token) throws SQLException {
        String login = checkToken(token);
        if (login == null) {
            return "EmailCorrect";
        } else {
            String query = "DELETE FROM Users where token = ?";
            try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
                 PreparedStatement pst = connection.prepareStatement(query)) {
                pst.setString(1, generate(token));
                pst.executeUpdate();
                return login;
            }
        }
    }

    private void updateTime(String login) throws SQLException {
        long time = System.currentTimeMillis();
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             PreparedStatement pst = connection.prepareStatement("UPDATE Users set time = ? where login = ?")
        ) {
            pst.setLong(1, time);
            pst.setString(2, login);
            pst.execute();
        }
    }

    private String getToken(String login) throws SQLException {
        String token = tokenFactory.nextString();
        long time = System.currentTimeMillis();
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             PreparedStatement pst = connection.prepareStatement("UPDATE Users set token = ?, time = ? where login = ?")) {
            pst.setString(1, generate(token));
            pst.setLong(2, time);
            pst.setString(3, login);
            pst.execute();
            return token;
        }
    }

    private String generate(String password) {
        byte[] hashByte = SCrypt.generate(password.getBytes(), "salt".getBytes(), 8, 8, 8, 7);
        return Base64.getEncoder().encodeToString(hashByte);

    }

    private Long getUserId(String token) throws SQLException {
        if (token == null) return null;
        String query = "SELECT user_id from Users where token = ?";
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, generate(token));
            pst.execute();
            try (ResultSet rs = pst.getResultSet()) {
                rs.next();
                return rs.getLong(1);
            }
        }
    }

    String getColor(String login) throws SQLException {
        String query = "SELECT color from users where login = ? and color is not null";
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, login);
            pst.execute();
            try (ResultSet rs = pst.getResultSet()) {
                rs.next();
                return rs.getString(1);
            }
        }
    }

    CopyOnWriteArrayList<Creature> synchronize() {
        String query = "SELECT * from Creatures";
        CopyOnWriteArrayList<Creature> Creatures;
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             PreparedStatement pst = connection.prepareStatement(query)) {
            synchronizeUsers();
            pst.execute();
            try (ResultSet rs = pst.getResultSet()) {
                Creatures = new CopyOnWriteArrayList<>();
                if (rs.isBeforeFirst()) {
                    while (rs.next()) {
                        String name = rs.getString(1);
                        int hunger = rs.getInt(2);
                        String location = rs.getString(3);
                        Timestamp time = rs.getTimestamp(4);
                        String family = rs.getString(5);
                        Long creatureId = rs.getLong(7);
                        int x = rs.getInt(8);
                        int y = rs.getInt(9);
                        int size = rs.getInt(10);
                        String color = rs.getString(11);
                        Creatures.add(initFromDataBase(name, hunger, location, time, family, creatureId, x, y, size, color));
                    }
                }
            }
            return Creatures;
        } catch (SQLException e) {
            return new CopyOnWriteArrayList<>();
        }
    }

    private void synchronizeUsers() throws SQLException {
        String query = "SELECT * from users where color is not null";
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             PreparedStatement pst = connection.prepareStatement(query)) {
            pst.execute();
            try (ResultSet rs = pst.getResultSet()) {
                if (rs.isBeforeFirst()) {
                    while (rs.next()) {
                        colors.add(new Color(Integer.parseInt(rs.getString(1))));
                    }
                }
            }
        }
    }

    private Creature initFromDataBase(String name, int hunger, String locationStr, Timestamp timestamp, String family, Long creatureId, int x, int y, int size, String colorStr) throws SQLException {
        Location location;
        switch (locationStr) {
            case "TopFloor":
                location = Location.TopFloor;
                break;
            case "GroundFloor":
                location = Location.GroundFloor;
                break;
            case "Yard":
                location = Location.Yard;
                break;
            case "Hill":
                location = Location.Hill;
                break;
            case "Hangar":
                location = Location.Hangar;
                break;
            case "FootPath":
                location = Location.FootPath;
                break;
            case "LightHouse":
                location = Location.LightHouse;
                break;
            default:
                location = Location.NaN;
        }
        Colors color;
        switch (colorStr) {
            case "FernGreen":
                color = Colors.FernGreen;
                break;
            case "White":
                color = Colors.White;
                break;
            case "PareGold":
                color = Colors.PareGold;
                break;
            case "DeepRed":
                color = Colors.DeepRed;
                break;
            case "Purple":
                color = Colors.Purple;
                break;
            default:
                color = Colors.Black;
        }
        OffsetDateTime time = OffsetDateTime.ofInstant(Instant.ofEpochMilli(timestamp.getTime()), ZoneId.of("UTC"));
        Creature creature = new Creature(name, hunger, location, time, family, x, y, size, color);
        String query1 = "SELECT inventory from Inventory where creature_id = ?";
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             PreparedStatement pst = connection.prepareStatement(query1)) {
            pst.setLong(1, creatureId);
            pst.execute();
            try (ResultSet rs1 = pst.getResultSet()) {
                if (rs1.isBeforeFirst()) {
                    while (rs1.next())
                        creature.inventory.add(rs1.getString(1));
                }
            }
        }
        return creature;
    }
}



