package basics.tobyspring1.chapter12;


import java.sql.Connection;
import java.sql.SQLException;

public class DaumUserDao123 extends UserDao123 {
    @Override
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        // Daum 독자적인 기술 코드 어쩌구 저쩌구...
        return null;
    }
}
