package basics.tobyspring5.chapter52;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"file:src/main/resources/chapter52/test-applicationContext521.xml"})
public class UserServiceTest521 {

    @Autowired
    private UserDao521 userDao;

    private List<User521> userList;

    @BeforeEach
    public void setUp() {
        this.userList = Arrays.asList(
                new User521("id521-1", "name521-1", "psw521-1", Level521.BASIC, UserService521.MIN_LOGCOUNT_FOR_SILVER - 1, 0),
                new User521("id521-2", "name521-2", "psw521-2", Level521.BASIC, UserService521.MIN_LOGCOUNT_FOR_SILVER, 0),
                new User521("id521-3", "name521-3", "psw521-3", Level521.SILVER, 60, UserService521.MIN_RECOMMEND_FOR_GOLD - 1),
                new User521("id521-4", "name521-4", "psw521-4", Level521.SILVER, 60, UserService521.MIN_RECOMMEND_FOR_GOLD),
                new User521("id521-5", "name521-5", "psw521-5", Level521.GOLD, 100, 100)
        );
    }

    @Test
    public void upgradeAllOrNothing() {
        //
        UserService521 testUserService = new TestUserService(this.userList.get(3).getId());
        testUserService.setUserDao(this.userDao);
        //
        this.userDao.deleteAll();
        for(User521 user : this.userList) {
            testUserService.add(user);
        }
        //
        Assertions.assertThrows(TestUserServiceException.class, ()->{testUserService.upgradeLevels();});

    }

    static class TestUserService extends UserService521 {
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
