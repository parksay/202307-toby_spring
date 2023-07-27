package basics.tobyspring1.chapter11;

import java.sql.*;

public class UserDao112 {

    public void add(User111 user) throws ClassNotFoundException, SQLException {
        //
        Class.forName("com.mysql.jdbc.Driver");
        //
        Connection c = DriverManager.getConnection(
                "jdbc:mysql://localhost:springbook", "spring", "book");
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
        Class.forName("com.mysql.jdbc.Driver");
        //
        Connection c = DriverManager.getConnection(
                "jdbc:mysql://localhost/springbook", "spring", "book");
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
