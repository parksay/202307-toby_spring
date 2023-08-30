package basics.tobyspring6.chapter61;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"file:src/main/resources/chapter61/test-applicationContext612.xml"})
public class UserServiceTest622 {

    @Autowired
    private UserDao611 userDao;
    @Autowired
    private PlatformTransactionManager transactionManager;

    private List<User611> userList;

    @BeforeEach
    public void setUp() {
        this.userList = Arrays.asList(
                new User611("id622-1", "name622-1", "psw622-1", Level611.BASIC, UserService611.MIN_LOGCOUNT_FOR_SILVER - 1, 0),
                new User611("id622-2", "name622-2", "psw622-2", Level611.BASIC, UserService611.MIN_LOGCOUNT_FOR_SILVER, 0),
                new User611("id622-3", "name622-3", "psw622-3", Level611.SILVER, 60, UserService611.MIN_RECOMMEND_FOR_GOLD - 1),
                new User611("id622-4", "name622-4", "psw622-4", Level611.SILVER, 60, UserService611.MIN_RECOMMEND_FOR_GOLD),
                new User611("id622-5", "name622-5", "psw622-5", Level611.GOLD, 100, 100)
        );
    }

    @Test
    public void upgradeAllOrNothing() throws Exception {
        //
        TestUserService testUserService = new TestUserService(this.userList.get(3).getId());
        testUserService.setUserDao(this.userDao);
        UserServiceTx612 txUserService = new UserServiceTx612();
        txUserService.setTransactionManager(this.transactionManager);
        txUserService.setUserService(testUserService);
        //
        this.userDao.deleteAll();
        for(User611 user : this.userList) {
            txUserService.add(user);
        }
        try {
            txUserService.upgradeLevels();
            Assertions.fail("TestUserServiceException expected");
        } catch (TestUserServiceException e) {
            // on test
        } catch (Exception e) {
            throw e;
        }

        checkLevelUpgraded(this.userList.get(1), false);

    }

    private void checkLevelUpgraded(User611 user, boolean upgraded) {
        User611 userRead = this.userDao.get(user.getId());
        if(upgraded) {
            Assertions.assertEquals(user.getLevel().nextLevel(), userRead.getLevel());
        } else {
            Assertions.assertEquals(user.getLevel(), userRead.getLevel());
        }
    }

    static class TestUserService extends UserServiceImpl612 {
        private String stopId;
        public TestUserService(String id) {
            this.stopId = id;
        }
        public void upgradeLevel(User611 user) {
            if(user.getId().equals(this.stopId)) throw new TestUserServiceException();
            super.upgradeLevel(user);
        }
    }

    static class TestUserServiceException extends RuntimeException {
    }
    @Test
    public void upgradeLevelsTest() throws Exception {
        //
        MockUserDao622 mockUserDao = new MockUserDao622(this.userList);
        UserServiceImpl612 userServiceImpl = new UserServiceImpl612();
        userServiceImpl.setUserDao(mockUserDao);
        //
        try {
            userServiceImpl.upgradeLevels();
        } catch (Exception e) {
            throw e;
        }

        List<User611> updated = mockUserDao.getUpdated();
        Assertions.assertEquals(2, updated.size());
        checkUserAndLevel(updated.get(0), "id622-2", Level611.SILVER);
        checkUserAndLevel(updated.get(1), "id622-4", Level611.GOLD);
    }
    public void checkUserAndLevel(User611 updated, String expectedId, Level611 expectedLevel) {
        Assertions.assertEquals(expectedId, updated.getId());
        Assertions.assertEquals(expectedLevel, updated.getLevel());
    }
    static class MockUserDao622 implements UserDao611 {
        List<User611> userList;
        List<User611> updated = new ArrayList<>();
        public MockUserDao622(List<User611> userList) {
            this.userList = userList;
        }
        public List<User611> getAll() {
            return this.userList;
        }
        public void update(User611 user) {
            this.updated.add(user);
        }
        public List<User611> getUpdated() {
            return this.updated;
        }
        public void add(User611 user) {
            throw new UnsupportedOperationException();
        }
        public User611 get(String id) {
            throw new UnsupportedOperationException();
        }
        public int getCount() {
            throw new UnsupportedOperationException();
        }
        public void deleteAll() {
            throw new UnsupportedOperationException();
        }
    }

}

//p.413 - chapter 6.2.2
//테스트를 할 때 가장 좋은 방식은 잘게 잘게 나누는 것.
//왜냐하면 테스트가 실패했을 때 원인을 빠르고 정확하게 찾을 수 있기 때문이야.
//클래스가 수십 개씩 얽혀 있는 기능 하나를 테스트하는 게
//메소드 하나만 가져다가 테스트하는 것보다 복잡하고 실패 원인을 찾기 어렵다는 점은 당연하겠지.
//그럼 우리가 지금까지 만든 UserServiceTest 는 과연 잘게 나누어서 테스트하는 상태일까?
//한번 살펴보자.
////
//UserServiceTest 에서 테스트하고 싶은 대상은 UserService 클래스야.
//사용자 정보를 관리하는 비지니스 로직을 구현한 코드이지.
//테스트 단위나 범위가 UserService 클래스가 되어야 해.
//그런데 한 번 더 생각해 보니까 지금은 또 그렇게 굴러가지만은 않아.
//우리는 테스트 코드에서 UserService 구현체를 가져다가 쓰고 있기는 하지.
//근데 그 안을 살펴 보면 UserService 는
//트랜잭션을 관리해주는 TransactionManger 에 의존하고 있고,
//JDBC 를 이용해서 DB 접근 관리해주는 UserDaoJdbc 에도 의존하고 있고,
//UserDaoJdbc 는 DataSource 구현 클래스와 DB 드라이버에 의존하고 있고....
//지금 우리가 만든 애플리케이션은 진짜 간단한 수준이야.
//그냥 스프링 프레임워크를 공부하려고 만든 예시 코드 수준이지.
//근데도 이렇게나 많은 의존 오브젝트들에 걸쳐 있고 복잡한 구조라면...?
//이게 나중에 애플리케이션이 더 커지거나 복잡해지면 얼마나 더 복잡해질지..?
//우리가 지금 UserService 를 테스트한다고 믿고 있던 테스트 클래스는
//사실 UserService 뒤에 숨겨져 있는 수 많은 의존 기술들까지 싹 다 묶어서 테스트 중이였던 거야.
//UserService 하나 테스트하겠다는데 줄줄이 딸려서 나오는 의존 오브젝트들이 너무 많은 상태야.
////
//UserService 에 딸려 있는 의존 오브젝트들을 잘라냄으로써 테스트 대상 오브젝트를 고립시켜 보자.
//고립하는 방법은 무엇일까?
//테스트 대역을 사용하는 것.
//실제 기술로 구현한 오브젝트들이 아니라, 테스트하려고 임시로 만든 오브젝트들을 사용하도록 만드는 거야.
//그럼 여기서 잠깐 '테스트 대역'에 대해서 좀 더 자세히 알아보고 넘어갈게.
////
//영화 찍을 때 어려운 액션 씬을 대신해주는 사람을 '스턴트맨'이라고 하지.
//테스트를 할 때도 실제 오브젝트를 가져오지 않고 테스트 때만 임시로 사용할 대역 오브젝트를 만들어.
//이러한 임시 오브젝트들을 '테스트 더블(Test Double)' / '테스트 대역' 이라고 해.
//테스트 더블은 형태나 역할에 따라서 또 다양하게 나뉘어.
//Dummy Object / Fake Object / Test Spy / Test Stub / Mock Object ...
//그렇다고 뭐 뚜렷한 경계선이 있는 건 아니라서 혼용해서 쓰기도 해.
//대신 크게 Mock 이랑 Stub 정도로 구분해서 쓰는 게 보통이야.
//그럼 둘이 뭐가 다르냐?
//Mock 은 '행위 검증' 이라고 하고 Stub 은 '상태 검증' 이라고 보통 말해.
////
//Mock 을 이용한 행위 검증 / Stub 을 이용한 상태 감증
//Stub 은 반환할 값을 미리 정해놓고 해당 메소드를 호출하면 미리 준비해놓은 값을 반환해줌.
//Mock 는 호출될 것이라고 기대되는 값으로 미리 만들어진 객체 (뭔 소리임?)
//UserService 를 예시로 보자면 getAll() 은 스텁이 필요하고
//update() 메소드는 오브젝트가 필요해.
//즉, 테스트하다가 getAll() 부르면 미리 만들어 놓은 User 리스트 반환해주도록 하기.
//그게 Stub.
//테스트하다가 update() 메소드가 몇 번 호출이 됐고 그 안에 내용물은 어땠는지, 어떻게 호출됐는지.
//그걸 확인하는 게 Mock
//이 둘을 활용한 테스트 예시를 '클린 코드'의 저자 '마틴 파울러'가 만든 걸 보자.
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>.
//public interface MailService {
//    public void send(Message msg);
//}
//public class MailServiceStub implements MailService {
//    private List<Message> messages = new ArrayLisst<Message>();
//    public void send(Message msg) {
//        messages.add(msg);
//    }
//    public int numberSent() {
//        return messages.size();
//    }
//}
//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
//class OrderInternetactionTest {
//    public void testOrderSendsMailIfUnfilled() {
//        Order order  = new Order(TALISKER, 51);
//        Mock warehouse = mock(Warehouse.class);
//        Mock mailer = mock(MailService.class);
//        order.setMailer((MailService) mailer.proxy());
//
//        mailer.expects(once()).method("send");
//        warehouse.expects(once()).method("hasInventory")
//                .withAnyArguments()
//                .will(returnValue(false));
//
//        order.fill((Warehouse) warehoust.proxy());
//    }
//}
//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
////
//UserDaoJdbc 를 대신할 테스트용 목 오브젝트 MockUserDao 를 만들어 보자.
//이것도 테스트에서만 쓸 예정이므로 클래스 내부에 스태틱 내부 클래스로 선언하기.(굳이 파일 따로 만들어도 상관은 없음.)
//당연히 UserDao 인터페이스를 구현해야겠지.
//그런데 우리가 테스트할 때 쓸 메소드는 getAll() 이랑 update() 뿐이야.
//그래도 인터페이스니까 어쩔 수 없이 나머지 메소드들도 모두 구현해줘야 해.
//내부는 null 을 던져주거나 빈 채로 두어도 되긴 해.
//하지만 혹시 모르게 사용될 수 있으니 사용되지 않는 메소드들은 예외를 발생시키도록 해두자.
//혹시라도 다른 데에서 실수로 만들어져서 호출될 수 있으니.
//throw new UnsupportedOperationException();
//MockUserDao 에서 들고 있을 사용자 목록은 생성자로 받아주기.
//getAll() 하면 mock 오브젝트가 들고 있는 사용자 목록 던져주기.
//updated() 도 몇 번 호출됐는지, 사용자들 어떻게 업데이트하도록 요청했는지
//확인해야 테스트가 제대로 되는 거기 때문에 그 요청도 mock 에서 들고 있기.
//내부에 updated 라고 사용자 리스트 새로 하나 더 들고 있다가 update() 호출하면 다 저장해뒀다가
//나중에 다시 꺼내서 확인해 보기.





