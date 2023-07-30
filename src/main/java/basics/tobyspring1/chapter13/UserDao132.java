package basics.tobyspring1.chapter13;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao132 {

    private ConnectionMaker132 connectionMaker;

    public UserDao132() {
        // this.connectionMaker = new NaverConnectionMaker132();
        this.connectionMaker = new DaumConnectionMaker132();
    }

    public void add(User111 user) throws ClassNotFoundException, SQLException {
        //
        Connection c = connectionMaker.makeConnection();
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
        Connection c = connectionMaker.makeConnection();
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


// p.77
//이렇게 해서 UserDao131.java 에서 제시했던 첫 번째 문제는 해결했어.
//메소드에 의존하는 문제.
//이제는 인터페이스라는 틀이 정해져 있지.
//UserDao 에서는 인터페이스의 makeConnection() 메소드를 사용하고,
//네이버든 다음이든 이 인터페이스의 makeConnection() 메소드를 알아서 구현하면 됨.
//즉, 인터페이스가 중간에서 일종의 약속 역할을 함.
//양쪽으로 이렇게 갖다 쓸 거니까 이렇게 만들어 달라, 저렇게 만들어 줄 테니까 저렇게 갖다 써라, 하고.
//그런데 아직도 두 번째 문제는 해결되지 않았지.
//특정한 클래스 이름에 의존하고 있어.
//특정하고 구체적인 클래스 자체에 의존하고 있어.
//그렇다는 건 특정한 기술 하나에 의존하고 있다는 것.
//바꾸려면 소스 코드를 수정해야 한다는 것.
//이 문제는 어떻게 해결해줘야 할까?
//
//먼저 알아야 하는 개념이 있어.
//클래스랑 오브젝트랑 구분하는 거야.
//이 둘은 다른 개념이거든.
//두 클래스가 관계를 맺는다?
//그러면 A클래스가 어딘가에서 B클래스를 직접 가져다 쓴다는 거야.
//A클래스 내부에 B클래스를 불러다 쓰는 코드가 있다는 거지.
//오브젝트는 달라.
//두 오브젝트가 관계를 맺는다?
//오브젝트는 클래스를 객체화한 결과야.
//A오브젝트가 B오브젝트를 가져다 쓴다?
//A오브젝트는 B오브젝트를 가져다 쓰고 있기는 하지만
//그 B오브젝트가 어디에서 어떻게 무슨 재료로 만들어졌는지 아무 것도 몰라.
//어떤 클래스 안에서 만들어진 오브젝트인지?
//클래스로 받았는지 인터페이스로 받았는지?
//인터페이스라면 그 인터페이스를 구현한 B오브젝트가
//X클래스로 만든 건지 Y클래스로 만든 건지?
//
//지금 우리가 UserDao132 에서는 Interface 를 받아서 사용하고 있음에도
//NaverConnectionMaker / DaumConnectionMaker 를 자유자재로 수정하지 못하는 이유는?
//클래스끼리 관계를 맺고 있어서야.
//UserDao 클래스의 소스 코드 안에 구체적인 클래스 이름이 등장하니까.
//여기서 UserDao 는 월권 행위를 하고 있지.
//자기 책임이 아닌 일까지 하고 있어.
//관계를 설정한다는 점에서야.
//
//들어봐.
//두 클래스 또는 오브젝트가 관계를 맺을 때,
//어느 한 쪽이 다른 한 쪽을 가져다 쓰겠지.
//A가 B를 가져다 쓰면, A는 서비스가 되고, B는 클라이언트가 돼.
//A는 요청대로 기능을 제공하는 쪽이고, B는 A한테 기능을 요청하는 쪽이고.
//근데 지금 UserDao 는 이렇게 하고 있는 거야.
//"UserDao 야. 나 유저26 정보 좀 불러와 줘."
//"어 알았어. 불러올 때 기술은 JDBC 이용할게!"
//자기 책임을 넘어서 너무 나서는 거지.
//서브웨이 가서 주문하는데
//"로스트치킨 15cm 주세요"
//"네 빵은 이걸로 해드리고 치즈는 이걸로 해드리고 소스는 이걸로 해드릴게요"
//"...?"
//지금 UserDao 는 어떤 기술을 쓸지, 무엇을 재료로 사용할지를
//자기 소스 코드 안에서 스스로 정하고 있어.
//어떤 오브젝트를 사용할지 꼭 내부 안에서 스스로 정할 필요는 없어.
//어떤 기술을 사용할지, 어떤 오브젝트를 사용할지는 클라이언트가 정해.
//그 서비스를 요청하는 사람이 정하는 거야.
//근데 UserDao 는 자기가 사용할 기술을 자기가 알아서 정하고 있는 거지.
//이 책임을 다시 클라이언트 쪽으로 돌려줘야 해.
//클라이언트가 서비스를 요청할 때 이 오브젝트 쓸 거라고 재료까지 같이 줄 수 있어야 해.
//오브젝트를 주고 받는 방법은?
//메소드 호출할 때 파라미터로 던져주면 되지.
//클라이언트가 쓰고 싶은 오브젝트를 UserDao 생성할 때 생성자에다가 파라미터로 넣어주면 되겠다.
//
//그러니까, 처음에 UserDao 에는 책임이 총 세 개가 있었던 거야.
//첫째, DB 연결 가져오기.
//처음에는 같은 클래스 안에 다른 메소드로 분리했었다가,
//다음에는 템플릿 메소드 패턴을 사용해서 그 클래스를 상속받는 외부 서브클래스가 직접 구현할 수 있도록 했다가,
//마지막에는 ConnectionMaker 인터페이스를 거쳐서 사용하도록 수정했지.
//둘째, SQL 생성해서 DB로부터 정보 받아오기.
//이 부분 로직은 UserDao 책임이 맞아서 그대로 두었고.
//셋째, 오브젝트들끼리 관계를 맺어주기.
//이거는 생성자를 통해서 오브젝트를 받아서만 사용하도록 책임을 클라이언트 쪽으로 떠넘겼지.
