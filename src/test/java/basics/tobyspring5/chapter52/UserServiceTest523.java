package basics.tobyspring5.chapter52;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"file:src/main/resources/chapter52/test-applicationContext523.xml"})
public class UserServiceTest523 {

    @Autowired
    private UserDao521 userDao;
    @Autowired
    private DataSource dataSource;

    private List<User521> userList;

    @BeforeEach
    public void setUp() {
        this.userList = Arrays.asList(
                new User521("id523-1", "name523-1", "psw523-1", Level521.BASIC, UserService523.MIN_LOGCOUNT_FOR_SILVER - 1, 0),
                new User521("id523-2", "name523-2", "psw523-2", Level521.BASIC, UserService523.MIN_LOGCOUNT_FOR_SILVER, 0),
                new User521("id523-3", "name523-3", "psw523-3", Level521.SILVER, 60, UserService523.MIN_RECOMMEND_FOR_GOLD - 1),
                new User521("id523-4", "name523-4", "psw523-4", Level521.SILVER, 60, UserService523.MIN_RECOMMEND_FOR_GOLD),
                new User521("id523-5", "name523-5", "psw523-5", Level521.GOLD, 100, 100)
        );
    }

    @Test
    public void upgradeAllOrNothing() throws Exception {
        //
        UserService523 testUserService = new TestUserService(this.userList.get(3).getId());
        testUserService.setDataSource(this.dataSource);
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

    static class TestUserService extends UserService523 {
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



