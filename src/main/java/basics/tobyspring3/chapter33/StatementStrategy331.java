package basics.tobyspring3.chapter33;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface StatementStrategy331 {

    public abstract PreparedStatement makePreparedStatement(Connection c) throws SQLException;
}
