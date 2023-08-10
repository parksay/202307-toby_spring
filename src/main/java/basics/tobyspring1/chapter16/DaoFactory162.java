package basics.tobyspring1.chapter16;


import java.sql.SQLException;


public class DaoFactory162 {

    public UserDao161 userDao() throws ClassNotFoundException, SQLException {
        return new UserDao161(connectionMaker());
    }

    public ConnectionMaker161 connectionMaker() {
        return new SimpleConnectionMaker161();
    }
}

