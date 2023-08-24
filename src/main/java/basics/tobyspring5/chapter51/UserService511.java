package basics.tobyspring5.chapter51;

import java.util.List;

public class UserService511 {

    private UserDao511 userDao;


    public void setUserDao(UserDao511 userDao) {
        this.userDao = userDao;
    }


    public void upgradeLevels() {
        List<User511> userList = this.userDao.getAll();
        for(User511 user : userList) {
            boolean changed = false;
            if(user.getLevel() == Level511.BASIC && user.getLogin() >= 50) {
                user.setLevel(Level511.SILVER);
                changed = true;
            } else if(user.getLevel() == Level511.SILVER && user.getRecommend() >= 30) {
                user.setLevel(Level511.GOLD);
                changed = true;
            } else if(user.getLevel() == Level511.GOLD) {
                changed = false;
            } else {
                changed = false;
            }
            if(changed) {
                this.userDao.update(user);
            }
        }
    }

    public void add(User511 user) {
        if(user.getLevel() == null) {
            user.setLevel(Level511.BASIC);
        }
        this.userDao.add(user);
    }
}
