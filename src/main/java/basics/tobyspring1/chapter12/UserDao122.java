package basics.tobyspring1.chapter12;

import java.sql.*;

public class UserDao122 {

    public void add(User111 user) throws ClassNotFoundException, SQLException {
        //
        Connection c = getConnection();
        //
        PreparedStatement ps = c.prepareStatement(
                "insert into users(id, name, password) values (?, ?, ?)");
        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());
        //
        ps.executeUpdate();
        //
        ps.close();
        c.close();

    }

    public User111 get(String id) throws ClassNotFoundException, SQLException {
        //
        Connection c = getConnection();
        //
        PreparedStatement ps = c.prepareStatement(
                "select * from users where id = ?");
        ps.setString(1, id);
        //
        ResultSet rs = ps.executeQuery();
        rs.next();
        User111 user = new User111();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));
        //
        rs.close();
        ps.close();
        c.close();
        //
        return user;
    }

    private Connection getConnection() throws ClassNotFoundException, SQLException {
        //
        Class.forName("com.mysql.cj.jdbc.Driver");

        //
        Connection c = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/springbook", "root", "0000");
        //
        return c;
    }
}

// p.65
//UserDao112.class 에서 설명했듯이, DB 연결 받아오는 부분을 '메소드 추출'해서 따로 빼놨어.
//이제 DB 드라이버 클래스나 url이나 연결 정보가 바뀌어도 한 곳만 수정하면 되겠지.
//아직 고쳐야 할 점이 많지만 훨씬 나아졌어.
//근데 한 단계 더 나아가서 더 발전시켜 보자.
//전에 보이는 문제가 '반복되는 코드'였다면, 이제 보이는 문제는 확장성.
//예를 들어볼게.
//네이버랑 다음에서 우리 UserDao 를 사서 쓰고 싶대.
//대신 getConnection() 부분은 자기네 가이드라인이 있어서 알아서 만들고 싶대.
//그럼 이걸 어떻게 하지..?
//우리가 만든 UserDao 소스파일 자체를 넘겨버려?
//거기 안에 들어 있는 우리 독자적인 기술은..? 다 노출되는데?
//소스는 우리만 갖고 있고 고객한테는 컴파일된 클래스 바이너리 파일만 제공하려고 해.
//어떻게 우리가 만든 클래스는 가져다가 사용하도록 하면서 getConnection() 은 각자 알아서 만들어 쓰도록 할 수 있을까?
//자바에는 이럴 때 쓰면 좋은 기능이 있어.
//바로 추상 메소드이야.
//getConnection() 메소드를 추상 메소드(abstract) 로 선언하고 내용은 비워 두기.
//그럼 메소드 껍데기만 있고 내용물은 없는 상태.
//어떤 클래스가 가지고 있는 멤버 메소드 중에 추상 메소드가 하나라도 있다면 그 클래스는 추상 클래스가 되어야 함.(abstract)
//이제 가져다 쓰고 싶은 애들이, 네이버나 다음이, 우리 UserDao 클래스를 상속하고, 그 안에 getConnection() 메소드만 채워 넣으면 됨.
//이렇게 수정한 파일이 UserDao123

