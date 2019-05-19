import org.bouncycastle.crypto.generators.SCrypt;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.*;
import java.time.*;
import java.util.Base64;
import java.util.LinkedList;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;


class DataBaseManager {

    private final String URL = "jdbc:postgresql://localhost:5432/test";
    private final String LOGIN = "postgres";
    private final String PASSWORD = "postgresql";
    private TokenFactory tokenFactory;

    DataBaseManager() {
        tokenFactory = new TokenFactory();
    }

    boolean addIfMax(Creature forAction, Receiver receiver, String token) {
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
            if (add) addCreature(forAction, receiver, token);
            return add;
        } catch (SQLException e) {
            receiver.add(sqlException(e.getMessage()));
            return false;
        }
    }

    boolean addCreature(Creature forAction, Receiver receiver, String token) {
        String query = "INSERT INTO Creatures(name, hunger, location, creation_time, family, user_id) VALUES(?, ?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             PreparedStatement pst = connection.prepareStatement(query)) {
            Object[] cr = Initialization(forAction);
            pst.setString(1, (String) cr[0]);
            pst.setInt(2, (int) cr[1]);
            pst.setObject(3, cr[2], Types.OTHER);
            OffsetDateTime time = (OffsetDateTime) cr[3];
            pst.setTimestamp(4, Timestamp.valueOf(LocalDateTime.ofInstant(time.toInstant(), ZoneOffset.UTC)));
            pst.setString(5, (String) cr[4]);
            Long userId = getUserId(token);
            if (userId == null) pst.setLong(6, 1);
            else pst.setLong(6, userId);
            int row = pst.executeUpdate();
            LinkedList<String> inventory = forAction.getInventory();
            for (String s : inventory) addInventory(s, (String) cr[0], (String) cr[4]);
            return row > 0;
        } catch (SQLException e) {
            receiver.add(sqlException(e.getMessage()));
            return false;
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

    boolean removeCreature(Creature forAction, Receiver receiver, String token) {
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
                            return pst1.executeUpdate() > 0;
                        }
                    } else {
                        receiver.add("ОШИБКА: Существо не пренадлежит вам!");
                        return false;
                    }
                } else {
                    receiver.add("ОШИБКА: Такого существа не существует!");
                    return false;
                }
            }
        } catch (SQLException e) {
            receiver.add(sqlException(e.getMessage()));
            return false;
        }
    }

    private Object[] Initialization(Creature forAction) {
        Object[] creature = new Object[5];
        creature[0] = forAction.getName();
        creature[1] = forAction.getHunger();
        creature[2] = forAction.getLocation().toString();
        creature[3] = forAction.getCreationTime();
        creature[4] = forAction.getFamily();
        return creature;
    }

    boolean clearCreature(Receiver receiver, String token) {
        String query = "DELETE FROM Creatures where user_id = ?";
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setLong(1, getUserId(token));
            int rows = pst.executeUpdate();
            receiver.add("Удалено " + rows + " Существ");
            return rows > 0;
        } catch (SQLException e) {
            receiver.add(sqlException(e.getMessage()));
            return false;
        }
    }

    void info(Receiver receiver) {
        String query = "SELECT pg_size_pretty(pg_database_size(current_database()));"
                + "Select current_database();"
                + "SELECT COUNT(*) FROM creatures";
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             PreparedStatement pst = connection.prepareStatement(query)) {
            pst.execute();
            boolean isMoreResult = false;
            String size = null;
            String name = null;
            int count;
            do {
                try (ResultSet rs = pst.getResultSet()) {
                    if (!isMoreResult) {
                        rs.next();
                        size = rs.getString(1);
                    } else if (name == null) {
                        rs.next();
                        name = rs.getString(1);
                        receiver.add("Размер базы данных " + name + ": " + size);
                    } else {
                        rs.next();
                        count = rs.getInt(1);
                        receiver.add("Содержит " + count + " существ");
                    }
                    isMoreResult = pst.getMoreResults();
                }
            } while (isMoreResult);
        } catch (SQLException e) {
            receiver.add(sqlException(e.getMessage()));
        }
    }

    //true, если такого логина нет
    boolean checkLogin(String login, Receiver receiver) {
        String query = "SELECT count(*) from users where login = ?";
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, login);
            pst.execute();
            try (ResultSet rs = pst.getResultSet()) {
                rs.next();
                int count = rs.getInt(1);
                return count <= 0;
            }
        } catch (SQLException e) {
            receiver.add(sqlException(e.getMessage()));
            return false;
        }
    }

    boolean signUp(String login, String password, Receiver receiver) {
        String query = "UPDATE Users SET password = ? WHERE login = ?";
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, generate(password));
            pst.setString(2, login);
            int row = pst.executeUpdate();
            return row > 0;
        } catch (SQLException e) {
            receiver.add(sqlException(e.getMessage()));
            return false;
        }
    }

    boolean logIn(String login, String password, Receiver receiver) {
        String query = "SELECT password from users where login = ?";
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, login);
            pst.execute();
            try (ResultSet rs = pst.getResultSet()) {
                rs.next();
                if (generate(password).equals(rs.getString(1))) {
                    return updateToken(login, receiver);
                } else {
                    receiver.add("Пароль неверный");
                    return false;
                }
            }
        } catch (SQLException e) {
            receiver.add(sqlException(e.getMessage()));
            return false;
        }
    }

    boolean sendToken(String login, Receiver receiver) {
        String token = tokenFactory.nextString();
        if (sendMessage(login, token, receiver)) {
            String query = "INSERT INTO Users(login, token, time) VALUES(?, ?, ?)";
            try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
                 PreparedStatement pst = connection.prepareStatement(query)) {
                pst.setString(1, login);
                pst.setString(2, generate(token));
                pst.setLong(3, System.currentTimeMillis());
                int row = pst.executeUpdate();
                return row > 0;
            } catch (SQLException e) {
                receiver.add(sqlException(e.getLocalizedMessage()));
                return false;
            }
        }
        return false;
    }

    private boolean sendMessage(String login, String token, Receiver receiver) {
        String from = "your.father@0hcow.com";
        String host = "mail.0hcow.com";
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
            String message = mex.getMessage();
            if (message.contains("Invalid Addresses"))
                receiver.add("Неверный почтовый адрес");
            else receiver.add(message);
            return false;
        }
    }

    String checkToken(String token, Receiver receiver) {
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
                        this.updateTime(login, receiver);
                        return null;
                    } else {
                        receiver.add("Ваше время истекло");
                        return login;
                    }
                } else return "-1";
            }
        } catch (SQLException e) {
            receiver.add(sqlException(e.getLocalizedMessage()));
            return "-1";
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

    boolean checkEmail(String token, Receiver receiver) {
        String login = checkToken(token, receiver);
        if (login == null) {
            return true;
        } else {
            String query = "DELETE FROM Users where token = ?";
            try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
                 PreparedStatement pst = connection.prepareStatement(query)) {
                pst.setString(1, generate(token));
                pst.executeUpdate();
                return false;
            } catch (SQLException e) {
                receiver.add(sqlException(e.getMessage()));
                return false;
            }
        }
    }

    private void updateTime(String login, Receiver receiver) {
        long time = System.currentTimeMillis();
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             PreparedStatement pst = connection.prepareStatement("UPDATE Users set time = ? where login = ?")
        ) {
            pst.setLong(1, time);
            pst.setString(2, login);
            pst.execute();
        } catch (SQLException e) {
            receiver.add("Неудачно");
        }
    }

    private boolean updateToken(String login, Receiver receiver) throws SQLException {
        String token = tokenFactory.nextString();
        long time = System.currentTimeMillis();
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             PreparedStatement pst = connection.prepareStatement("UPDATE Users set token = ?, time = ? where login = ?")) {
            pst.setString(1, generate(token));
            pst.setLong(2, time);
            pst.setString(3, login);
            pst.execute();
            receiver.add(token);
            return true;
        }
    }

    private String generate(String password) {
        byte[] hashByte = SCrypt.generate(password.getBytes(), "salt".getBytes(), 8, 8, 8, 7);
        return Base64.getEncoder().encodeToString(hashByte);
    }

    String getLogin(String token, Receiver receiver) {
        String query = "SELECT login from users where token = ?";
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, generate(token));
            pst.execute();
            try (ResultSet rs = pst.getResultSet()) {
                rs.next();
                return rs.getString(1);
            }
        } catch (SQLException e) {
            receiver.add(sqlException(e.getMessage()));
            return null;
        }
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

    CopyOnWriteArrayList<Creature> synchronize(Receiver receiver) {
        String query = "SELECT * from Creatures";
        CopyOnWriteArrayList<Creature> Creatures;
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             PreparedStatement pst = connection.prepareStatement(query)) {
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
                        Long userId = rs.getLong(7);
                        Creatures.add(initFromDataBase(name, hunger, location, time, family, userId));
                    }
                }
            }
            return Creatures;
        } catch (SQLException e) {
            receiver.add(sqlException(e.getMessage()));
            return new CopyOnWriteArrayList<>();
        }
    }

    private Creature initFromDataBase(String name, int hunger, String locationStr, Timestamp timestamp, String family, Long userId) throws SQLException {
        Location location;
        String[] inventory;
        switch (locationStr) {
            case "TopFloor":
                location = Location.TopFloor;
                break;
            case "GroudFloor":
                location = Location.GroudFloor;
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
        OffsetDateTime time = OffsetDateTime.ofInstant(Instant.ofEpochMilli(timestamp.getTime()), ZoneId.of("UTC"));
        Creature creature = new Creature(name, hunger, location, time, family);
        String query1 = "SELECT inventory from Inventory where creature_id = ?";
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             PreparedStatement pst = connection.prepareStatement(query1)) {
            pst.setLong(1, userId);
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

    private String sqlException(String exception) {
        String[] message = exception.split("Подробности: ");
        if (message.length > 1) return "ОШИБКА:" + message[1];
        else return message[0];
    }
}


