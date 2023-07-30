package basics.tobyspring1.chapter12;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class NaverUserDao123 extends UserDao123{
    @Override
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        //
        // Naver 독자적인 기술 코드 어쩌구 저쩌구....
        //
        Class.forName("com.mysql.cj.jdbc.Driver");
        //
        Connection c = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/springbook", "root", "0000");
        //
        return c;
    }
}
