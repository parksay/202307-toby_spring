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
        txHandler.setFilter("upgrade");
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


