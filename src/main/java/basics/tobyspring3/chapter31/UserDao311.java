package basics.tobyspring3.chapter31;

import basics.tobyspring2.chapter23.User232;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao311 {

    private DataSource dataSource;

    public UserDao311() {
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
            ps = c.prepareStatement(
                    "insert into users(id, name, password) values (?, ?, ?)");
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
            ps = c.prepareStatement(
                    "select * from users where id = ?");
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
            ps = c.prepareStatement("select count(*) from users");
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
            ps = c.prepareStatement("delete from users");
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

//p. 209 - chapter 3.1
//개방폐쇄원칙을 생각해 보자.
//처음에는 한 파일 안에 코드를 다 때려박았다.
//나중에는 변경이 일어날 것 같은 부분은 다른 클래스로 따로 분리해서 만들어 두고 그 클래스의 오브젝트를 다른 곳에서 받아서 사용하도록 만들었다.
//그러면 변경이 일어날 때 분리해 둔 클래스만 수정하면 되고, 원래 있던 클래스는 수정할 필요가 없다.
//이렇게 확장에는 열려 있고 수정에는 닫혀 있는 구조를 개방폐쇄원칙이라고 한다.
//이걸 다시 한 번 생각해 보면 코드가 모두 같지는 않다는 걸 알 수 있다.
//어떤 코드는 다양해지고 확장하려고 하고, 어떤 코드는 고정되고 변하지 않으려고 한다.
//이 두 성질을 가진 코드를 분리해서 관리할 수 있다면 안정적인 구조가 될 것이다.
//그 방법 중 하나가 바로 템플릿이다.
//이번 3장에서는 템플릿 구조에 대해서 배워 보자.
//
//p.210 - chapter 3.1.1 / 예외처리
//// 리소스
//프론트와는 다르게 백엔드 개발할 때 신경써야 하는 점 하나가 바로 리소스다.
//Connection 이나 PreparedStatement 같은 리소스는 풀(Pool) 방식으로 운영된다.
//요청이 오면 그때 그때 새로운 오브젝트를 생성하고 소멸하기를 반복하지 않는다.
//일단 미리 리소스를 여러 개 생성해서 바구니에 담아 둔다.
//요청이 오면 바구니로부터 리소스 하나를 꺼내서 빌려주고, 사용이 끝나면 돌려받아서 다시 바구니에 넣어 둔다.
//이렇게 한정된 리소스를 풀에 넣어 두고 다같이 공유해서 쓰는 방식을 풀 방식이라고 한다.
//이런 풀 방식을 사용할 때는 주의해야 할 점이 하나 있다.
//사용이 끝난 리소스는 꼭 다시 반납해야 한다는 점이다.
//사용하고 나서 반납을 하지 않으면 한두 개야 괜찮겠지만 너도 나도 쌓이다 보면 결국에는 풀 안에 리소스가 하나도 남지 않게 된다.
//요청이 왔을 때 풀 안에 빌려줄 리소스가 없다면 서버는 리소스가 모자라다는 심각한 에러를 내버리고 애플리케이션이 멈춰 버릴 수 있다.
//
//따라서 Connection / PreparedStatement / ResultSet 같은 리소스들이 무사히 반환되도록 UserDao 를 수정해 보자.
//근데 이제 문제는 어느 시점에서 메소드에 예외가 발생했을지 모른다는 점이다.
//네트워크에 문제가 있거나 DB 서버에 문제가 있거나 하면 어느 시점에서든 문제가 생길 수 있다.
//로직 순서는 Connection 열고 PreparedStatement 실행해서 ResultSet 을 받아온다.
//그런데 만약 Connection 열다가 예외가 났는데, PreparedStatement 에다가 close() 실행해 버리면?
//PreparedStatement 는 null 로 초기화해 뒀기 때문에 close() 를 실행하면 NullPointerException 이 뜨고 만다.
//ResultSet 도 마찬가지다.
//그러므로 정상 로직을 try/catch/finally 구문으로 감싸고 그 안에 다시 예외처리를 한 번 더 넣어줘야 한다.
//
//한편 애초에 메소드에서 throws SQLExepction 라고 선언해 놓았으니 괜찮은 거 아니냐 생각할 수 있다.
//예외야 던져주기야 하겠지만 밑에 close() 구문까지 도달하지 못한 채로 메소드를 빠져나오기 때문에 try/catch 구문이 꼭 필요하다.
//catch 구문 안에는 별다른 처리도 없는데도 굳이 써줘야 하냐고 생각할 수 있다.
//지금은 별 내용이 없이 그냥 예외를 메소드 밖으로 던져주는 역할뿐인 건 사실이다.
//하지만 나중에 애플리케이션이 커지고 나면 예외가 발생했을 때마다 미리 정해둔 대응 절차가 있다든가 로그를 남겨둔다든가 하는 처리를 해줄 때가 많다.
//그런 경우를 대비해서 try/catch 를 일단 만들어 두는 습관을 들이는 게 좋다.
//
//근데 이게 완성된 코드가... 
//안정성은 좋아졌는데 코드가 너무 길어졌지
//try/catch 가 너무 반복되고 2중으로 감싸고 늘어지고....
//이걸 어떻게 해결할 수 있을까?
//UserDao3221.class 로 가보자