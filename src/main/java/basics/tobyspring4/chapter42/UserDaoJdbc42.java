package basics.tobyspring4.chapter42;

import basics.tobyspring2.chapter23.User232;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDaoJdbc42 implements UserDao42{

    private JdbcTemplate jdbcTemplate;
    private RowMapper<User232> rowMapper = new RowMapper<User232>() {
        @Override
        public User232 mapRow(ResultSet rs, int rowNum) throws SQLException {
            User232 user = new User232();
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
            return user;
        }
    };

    public UserDaoJdbc42() {
    }

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public void deleteAll() {
        this.jdbcTemplate.update("delete from users");
    }

    public void add(User232 user) {
        this.jdbcTemplate.update("insert into users(id, name, password) values (?, ?, ?)",
                user.getId(), user.getName(), user.getPassword());
    }

    public int getCount() {
        return this.jdbcTemplate.query(
                new PreparedStatementCreator() {
                    @Override
                    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                        return con.prepareStatement("select count(*) from users");
                    }
                },
                new ResultSetExtractor<Integer>() {
                    @Override
                    public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
                        rs.next();
                        return rs.getInt(1);
                    }
                }
        );
    }

    public User232 get(String id) {
         return this.jdbcTemplate.queryForObject("select * from users where id = ?", this.rowMapper, new Object[] {id});
    }

    public List<User232> getAll() {
        return this.jdbcTemplate.query("select * from users order by id", this.rowMapper);
    }
}

