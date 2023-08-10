package basics.tobyspring1.chapter16;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;

public class UserDaoTest161 {
    public static void main(String args[]) throws ClassNotFoundException, SQLException {
        // 오브젝트 생성
        DaoFactory162 factory = new DaoFactory162();
        UserDao161 dao1 = factory.userDao();
        UserDao161 dao2 = factory.userDao();
        // 참조값 주소 비교
        System.out.println(dao1);
        System.out.println(dao2);
        // 동일성 비교
        System.out.println( "dao1 === dao2 ? " + (dao1 == dao2) );
        // 동등성 비교
        System.out.println( "dao1 === dao2 ? " + (dao1.equals(dao2)) );

        // 오브젝트 생성
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory161.class);
        UserDao161 dao3 = context.getBean("userDao", UserDao161.class);
        UserDao161 dao4 = context.getBean("userDao", UserDao161.class);
        // 참조값 주소 비교
        System.out.println(dao3);
        System.out.println(dao4);
        // 동일성 비교
        System.out.println( "dao3 === dao4 ? " + (dao3 == dao4) );
        // 동등성 비교
        System.out.println( "dao3 === dao4 ? " + (dao3.equals(dao4)) );
    }
}

//// p.102
//
//// 싱글톤일까?
//DaoFactory 에서 userDao() 를 여러 번 호출한다면?
//그렇게 받아온 userDao 오브젝트 여러 개는 모두 같은 오브젝트일까?
//그렇지 않다.
//// return new ~~~
//했기 때문에 부를 때마다 새로운 오브젝트가 생성된다.
////
//반면에 ApplicationContext 에서 getBean() 으로 받아온 userDao 빈들은 어떤가?
//context 로부터 받아온 userDao 오브젝트 여러 개는 모두 같은 오브젝트인가?
//그렇다.
//스프링 IoC 컨테이너는 기본적으로 모든 Bean을 싱글톤으로 관리하기 때문이다.
//
//// p.105
//// 1.6.1 - 스프링이 싱글톤을 사용하는 이유
//그렇다면 왜 스프링은 굳이 오브젝트들을 싱글톤으로 관리할까?
//그 이유는 스프링은 주로 자바 엔터프라이즈 기술을 사용하는 서버 환경이기 때문이다.
//애초에 스프링이라는 프레임워크 자체가 엔터프라이즈 시스템을 개발하기 위해 만든 기술이다.
//물론 윈도우 응용 프로그램 같은 것도 만들 수는 있다.
//하지만 가장 잘 활용하는 경우는 엔터프라이즈 서버 환경인 경우다.
//서버 환경에서는 브라우저든 시스템이든 수백 수천 개의 요청을 동시다발적으로 받는다.
//요청을 하나 받을 때마다 오브젝트가 몇 개씩 생성된다고 생각해 보자.
//데이터 엑세스 로직, 서비스 로직, 비지니스 로직, 프레젠테이션 로직 등...
//총 생성해야 하는 오브젝트는 문어발식으로 엄청나게 많아질 것이다.
//메모리 낭비도 심하고 서버에 부하가 일어나기 쉽다.
//그래서 서버 환경이라면 오브젝트를 싱글톤으로 관리하는 게 기본이 된다.
//
//// 1.6.1 - 싱글톤의 한계는?
//싱글톤은 가장 기본적이면서 가장 많이 사용하는 디자인 패턴이다.
//그럼에도 비판 받는 디자인 패턴이기도 하다.
//어떤 단점이 있길래 싱글톤 패턴은 비판을 받는 걸까?
//싱글톤이 받는 비판을 이해하려면 일단 싱글톤 구현하는 방법을 일단 알아야 한다.
//// 싱글톤 패턴 만드는 방법
////public class UserDao {
////    private static UserDao INSTANT;
////    pricate UserDao(ConnectionMaker connectionMaker) {
////        this.connectionMaker = connectionMaker;
////    }
////    public static synchronized UserDao getInstance() {
////        if(INSTANT == null) INSTANT = new UserDao(???);
////        return INSTANT;
////    }
////}
//외부에서는 오브젝트를 생성할 수 없도록 생성자를 private 으로 만든다.
//오브젝트는 나 혼자만 생성할 수 있고 내가 생성한 오브젝트는 내가 들고 있는다.
//내가 생성한 오브젝트를 담아둘 스태틱 필드를 선언해 둔다.
//스태틱 팩토리 메소드를 하나 만들어 둔다.
//외부에서 오브젝트를 얻고 싶으면 무조건 이 팩토리 메소드를 통해서만 얻을 수 있도록 한다.
//클래스에서 생성한 오브젝트가 스태틱 필드에 있다면 팩토리 메소드 안에서는 그 오브젝트를 던져준다.
//클래스 스태틱 필드에 아직 오브젝트가 없다면 새로 하나 생성해서 클래스 스태틱 필드에 넣어준다.
//이렇게 해두면 팩토리 메소드가 처음 실행될 때 딱 한 번만 오브젝트를 생성하고,
//그 다음부터는 이미 생성해둔 오브젝트를 던져준다.
//// 싱글톤의 한계
//1. private 생성자만을 사용해야 한다.
//다른 클래스가 상속하려면 private 이 아닌 다른 생성자가 필요하다.
//객체지향이 가지는 장점을 충분히 활용하기 어렵다.
//2. 스태틱 필드와 메소드를 사용한다.
//상속성과 다형성 같은 다른 객체지향의 장점을 이용하기 어렵다.
//3. 테스트하기가 힘들다
//싱글톤 패턴은 오브젝트를 생성하는 방식이 폐쇄적이고 제한적이다.
//목 오브젝트로 대체하기도 어렵다.
//의존 오브젝트들을 생성자에 동적으로 주입하기도 어렵다.
//4. 오브젝트가 하나만 만들어진다는 보장이 어렵다.
//서버 환경에서는 JVM 자체를 여러 개로 분산해서 설치하는 경우가 있다.
//이런 경우에는 싱글톤을 보장하기 어렵다.
//5. 싱글톤은 전역 상태가 된다.
//스태틱으로 선언하다 보니 애플리케이션 어디서나 접근하고 수정할 수 있는 전역 상태가 되기 쉽다.
//// 싱글톤이 갖는 이러한 단점을 해결해 주는 스프링 IoC 컨테이너
//스프링 컨테이너는 이러한 싱글톤 단점들을 한 번에 해결해 준다.
//private 이니 스태틱이니 그런 문법을 굳이 지키지 않아도 된다.
//평범한 자바 클래스라도 IoC 컨테이너에 생성, 관계 설정, 사용 등 제어권을 넘겨주면 알아서 관리해 준다.
//대신 보통 자바 클래스도 싱글톤으로 관리할 때는 주의해야 할 점이 있다.
//싱글톤 오브젝트는 stateless 하게 만들어야 한다는 점이다.
//오브젝트는 하나인데 여기 저기서 사용되고 언제 어디서 사용될지 모르기 때문이다.
//변수를 사용해야 하거나 상태를 사용해야 할 때는 메소드 안에서만 생성하고 소멸할 있도록 한다.
//메소드 파라미터로 받거나 메소드 안에서 변수를 선언하고 끝내야 한다.
//예를 들어서 싱글톤 클래슬르 아래와 같이 설계해서는 안 된다.
//user 나 connection 을 클래스에서 들고 있으면서 상태가 생긴다.
//단, connectionMaker 는 상관 없는데, 읽기 전용이기 때문이다.
//connectionMaker 또한 마찬가지로 싱글톤 오브젝트이다.
//따라서 stateless 로 만들어졌을 것이다.
//클래스 변수도 단순한 읽기 전용 값이나 상수를 사용하려면 클래스 변수로 선언해도 된다.
//단, 그런 경우라면 static final 이나 final 로 선언하는 게 더 낫겠지만.
//public class UserDao {
//    private  ConnectionMaker connectionMaker;
//    private Connection connection;
//    private User user;
//
//    public User get(String id) throws ClassNotFoundException, SQLException {
//        this.c = connectionmaker.makeConnection();
//        // ~~~~ 로직
//        this.user = new User();
//        this.user.setId(rs.getString("id"));
//        this.user.setName(rs.getString("name"));
//        this.user.setPassword(rs.getString("password"));
//        // ~~~ 로직
//        return this.user;
//    }
//}

