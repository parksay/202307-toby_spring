package basics.tobyspring6.chapter63;

import basics.tobyspring6.chapter61.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;

import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"file:src/main/resources/chapter63/TxProxyFactoryBean634-context.xml"})
public class UserServiceTest634 {

    @Autowired
    ApplicationContext context;
    @Autowired
    private UserDao611 userDao;

    private List<User611> userList;

    @BeforeEach
    public void setUp() {
        this.userList = Arrays.asList(
                new User611("id634-1", "name634-1", "psw634-1", Level611.BASIC, UserService611.MIN_LOGCOUNT_FOR_SILVER - 1, 0),
                new User611("id634-2", "name634-2", "psw634-2", Level611.BASIC, UserService611.MIN_LOGCOUNT_FOR_SILVER, 0),
                new User611("id634-3", "name634-3", "psw634-3", Level611.SILVER, 60, UserService611.MIN_RECOMMEND_FOR_GOLD - 1),
                new User611("id634-4", "name634-4", "psw634-4", Level611.SILVER, 60, UserService611.MIN_RECOMMEND_FOR_GOLD),
                new User611("id634-5", "name634-5", "psw634-5", Level611.GOLD, 100, 100)
        );
    }

    @Test
    @DirtiesContext
    public void upgradeAllOrNothing() throws Exception {
        //
        TestUserService testUserService = new TestUserService(this.userList.get(3).getId());
        testUserService.setUserDao(this.userDao);
        //
        TxProxyFactoryBean634 txFactory = this.context.getBean("&userService", TxProxyFactoryBean634.class);
        txFactory.setTarget(testUserService);
        //
        UserService612 userService = (UserService612) txFactory.getObject();
        this.userDao.deleteAll();
        for(User611 user : this.userList) {
            userService.add(user);
        }
        try {
            userService.upgradeLevels();
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


//지금 UserService 클래스의 upgradeLevels() 메소드가 트랜잭션 기능이 적용되는지 확인해보고 싶은 거야.
//우리가 프록시로 만든 빈이 잘 작동하나 안 하나.
//그러려면 정상 작동하는 UserServiceImpl 이 아니라 중간에 예외를 발생시키는 TestUserService 를 실행해야지.
//근데 우리가 빈으로 등록해 놓은 TxProxyFactoryBean634 에서는 userServiceImpl 가 타겟으로 들어가 있어.
//이걸 바꾸고 싶은데 또 다른 테스트에서는 잘 쓰고 있고.
//이럴 때 방법은 그냥 팩토리 빈 자체를 꺼내와서 target 을 다시 set 해주는 것.
//우리 팩토리 빈 그대로 가져오는 거 배웠지.
//FactoryBean 인터페이스를 구현한 클래스를 빈으로 등록하면 그 클래스의 오브젝트가 아니라 getObject() 메소드를 실행한 결과를 빈으로 등록한다고 했어.
//근데 반대로 getObject() 가 아니라 그냥 FactoryBean 인터페이스를 구현한 그 클래스 자체를 받아오고 싶을 때.
//이거 전에 배웠었지.
//context.getBean() 안에 파라미터로 빈 이름 지정해서 넣을 때 앞에 "&" 붙이면 된다고 했어.
//MessageFactoryBeanTest634.class 가 보면 있음.
//암튼 그래서 이 테스트에서는 TxProxyFactoryBean634 팩토리 빈을 그대로 꺼내와서 setTarget() 을 다시 해줌.


