package basics.tobyspring1.chapter16;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SimpleConnectionMaker161 implements ConnectionMaker161 {
    public Connection makeConnection() throws ClassNotFoundException, SQLException {
        //
        Class.forName("com.mysql.cj.jdbc.Driver");
        //
        Connection c = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/springbook", "root", "0000");
        //
        return c;
    }
}
