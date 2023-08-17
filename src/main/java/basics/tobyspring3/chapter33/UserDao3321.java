package basics.tobyspring3.chapter33;

import basics.tobyspring2.chapter23.User232;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDao3321 {

    private DataSource dataSource;

    public UserDao3321() {
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void add(User232 user) throws SQLException {
        // nested class >>>>>>>>>>>>>>>>>
        class AddStatement332 implements StatementStrategy331 {
            User232 user;

            public AddStatement332(User232 user) {
                this.user = user;
            }

            @Override
            public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values (?, ?, ?)");
                ps.setString(1, this.user.getId());
                ps.setString(2, this.user.getName());
                ps.setString(3, this.user.getPassword());
                return ps;
            }
        }
        // nested class <<<<<<<<<<<<<<<

        StatementStrategy331 stmt = new AddStatement332(user);
        this.jdbcContextWithStrategy(stmt);
    }

    public void deleteAll() throws SQLException {
        // nested class >>>>>>>>>>>>>>>>>>>>>
        class DeleteAllStatement332 implements StatementStrategy331 {
            @Override
            public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                PreparedStatement ps = c.prepareStatement("delete from users");
                return ps;
            }
        }
        // nested class <<<<<<<<<<<<<<<<<

        StatementStrategy331 stmt = new DeleteAllStatement332();
        this.jdbcContextWithStrategy(stmt);
    }

    public void jdbcContextWithStrategy(StatementStrategy331 stmt) {
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




// p.224 - chapter 3.3
//UserDao3223.class 에서 넘어와 봐.
//이렇게 하니까 일단 파일을 메소드마다 하나씩 만들어야 하는 점은 해결이 됐지.
//UserDao 안에다가 클래스는 중첩해서 더 만들어버리기.
//그럼 파일은 하나만 가지고 되기는 하는데...
//여전히 뭔가 좀 복잡해보이고 번거로워 보이고 정신 없지.
//여기서 중첩 클래스들을 익명 로컬 클래스로 선언하면 좀 더 간단해보일 거야.
//구조는 똑같은데 그냥 코드만 좀 더 단순해보임.
//그게 userDao3322.java



