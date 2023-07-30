package basics.tobyspring1.chapter13;

import java.sql.*;

public class UserDao131 {

    private SimpleConnectionMaker131 simpleConnectionMaker;

    public UserDao131() {
        this.simpleConnectionMaker = new SimpleConnectionMaker131();
    }

    public void add(User111 user) throws ClassNotFoundException, SQLException {
        //
        Connection c = this.simpleConnectionMaker.makeNewConnection();
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
        Connection c = this.simpleConnectionMaker.makeNewConnection();
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

// p. 72
//처음에 우리가 UserDao 를 만들었을 때를 생각해 보자.
//UserDao 라는 한 클래스 안에서 메소드끼리 중복이 있었어.
//DB 연결하는 부분에 수정이 있으면 일일이 찾아서 다 바꿔야 했지.
//DB 연결하는 내용과 DB 데이터를 다루는 내용은 관심사가 다르기도 했고.
//그래서 그 메소드만 따로 빼놓기로 했어.
//다른 메소드에서는 따로 뺴놓은 그 메소드를 불러다 쓰는 구조.
//그러면 수정이 일어나도 한 곳만 수정하면 다른 메소드는 손댈 필요가 없게 됐지.
//근데 문제는 확장성이었어.
//다른 로직은 그대로 두고, DB 연결하는 부분만 자유롭게 바꿔서 쓸 수 있게 하고 싶어.
//이때 사용하면 좋은 기술이 자바 본연의 기술인 '추상 클래스'
//바꿔 쓸 수 있게 만들고 싶은 부분은 abstract 로 선언하기.
//그러면 서브클래스가 상속받으면서 그 내용 알아서 채워서 쓰게끔 했지.
//전체적인 큰 틀과 내용은 다 채워져 있고 일부분만 빈칸으로 둔 템플릿 패턴.
//근데 이제 다시 문제는 확장성.
//자바 언어에서 상속은 하나만 받아올 수 있어.
//UserDao 를 가져다 쓰고 싶은 서브클래스들은 상속을 받아야만 했어.
//그러면 해당 서브클래스가 다른 클래스도 동시에 상속받고 싶을 때는 방법이 없었지.
//또 UserDao 를 상속받는 서브클래스마다 DB연결부분을 구현하는 부분이 중복되기도 했고.
//그래서 생각해 본 해결책이, 그냥 클래스로 깔끔하게 분리하기.
//슈퍼클래스 서브클래스 구조 말고 그냥 서로 아예 다른 클래스로.
//이렇게 하니까 이전의 문제는 해결됐겠지.
//근데 또 다른 문제가 생겼어.
//첫째, 메소드 이름.
//우리가 만들어 놓은 SimpleConnectionMaker 는 우리가 만들어서 우리가 쓰는 중.
//그런데 우리는 UserDao131.java 만 배포할 거지, SimpleConnectionMaker 는 안 줄 거잖아.
//그거는 네이버나 다음 같은 고객사들이 알아서 구현해서 만들기로 했잖아.
//우리는 SimpleConnectionMaker 를 만들 때 메소드 이름을 makeNewConnection() 로 해놨어.
//근데 네이버나 다음이 그걸 알아..? 알고서 맞춰서 구현해줄까?
//엉뚱한 이름으로 만들어 버리면 그걸 가져다 써야 하는 UserDao 에서는
//그런 메소드는 없는 메소드라고 컴파일 에러만 계속 나고 작동 불가.
//그러면 이걸 고객사에 넘겨줄 때 서류로 만들어서 무슨 메소드는 이렇게, 무슨 메소드는 저렇게...
//요구조건을 다 해서 넘길까...? 메소드가 몇 백 개 몇 천 개 이러면...?
//둘째, 클래스 이름.
//우리는 SimpleConnectionMaker 라는 클래스를 만들어서 연결 정보 가져와.
//근데 그 안에 들어가 있는 기술을 바꾸게 된다면...?
//예를 들어 PcConnectionMaker 도 있고 MobileConnectionMaker 도 있어.
//이 둘은 사용하는 기술이 달라.
//그때 그때 환경에 따라서 다른 기술을 사용해서 연결 정보를 가져와야 해.
//둘 다 만들어 놓고 클래스만 그대로 바꿔치기 해서 사용할 수 있으면 편할 텐데,
//UserDao 가 사용하는 클래스의 이름이 SimpleConnectionMaker 로 고정돼 있으니까
//우리는 환경이 바뀔 때마다 SimpleConnectionMaker 클래스를 직접 들어가서
//PcConnectionMaker 의 기술과 MobileConnectionMaker 의 기술로 코드를 계속 수정해줘야 해.
//또는 JDBC 로 할지 JPA 로 할지 MyBatis 로 할지 그때그때 다르게 쓰고 싶은데 클래스 이름이 고정돼 있어.
//이런 복잡하고 번거로운 문제를 해결하기 위해서 어떻게 해야 할까?
//해결책은 바로 자바 언어가 지원하는 기술인 '인터페이스'
//뼈와 뼈 사이에 연골을 두듯이, 문짝과 문짝 사이에 경첩을 두듯이,
//클래스와 클래스 사이에 인터페이스를 끼워넣기.
//사용하는 클래스는 이 인터페이스를 쓴다고까지만 이야기했지 구체적으로 어떤 클래스가 오는지는 상관 없음.
//사용되는 클래스는 그 인터페이스 내용물만 채워넣고 구현하기만 하면 어떤 클래스에 가서 사용될 수가 있음.
//인터페이스는 그 인터페이스를 구현한 구현 클래스를 한 번 감싸서 내용물을 감추어 줌.
//내 포장지 안에 어떤 구체적인 클래스가 들어있는지 감추어 줌.
//동시에 포장지는 어떤 틀의 역할을 하기도 함.
//그 틀의 모양에 맞기만 하면 어떤 곳에든 퍼즐 조각처럼 들어가서 사용될 수 있음.


