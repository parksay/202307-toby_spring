package basics.tobyspring3.chapter32;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface StatementStrategy3223 {

    public abstract PreparedStatement makePreparedStatement(Connection c) throws SQLException;
}
