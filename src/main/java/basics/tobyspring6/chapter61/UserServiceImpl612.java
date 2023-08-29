package basics.tobyspring6.chapter61;


import java.util.List;

public class UserServiceImpl612 implements UserService612 {

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

    public boolean canUpgradeLevel(User611 user) {
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

}

// p.402 - chapter 6.1.2
//try/catch 구문과 try블록 안에 있는 비지니스 로직 사이에는 아무런 관계가 없어.
//파라미터를 주고 받지도 않고 Connection 이런 걸 물고 다닐 필요도 없어.
//그냥 분리시켜도 된다는 거지.
//어떻게 해결할지?
//일단 UserService 인터페이스를 만들어.
//원래 있던 UserService 는 UserServiceImpl 로 만들어.
//거기서 트랜잭션 관리하는 로직만 뽑아서 UserServiceTx 로 옮겨
//다른 애들 입장에서는 UserServiceTx 를 불러다 써.
//그럼 UserServiceTx 는 일단 트랜잭션 로직을 실행해.
//그러고 try/catch 블록 안에서 try 에다가 실제로 비지니스 로직이 있는 UserServiceImpl 을 호출해.
//나머지는 그대로 진행.
//그러니까 UserServiceTx 는 겉보기에만 UserService 인 거지.
//안에 들여다 보면 실제 작업은 다 UserServiceImpl 한테 위임하고 있는 구조.
//이제 스프링 컨테이너 DI 만 잘 맞춰주면 돼.
//UserService 인터페이스를 구현하고 있는 클래스는 2개야.
//다른 데에서도 불러다 쓸 대표 구현 클래스는 UserServiceTx 로 해두기.
//UserServiceTx 를 userService 빈으로 등록하기.
//UserServiceTx 안에는 실제 비지니스 로직을 실행한 UserServiceImpl 을 의존 오브젝트로 넣어주기.
//외부 로직 ------> UserServiceTx ------> UserServiceImpl







