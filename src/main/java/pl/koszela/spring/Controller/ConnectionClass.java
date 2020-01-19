package pl.koszela.spring.Controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionClass {

    private void hmm() throws SQLException {
        String template = "jdbc:mysql://kubus.omss.pl/nowoczesnebudowanie_pl?useEncoding=true&characterEncoding=UTF-8&user=nowoczesnebudowanie_pl&password=r7PPQvd3KJPH";
        String url = "jdbc:mysql://kubus.omss.pl:3307/nowoczesnebudowanie_pl";
        String username = "nowoczesnebudowanie_pl";
        String password = "r7PPQvd3KJPH";
        String testUrl = "jdbc:mysql://serwer1548369.home.pl:3306/18653183_0000064";
        String testUsername ="18653183_0000064";
        String testPassword = "ILSD&WQWO,;V";
        final String DATABASE_DRIVER = "com.mysql.cj.jdbc.Driver";

        System.out.println("Connecting database...");
        Connection con = null;
//        System.out.println(con.isClosed());
        try {
            Class.forName(DATABASE_DRIVER);
//            con = DriverManager.getConnection(template);
            con = DriverManager.getConnection(url, username, password);
//            con = DriverManager.getConnection(testUrl, testUsername, testPassword);
            System.out.println("Database connected!");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }

    public static void main(String[] args) throws SQLException {
        ConnectionClass connection = new ConnectionClass();
        connection.hmm();
    }
}
