package basics.tobyspring5.chapter51;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

public class UserTest512 {

    private User512 user;


    @BeforeEach
    public void setUp() {
        this.user = new User512();
    }

    @Test
    public void upgradeTest() {
        Level512[] levelList = Level512.values();
        for(Level512 level : levelList) {
            if(level.nextLevel() == null) continue;
            user.setLevel(level);
            user.upgradeLevel();
            Assertions.assertEquals(user.getLevel(), level.nextLevel());
        }
    }

    @Test
    public void upgradeFail() {
        Level512[] levelList = Level512.values();
        for(Level512 level : levelList) {
            if(level.nextLevel() != null) continue;
            user.setLevel(level);
//            Executable executable = () -> {
//                user.upgradeLevel();
//            };
            Assertions.assertThrows(IllegalStateException.class, () -> { user.upgradeLevel(); });
        }
    }
}
