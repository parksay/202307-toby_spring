package basics.tobyspring6.chapter63;

import basics.tobyspring6.chapter61.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;

import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"file:src/main/resources/chapter61/test-applicationContext612.xml"})
public class UserServiceTest633 {

    @Autowired
    private UserDao611 userDao;
    @Autowired
    private PlatformTransactionManager transactionManager;

    private List<User611> userList;

    @BeforeEach
    public void setUp() {
        this.userList = Arrays.asList(
                new User611("id633-1", "name633-1", "psw633-1", Level611.BASIC, UserService611.MIN_LOGCOUNT_FOR_SILVER - 1, 0),
                new User611("id633-2", "name633-2", "psw633-2", Level611.BASIC, UserService611.MIN_LOGCOUNT_FOR_SILVER, 0),
                new User611("id633-3", "name633-3", "psw633-3", Level611.SILVER, 60, UserService611.MIN_RECOMMEND_FOR_GOLD - 1),
                new User611("id633-4", "name633-4", "psw633-4", Level611.SILVER, 60, UserService611.MIN_RECOMMEND_FOR_GOLD),
                new User611("id633-5", "name633-5", "psw633-5", Level611.GOLD, 100, 100)
        );
    }

    @Test
    public void upgradeAllOrNothing() throws Exception {
        //
        TestUserService testUserService = new TestUserService(this.userList.get(3).getId());
        testUserService.setUserDao(this.userDao);
        TransactionHandler633 txHandler = new TransactionHandler633();
        txHandler.setPattern("upgrade");
        txHandler.setTarget(testUserService);
        txHandler.setTransactionManager(this.transactionManager);
        UserService612 userServiceTx = (UserService612) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[] { UserService612.class },
                txHandler
        );
        //
        this.userDao.deleteAll();
        for(User611 user : this.userList) {
            userServiceTx.add(user);
        }
        try {
            userServiceTx.upgradeLevels();
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
        UserDao611 mockUserDao = Mockito.mock(UserDao611.class);
        Mockito.when(mockUserDao.getAll()).thenReturn(this.userList);
        UserServiceImpl612 userServiceImpl = new UserServiceImpl612();
        userServiceImpl.setUserDao(mockUserDao);
        //
        try {
            userServiceImpl.upgradeLevels();
        } catch (Exception e) {
            throw e;
        }

        Mockito.verify(mockUserDao, Mockito.times(2)).update(Mockito.any(User611.class));
        Mockito.verify(mockUserDao).update(this.userList.get(1));
        Assertions.assertEquals(this.userList.get(1).getLevel(), Level611.SILVER);
        Mockito.verify(mockUserDao).update(this.userList.get(3));
        Assertions.assertEquals(this.userList.get(3).getLevel(), Level611.GOLD);
    }

}


//p.449 - chapter6.3.4
//
//우리는 앞에서 Transaction 기능을 추가하는 InvokeHandler 를 만들었지.
//이거는 만능이었어.
//인터페이스도 자유자재로 받을 수가 있었지.
//data type 을 Object 로 받아서 Object 로 return 해 주니까.
//Proxy.newProxyInstance() 해서 파라미터 넘겨줄 때 TransactionHandler 만 넣어주면 됐었어.
//그러면 프록시의 메소드들을 실행할 때 invoke 메소드가 대신 다 받아줌.
//이제 트랜잭션 기능이 필요한 메소드마다 트랜잭션 기능이 추가된 프록시 파일을 일일이 하나씩 더 만들어 놓아야 할 필요가 없어졌지.
////
//자, 근데 이제 문제가 또 생겼지.
//우리는 지금 스프링을 쓰고 있잖아.
//로직 코드에서 지금 Proxy 만들어가지고 handler 만들고 해가지고 setter 로 타겟 오브젝트 꽂아주고...
//그러고 있을래?
//스프링에서는 그렇게 안 하지.
//설정 정보 파일, xml 이용하지.
//설정 정보를 바탕으로 DI 컨테이너 만들어서 빈 관리하잖아.
//애플리케이션 단에서는 컨테이너에서 빈을 받아다 쓰고 있고.
//그래서 프록시도 컨테이너에 등록해 두고 DI 받아서 쓰려고 했는데....
//이걸 어떻게 등록함?
//프록시는 Proxy.newProxyInstance() 해서 불러야 하잖아.
//Proxy 클래스의 스태틱 팩토리 메소드를 호출해서 인스턴스 받아와야 하잖아.
//그 안에 파라미터도 여러 개 넣어줘야 하고.
////
//사실 스프링도 내부적으로 Reflection API 쓰기는 써.
//컨테이너 만들 때 설정 정보에 class 속성이랑 property 속성이랑 넣잖아.
//일단 Class 에다가 newInstance() 메소드 호출부터 해.
//그러면 파라미터 없이 그 class 의 기본 생성자로 인스턴스를 만들어서 넘겨 줌.
//거기다가 이제 우리가 property 로 넣은 애들을 DI 로 꽂아주는 것.
//예를 들어서 아래처럼 호출하면 인스턴스 받아올 수가 있음.
//Date now = (Date) Class.forName("java.util.Date").newInstance();
//근데 Proxy 같은 경우는 인스턴스를 받아올 때 따로 스태틱 팩토리 메소드 newProxyInstance() 를 호출해서 해서 받아와야 해.
//기본 생성자를 private 으로 막아놨지.
//이렇게 막아 놓는 데에는 이유가 있겠지 않냐고.
//이거를 억지로 리플렉션 API 써서 인스턴스를 만든다고 해도 제대로 작동하지 않을 가능성도 높고.
//<bean id="message" class="springbook.learningtest.spring.factorybean.Message">
//이렇게 private 생성자를 가진 클래스를 bean 으로 등록하는 건 금지야.
////
//그럼 우리가 만든 프록시를 어떻게 빈으로 등록하느냐?
//이럴 때 또 쓰는 방법이 있어.
//스프링 컨테이너는 FactoryBean 이라는 인터페이스를 구현한 클래스는 조금 특별하게 여겨.
//FactoryBean 인터페이스를 구현한 클래스를 컨테이너 설정 정보에서 빈으로 등록하잖아?
//그러면 이거는 그 클래스를 오브젝트로 만들어서 등록하지 않아.
//그런 클래스는 특별히, 그 클래스가 구현한 getObject() 라는 메소드를 호출하고, 그 결과를 빈으로 등록해.
//그러니까 이 getObject() 라는 메소드 안에 우리가 원하는 오브젝트가 만들어지는 과정을 어느 정도 조작할 수가 있다는 거지.
//이 FactoryBean 이라는 인터페이스가 어떻게 작동하는지 감을 잡을 수 있도록 학습 테스트를 하나 만들어 보자.
//Message634.class 로 가 보자.
//
