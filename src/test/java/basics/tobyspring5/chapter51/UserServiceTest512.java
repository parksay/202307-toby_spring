package basics.tobyspring5.chapter51;


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
@ContextConfiguration(locations = {"file:src/main/resources/chapter51/test-applicationContext512.xml"})
public class UserServiceTest512 {

    @Autowired
    private UserDao512 userDao;
    @Autowired
    private UserService512 userService;

    private List<User512> userList;

    @BeforeEach
    public void setUp() {
        // 배열 형태로 주면 그걸로 Array 만들어주는 편리한 기능 Arrays.asList(a, b, c, d...);
        userList = Arrays.asList(
                new User512("id512-1", "name512-1", "psw512-1", Level512.BASIC, UserService512.MIN_LOGCOUNT_FOR_SILVER - 1, 0),
                new User512("id512-2", "name512-2", "psw512-2", Level512.BASIC, UserService512.MIN_LOGCOUNT_FOR_SILVER, 0),
                new User512("id512-3", "name512-3", "psw512-3", Level512.SILVER, 60, UserService512.MIN_RECOMMEND_FOR_GOLD - 1),
                new User512("id512-4", "name512-4", "psw512-4", Level512.SILVER, 60, UserService512.MIN_RECOMMEND_FOR_GOLD),
                new User512("id512-5", "name512-5", "psw512-5", Level512.GOLD, 100, 100)
        );
    }

    @Test
    public void beanTest() {
        Assertions.assertNotNull(this.userService);
    }

    @Test
    public void upgradeLevelsTest() {

        //
        this.userDao.deleteAll();
        for(User512 user : this.userList) {
            this.userDao.add(user);
        }
        //
        this.userService.upgradeLevels();
        //
        this.checkLevel(this.userList.get(0), false);
        this.checkLevel(this.userList.get(1), true);
        this.checkLevel(this.userList.get(2), false);
        this.checkLevel(this.userList.get(3), true);
        this.checkLevel(this.userList.get(4), false);
    }


    public void checkLevel(User512 user, boolean upgraded) {
        User512 userUpdate = this.userDao.get(user.getId());
        if(upgraded) {
            Assertions.assertEquals(userUpdate.getLevel(), user.getLevel().nextLevel());
        } else {
            Assertions.assertEquals(userUpdate.getLevel(), user.getLevel());
        }
    }

    @Test
    public void addTest() {
        //
        this.userDao.deleteAll();
        //
        User512 userWithLevel = this.userList.get(4);
        User512 userWithoutLevel = this.userList.get(0);
        userWithoutLevel.setLevel(null);
        //
        this.userService.add(userWithLevel);
        this.userService.add(userWithoutLevel);
        //
        User512 userWithLevelRead = this.userDao.get(userWithLevel.getId());
        User512 userWithoutLevelRead = this.userDao.get(userWithoutLevel.getId());
        //
        Assertions.assertEquals(userWithLevelRead.getLevel(), userWithLevelRead.getLevel());
        Assertions.assertEquals(userWithoutLevelRead.getLevel(), Level512.BASIC);
    }
}



