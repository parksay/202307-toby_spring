package basics.tobyspring1.chapter18;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao184 {

    private DataSource dataSource;

    public UserDao184() {
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void add(User111 user) throws SQLException {
        //
        Connection c = dataSource.getConnection();
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

    public User111 get(String id) throws SQLException {
        //
        Connection c = dataSource.getConnection();
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
//p.138
// xml 버전으로 만들 때는 기본 생성자 메소드 + 변수마다 수정자 메소드 만들기
// 스프링 컨테이너가 빈을 만들 때 기본 생성자 메소드로 오브젝부터 만들고
// <property> 태그마다 변수 이름을 불러와서 수정자 메소드를 거쳐서 값들 하나씩 꽂아주는 순서로 작동함.
// DaoFactory183.java 랑 똑같은 기능을 하는 applicationContext.xml 을 만들어야 하니까 참조하면서 만들기.
//<bean id="dataSource" class="org.springframework.jdbc.datasource.SimpleDriverDataSource">
//    <property name="driverClass" value="com.mysql.cj.jdbc.Driver" />
//    <property name="url" value="jdbc:mysql://localhost:3306/springbook" />
//    <property name="username" value="root" />
//    <property name="password" value="0000" />
//</bean>
//만들면 이렇게 됨.
//여기서 의문이 있어.
//이전에 우리가 applicationContext181.xml 만들 때는 아래처럼 만들었었어.
//<property name="connectionMaker" ref="connectionMaker" />
//이 때는 <property> 태그에 name 속성과 ref 속성이 있지.
//그리고 이 ref 속성에는 해당 스프링 컨테이너가 가지고 있는 다른 bean 이름을 넣어준다고 했어
//근데 이번에 만드는 applicationContext184.xml 에서는 속성 이름이 조금 달라졌지.
////    <property name="driverClass" value="com.mysql.cj.jdbc.Driver" />
//name 속성은 똑같은데 ref 속성이 사라지고 대신 value 속성이 생겼어.
//ref 속성에는 bean 이름을 넣어준다고 했지만 반면에 value 속성에는 그냥 단순한 값들을 그대로 넣어줌.
//예를 들어서 String, Integer, bool, List, Set 등 다양하게 넣을 수 있음
//근데 또 궁금한 게 생기지.
//DaoFactory.183 을 보면 아래처럼 돼 있어.
//> dataSource.setDriverClass(com.mysql.cj.jdbc.Driver.class);
//> dataSource.setUsername("root");
//파라미터 데이터 타입을 잘 봐 봐.
//SimpleDriverDataSource 클래스의 setDriverClass() 메소드는 파라미터를 클래스 타입으로 받고 있고,
//SimpleDriverDataSource 클래스의 setUsername() 메소드는 파라미터를 문자열로 받고 있어.
//근데 xml 버전으로 만든 applicationContext184.xml 을 보면 아래처럼 돼 있지.
//<property name="driverClass" value="com.mysql.cj.jdbc.Driver" />
//<property name="username" value="root" />
//어떤 거는 class 타입으로 받고, 어떤 거는 String 타입으로 받고 있는데,
//그런 거 상관없이 <property> 의 value 속성에서는 그냥 다 똑같이 문자열처럼 "" 해서 받고 있어.
//자바 코드로 넣을 때는 데이터 타입이 정해져 있고, 그 데이터 타입대로 넣어줘야 했지.
//근데 xml 에서는 데이터 타입을 지정할 수가 없어.
//근데도 스프링에서는 xml 파일로부터 값들을 읽어와서 자바 코드로 만들어서 잘 작동해.
//<property> 태그의 value 속성에 들어가는 값들을 읽어오고 나면 필요한 데이터 타입으로 알아서 변환해서 사용하는 기능이 스프링에 있어.
//따라서 xml 에서는 데이터 타입을 일일이 지정해 주지 않아도 되는 거지.




