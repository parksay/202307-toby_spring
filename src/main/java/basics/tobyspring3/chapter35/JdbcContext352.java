package basics.tobyspring3.chapter35;

import basics.tobyspring3.chapter33.StatementStrategy331;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JdbcContext352 {

    private DataSource dataSource;

    public JdbcContext352() {
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void workWithStatementStrategy(StatementStrategy331 stmt) {
        //
        Connection c = null;
        PreparedStatement ps = null;
        try {
            //
            c = dataSource.getConnection();
            //
            ps = stmt.makePreparedStatement(c);
            //
            ps.execute();
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            //
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    System.out.println(e);
                }
            }
            //
            if (c != null) {
                try {
                    c.close();
                } catch (SQLException e) {
                    System.out.println(e);
                }
            }
        }
    }

    public void executeSql(final String query) {
        this.workWithStatementStrategy(
                new StatementStrategy331() {
                    @Override
                    public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                        PreparedStatement ps = c.prepareStatement(query);
                        return ps;
                    }
                }
        );
    }
}
