package basics.tobyspring6.chapter61;

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

public class UserDaoJdbc611 implements UserDao611 {

    private JdbcTemplate jdbcTemplate;
    private RowMapper<User611> rowMapper = new RowMapper<User611>() {
        @Override
        public User611 mapRow(ResultSet rs, int rowNum) throws SQLException {
            User611 user = new User611();
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
            user.setLevel(Level611.valueOf(rs.getInt("level")));
            user.setLogin(rs.getInt("login"));
            user.setRecommend(rs.getInt("recommend"));
            return user;
        }
    };

    public UserDaoJdbc611() {
    }

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public void deleteAll() {
        this.jdbcTemplate.update("delete from users");
    }

    public void add(User611 user) {
        this.jdbcTemplate.update("insert into users(id, name, password, level, login, recommend) values (?, ?, ?, ?, ?, ?)",
                user.getId(), user.getName(), user.getPassword(), user.getLevel().intVal(), user.getLogin(), user.getRecommend());
    }

    public void update(User611 user) {
        this.jdbcTemplate.update("update users set name = ?, password = ? , level = ?, login = ?, recommend = ? where id = ?",
               user.getName(), user.getPassword(), user.getLevel().intVal(), user.getLogin(), user.getRecommend(), user.getId());
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

    public User611 get(String id) {
         return this.jdbcTemplate.queryForObject("select * from users where id = ?", this.rowMapper, new Object[] {id});
    }

    public List<User611> getAll() {
        return this.jdbcTemplate.query("select * from users order by id", this.rowMapper);
    }
}


