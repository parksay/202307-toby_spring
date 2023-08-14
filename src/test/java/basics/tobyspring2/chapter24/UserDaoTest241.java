package basics.tobyspring2.chapter24;

import basics.tobyspring2.chapter23.User232;
import basics.tobyspring2.chapter23.UserDao233;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.SQLException;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"file:src/main/resources/chapter23/applicationContext233.xml"})
public class UserDaoTest241 {

    // 픽스처 dao 테스트 오브젝트 생성
    //@Autowired
    //ApplicationContext context;
    @Autowired
    private UserDao233 dao;
    // 테스트 데이터 만들어 두기
    private User232 user1;
    private User232 user2;
    private User232 user3;


    @BeforeEach
    public void setUp() {
        // 픽스처 user 테스트 데이터 생성
        this.user1 = new User232("id241-1", "name241-1", "psw241-1");
        this.user2 = new User232("id241-2", "name241-2", "psw241-2");
        this.user3 = new User232("id241-3", "name241-3", "psw241-3");
        System.out.println(this);
        System.out.println(this.dao);
        //System.out.println(this.context);
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


//@ExtendWith 라는 어노테이션은 테스트를 확장시켜서 사용할 수 있도록 해줌
//우리는 스프링 컨테이너를 사용할 거라서 같이 넣어줬음.
//setUp() 메소드를 보면 this 와 this.dao 를 출력해 보고 있음.
//this 는 테스트 오브젝트이므로 테스트 메소드마다 새로 생성해서 바뀌지
//근데 dao 는 바뀌지 않고 한 오브젝트를 계속 사용하고 있어
//어떻게 이게 되느냐?
//JUnit 도 프레임워크고 어딘가에서 실행시키고 있겠지.
//테스트를 실행하기 전에 스프링 오브젝트를 미리 만들어 두고 그걸 JUnit 에 넣어주면서 테스트 실행하라고 하고 있겠지.
//일종의 DI 이고 오브젝트 컨테이너인 셈이지.
//각주로 처리해 뒀는데, ApplicationContext 자체도 @Autowired 로 받을 수 있어.
//즉, 스프링 컨테이너는 자기 자신도 빈으로 등록해두고 관리한다는 점을 알 수 있지.
//
//@Autowired 를 달아두면 빈을 찾는 방버이 두 가지라고 했어.
//첫째로 데이터 타입으로 찾아.
//인터페이스 또는 구현 클래스가 같은 데이터 타입으로 만들어진 오브젝트가 있다면 그걸 넣어줘.
//둘째로 빈 이름으로 찾아.
//만약 같은 데이터 타입으로 만들어진 빈이 두 개 이상 발견되면 무엇을 넣어줘야 할지를 멋대로 판단할 수가 없어.
//그래서 일단 변수 이름이랑 같은 이름으로 빈이 있다면 그걸 넣어줘.
//만약 같은 데이터 타입이 두 개인데 변수 이름이랑 겹치는 빈이 없다면 그냥 예외 던지고 멈춤.
//
//나아가서 한 가지 더 알아두어야 할 점.
//그럼 데이터 타입이나 변수 이름을 어떻게 설정해두는 게 좋겠느냐?
//예를 들어서 DataSource 인터페이스를 구현한 SimpleDriverDataSource 가 빈으로 등록돼 있어.
//그럼 테스트 클래스에서 변수를 선언할 때는 아래 둘 중에 어떤 쪽으로 변수를 선언하는 게 좋을까?
//DataSource dataSource;
//SimpleDriverDataSource simpleDriverDataSource;
//dataSource 라고 조금 더 추상화해서 부르는 게 좋을까?
//아니면 simpleDriverDataSource 라고 구체적으로 부르는 게 좋을까?
//그건 상황에 따라서 그때 그때 다르다.
//우리가 애플리케이션 구현 코드에서는 웬만하면 dataSource 로 추상화해서 선언하고 가져다 쓰고 있었지.
//그 이유는?
//dataSoucre 를 구현한 기술이 바뀌더라도 그걸 가져다 쓰고 있는 다른 클래스에서는 내부 코드를 수정할 일이 없도록 하려고.
//SimpleDriverDataSource 든 DaumDriverDataSource 든 NaverDriverDataSource 든 바뀌더라도 가져다 쓰는 곳에는 영향이 없도록 하려고.
//개방-폐쇄 원칙을 지키려고.
//근데 테스트에서는 조금 다르다.
//테스트하는 대상이 누구냐에 따라서 달라지겠지.
//예를 들어서 나는 다른 기능을 테스트하고 싶은 거고 그 과정에서 dataSource 를 가져다 써야 해.
//이럴 때는 dataSource 로 어떤 기술이 들어오든 딱히 상관은 없지.
//그런 거면 변수를 선언할 때 데이터 타입이나 변수 이름을 그냥 dataSource 로 추상화하는 게 낫겠지.
//반대로 지금 테스트하려는 게 NaverDriverDataSource 의 기능이야.
//방금 구현한 NaverDriverDataSource 가 잘 작동하는지 테스트하고 싶어.
//그러면 dataSource 라고 추상화하지 말고 구체적으로 NaverDriverDataSource 로 선언하는 게 맞겠지.
//해서 내 목적에 맞게 내가 원하는 오브젝트를 DI 받을 수 있도록 하기.
