import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.time.format.DateTimeFormatter;

public class DataBaseManager {
    private Connection connection;

    DataBaseManager() {
        String url = "jdbc:postgresql://localhost:5432/test";
        String login = "postgres";
        String password = "postgresql";
        try {
            connection = DriverManager.getConnection(url, login, password);
            Class.forName("org.postgresql.Driver");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void connect() {

    }
    void addIfMax(Creature forAction){
        Object[] cr =Initialization(forAction);
//        ResultSet rs3 = stmt3.executeQuery("select count(*) from Creatures where  >10000");
       /* while(rs3.next()){
            count = rs3.getInt("count");
        select count(*) from Flights where flight_id >10000*/
    }
    boolean addCreature(Creature forAction) throws SQLException {
        Object[] cr = Initialization(forAction);
        String query = "INSERT INTO creatures(name, hunger, location, creation_time, family) VALUES(?, ?, '" + cr[2] + "', '" + cr[3] + "', ?)";
        PreparedStatement pst = connection.prepareStatement(query);
        pst.setString(1, (String) cr[0]);
        pst.setInt(2, (int) cr[1]);
        pst.setString(3, (String) cr[4]);
        pst.executeUpdate();
        pst.close();
        return true;

        /*while (rs.next()) {
            str = rs.getString("contact_id") + ":" + rs.getString(2);
        }*/
    }
    private Object[] Initialization(Creature forAction){
        Object[] creature = new Object[5];
        creature[0] = forAction.getName();
        creature[1] = forAction.getHunger();
        creature[2] = forAction.getLocation();
        creature[3] = forAction.getCreationTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
        creature[4] = forAction.getFamily();
        return creature;
    }
    public void exit() throws SQLException {
        connection.close();
    }
}
