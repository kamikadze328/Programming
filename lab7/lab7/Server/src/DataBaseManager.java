import org.bouncycastle.crypto.generators.SCrypt;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.Properties;


class DataBaseManager {
    private final String URL = "jdbc:postgresql://localhost:5432/test";
    private final String LOGIN = "postgres";
    private final String PASSWORD = "postgresql";
    private TokenFactory tokenFactory;

    DataBaseManager() {
        tokenFactory = new TokenFactory();
    }

    boolean addIfMax(Creature forAction, Receiver receiver) {
        int lengthName = forAction.getName().length();
        boolean add = false;
        String query = "Select name from Creatures";
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             PreparedStatement pst = connection.prepareStatement(query)) {
            boolean isResult;
            pst.execute();
            do {
                try (ResultSet rs = pst.getResultSet()) {
                    while (rs.next()) {
                        if (rs.getString(1).length() < lengthName)
                            add = true;
                    }
                    isResult = pst.getMoreResults();
                }
            } while (isResult && add);
            if (add) addCreature(forAction, receiver);
            return add;
        } catch (SQLException e) {
            receiver.add(e.getMessage());
            return false;
        }
    }

    boolean addCreature(Creature forAction, Receiver receiver) {
        Object[] cr = Initialization(forAction);
        String query = "INSERT INTO Creatures(name, hunger, location, creation_time, family) VALUES(?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, (String) cr[0]);
            pst.setInt(2, (int) cr[1]);
            pst.setObject(3, cr[2], Types.OTHER);
            pst.setString(5, (String) cr[4]);
            OffsetDateTime time = (OffsetDateTime) cr[3];
            pst.setTimestamp(4, Timestamp.valueOf(LocalDateTime.ofInstant(time.toInstant(), ZoneOffset.UTC)));
            int row = pst.executeUpdate();
            return row > 0;
        } catch (SQLException e) {
            receiver.add(e.getMessage());
            return false;
        }
    }

    boolean removeCreature(Creature forAction, Receiver receiver) {
        Object[] cr = Initialization(forAction);
        String query = "DELETE FROM Creatures where name = ? AND family = ?";
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, (String) cr[0]);
            pst.setString(2, (String) cr[4]);
            int row = pst.executeUpdate();
            return row > 0;
        } catch (SQLException e) {
            receiver.add(e.getMessage());
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

    boolean clearCreature(Receiver receiver) {
        String query = "DELETE FROM Creatures";
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             PreparedStatement pst = connection.prepareStatement(query)) {
            int rows = pst.executeUpdate();
            receiver.add("Удалено " + rows + " Существ");
            return rows > 0;
        } catch (SQLException e) {
            receiver.add(e.getMessage());
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
            receiver.add(e.getMessage());
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
            receiver.add(e.getMessage());
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
            receiver.add(e.getMessage());
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
            receiver.add(e.getMessage());
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
                receiver.add(e.getMessage());
                return false;
            }
        }
        return false;
    }

    private boolean sendMessage(String login, String token, Receiver receiver) {
        String from = "adalei.vy@0hcow.com";
        String host = "mail.0hcow.com";
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", host);
        Session session = Session.getDefaultInstance(properties);
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(login));
            message.setSubject("Your secret token");
            message.setText("Token:\t" + token);
            Transport.send(message);
            return true;
        } catch (MessagingException mex) {
            receiver.add(mex.getMessage());
            mex.printStackTrace();
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
            }
        } catch (SQLException e) {
            receiver.add(e.getMessage());
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
                receiver.add(e.getMessage());
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

    private boolean updateToken(String login, Receiver receiver) {
        String token = tokenFactory.nextString();
        long time = System.currentTimeMillis();
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             PreparedStatement pst = connection.prepareStatement("UPDATE Users set token = ?, time = ? where login = ?")
        ) {
            pst.setString(1, generate(token));
            pst.setLong(2, time);
            pst.setString(3, login);
            pst.execute();
            receiver.add(token);
            return true;
        } catch (SQLException e) {
            receiver.add(e.getMessage());
            return false;
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
            receiver.add(e.getMessage());
            return null;
        }
    }
}


