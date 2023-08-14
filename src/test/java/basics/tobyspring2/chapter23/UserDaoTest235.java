package basics.tobyspring2.chapter23;

import org.junit.jupiter.api.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;

import java.sql.SQLException;

public class UserDaoTest235 {
    // 오브젝트 생성
    UserDao233 dao;
    // 테스트 데이터 만들어 두기
    User232 user1;
    User232 user2;
    User232 user3;

    @BeforeEach
    public void setUp() {
        // 픽스처 dao 테스트 오브젝트 생성
        ApplicationContext context = new GenericXmlApplicationContext("chapter23/applicationContext233.xml");
        this.dao = context.getBean("userDao", UserDao233.class);
        // 픽스처 user 테스트 데이터 생성
        this.user1 = new User232("id235-1", "name235-1", "psw235-1");
        this.user2 = new User232("id235-2", "name235-2", "psw235-2");
        this.user3 = new User232("id235-3", "name235-3", "psw235-3");
    }

    @Test
    public void addAndGet() throws SQLException {

        // deleteAll() / getCount() 테스트
        this.dao.deleteAll();
        Assertions.assertEquals(0, this.dao.getCount());

        // add() / get() 테스트 > add
        this.dao.add(this.user1);
        Assertions.assertEquals(1, this.dao.getCount());
        this.dao.add(this.user2);
        Assertions.assertEquals(2, this.dao.getCount());

        // add() / get() 테스트 > get
        User232 user3 = this.dao.get(this.user1.getId());
        Assertions.assertEquals(user3.getId(), this.user1.getId() );
        Assertions.assertEquals(user3.getName(), this.user1.getName() );
        Assertions.assertEquals(user3.getPassword(), this.user1.getPassword() );
        User232 user4 = this.dao.get(this.user2.getId());
        Assertions.assertEquals(user4.getId(), this.user2.getId() );
        Assertions.assertEquals(user4.getName(), this.user2.getName() );
        Assertions.assertEquals(user4.getPassword(), this.user2.getPassword() );

        // deleteAll() / getCount() 테스트
        this.dao.deleteAll();
        Assertions.assertEquals(0, this.dao.getCount());


    }

    @Test
    public void getCount() throws SQLException{

        // 0개 부터 시작해서 하나씩 올려보고 마지막에 다시 0개로 끝내기
        this.dao.deleteAll();
        Assertions.assertEquals(0, this.dao.getCount());
        this.dao.add(this.user1);
        Assertions.assertEquals(1, this.dao.getCount());
        this.dao.add(this.user2);
        Assertions.assertEquals(2, this.dao.getCount());
        this.dao.add(this.user3);
        Assertions.assertEquals(3, this.dao.getCount());
        this.dao.deleteAll();
        Assertions.assertEquals(0, this.dao.getCount());
    }


    @Test
    public void getUserFailure() throws SQLException {

        // 혹시나 일치하는 데이터가 우연히 있을지 모르니 일단 모두 지우고
        this.dao.deleteAll();
        // 엉뚱한 데이터로 uesr 찾아보기
        Assertions.assertThrows(EmptyResultDataAccessException.class, ()->this.dao.get("emptyId"));

    }
}


//p.179
//리팩토링 대상은 애플리케이션 코드만이 아니다.
//테스트 코드 또한 리팩토링 대상이다.
//테스트 코드도 한눈에 보기 쉽고이해하기 쉽고 깔끔하고 변경이 용이한 코드로 만들어야 좋다.
//애플리케이션에 변화나 수정이 일어날 때마다 두고두고 돌려 보면서 이상이 없는지 체크해야 하기 때문이다.
//우리 테스트 메소드들을 보면 아래 코드가 반복되고 있다.
//> ApplicationContext context = new GenericXmlApplicationContext("chapter23/applicationContext233.xml");
//애플리케이션 코드였다면 어떻게 리팩토링 했을까?
//공통 부분은 메소드로 따로 빼두는 '메소드 추출' 방식을 사용했을 것이다.
//하지만 테스트 코드를 리팩토링할 때는 조금 다르다.
//테스트는 특성이 있기 때문이다.
//테스트 메소드들끼리 서로 영향을 주고 받지 않고 각 메소드가 독립적인 환경에서 수행했을 때 원하는 대로 동작해야 한다.
//그리고 반복해서 수행했을 때 똑같이 동작해야 한다.
//각 메소드는 독립적인 환경 속에서 여러 번 수행해도 같은 결과가 나와야 한다.
//따라서 JUnit 은 여러 가지 기능을 제공해 이러한 환경을 손쉽게 만들어주려고 한다.
//
//JUnit 은 테스트 '프레임워크'라고 했다.
//프레임워크는 자기가 실행 흐름을 쥐고 있다.
//우리가 짜는 코드는 그 흐름 안에서 불려가서 사용될 뿐이다.
//따라서 프레임워크를 학습할 때는 그 흐름을 알아야 한다.
//JUnit 의 실행 순서는 다음과 같다.
//@BeforeAll
//@BeforeEach
//@Test testMethod1 ~~
//@AfterEach
//@BeforeEach
//@Test testMethod2 ~~
//@AfterEach
//@BeforeEach
//@Test testMethod3 ~~
//@AfterEach
//....
//@AfterAll
//
//
//테스트 메소드를 하나 실행할 때마다 그 전에 @BeforeEach 메소드를 실행하고 그 후에는 @AfterEach 메소드를 실행한다.
//그리고 모든 메소드를 통틀어 시작하기 전에 @BeforeAll 메소드를 한 번 실행하고 끝나고 나서 @AfterAll 메소드를 한 번 실행한다.
//@BeforeEach 메소드나 @AfterEach 메소드를 테스트 메소드가 직접 부르거나 하지 않기 때문에 공유할 데이터가 있으면 클래스 변수를 거쳐서 주고 받아야 한다.
//대표적으로 픽스처가 있다.
//픽스처(fixture) 란 테스트를 수행하는 데에 필요한 정보나 오브젝트를 말한다.
//일반적으로 픽스처는 여러 테스트 메소드에 거쳐서 반복적으로 사용하기 때문에 @BeforeEach 메소드에서 생성해 두면 편하다.
//우리에게는 user1, user2, user3 이런 테스트 데이터들이 픽스처가 될 수 있다.
//물론 이런 데이터를 쓰지 않는 메소드도 있을 수 있지만 여러 메소드에 걸쳐서 사용할 것이므로 클래스 변수에 넣어두기로 하자.
//어차피 테스트 데이터이므로 하드 코딩으로 선언자에 박아두어도 된다.
//private User user1 = new User("id", "name", "psw");
//그렇지만 픽스처 생성하는 코드를 @Before 에 모아두는 게 보기 좋으므로 @BeforeEah 에서 생성하도록 하자.
//우리가 메소드마다 사용하고 있는 ApplicationContext 도 사실 픽스처 오브젝트지.
//ApplicationContext context = new GenericXmlApplicationContext("chapter23/applicationContext232.xml");
//이것도 마찬가지로 @BeforeEach 에서 생성해 보자.

//이렇게 하면 테스트는 모두 문제 없이 통과한다.
//그런데 성능은 좋지가 않다.
//ApplicaionContext 는 초기화하는 데에 오래걸린다.
//그거를 @BeforeEach 에 넣어 두면 테스트 메소드 하나 실행할 때마다 오브젝트를 계속해서 새로 만든다.
//이게 당장은 문제가 없지만 애플리케이션이 커지고 복잡해지면 문제가 될 수도 있다.
//어떤 오브젝트는 초기화할 때 독립적으로 쓰레드를 쓰기도 하고 독자적으로 이런 저런 리소스를 사용하기도 한다.
//이런 경우는 오브젝트를 새로 만들 때마다 리소스도 같이 초기화하는 과정을 깔끔하게 해줘야 반복할 때 문제가 생기지 않는다.
//이런 작업을 테스트 메소드마다 반복하려니까 성능이 떨어진다.
//따라서 테스트할 때 초기화하는 데에 시간이 오래 걸리거나 리소스를 많이 잡아먹는 오브젝트는 하나만 만들어서 테스트 전체가 공유하도록 만들기도 한다.
//물론 테스트마다 일관된 결과를 보장해야 하고 순서에도 상관 없어야 한다.
//스프링 애플리케이션 컨텍스트는 초기화하는 데에 시간이 걸리고 한 번 만들어 두면 상태가 바뀌지 않도록 stateless 하게 만들었으므로 테스트 전체가 공유해도 된다.


