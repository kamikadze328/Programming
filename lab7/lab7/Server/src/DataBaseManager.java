import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.time.format.DateTimeFormatter;

public class DataBaseManager {
    private Connection connection;
    private Statement statement;

    DataBaseManager() {
        String url = "jdbc:postgresql://localhost:5432/test";
        String login = "postgres";
        String password = "postgresql";
        try {
            connection = DriverManager.getConnection(url, login, password);
            statement = connection.createStatement();
            Class.forName("org.postgresql.Driver");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void connect() {

    }

    String addCreature(Creature forAction) throws SQLException{
        Object[] cr = Initialization(forAction);
        String str = null;
        ResultSet rs = statement.executeQuery("INSERT INTO Creatures VALUES ('"+cr[0]+"', "+cr[1]+", '"+cr[2]+"', '"+cr[3]+"', '"+cr[4]+"')");
        /*while (rs.next()) {
            str = rs.getString("contact_id") + ":" + rs.getString(2);
        }*/
        return str;
    }
    private Object[] Initialization(Creature forAction){
        Object[] creature = new Object[5];
        creature[0] = forAction.getName();
        creature[1] = forAction.getHunger();
        creature[2] = forAction.getLocation();
        creature[3] = forAction.getCreationTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ssX"));
        creature[4] = forAction.getFamily();
        return creature;
    }
    public void exit() throws SQLException {
        statement.close();
        connection.close();
    }
}
