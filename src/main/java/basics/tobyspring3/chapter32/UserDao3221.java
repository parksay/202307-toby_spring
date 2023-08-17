package basics.tobyspring3.chapter32;

import basics.tobyspring2.chapter23.User232;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao3221 {

    private DataSource dataSource;

    public UserDao3221() {
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void add(User232 user) throws SQLException {
        //
        Connection c = null;
        PreparedStatement ps = null;
        //
        try {
            c = dataSource.getConnection();
            //
            ps = this.addStatement(c);
            ps.setString(1, user.getId());
            ps.setString(2, user.getName());
            ps.setString(3, user.getPassword());
            //
            ps.executeUpdate();
        } catch(SQLException e) {
            System.out.println(e);
        } finally {
            //
            if(ps != null) {
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

    public User232 get(String id) throws SQLException {
        //
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        User232 user = null;
        try {
            c = dataSource.getConnection();
            //
            ps = this.getStatement(c);
            ps.setString(1, id);
            //
            rs = ps.executeQuery();
            //
            boolean isNext = rs.next();
            if (isNext) {
                user = new User232();
                user.setId(rs.getString("id"));
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));
            }
            //
            if(user == null) {
                throw new EmptyResultDataAccessException(1);
            }
            //
        } catch (SQLException e) {
            //
            System.out.println(e);
        } finally {
            //
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.out.println(e);
                }
            }
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
        return user;
    }

    public int getCount() throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int cnt = 0;    // int 같은 거는 초기화 어떻게 해둬야 하나..?
        try {
            //
            c = dataSource.getConnection();
            //
            ps = this.getCountStatement(c);
            //
            rs = ps.executeQuery();
            rs.next();
            cnt = rs.getInt(1);
            return cnt;
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            //
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.out.println(e);
                }
            }
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
        //
        return cnt;
    }

    public void deleteAll() throws SQLException {
        //
        Connection c = null;
        PreparedStatement ps = null;
        try {
            //
            c = dataSource.getConnection();
            //
            ps = this.deleteAllStatement(c);
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

    private PreparedStatement addStatement(Connection c) throws SQLException {
        PreparedStatement ps = c.prepareStatement(
                "insert into users(id, name, password) values (?, ?, ?)");
        return ps;
    }
    private PreparedStatement getStatement(Connection c) throws SQLException {
        PreparedStatement ps = c.prepareStatement(
                "select * from users where id = ?");
        return ps;
    }
    private PreparedStatement getCountStatement(Connection c) throws SQLException {
        PreparedStatement ps = c.prepareStatement("select count(*) from users");
        return ps;
    }
    private PreparedStatement deleteAllStatement(Connection c) throws SQLException {
        PreparedStatement ps = c.prepareStatement("delete from users");
        return ps;
    }
}




// p.216 - chapter 3.2.1
//UserDao311.java 봐봐...
//좋다 이거야
//근데 반복되는 게 너무 많지
//try/catch 문이 메소드마다 반복되고 2중으로 들어가 있고...
//굉장히 불편해
//그리고 UserDao 안에서 get() / add() / deleteAll() / getCount() ... 모든 메소드마다 반복돼고 있어
//근데 애플리케이션이 커지면 UserDao.class / ProductDao.class / BookDao.class / ... 클래스마다 또 반복될 거야.
//이런 상황에서 만약에, try/catch 에서 예외 처리 프로토콜이 바꼈대.
//예외가 났을 경우 로그 남기는 포맷이 바꼈대.
//예외 났을 때 처리해야 하는 방침이 바꼈대.
//그러면 그거 다 일일이 들어가서 수정하고 앉아 있을 거야...?
//몇 천 개 될 텐데...?
//try/catch 문은 바뀌지 않는 부분이니까 그대로 두고, 바뀌는 부분만 그때 그때 유연하게 쓸 수 없을까?
//이런 고민에서 시작되는 게 chapter3.2
//일단 가장 먼저 나온 버전이 UserDao3221.class 야.
//일단 변하는 코드 (try/catch) 랑 바뀌는 코드 statement 랑 분리했어
//가장 기본적인 리팩토링 기술, 메소드 추출로.
//근데 보니까 변한 게 없지.
//변하지 않아서 반복되는 부분은 메소드로 추출하면 반복이 해결돼.
//근데 우리가 지금 해결해야 하는 문제는 반대야.
//변하는 부분을 메소드로 추출하니까 변하지 않는 부분이 반복되는 건 해결되지가 않았어.
//반복되고 변하지 않는 부분이 바깥에서 감싸고 있어.
//변하는 부분이 안쪽에 둘러싸여 있어.
//이러니까 반복되는 부분을 추출하지 못하고, 반복되지 않는 부분을 추출해 버린 거야.
//그러니까 반복되는 부분은 계속 반복되고 있고, 해결되지 않은 거지.
//코드 양도 줄어드는 것도 아니고, 반복되는 부분이 메소드 전체에 걸쳐서 그대로 퍼져 있는 거지.
//자, 그럼 어떻게 할 수 있을까?
//변하지 않는 반복되는 부분은 그대로 두고, 변하는 부분만 알맹이만 쏙쏙 바꿔서 쓸 수 있는 방법?
//일단 템플릿 메소드 패턴으로 가보자.
//UserDao3222.class 로 가 보자.
