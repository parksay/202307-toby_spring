package basics.tobyspring6.chapter66;


import basics.tobyspring6.chapter61.Level611;
import basics.tobyspring6.chapter61.User611;
import basics.tobyspring6.chapter61.UserDao611;

import java.util.List;

public class UserServiceImpl664 implements UserService664 {

    private UserDao611 userDao;
    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;


    public void setUserDao(UserDao611 userDao) {
        this.userDao = userDao;
    }


    public void upgradeLevels() {
        List<User611> userList = this.userDao.getAll();
        for (User611 user : userList) {
            if (this.canUpgradeLevel(user)) {
                this.upgradeLevel(user);
            }
        }
    }

    private boolean canUpgradeLevel(User611 user) {
        Level611 currentLevel = user.getLevel();
        switch (currentLevel) {
            case BASIC: return (user.getLogin() >= 50);
            case SILVER: return (user.getRecommend() >= 30);
            case GOLD: return false;
            default: throw new IllegalArgumentException("Unknown Level: " + currentLevel);
        }
    }


    public void upgradeLevel(User611 user) {
        user.upgradeLevel();
        this.userDao.update(user);
    }


    public void add(User611 user) {
        if(user.getLevel() == null) {
            user.setLevel(Level611.BASIC);
        }
        this.userDao.add(user);
    }

    public User611 get(String id) {
        return this.userDao.get(id);
    }


    public List<User611> getAll() {
        return this.userDao.getAll();
    }

    public void deleteAll() {
        this.userDao.deleteAll();
    }

    public void update(User611 user) {
        this.userDao.update(user);
    }
}








