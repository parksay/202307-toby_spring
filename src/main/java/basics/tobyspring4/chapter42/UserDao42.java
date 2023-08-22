package basics.tobyspring4.chapter42;

import basics.tobyspring2.chapter23.User232;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface UserDao42 {

    public abstract void add(User232 user);

    public abstract User232 get(String id);

    public abstract int getCount();
    public abstract List<User232> getAll();

    public abstract void deleteAll();

}
