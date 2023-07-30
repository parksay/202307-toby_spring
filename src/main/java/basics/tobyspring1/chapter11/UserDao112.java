package basics.tobyspring1.chapter11;

import java.sql.*;

public class UserDao112 {

    public void add(User111 user) throws ClassNotFoundException, SQLException {
        //
        Class.forName("com.mysql.cj.jdbc.Driver");
        //
        Connection c = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/springbook", "root", "0000");
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
        Class.forName("com.mysql.cj.jdbc.Driver");

        //
        Connection c = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/springbook", "root", "0000");
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
}

// p.60
// 이 파일의 가장 큰 문제는?
// 변화나 변경에 대응하기 어렵다는 것.
// 설계가 변함을 뭐라 하지 마라.
// 세상에 변하지 않는 것은 없다.
// 변한다는 사실을 받아들여라.
// 그리고 변화에 쉽게 대응할 수 있는 코드를 설계해라.
// 애플리케이션이 변하지 않는 때는 그 애플리케이션을 더 이상 사용하지 않아 폐기처분할 때뿐이다.
// 좋은 설계, 디자인 패턴, 그런 건 거의 다가 보면 변화에 쉽게 대응할 수 있는 코드들을 말한다.
// 가장 먼저 보이는 문제는, DB 연결 정보가 반복된다.
// DB를 MySQL 에서 Oracle 로 바꾸면? DB 드라이버 클래스를 변경하면? id나 password 수정하면?
// DB 연결 코드들 다 일일이 찾아서 수정할까? 한 200개 되면? 아니면 2000개면..?
// 해결책은 DB 연결을 가져오는 부분을 따로 빼서 메소드 하나로 만들어주기.
// 메소드 추출(extract method) 라고 한다.
// 중복되는 코드나 한 테두리로 묶이는 기능을 담당하는 코드를 따로 뽑아서 한 메소드로 만들어주는 리팩토링 기법.
// 이렇게 수정한 클래스가 UserDao122.java



// >> dependency 추가하는 법
// build.gradle 에다가 아래 문구 추가하기
//	dependencies {
//		runtimeOnly 'com.mysql:mysql-connector-j'
//	}
// 이게 예전에는 아래였음.
// runtimeOnly 'mysql:mysql-connector-java'
// 근데 springboot 3.0 넘어가면서부터 아래 걸로 바뀜.
// runtimeOnly 'com.mysql:mysql-connector-j'

// >> driver 가져오기
// 토비의 스프링 책에는 아래처럼 나와있음.
// Class.forName("com.mysql.jdbc.Driver");
// 근데 책도 거의 10년 전 내용이니까...
// 위 이름은 deprecated 되고 아래 걸로 바뀜
// Class.forName("com.mysql.cj.jdbc.Driver");

// >> DB 접근 파라미터
//  Connection c = DriverManager.getConnection(
//                "jdbc:mysql://localhost:3306/mysql", "root", "0000");
// "jdbc:mysql://localhost:포트 번호/database 이름", "mysql 아이디", "mysql 비밀번호"
// 포트 번호랑 아이디, 비밀번호는 처음에 mysql 설치할 때 입력했던 내용
// database 이름은 mysql 실행하고 show databases; 했을 때 나오는 그 database 들.
// 포트 번호는 cmd 창 열어서 netstat 쳐보면 리스트 나옴


