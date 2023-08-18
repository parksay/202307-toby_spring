package basics.tobyspring3.chapter34;

import basics.tobyspring2.chapter23.User232;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import java.sql.SQLException;

public class UserDaoTest341 {

    private UserDao341 dao;
    private User232 user1;
    private User232 user2;
    private User232 user3;

    @BeforeAll
    public static void setUpAll() {

    }

    @BeforeEach
    public void setUp() {
        //
        ApplicationContext context = new GenericXmlApplicationContext("chapter34/test-applicationContext341.xml");
        this.dao = context.getBean("userDao", UserDao341.class);
        //
        this.user1 = new User232("id341-1", "name341-1", "psw341-1");
        this.user2 = new User232("id341-2", "name341-2", "psw341-2");
        this.user3 = new User232("id341-3", "name341-3", "psw341-3");
    }

    @Test
    public void add() throws SQLException {
        // deleteAll() 테스트
        this.dao.deleteAll();

        // add()  테스트 > add
        this.dao.add(this.user1);
        this.dao.add(this.user2);
        this.dao.add(this.user3);

        // deleteAll() 테스트
        this.dao.deleteAll();
    }

}


//이번 테스트에서는 스프링 컨테이너 만들어서 쓰고 싶은데...
//DI 받는 거 테스트하려고
//근데 @BeforeAll 은 메소드를 static 으로 선언해야 해서 스프링 컨테이너를 만들 수가 없어.
//스프링 컨테이너를 생성까지는 할 수 있지만, 그래서 빈까지 받아올 수 있지만, 그걸 클래스 변수에 넣어줄 수가 없어.
//static 메소드에서는 클래스 변수에 접근할 수가 없거든.
//static 메소드는 클래스가 오브젝트로 생성되기도 전에 실행되니까.
//@BeforeEach 메소드에서 스프링 컨테이너 계속 만들고 삭제하고 다시 만들고 반복하는 수밖에 없어.
//그게 지금 만든 거.
//그게 싫으면 JUnit 프레임워크에 스프링 컨테이너 기능을 결합해서(확장시켜서) 실행하는 수밖에 없음.
//그러면 스프링 컨테이너를 하나 생성하고 변수들도 스프링 빈처럼 @Autowired 로 받아올 수가 있음.
//아래 참고.

//@ExtendWith(SpringExtension.class)
//@ContextConfiguration(locations = {"file:/src/main/resources/chapter34/test-applicationContext341.xml"})
//public class UserDaoTest341 {
//    @Autowired
//    private UserDao341 dao;
//    (....)
//}