package basics.tobyspring5.chapter52;


import java.util.List;

public class UserService521 {

    private UserDao521 userDao;
    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;


    public void setUserDao(UserDao521 userDao) {
        this.userDao = userDao;
    }

    public void upgradeLevel(User521 user) {
        user.upgradeLevel();
        this.userDao.update(user);
    }

    public void upgradeLevels() {
        //
        List<User521> userList = userDao.getAll();
        //
        for(User521 user : userList) {
            if(this.canUpgradeLevel(user)) {
                this.upgradeLevel(user);
            }
        }
    }

    public boolean canUpgradeLevel(User521 user) {
        Level521 currentLevel = user.getLevel();
        switch (currentLevel) {
            case BASIC: return (user.getLogin() >= 50);
            case SILVER: return (user.getRecommend() >= 30);
            case GOLD: return false;
            default: throw new IllegalArgumentException("Unknown Level: " + currentLevel);
        }
    }

    public void add(User521 user) {
        if(user.getLevel() == null) {
            user.setLevel(Level521.BASIC);
        }
        this.userDao.add(user);
    }
}

