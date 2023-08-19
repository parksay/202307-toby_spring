package basics.tobyspring3.chapter34;

import basics.tobyspring2.chapter23.User232;
import basics.tobyspring3.chapter33.StatementStrategy331;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDao342 {

    private DataSource dataSource;
    private JdbcContext341 jdbcContext;

    public UserDao342() {
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcContext = new JdbcContext341();
        this.jdbcContext.setDataSource(this.dataSource);
    }

    public void add(final User232 user) throws SQLException {
        this.jdbcContext.workWithStatementStrategy(
            new StatementStrategy331() {
                @Override
                public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                    PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values (?, ?, ?)");
                    ps.setString(1, user.getId());
                    ps.setString(2, user.getName());
                    ps.setString(3, user.getPassword());
                    return ps;
                }
            }
        );
    }

    public void deleteAll() throws SQLException {
        this.jdbcContext.workWithStatementStrategy(
            new StatementStrategy331() {
                @Override
                public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                    PreparedStatement ps = c.prepareStatement("delete from users");
                    return ps;
                }
            }
        );
    }

}



//
