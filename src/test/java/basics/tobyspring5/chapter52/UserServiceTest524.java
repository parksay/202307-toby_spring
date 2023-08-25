package basics.tobyspring5.chapter52;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Arrays;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"file:src/main/resources/chapter52/test-applicationContext524.xml"})
public class UserServiceTest524 {

    @Autowired
    private UserDao521 userDao;
    @Autowired
    private PlatformTransactionManager transactionManager;

    private List<User521> userList;

    @BeforeEach
    public void setUp() {
        this.userList = Arrays.asList(
                new User521("id524-1", "name524-1", "psw524-1", Level521.BASIC, UserService524.MIN_LOGCOUNT_FOR_SILVER - 1, 0),
                new User521("id524-2", "name524-2", "psw524-2", Level521.BASIC, UserService524.MIN_LOGCOUNT_FOR_SILVER, 0),
                new User521("id524-3", "name524-3", "psw524-3", Level521.SILVER, 60, UserService524.MIN_RECOMMEND_FOR_GOLD - 1),
                new User521("id524-4", "name524-4", "psw524-4", Level521.SILVER, 60, UserService524.MIN_RECOMMEND_FOR_GOLD),
                new User521("id524-5", "name524-5", "psw524-5", Level521.GOLD, 100, 100)
        );
    }

    @Test
    public void upgradeAllOrNothing() throws Exception {
        //
        UserService524 testUserService = new TestUserService(this.userList.get(3).getId());
        testUserService.setTransactionManager(this.transactionManager);
        testUserService.setUserDao(this.userDao);
        //
        this.userDao.deleteAll();
        for(User521 user : this.userList) {
            testUserService.add(user);
        }
        //
        try {
            testUserService.upgradeLevels();
            Assertions.fail("TestUserServiceException expected");
        } catch (TestUserServiceException e) {
            // on test
        } catch (Exception e) {
            throw e;
        }

        checkLevelUpgraded(this.userList.get(1), false);

    }

    private void checkLevelUpgraded(User521 user, boolean upgraded) {
        User521 userRead = this.userDao.get(user.getId());
        if(upgraded) {
            Assertions.assertEquals(user.getLevel().nextLevel(), userRead.getLevel());
        } else {
            Assertions.assertEquals(user.getLevel(), userRead.getLevel());
        }
    }

    static class TestUserService extends UserService524 {
        private String stopId;
        public TestUserService(String id) {
            this.stopId = id;
        }
        public void upgradeLevel(User521 user) {
            if(user.getId().equals(this.stopId)) throw new TestUserServiceException();
            super.upgradeLevel(user);
        }

    }

    static class TestUserServiceException extends RuntimeException {
    }

}



