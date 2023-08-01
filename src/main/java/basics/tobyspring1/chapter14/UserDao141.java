package basics.tobyspring1.chapter14;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao141 {

    private ConnectionMaker141 connectionMaker;

    public UserDao141(ConnectionMaker141 connectionMaker) {
        this.connectionMaker = connectionMaker;
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



//이렇게까지 한 걸 개념화 하고 이론화 해서 설명해볼까.
//1. 개방폐쇄 원칙 / 2. 응집도와 결합도 / 3. 전략 패턴
// 1. 개방폐쇄 원칙
//확장에는 열려 있고, 수정에는 닫혀 있음.
//외부적으로는 확장을 자유롭게 할 수 있도록 열려 있어야 좋은 것.
//내부적으로는 다른 기술이 변경되더라도 수정할 필요 없이 닫혀 있어야 좋은 것.
//이때 중요한 게 인터페이스.
//직접 구체적이고 특정한 클래스를 가져다 쓰는 게 아니라 인터페이스를 거쳐서 씀.
//그리고 그 인터페이스라는 틀만 지키면 그 내부를 어떻게 구현하든 가져다 쓰는 입장에서는 상관 없음.
//인터페이스라는 연결 파이프만 잘 지키면 확장이 자유로움
//2. 응집도와 결합도
//비슷한 기능, 관련성이 높은 기능끼리는 묶어서 만들수록 좋음. 응집력은 높게.
//서로 다른 기능들을 조합해서 써야 할 때는 조합을 바꾸기 쉽도록 연결 부위를 느슨하게 만들수록 좋음. 결합도는 낮게.
//관련성이 높은 거는 한 클래스 / 한 메소드 안에 묶어서 넣으면 됨. 응집력 있게.
//관련성이 낮은 거는 중간에 인터페이스라는 매개체를 끼워넣어서 결합력을 낮게 만들면 됨.
//3. 전략 패턴.
//컨텍스트랑 전략이라는 개념이 있음.
//컨텍스트는 이미 정해져 있고 전체적으로 변하지 않는 상태.
//그 컨텍스트 안에는 어떤 부분이 비워져 있고 틀만 짜여 있음.
//그 틀 안을 어떻게 채울지는 외부에서 전달해줌으로써 결정함.
//컨텍스트는 가만 있고 전략을 이것 저것 바꿔가면서 쓰기 편함.
//디자인 패턴의 꽃이라고도 불릴 만큼 중요하고 많이 쓰이는 패턴.
//추가.
//SOLID 는 JAVA 언어뿐 아니라 모든 객체지향 언어에 적용되는 원리
//the Single responsibility principle / S: 단일 책임 원칙
//the Open-closed principle / O: 개방 폐쇄 원칙
//the Liskov substitution principle / L: 리스코프 치환 원칙
//the Interface segregation principle / I: 인터페이스 분리 원칙
//the Dependency Inversion principle / D: 의존관계 역전 원칙
//높은 응집도 / 낮은 결합도는 객체지향 언어뿐 아니라 모든 소프트웨어 개발의 고전 원리

//근데 이렇게 설계했을 떄 문제점이 있어.
//DB 커넥션을 받아오는 걸 원래는 UserDao 가 직접 구현했었지.
//근데 어떤 로직이나 어떤 기술로 DB 커넥션을 받아올지 직접 구현하지 않고 외부에서 받아올 수 있게끔 설계했어.
//그래서 DB 커넥션 만들 때 어떤 클래스 쓸지 클라이언트가 직접 정해서 그 클래스로 오브젝트 만들어서 UserDao 에 넘겨주기로 했어.
//그렇게 해서 여기까지 왔지.
//지금 이 상태에서 클라이언트라고 하는 게 UserDaoTest133.java 야.
//근데 이 클래스는 원래는 UserDao 를 테스트하려고 만든 거였잖아.
//DB connection 을 어떻게 구성해서 가져올지, 그거를 결정하려고 만든 거야?
//그게 아니었잖아.
//결국 UserDaoTest 클래스는 자기 책임이 아닌 다른 로직을 껴안게 됐어.
//자, 그럼 그냥 클래스 간의 관계를 설정하고 오브젝트 간의 관계를 결정해주는 클래스를 따로 만들어 버리자.
//이렇게 특정한 클래스를 오브젝트로 만들 때 어떤 재료로 조합해서 만들지 결정해서 그 결과로 만들어진 오브젝트를 되돌려주는 오브젝트를 '팩토리'라고 함.
//지금부터 그러면 팩토리를 하나 만들어 보자.
//단, 여기서 말하는 팩토리는 팩토리 패턴이나 팩토리 메소드랑은 다른 의미임.
//UserDaoTest 에서는 팩토리가 미리 만들어 놓은 UserDao 객체를 받아오기만 해서 테스트만 하면 되게끔 만들기.
//DaoFactory141.java 로 가보자.