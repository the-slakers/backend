import java.sql.*;

public class Main {

    public static void main(String[] arg) throws Exception
    {
        // Udskift med din egen databasedriver og -URL
        Connection connection = null;
        Statement statement = null;

        try {
            connection = ConnectionConfiguration.getConnection();
            statement = connection.createStatement();
        }catch (Exception e){
            System.exit(1);
        }

        statement.executeUpdate("USE test;");
        ResultSet rs = statement.executeQuery("SELECT  * FROM MyGuests;");

        while (rs.next())
        {
            String navn = rs.getString("firstname");
            String lastname = rs.getString("lastname");
            System.out.println(navn+" "+lastname);
        }

/*        stmt.executeUpdate("create table KUNDER (NAVN varchar(32), KREDIT float)" );

        stmt.executeUpdate("insert into KUNDER values('Jacob', -1799)");
        stmt.executeUpdate("insert into KUNDER values('Brian', 0)");

 */
    }
}
