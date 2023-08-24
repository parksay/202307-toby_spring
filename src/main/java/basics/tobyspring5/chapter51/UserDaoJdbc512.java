package basics.tobyspring5.chapter51;

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

public class UserDaoJdbc512 implements UserDao512 {

    private JdbcTemplate jdbcTemplate;
    private RowMapper<User512> rowMapper = new RowMapper<User512>() {
        @Override
        public User512 mapRow(ResultSet rs, int rowNum) throws SQLException {
            User512 user = new User512();
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
            user.setLevel(Level512.valueOf(rs.getInt("level")));
            user.setLogin(rs.getInt("login"));
            user.setRecommend(rs.getInt("recommend"));
            return user;
        }
    };

    public UserDaoJdbc512() {
    }

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public void deleteAll() {
        this.jdbcTemplate.update("delete from users");
    }

    public void add(User512 user) {
        this.jdbcTemplate.update("insert into users(id, name, password, level, login, recommend) values (?, ?, ?, ?, ?, ?)",
                user.getId(), user.getName(), user.getPassword(), user.getLevel().intVal(), user.getLogin(), user.getRecommend());
    }

    public void update(User512 user) {
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

    public User512 get(String id) {
         return this.jdbcTemplate.queryForObject("select * from users where id = ?", this.rowMapper, new Object[] {id});
    }

    public List<User512> getAll() {
        return this.jdbcTemplate.query("select * from users order by id", this.rowMapper);
    }
}


// 이번 챕터에서는 서비스 추상화에 대해서 배울 거야.
//비슷비슷한 기술들을 스프링에서는 어떻게 묶어서 추상화해서 사용하는지?
//
//유저에 레벨 기능을 추가해 보자.
//레벨은 BASIC/SILVER/GOLD 가 있음
//가입 후 50번 로그인 하면 SILVER 됨.
//SILVER 이면서 30번 이상 추천 받으면 GOLD 됨.
//레벨 업그레이드는 모아놨다가 한 번에 처리한다
//User 에 level 이랑 login 이랑 recomment 변수 추가하기
//UserDaoJdbc 수정하기
//UserDaoTest 수정하기
//DB 테이블에 level, login, recommend 칼럼 추가하기.
//단, level 값을 DB 에 어떻게 넣을까?
//문자열로 "BASIC"/"SILVER"/"GOLD" 이렇게 넣을까?
//혹시나 오타라도 나는 날에는...?
//이럴 때는 1/2/3 이렇게 코드값을 넣는 게 나음.
//용량도 훨씬 효율적이고.
//그럼 User 에다가 static 으로 코드값들을 만들어 둘까?
//아니, 그것도 데이터 정합성 문제가 있을 수 있음.
//이럴 때 쓰라고 자바에서는 Enum 이라는 타입을 제공함.
//User512 에 가 보면 설명 더 있음.
//
//이번엔 사용자 정보를 수정하는 기능을 만들어 보자.
//id 가 아닌 이름이나 비밀번호, 레벨 같은 거는 자주 수정하는 값이겠지.
//TDD 적용해 보자.
//UserDaoTest 에 먼저 update() 기능 테스트 코드를 만들어 보고.
//UserDao 와 UserDaoJdbc 에 기능 추가해 보기.

