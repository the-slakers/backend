package RestAPI;

import java.sql.*;

/**
 * Created with IntelliJ IDEA.
 * User: Branislav
 * Date: 5/19/13
 * Time: 2:07 PM
 */
public class ConnectionConfiguration {

    public static Connection getConnection() {
        Connection connection = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://207.154.253.167:3306", "root", "testtest");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return connection;
    }

}
