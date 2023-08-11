package basics.tobyspring2.chapter23;

import org.junit.jupiter.api.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;

import java.sql.SQLException;

public class UserDaoTest235 {

    @Test
    public void addAndGet() throws SQLException {

        // 오브젝트 생성
        ApplicationContext context = new GenericXmlApplicationContext("chapter23/applicationContext233.xml");
        UserDao233 dao = context.getBean("userDao", UserDao233.class);

        // 테스트 데이터 만들어 두기
        User232 user1 = new User232("id232-1", "name232-1", "psw232-1");
        User232 user2 = new User232("id232-2", "name232-2", "psw232-2");

        // deleteAll() / getCount() 테스트
        dao.deleteAll();
        Assertions.assertEquals(0, dao.getCount());

        // add() / get() 테스트 > add
        dao.add(user1);
        Assertions.assertEquals(1, dao.getCount());
        dao.add(user2);
        Assertions.assertEquals(2, dao.getCount());

        // add() / get() 테스트 > get
        User232 user3 = dao.get(user1.getId());
        Assertions.assertEquals(user3.getId(), user1.getId() );
        Assertions.assertEquals(user3.getName(), user1.getName() );
        Assertions.assertEquals(user3.getPassword(), user1.getPassword() );
        User232 user4 = dao.get(user2.getId());
        Assertions.assertEquals(user4.getId(), user2.getId() );
        Assertions.assertEquals(user4.getName(), user2.getName() );
        Assertions.assertEquals(user4.getPassword(), user2.getPassword() );

        // deleteAll() / getCount() 테스트
        dao.deleteAll();
        Assertions.assertEquals(0, dao.getCount());


    }

    @Test
    public void getCount() throws SQLException{
        // 오브젝트 생성
        ApplicationContext context = new GenericXmlApplicationContext("chapter23/applicationContext232.xml");
        UserDao232 dao = context.getBean("userDao", UserDao232.class);

        // user 만들어 두기
        User232 user1 = new User232("id232-1", "name232-1", "psw232-1");
        User232 user2 = new User232("id232-2", "name232-2", "psw232-2");
        User232 user3 = new User232("id232-3", "name232-3", "psw232-3");

        // 0개 부터 시작해서 하나씩 올려보고 마지막에 다시 0개로 끝내기
        dao.deleteAll();
        Assertions.assertEquals(0, dao.getCount());
        dao.add(user1);
        Assertions.assertEquals(1, dao.getCount());
        dao.add(user2);
        Assertions.assertEquals(2, dao.getCount());
        dao.add(user3);
        Assertions.assertEquals(3, dao.getCount());
        dao.deleteAll();
        Assertions.assertEquals(0, dao.getCount());
    }


    @Test
    public void getUserFailure() throws SQLException {
        // 오브젝트 생성
        ApplicationContext context = new GenericXmlApplicationContext("chapter23/applicationContext232.xml");
        UserDao232 dao = context.getBean("userDao", UserDao232.class);

        // 혹시나 일치하는 데이터가 우연히 있을지 모르니 일단 모두 지우고
        dao.deleteAll();
        // 엉뚱한 데이터로 uesr 찾아보기
        Assertions.assertThrows(EmptyResultDataAccessException.class, ()->dao.get("emptyId"));

    }
}


//p.179
리팩토링 대상은 애플리케이션 코드만이 아니다.
테스트 코드 또한 리팩토링 대상이다.
테스트 코드도 한눈에 보기 쉽고이해하기 쉽고 깔끔하고 변경이 용이한 코드로 만들어야 좋다.
애플리케이션에 변화나 수정이 일어날 때마다 두고두고 돌려 보면서 이상이 없는지 체크해야 하기 때문이다.
우리 테스트 메소드들을 보면 아래 코드가 반복되고 있다.
> ApplicationContext context = new GenericXmlApplicationContext("chapter23/applicationContext233.xml");
애플리케이션 코드였다면 어떻게 리팩토링 했을까?
공통 부분은 메소드로 따로 빼두는 '메소드 추출' 방식을 사용했을 것이다.
하지만 테스트 코드를 리팩토링할 때는 조금 다르다.
테스트는 특성이 있기 때문이다.
테스트 메소드들끼리 서로 영향을 주고 받지 않고 각 메소드가 독립적인 환경에서 수행했을 때 원하는 대로 동작해야 한다.
그리고 반복해서 수행했을 때 똑같이 동작해야 한다.
각 메소드는 독립적인 환경 속에서 여러 번 수행해도 같은 결과가 나와야 한다.
따라서 JUnit 은 여러 가지 기능을 제공해 이러한 환경을 손쉽게 만들어주려고 한다.

JUnit 은 테스트 '프레임워크'라고 했다.
프레임워크는 자기가 실행 흐름을 쥐고 있다.
우리가 짜는 코드는 그 흐름 안에서 불려가서 사용될 뿐이다.
따라서 프레임워크를 학습할 때는 그 흐름을 알아야 한다.
JUnit 의 실행 순서는 다음과 같다.
@BeforeAll
@BeforeEach
@Test testMethod1 ~~
@AfterEach
@BeforeEach
@Test testMethod2 ~~
@AfterEach
@BeforeEach
@Test testMethod3 ~~
@AfterEach
....
@AfterAll

1. 테스트 클래스에서 @Test 어노테이션이 붙은 메소드 중에
접근제어자가 public 이고 return type 이 void 이고 method parameter 가 없는 모든 메소드를 긁어온다.




