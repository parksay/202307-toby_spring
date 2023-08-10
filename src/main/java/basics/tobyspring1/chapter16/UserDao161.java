package basics.tobyspring1.chapter16;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao161 {

    private ConnectionMaker161 connectionMaker;

    public UserDao161(ConnectionMaker161 connectionMaker) {
        this.connectionMaker = connectionMaker;
    }

    public void add(User111 user) throws ClassNotFoundException, SQLException {
        //
        Connection c = connectionMaker.makeConnection();
        //
        PreparedStatement ps = c.prepareStatement(
                "insert into users(id, name, password) values (?, ?, ?)");
        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());
        //
        ps.executeUpdate();
        //
        ps.close();
        c.close();

    }

    public User111 get(String id) throws ClassNotFoundException, SQLException {
        //
        Connection c = connectionMaker.makeConnection();
        //
        PreparedStatement ps = c.prepareStatement(
                "select * from users where id = ?");
        ps.setString(1, id);
        //
        ResultSet rs = ps.executeQuery();
        rs.next();
        User111 user = new User111();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));
        //
        rs.close();
        ps.close();
        c.close();
        //
        return user;
    }

}

