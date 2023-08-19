package basics.tobyspring3.chapter32;

import basics.tobyspring2.chapter23.User232;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDao3223 {

    private DataSource dataSource;

    public UserDao3223() {
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void add(User232 user) throws SQLException {
        StatementStrategy3223 stmt = new AddStatement3223(user);
        this.jdbcContextWithStrategy(stmt);
    }

    public void deleteAll() throws SQLException {
        StatementStrategy3223 stmt = new DeleteAllStatement3223();
        this.jdbcContextWithStrategy(stmt);
    }

    public void jdbcContextWithStrategy(StatementStrategy3223 stmt) {
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
}




// p.219 - chapter 3.2.2
//DeleteAllStatement <---> StatementStrategy <---> UserDao.jdbcContextWithStrategy
//
//전략 패턴 구조가 완성 됐음.
//UserDao 에서 바뀌지 않는 부분, 반복되는 부분은 남겨둠.
//그리고 바뀌는 부분은 밖에서 받아옴.
//바뀌지 않는 부분 실행하다가 바뀌는 부분 나오면 밖에서 던져준 로직 실행하고 다시 바뀌지 않는 부분으로 돌아와서 계속 나아감.
//이제 이걸 쓰고 싶은 사람이 있으면 자기가 쓸 로직을 직접 만들어서 UserDao 쪽에 넣어주기만 하면 됨.
//그럼 틀이 고정돼 있고 알맹이만 넣어주는 느낌으로 계속 쓸 수가 있음.
//스마트폰 케이스가 있고, 그 안에 스마트폰만 계속 바꿔서 사용할 수 있음.
//단, 기종이 같아야지.
//틀에 맞아야지.
//여기서 껍데기가 되는 스마트폰 케이스가 예외처리를 해주는, try/catch 구문으로 감싸주는 부분.
//알맹이가 되는 스마트폰이 밖에서 받아오는 전략, 그때 그때 바뀌는 로직 부분.
//UserDao 클래스는 jdbcContextWithStrategy() 메소드를 실행할 때 어떤 오브젝트를 받아옴.
//그 오브젝트로부터 PreparedStatement 를 받아옴.
//어떻게? 그 오브젝트의 makePreparedStatement() 메소드를 실행함으로써.
//근데 그 오브젝트에 makePreparedStatement() 라는 메소드가 있을지, 구현은 해놨을지 어떻게 확신함?
//인터페이스 StatementStrategy 를 상속받았을 거라는 걸 이미 알기 때문.
//내가 받아오는 오브젝트는 StatementStrategy 인터페이스라는 틀을 지켜서 만든 오브젝트만 받겠다고 해놨음.
//그 안이 어떻게 구현돼 있고, 어떤 로직이 들어가 있을 거고, 어떤 SQL 구문이 들어 있을지 나는 모름.
//다만 최소한 StatementStrategy 인터페이스를 구현했다면 makePreparedStatement() 메소드를 구현했을 거라는 건 알 수 있음.
//UserDao 클래스의 jdbcContextWithStrategy() 메소드는 StatementStrategy 라는 인터페이스를 받아오고,
//클라이언트는 StatementStrategy 인터페이스라는 틀에 DeleteAllStatement 라는 알맹이를 구현해서 넣어주고,
//클라이언트는 UserDao 랑 DeleteAllStatement 랑 조합해서 쓰는 거.
//마치 스마트폰 케이스랑 스마트폰이랑 결합해서 쓰는 것처럼.
//그 사이에 기종을 딱 정해주는 게 인터페이스.
//가로는 몇 센치, 세로는 몇 센치, 카메라는 어디에, 마이크는 어디에, 등등...
//UserDao 클래스는 StatementStrategy 인터페이스에 의존하고, StatementStrategy 인터페이스는 DeleteAllStatement 를 구현해서 넣고..
//UserDao 클래스와 DeleteAllStatement 클래스 사이에 StatementStrategy 인터페이스를 이음새로 두고 주고 받음.
//주거나 받을 때 틀을 미리 정해뒀음.
//이 인터페이스라는 틀에 맞춰서 만들어서 줄 테니까 너도 이 인터페이스라는 틀로 맞춰서 받아서 사용하라고.



// 단점.
//근데 이게 또 맘에 안 드는 게 있음.
//add() 메소드 쓰려면 AddStatement 클래스 만들고,
//deleteAll() 쓰려면 DeleteAllStatement 클래스 만들고,
//getCount() 메소드 쓰려면 GetCountStatement 클래스 만들고...
//메소드 하나마다 StatementStrategy 인터페스를 구현한 클래스를 하나씩 만들어야 한다는 점이 번거로워.
//이 점은 그냥 템플릿 메소드 패턴을 썼을 때랑 똑같잖아.
//둘째로, try/catch/finally 부분은 더 이상 UserDao 클래스 안에서는 중복이 일어나지 않아.
//그것 좋다 이거야.
//근데 UserDao / BookDao / CarDao / ChairDao / ....
//Dao 만들 때마다 다 겹쳐. 그건 또 어쩔 건데...?
//일단 첫 번째, 클래스 파일이 많아지는 건 간단하게 해결할 방법이 있어.
//JavaScript 쓸 때 한 번 쓰고 버릴 함수는 익명 함수로 만들어서 넣어주잖아.
//Java 에서도 그냥 한 번 쓰고 말 클래스는 익명 클래스로 만들 수가 있어.
//클래스도 파일로 안 만들어도 돼.
//그냥 그 메소드 안에다가 / 또는 그 클래스 안에다가 바로 만들어버릴 수가 있어.
//이걸 중첩 클래스라고 해.
//중첩 클래스(nested class) 는 스태틱 클래스(static class) 와 내부 클래스(inner class) 로 구분된다.
//다시 내부 클래스는 로컬 클래스(local class) 와 멤버 내부 클래스(member inner class) 그리고 익명 내부 클래스(anonymous inner class) 로 구분된다.
//이렇게 내부 클래스로 만든 게 UserDao332.class 가 보자.


