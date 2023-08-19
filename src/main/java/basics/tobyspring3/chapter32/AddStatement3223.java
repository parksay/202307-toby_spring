package basics.tobyspring3.chapter32;

import basics.tobyspring2.chapter23.User232;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddStatement3223 implements StatementStrategy3223 {

    private final User232 user;

    public AddStatement3223(User232 user) {
        this.user = user;
    }

    @Override
    public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
        PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values (?, ?, ?)");
        ps.setString(1, this.user.getId());
        ps.setString(2, this.user.getName());
        ps.setString(3, this.user.getPassword());
        return ps;
    }
}
