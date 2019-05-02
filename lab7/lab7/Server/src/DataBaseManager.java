import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.*;
import java.util.Properties;

class DataBaseManager {
    private final String URL = "jdbc:postgresql://localhost:5432/test";
    private final String LOGIN = "postgres";
    private final String PASSWORD = "postgresql";
    TokenFactory tokenFactory;

    DataBaseManager() {
        tokenFactory = new TokenFactory();
    }

    boolean addIfMax(Creature forAction) {
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
            if (add) addCreature(forAction);
            return add;
       /* while(rs3.next()){
            count = rs3.getInt("count");
        select count(*) from Flights where flight_id >10000*/
        } catch (SQLException e) {
            Receiver.add(e.getMessage());
            return false;
        }
    }

    boolean addCreature(Creature forAction) {
        Object[] cr = Initialization(forAction);
        String query = "INSERT INTO Creatures(name, hunger, location, creation_time, family) VALUES(?, ?, '" + cr[2] + "', '" + cr[3] + "', ?)";
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, (String) cr[0]);
            pst.setInt(2, (int) cr[1]);
            pst.setString(3, (String) cr[4]);
            int row = pst.executeUpdate();
            return row > 0;
        } catch (SQLException e) {
            Receiver.add(e.getMessage());
            return false;
        }
    }

    boolean removeCreature(Creature forAction) {
        Object[] cr = Initialization(forAction);
        String query = "DELETE FROM Creatures where name = ? AND family = ?";
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, (String) cr[0]);
            pst.setString(2, (String) cr[4]);
            int row = pst.executeUpdate();
            return row > 0;
        } catch (SQLException e) {
            Receiver.add(e.getMessage());
            return false;
        }
    }

    private Object[] Initialization(Creature forAction) {
        Object[] creature = new Object[5];
        creature[0] = forAction.getName();
        creature[1] = forAction.getHunger();
        creature[2] = forAction.getLocation();
        creature[3] = forAction.getCreationTime();
        creature[4] = forAction.getFamily();
        return creature;
    }

    boolean clearCreature() {
        String query = "DELETE FROM Creatures";
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             PreparedStatement pst = connection.prepareStatement(query)) {
            int rows = pst.executeUpdate();
            Receiver.add("Удалено " + rows + " Существ");
            return rows > 0;
        } catch (SQLException e) {
            Receiver.add(e.getMessage());
            return false;
        }
    }

    void info() {
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
                        Receiver.add("Размер базы данных " + name + ": " + size);
                    } else {
                        rs.next();
                        count = rs.getInt(1);
                        Receiver.add("Содержит " + count + " существ\n");
                    }
                    isMoreResult = pst.getMoreResults();
                }
            } while (isMoreResult);
        } catch (SQLException e) {
            Receiver.add(e.getMessage());
        }
    }

    //true, если такого логина нет
    boolean checkLogin(String login) {
        String query = "SELECT count(*) from users where login = '" + login + "'";
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             PreparedStatement pst = connection.prepareStatement(query)) {
            pst.execute();
            try (ResultSet rs = pst.getResultSet()) {
                rs.next();
                int count = rs.getInt(1);
                return count <= 0;
            }
        } catch (SQLException e) {
            Receiver.add(e.getMessage());
            return false;
        }
    }

    boolean signUp(String login, String password) {
        String query = "INSERT INTO Users(login, password) VALUES(?, ?)";
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, login);
            pst.setString(2, password);
            int row = pst.executeUpdate();
            return row > 0;
        } catch (SQLException e) {
            Receiver.add(e.getMessage());
            return false;
        }
    }

    boolean logIn(String login, String password) {
        String query = "SELECT password from users where login = '" + login + "'";
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             PreparedStatement pst = connection.prepareStatement(query)) {
            pst.execute();
            try (ResultSet rs = pst.getResultSet()) {
                rs.next();
                if (password.equals(rs.getString(1)))
                    return this.updateToken(login);
                else {
                    Receiver.add("Пароль неверный");
                    return false;
                }
            }
        } catch (SQLException e) {
            Receiver.add(e.getMessage());
            return false;
        }
    }

    void sendMessage(String login, String token) {
        Properties prop = System.getProperties();
        prop.setProperty("mail.smtp.host", "mail.se.ifmo.ru");
        Session sess = Session.getDefaultInstance(prop);
        try {
            MimeMessage msg = new MimeMessage(sess);
            msg.setFrom(new InternetAddress("s264434@niuitmo.ru"));
            msg.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(login));
            msg.setSubject("Your secret password");
            msg.setText("Password: T&^9hs09\21");
            Transport.send(msg);
        } catch (MessagingException e) {
        }
    }

    int checkToken(String token) {
        String query = "SELECT count(*) from users where token = '" + token + "'";
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             PreparedStatement pst = connection.prepareStatement(query)) {
            pst.execute();
            try (ResultSet rs = pst.getResultSet()) {
                rs.next();
                int count = rs.getInt(1);
                if (count > 0) {
                    PreparedStatement pst1 = connection.prepareStatement("SELECT time, login, user_id from users where token = '" + token + "'");
                    pst1.execute();
                    ResultSet rs1 = pst1.getResultSet();
                    rs1.next();
                    long time = rs1.getLong(1);
                    int userId = rs1.getInt(3);
                    if (System.currentTimeMillis() - time < 90000) {
                        this.updateTime(rs1.getString(2));
                        rs1.close();
                        pst1.close();
                        return userId;
                    } else {
                        rs1.close();
                        pst1.close();
                        Receiver.add("Вас не было слишком долго");
                        return -1;
                    }
                } else return count;
            }
        } catch (SQLException e) {
            Receiver.add(e.getMessage());
            return -1;
        }
    }
    boolean updateTime(String login){
        String token = tokenFactory.nextString();
        long time = System.currentTimeMillis();
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             PreparedStatement pst1 = connection.prepareStatement("UPDATE Users set time = " + time + " where login = '" + login + "'");
        ) {
            pst1.execute();
            return true;
        } catch (SQLException e) {
            Receiver.add("Неудачно");
            return false;
        }
    }
    boolean updateToken(String login) {
        String token = tokenFactory.nextString();
        long time = System.currentTimeMillis();
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             PreparedStatement pst1 = connection.prepareStatement("UPDATE Users set token = '" + token + "', time = " + time + " where login = '" + login + "'");
        ) {
            pst1.execute();
            Receiver.add(token);
            return true;
        } catch (SQLException e) {
            Receiver.add("Неудачно");
            return false;
        }
    }
}

