package basics.tobyspring3.chapter35;

import basics.tobyspring2.chapter23.User232;
import basics.tobyspring3.chapter33.StatementStrategy331;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDao351 {

    private DataSource dataSource;
    private JdbcContext351 jdbcContext;

    public UserDao351() {
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcContext = new JdbcContext351();
        this.jdbcContext.setDataSource(this.dataSource);
    }

    public void add(final User232 user) throws SQLException {
        this.executeSql("insert into users(id, name, password) values (?, ?, ?)");
    }

    public void deleteAll() throws SQLException {
        this.executeSql("delete from users");
    }

    private void executeSql(final String query) {
        this.jdbcContext.workWithStatementStrategy(
                new StatementStrategy331() {
                    @Override
                    public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                        PreparedStatement ps = c.prepareStatement(query);
                        return ps;
                    }
                }
        );
        // add() 는 query 에 파라미터 세팅해주는 로직 필요함.
        // ps.setString(1, user.getId());

    }

}



// p.240 - chapter 3.5.1
//가만히 보니까 콜백 함수 넣어주는 부분도 계속 반복되네.
//사실은 StatementStrategy331 오브젝트 만들어서 makePreparedStatement() 메소드 @Override 하고 PreparedStatement 를 return 해주는 거밖에 없잖아.
//바뀌는 부분은 sql query 부분뿐.
//그러면 이거를 공통 메소드로 추출하자고.
//그리고 query 부분만 파라미터로 받아오면 되잖아.
//단, 메소드가 받아오는 파라미터 query 는 내부 익명 클래스가 사용하므로 final 로 선언해줘야 함.
//이렇게 하면 add() / deleteAll() / getCount() / get() 등 메소드에서는 한 줄이면 끝나.
//공통 메소드 부르면서 query 만 던져주면 돼.
//너무나 편하겠지.
//바뀌는 부분과 바뀌지 않는 부분을 분리하고, 바뀌지 않고 반복되는 부분은 공통으로 빼내서 유연하게 재사용 가능하도록 만들기.
//그 끝을 가다 보니 여기까지 왔어.
//사실 add() 같은 경우처럼 parameter 설정해줘야 하는 부분은 좀 더 복잡한 코드가 들어가야 함.
//지금은 그게 초점이 아니기 때문에 일단 넘어감.
//
//근데 또 한 발 더 나아가서, 이렇게 좋은 걸 UserDao 만 쓰게?
//BookDao / ChairDao / DeskDao ....
//다 같이 쓰고 싶어.
//그러면 클래스로 빼야겠지.
//여기까지는 생각이 맞아.
//근데 클래스를 따로 하나 만드는 건 너무 갔어.
//사실 지금 UserDao / JdbcContext 자체도 응집도가 높은 코드야.
//다만 반복되기도 하고 다른 클래스에서도 같이 접근해서 쓰려고 JdbcContext 로 분리하긴 했지만, 원래는 한 클래스에 있어야 자연스러운 코드들이었지.
//한 관심사로 한 기능을 수행하기 위해서 모인 코드들이니까.
//그런데 거기다가 makePreparedStatement() 를 공통으로 쓰려고 클래스를 하나 더 만든다?
//보통의 경우라면 맞았겠지만, 이렇게 응집도가 높은 경우에는 오히려 좋지 않음.
//응집도가 높게 유지하기 위해서, makePreparedStatement() 메소드를 새로운 클래스에 넣지 말고 JdbcContext 클래스에 넣기.
//그러면 반복은 줄이면서, 다른 애들도 접근해서 다같이 쓸 수 있으면서, 응집도까지 유지할 수 있지.
//그렇게 만든 게 (UserDao352 + JdbcContext352)


