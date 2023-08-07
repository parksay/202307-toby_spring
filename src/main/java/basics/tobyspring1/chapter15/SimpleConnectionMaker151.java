package basics.tobyspring1.chapter15;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SimpleConnectionMaker151 {
    public Connection makeNewConnection() throws ClassNotFoundException, SQLException {
        //
        Class.forName("com.mysql.cj.jdbc.Driver");
        //
        Connection c = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/springbook", "root", "0000");
        //
        return c;
    }
}
