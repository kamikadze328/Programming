
import java.sql.*;

class DataBaseManager {
    private final String URL = "jdbc:postgresql://localhost:5432/test";
    private final String LOGIN = "postgres";
    private final String PASSWORD = "postgresql";


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
                        Receiver.add("Содержит " + count + " существ");
                    }
                    isMoreResult = pst.getMoreResults();
                }
            } while (isMoreResult);
        } catch (SQLException e) {
            Receiver.add(e.getMessage());
        }

    }
}
