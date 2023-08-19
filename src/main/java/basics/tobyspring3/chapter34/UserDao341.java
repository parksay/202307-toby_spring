package basics.tobyspring3.chapter34;

import basics.tobyspring2.chapter23.User232;
import basics.tobyspring3.chapter33.StatementStrategy331;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDao341 {

    private JdbcContext341 jdbcContext;

    public UserDao341() {
    }

    public void setJdbcContext(JdbcContext341 jdbcContext) {
        this.jdbcContext = jdbcContext;
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
// p.235 - chapter 3.4.2
//UserDao3223 에서 전략 패턴을 적용했을 때 문제점을 말했지.
//첫째는 전략 하나마다 클래스 파일을 하나씩 더 만들어야 한다는 점.
//add() 메소드에 AddStatement.class 하나, deleteAll() 메소드에 deleteAllStatement.class 하나...
//그거는 중첩 클래스로 해결했고, 여기서는 익명 내부 클래스로 바꿨어.
//그럼 이제 두 번째 문제.
//try/catch/finally 처리 흐름을 다른 DAO 에서도 쓰고 싶을 것 같다.
////UserDao / BookDao / CarDao / ChairDao / ....
//한 애플리케이션 안에서는 예외 처리 방침을 통일성 있게 가져갈 텐데, 그럼 DAO 마다 가져다 쓰고 싶지 않을까?
//DAO 마다 안에 try/catch/finally 구문 하나씩 있으면, 로그 로직이 바뀌거나, 예외 처리 방침이 바뀌거나 등등 수정이 일어나 봐.
//DAO 파일 하나씩 다 찾아 들어가서 try/catch/finally 구문 다 수정해줘야 해.
//차라리 try/catch/finally 예외 처리 담당하는 클래스를 하나 따로 두고, 다른 DAO 들이 공통으로 이걸 가져다 쓰게 하면 얼마나 좋을까.
//자, 그래서 반복되는 try/catch/finally 부분을 클래스로 추출했어.
//추출한 클래스가 JdbcContext341.class 야.
//원래 try/catch 구문을 가지고 있던 UserDao 는 이제 JdbcContext 를 가져다 쓰고 있어.
//UserDao 는 JdbcContext 에 의존하고 있어.
//이 의존성을 어떻게 주입해줘야 할까?
//그냥 UserDao 혼자서 초기화하면 어떠려나?
//private JdbcContext341 jdbcContext = new JdbcContext341();
//이렇게 하면 어떠려나..?
//UserDaoTest341 을 보면 그냥 스프링 컨테이너에 맡겨서 받고 있어.
//원래대로라면 이거 또 UserDao 랑 JdbcContext 사이에 인터페이스 하나 끼워두고 결합도를 낮춰야 하는 거 아닌가?
//UserDao 입장에서는 JdbcContext 가 수정되거나 변경되면 다이나믹하게 동적으로 주입받으려면 인터페이스가 있어야 좋잖아.
//그럼에도 굳이 스프링 컨테이너를 써서 DI 받는 이유는...?
//첫째. 스프링 컨테이너가 싱글톤 패턴으로 관리해주기 때문.
//UserDao / BookDao / CarDao / ChairDao / ....
//가져다 쓰는 애들마다 JdbcContext 오브젝트 생성했다가 지웠다가 하면..?
//더 심한 경우는 메소드 하나 실행할 때마다 add() / deleteAll() / getCount() / 마다 생성했다 지웠다 반복하면...?
//스프링에 하나만 만들어 놓고 나머지 애들은 그거 공유해서 다같이 쓰는 게 효율적.
//둘째, JdbcContext 가 다른 빈에 의존하고 있음.
//JdbcContext 안에 DataSource 의존성을 가지고 있음.
//그리고 이 DataSource 의존성은 스프링으로부터 다이나믹하게 동적으로 주입받아야 함.
//근데 스프링 컨테이너한테 빈을 주입을 받으려면 자기 자신부터가 빈이 되어야 함.
//그래서 JdbcContext 를 스프링 빈에 등록해두고 UserDao 는 스프링으로부터 받아다가 쓰는 구조로 만들었다.

//그럼 인터페이스를 굳이 안 쓰는 이유는...?
//사실 만들어도 되기는 해.
//UserDao 랑 JdbcContext 랑 사이에 인터페이스 하나 만들어서 끼워넣어도 되긴 해.
//그렇게 하고 싶으면 해도 되긴 해.
//뭐 무난할지도.
//근데 이런 경우는 그냥 특정 클래스를 그대로 알고 있어도 된다 할 정도임.
//클래스 안에서 의존 관계가 이미 결정되어도 되는 정도임.
//UserDao 와 JdbcContext 사이의 응집도가 충분히 긴밀하기 때문에.
//우리가 의존관계 맺을 때 사이에 인터페이스를 이음새로 쓰는 이유는 결합도를 낮추기 위해서였지.
//근데 UserDao 랑 JdbcContext 랑은 이미 충분히 응집도가 있고 관계가 긴밀해서 강하게 결합해도 된다는 뜻.
//굳이 인터페이스 끼워 넣어서 결합도를 낮추지 않아도 되지 않냐고.
//강력한 결합을 그냥 허용해도 되지 않냐고.
//왜냐하면 UserDao 는 Jdbc 기술을 쓰고 있으므로 UserDao 는 항상 JdbcContext 랑 함께 쓰일 거야.
//JdbcContext 에 수정이 일어나면 UserDao 도 같이 수정해야 할 가능성이 높고, 그 반대도 마찬가지야.
//예를 들어서 JPA 를 쓰거나 하이버네이트를 쓰거나 쓰는 기술이 바뀌면 어차피 UserDao 랑 JdbcContext 를 함께 통째로 바꿔야 하는 거지.
//그래서 그냥 클래스 레벨에서 의존관계가 결정돼도 인정하는 정도.
//
//한 가지 더.
//나는 근데 그럼 JdbcContext 는 어차피 바뀔 일도 없는데 굳이 스프링 컨테이너까지 가고 않다?
//굳이 빈으로 등록해서 컨테이너한테 받아오게끔 하는 게 더 번거롭다?
//그러면 그냥 UserDao 안에서 JdbcContext 를 직접 초기화해도 돼.
//근데, 문제는 JdbcContext 가 DataSource 에 의존하고 있다는 사실이지.
//DataSource 는 스프링 컨테이너에 빈으로 등록돼 있는 친구라, 그걸 주입받으려면 JdbcContext 도 빈이 되어야 해.
//이걸 해결하려면...?
//UserDao 가 대신 받아서 JdbcContext 에 넣어주면 됨.
//
//스프링 컨테이너 ----> UserDao ----> JdbcContext
//
//스프링이 DataSource 빈을 꺼내서 UserDao 한테 넘겨주면,
//UserDao 는 JdbcContext 초기화할 때 넣어주면 됨.
//한 꺼풀 건너서 받는 거지.
//JdbcContext 를 바깥쪽으로 한 겹 싸고 있는 게 UserDao 가 되는 그림.
//이걸 구현한 게 UserDao342.class

