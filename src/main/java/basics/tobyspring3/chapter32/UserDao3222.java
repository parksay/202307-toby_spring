package basics.tobyspring3.chapter32;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class UserDao3222 {

    private DataSource dataSource;

    public UserDao3222() {
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void deleteAll() throws SQLException {
        //
        Connection c = null;
        PreparedStatement ps = null;
        try {
            //
            c = dataSource.getConnection();
            //
            ps = new UserDaoDeleteAll3222().makeStatement(c);
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

    public abstract PreparedStatement makeStatement(Connection c) throws SQLException;

}




// p.218 - chapter 3.2.1
//이렇게 완성된 UserDao3222.class 가 템플릿 메소드 패턴을 적용한 UserDao 야.
//변하지 않는 부분, 반복되는 부분을 그대로 두고
//변하는 부분만 쓰는 사람이 직접 작성해서 쓸 수 있도록 만든 패턴.
//이렇게 하면 또 근데 문제가 있지.
//말했지만 Java에서 클래스는 하나만 상속받을 수 있어.
//그러면 deleteALl() 메소드 만들 때 상속 한 번 받고,
//add() 메소드 만들 때 상속 한 번 받고,
//get() 메소드 만들 때 상속 한 번 받고,
//getCount() 메소드 만들 때 상속 한 번 받고,
//        ...
//        ...
//뭐 메소드 하나당 클래스 하나씩 뽑아내야 해.
//이거 감당 가능함..?
//그래서 좀 더 유연하게 확장할 수 있는 전략 패턴으로 한번 가보자.
//UserDao3223.class 로.




