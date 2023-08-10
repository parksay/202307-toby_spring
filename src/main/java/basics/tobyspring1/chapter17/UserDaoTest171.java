package basics.tobyspring1.chapter17;


import java.sql.SQLException;

public class UserDaoTest171 {
    public static void main(String args[]) throws ClassNotFoundException, SQLException {
        // 오브젝트 생성
        System.out.println( "Spring Container => IoC/DI Container");
    }
}

//// p.113
//스프링 컨테이너를 부를 때 IoC 컨테이너라는 말을 많이 썼다.
//그러나 IoC 컨테이너라는 말로는 스프링 컨테이너가 가지는 특징을 명확하게 표현해내지 못한다.
//IoC 는 스프링뿐만 아니라 다른 소프트웨어를 개발할 때도 사용하는 일반적인 개념이라고 했다.
//따라서 스프링 컨테이너가 갖는 특징을 좀 더 명확히 나타내기 위해서 IoC/DI 컨테이너라는 말을 쓴다.
//여기서 IoC 는 앞에서 말했듯이 Inversion of Control 제어의 역전
//그럼 DI 는 뭐냐? Dependency Injection 의존관계 주입
//그러면 일단 '의존관계'가 무엇인지 알아야겠지.
//A 기능을 만들 때 B 기능을 빌려다 쓰고 있어.
//A 클래스에서 어떤 기능을 구현할 때 B 클래스의 어떤 메소드를 호출하고 있어.
//그럼 A 가 제대로 작동하려면 B 가 없으면 안 돼.
//A 의 일부 기능은 B 의 일부 기능에 의존하고 있어.
//A 는 B 에 의존하고 있어.
//이런 관계를 '의존관계'라고 해.
//
//이렇게 의존 관계일 때 곤란해지는 점이 수정 사항이 발생했을 때야.
//예를 들어 A 는 B 의 b 메소드를 빌려다 쓰고 있는데, B의 b 메소드가 수정됐어.
//그러면 B 의 b 메소드를 불러다 쓰고 있는 모든 부분을 A 안에서 찾아서 수정해야 하지.
//이럴 때를 대비해서 의존관계를 형성할 때 중간에 인터페이스를 끼워 넣어.
//A 는 B 에 직접 의존하지 않고, C 라는 인터페이스에 의존해.
//그리고 C 인터페이스를 구현한 B 클래스를 가져다 써.
//그러면 B 클래스에 수정사항이 생기더라도 C 인터페이스만 수정하지 않고 그대로라면 A 도 수정할 일은 없어.
//왜냐하면 A 는 B 에 의존하고 있는 게 아니라 C 에 의존하고 있으니까.
//A 는 C 에 직접 의존하고 있고 B 에는 한 다리 건너서 간접 의존하고 있으니까.
//
//이런 간접 의존 관계 구조가 되면 의존관계를 주입해주는 기능이 필요해져.
//누군가 A 클래스에 C 인터페이스를 구현한 오브젝트를 어떤 걸 넣어줄지 정해줘야 해.
//그게 우리가 만들었던 DaoFactory 나 IoC 컨테이너야.
//이전에는 A 클래스가 자기가 쓸 오브젝트를 직접 선택해서 가져다 썼지.
//public class UserDao {
//    conntectionMaker = new SimpleConnectionMaker();
//        }
//위에서 A 클래스는 자기가 어떤 대상에 의존하고 있는지 클래스 자체가 코드에 드러나.
//아래에서 A 클래스가 자기가 어떤 오브젝트를 쓰게 될지 모르는 상태이고 컨테이너가 넣어주는 걸 받아서 쓸 뿐이야.
//컴파일 단계에서는 모르고 런타임까지 가 봐야 나한테 뭐가 들어올지 알게 돼.
//public class UserDao {
//    private ConnectionMaker connectionMaker;
//    public userDao(ConnectionMaker connectionMaker) {
//        this.connectionMaker = connectionMaker;
//    }
//}
//A 클래스가 어떤 오브젝트를 쓸지는 컨테이너가 결정해.
//A 가 의존하고 있는 대상을 컨테이너가 결정해서 A 한테 넣어줘.
//이런 의미에서 "의존관계 주입"이라고 불러.
//그래서 스프링 컨테이너를 다른 이름으로 IoC/DI 컨테이너라고 말해.
//
//p.117 - 의존관계 검색
//의존관계 검색이라는 개념도 있음.
//의존관계 주입은 컨테이너가 나한테 넣어주는 오브젝트를 받아서 쓰기만 하는 간접적인 형태
//의존관계 검색은 내가 컨테이너한테 이러이러한 오브젝트 있냐고 물어보고 찾아와서 쓰는 능동적인 형태
//예를 들면 아래와 같음.
//public userDao() {
//    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
//    this.connectionmaker = context.getBean("connectionMaker", Connectionmaker.class);
//        }
//이러한 의존관계 검색은 장단점이 있음
//단점은 당연히 딱 봐도 코드가 복잡하고 지저분해 보임.
//지금 dao 생성자인데 ApplicationContext 같은 API 클래스나 메소드까지도 다 직접 가져다 써야 하고.
//장점은 내 자신이 컨테이너에 빈으로 등록되어 있을 필요가 없음.
//의존관계 주입이 훨씬 코드가 깔끔하기는 하지만, 주입을 받으려면 일단 내 자신부터가 컨테이너 안에서 빈으로 등록되어 있어야겠지.
//반면에 의존관계 검색은 내가 직접 컨테이너를 소환해서 이거 달라고 요청하는 거기 때문에 빈이 될 필요는 없음.
//이게 왜 좋냐하면, 언젠가 한 번은 이런 게 필요함.
//자바에서는 main() 라는 기동 메소드가 있지.
//애플리케이션을 기동하는 시점에 적어도 한 번은 이러한 의존관계 검색 방식으로 오브젝트를 가져와야 할 거야.
//이럴 때 userDao 는 의존관계 검색으로 생성자를 구현해놨기 때문에 그냥 아무데서나 불러서 오브젝트를 생성해서 쓸 수 있게 됨.
//이런 점에서 의존관계 검색이 장점이 있음.
//
//p.126 - 메소드를 이용한 의존관계 주입
//지금까지 userDao 만들 때는 생성자에 파라미터를 넣어주는 방법으로 의존관계를 주입했지.
//그렇지만 꼭 생성자로 넣어줄 필요는 없음
//오히려 스프링에서는 수정자를 더 많이 씀
//수정자는 다른 말로 setter method 라고도 해.
//수정자 메소드는 클래스 변수 값을 바꿀 때 사용해.
//수정자 메소드를 만들 때는 약간 관습적인 게 있어.
//메소드 이름이 set 으로 시작하고 뒤에 변수 이름이 붙어.
//파라미터는 하나만 받고, 받아온 변수는 클래스 변수에 넣어줘.
//이외에도 스프링에서는 생성자 메소드/수정자 메소드/초기화 메소드/ 등등... 여러 방법으로 의존관계를 주입할 수 있도록 지원함.