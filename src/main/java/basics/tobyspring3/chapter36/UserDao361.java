package basics.tobyspring3.chapter36;

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

public class UserDao361 {

    private JdbcTemplate jdbcTemplate;
    // 밑에 보면 get() 이랑 getAll() 에서 rowMapper 콜백 오브젝트를 똑같은 거를 만들어서 쓰고 있음.
    // 중복되니까 차라리 클래스 변수에 만들어 놓고 공유해서 쓰기
    // 이처럼 익명 내부 클래스는 꼭 메소드 안에서만 가능한 게 아니라 클래스 안이라면 어디서든 가능함.
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

    public UserDao361() {
    }

    public void setDataSource(DataSource dataSource) {
        // 우리가 만들었던 JdbcContext 는 수정자 함수 setDataSource() 로 dataSource 를 넣어주었지.
        // jdbcTemplate 은 생성자에 파라미터로 넣어주면 돼.
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public void deleteAll() throws DataAccessException {
        // this.jdbcTemplate.update(new PreparedStatementCreator() {
        //     @Override
        //     public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
        //         return con.prepareStatement("delete from users");
        //     }
        // });
        // 위처럼 콜백을 직접 만들어서 넣어줘도 되고 아래처럼 SQL 만 넣어주고 내장 콜백을 이용해도 되고
        this.jdbcTemplate.update("delete from users");
    }

    public void add(User232 user) throws DataAccessException {
        this.jdbcTemplate.update("insert into users(id, name, password) values (?, ?, ?)",
                user.getId(), user.getName(), user.getPassword());
    }

    public int getCount() throws SQLException {
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
        return this.jdbcTemplate.queryForObject("select * from users where id = ?",
                new RowMapper<User232>() {
                    @Override
                    public User232 mapRow(ResultSet rs, int rowNum) throws SQLException {
                        User232 user = new User232();
                        user.setId(rs.getString("id"));
                        user.setName(rs.getString("name"));
                        user.setPassword(rs.getString("password"));
                        return user;
                    }
                },
                new Object[] {id}
                );
        // 위에는 메소드 안에서 rowMapper 콜백 오브젝트를 바로 만들어서 쓰고 바로 버리는 것.
        // 근데 이게 get() 이랑 getAll() 이랑 똑같은 걸 중복해서 만들고 있고 앞으로도 계속 나올 거 같아.
        // 그래서 이 rowMapper 콜백 오브젝트를 클래스 변수에 넣어두고 공유해서 쓰기로 함.
        // 아래는 클래스 변수에 미리 만들어 둔 rowMapper 콜백 오브젝트를 가져다 쓰는 것.
        // return this.jdbcTemplate.queryForObject("select * from users where id = ?", this.rowMapper,z new Object[] {id});
    }

    public List<User232> getAll() throws DataAccessException, SQLException {
        return this.jdbcTemplate.query("select * from users order by id",
                new RowMapper<User232>() {
                    @Override
                    public User232 mapRow(ResultSet rs, int rowNum) throws SQLException {
                        User232 user = new User232();
                        user.setId(rs.getString("id"));
                        user.setName(rs.getString("name"));
                        user.setPassword(rs.getString("password"));
                        return user;
                    }
                });
        // 위에는 메소드 안에서 rowMapper 콜백 오브젝트를 바로 만들어서 쓰고 바로 버리는 것.
        // 근데 이게 get() 이랑 getAll() 이랑 똑같은 걸 중복해서 만들고 있고 앞으로도 계속 나올 거 같아.
        // 그래서 이 rowMapper 콜백 오브젝트를 클래스 변수에 넣어두고 공유해서 쓰기로 함.
        // 아래는 클래스 변수에 미리 만들어 둔 rowMapper 콜백 오브젝트를 가져다 쓰는 것.
        // return this.jdbcTemplate.query("select * from users order by id", this.rowMapper);
    }
}



// p.260 - chapter 3.6.1
//자 chapter.3.5.6 까지 해서 템플릿/콜백 패턴을 익혀 봤어.
//jdbc 랑 비슷하게 try/catch/finally 구조로 템플릿/콜백이 필요한 계산기 기능을 만들어 봤어.
//하면서 리팩토링도 해보고 콜백을 단순화도 해보고 제네릭 타입까지 응요해 봤지.
//그렇게 연습한 이유는 우리가 이제부터 배울 JdbcTemplate 이 이 템플릿/콜백 패턴을 적극적으로 사용하고 있기 때문이지.
//그럼 이제부터 JdbcTempalte 에 대해서 배워보자.
//
//
//JdbcTemplate 은 우리가 만들었던 JdbcContext 와 비슷한 기능을 해.
//다만 우리가 만들었던 것보다 훨씬 정교하고 안정적이고 편리한 기능들을 지원해주고 있겠지.
//JdbcTemplate 은 거의 표준 기술이고 DB를 다루는 기술 중에 기본이라고 생각하면 돼.
//기본적인 기술이니까 이해해두면 좋고 공부해두면 좋고 나중에 활용해서 응용해도 좋고 다른 기술이나 언어나 프레임워크 다룰 때도 비슷한 기능들이 등장할 거야.
//어쨌든 익혀 두면 분명 언젠가는 쓸 것이야.
//요즘에는 JPA 도 많이 쓰지만 레거시 프로젝트는 대부분 JDBC 가 아직 많기도 하고.
//이제부터는 JdbcTemplate 이 제공하는 기능을 하나씩 써보면서 어떻게 쓰는지 익혀보자.
//
// update()
//jdbcTemplate.udpate("sql here");
// 이 파일에서 deleteALl() 봐봐.
//update() 메소드는 간단하게 sql 만 넣어주면 알아서 해줘.
//우리가 jdbcContext 에서 만들었던 executeSql() 과 비슷한 구조야.
//SQL 만 던져주면 그 안에서 알아서 콜백 함수까지 만들어서 템플릿을 호출해 줌.
//
// update()
//jdbcTemplate.update("insert into users(id, name, password) values (?, ?, ?)", "testId", "testName", "testPwd");
//이번엔 add() 메소드 봐봐.
//거기에 update() 함수는 치환자(?) 에다가 값을 바인딩해주는 기능도 편하게 제공해 줌.
//일단 SQL 먼저 넣어주고 치환자 하나에 넣어줄 값을 하나씩 파라미터로 넣어주면 됨.
//
// query()
// jdbcTemplate.query(new PreparedStatementCreator() { ~~~ }, new ResultSetExtractor<T>() { ~~~ });
// 자 getCount() 한번 보자.
//jdbcTemplate.query() 메소드는 파라미터로 콜백을 총 2개 받음.
//하나는 SQL 넣어서 PreparedStatement 만들어 주는 콜백, 다른 하나는 ResultSet 을 받아서 가공한 뒤에 어떤 값을 되돌려주는 콜백.
//지금까지 insert 나 delete 는 return 을 받을 필요가 없었지.
//근데 이제부터는 select 가 나오잖아.
//이거는 우리가 그 결과값을 받아서 뭔가 써먹어야 해.
//그래서 콜백이 하나 더 들어난 거.
//근데 return 받을 데이터가 어떤 데이터일지 모르니 ResultSetExtractor 는 return type 을 generic 제네릭으로 <T> 지정할 수 있게 해 놓음.
//ResultSetExtractor 의 return type 을 generic 으로 지정하면 그에 따라서 query() 템플릿의 return type 도 따라서 바뀐다.
//참고로 ResultSetExtractor 는 앞에서 Calculator 계산기 연습으로 만들어 볼 때 lineReadTemplate() 과 LineCallBack 에서 활용했던 방식이랑 비슷한 방식임.
//
// queryForObject()
//jdbcTemplate.queryForObject("sql here ?", new RowMapper<T>() { ~~~ }, new Object[] { ~~ });
// get() 메소드 봐봐.
//PreparedStatement 는 첫 번째 파라미터에 sql 만 넣으면 그거 갖다가 내장 콜백 써서 만들어 줌.
//이제 좀 더 살펴봐야 할 부분이 두 번째 파라미터 RowMapper 콜백임.
//바로 전에 썼던 거는 ResultSetExtractor 였지.
//이 콜백 함수는 ResultSet 을 한 번 받아와서 지가 혼자 내부에서 다 처리하고 최종적인 결과를 뽑아서 return 도 한 번만 함.
//반면에 RowMapper 는 조금 다름.
//RowMapper 콜백은 row 하나를 오브젝트 하나에 맵핑하는 기능이기 때문에 sql 실행한 결과 row 가 여러 개라면 그 갯수만큼 여러 번 호출될 수 있음.
//단, 우리가 지금 jdbcTemplate 에서 호출하고 있는 queryForObject 는 결과로 던져주는 row 가 1개뿐이라고 기대하는 메소드임.
//그래서 한 번만 불러지기는 함.
//이제 마지막 세 번째 파라미터는 치환자에 넣어줄 값들임.
//만약 sql 안에 치환자(?) 있으면 세 번째 파라미터 new Object[] { ~~ } 에다가 넣어줌.
//만약 sql 안에 치환자(?) 가 없다면 세 번째 파라미터 new Object[] { ~~ } 전체를 생략 가능.
//참고, 책에는 queryForObject 파라미터 순서가 아래처럼 나와 있는데 이건 이제 deprecatd 됐음.
// queryForObject("sql", new Object[] { ~~ }, rowMapper);
//
// query()
//jdbcTemplate.query("sql here ?", new RowMapper<T>() { ~~~ }, new Object[] { ~~ });
// queryForObject() 랑 똑같음.
// 단, query 는 sql 실행 결과 row 가 여러 줄인 경우에까지 일반적으로 쓸 수 있음.
// 예시 getAll()
//
// >>>>>>>>>>>>>>>>>>>>>>>>>
// getAll() 테스트 만들 때 생각해야 하는 점이 있어.
//테스트 만들 땐 항상 네거티브 케이스부터 만드는 습관을 들이라고 했지.
//getAll() 했는데 안에 결과 row 가 하나도 없어.
//이럴 경우 어떻게 할지..?
//어떤 개발자는 null 을 return 하고, 어떤 개발자는 비어 있는 List 를 return 하고, 어떤 개발자는 exception 던지고...
//이런 것부터 미리 정하고 개발을 시작해야 하는데, 그래야 로직 설계가 되는데, 이런 이야기는 항상 개발 마무리쯤에서 논의가 된다고.
//우리는 어떻게 할까?
//결과가 하나도 없으면 그냥 비어 있는 List 오브젝트를 던져주기로 하자.
//그럼 현재 query() 메소드는 결과가 없을 경우 어떻게 동작하느냐?
//똑같이 비어 있는 List 를 던져준다.
//그러면 우리가 딱히 구현할 게 있느냐?
//테스트도 만들 필요가 있느냐?
//있다.
//왜냐면 사실 테스트 입장에서는 얘가 JdbcTemplate 을 쓰는지, JdbcContext 를 쓰는지, JPA 를 쓰는지, 신경 안 씀.
//근데 "우리는 JdbcTemplate 쓰고 있는데 jdbcTemplate.query() 는 어차피 결과 없을 때 동작이 우리가 원하는 거랑 똑같으니깐!" 하면서 안 만든다?
//모르는 일이지.
//
// 위에서 get() 이랑 getAll() 에서 rowMapper 콜백 오브젝트를 똑같은 거를 만들어서 쓰고 있음.
// 중복되니까 차라리 클래스 변수에 만들어 놓고 공유해서 쓰기
// 이처럼 익명 내부 클래스는 꼭 메소드 안에서만 가능한 게 아니라 클래스 안이라면 어디서든 가능함.
//
// UserDao 가 dataSource 를 받아서 jdbcTemplate 에 넣어주고 있지.
// 이것도 스프링 컨테이너에서 빈으로 생성해서 DI 해주는 게 나을까?
// 사실 jdbcTemplate 은 Dao 안에서 직접 생성해서 쓰는 게 스프링의 관례이기는 해.
// 그렇다고 안 될 건 없고 원한다면 싱글톤 빈으로 등록해서 사용해도 됨.


