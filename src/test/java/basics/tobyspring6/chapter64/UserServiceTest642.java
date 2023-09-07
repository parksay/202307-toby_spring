package basics.tobyspring6.chapter64;

import basics.tobyspring6.chapter61.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"file:src/main/resources/chapter64/test-applicationContext642.xml"})
public class UserServiceTest642 {

    @Autowired
    ApplicationContext context;
    @Autowired
    private UserDao611 userDao;

    private List<User611> userList;

    @BeforeEach
    public void setUp() {
        this.userList = Arrays.asList(
                new User611("id642-1", "name642-1", "psw642-1", Level611.BASIC, UserService611.MIN_LOGCOUNT_FOR_SILVER - 1, 0),
                new User611("id642-2", "name642-2", "psw642-2", Level611.BASIC, UserService611.MIN_LOGCOUNT_FOR_SILVER, 0),
                new User611("id642-3", "name642-3", "psw642-3", Level611.SILVER, 60, UserService611.MIN_RECOMMEND_FOR_GOLD - 1),
                new User611("id642-4", "name642-4", "psw642-4", Level611.SILVER, 60, UserService611.MIN_RECOMMEND_FOR_GOLD),
                new User611("id642-5", "name642-5", "psw642-5", Level611.GOLD, 100, 100)
        );
    }

    @Test
    @DirtiesContext
    public void upgradeAllOrNothing() throws Exception {
        //
        TestUserService testUserService = new TestUserService(this.userList.get(3).getId());
        testUserService.setUserDao(this.userDao);
        //
        ProxyFactoryBean txProxyFactory = this.context.getBean("&userService", ProxyFactoryBean.class);
        txProxyFactory.setTarget(testUserService);
        //
        UserService612 userService = (UserService612) txProxyFactory.getObject();
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



