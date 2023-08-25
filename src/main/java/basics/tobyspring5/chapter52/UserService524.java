package basics.tobyspring5.chapter52;


import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.sql.SQLException;
import java.util.List;

public class UserService524 {

    private UserDao521 userDao;
    private PlatformTransactionManager transactionManager;
    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;


    public void setUserDao(UserDao521 userDao) {
        this.userDao = userDao;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public void upgradeLevel(User521 user) {
        user.upgradeLevel();
        this.userDao.update(user);
    }

    public void upgradeLevels() {
        //
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            List<User521> userList = userDao.getAll();
            for (User521 user : userList) {
                if (this.canUpgradeLevel(user)) {
                    this.upgradeLevel(user);
                }
            }
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw e;
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

// p.366 - chapter 5.2.4
//지금까지 만든 UserService 와 UserDaoJdbc 는 아주 훌륭해.
//JDBC 가 제공하는 API를 쓰면서도 책임이나 성격에 따라서 코드를 분리했어.
//기술이나 기능에 변경이 발생해도 개방폐쇄 원칙에 따라서 잘 유지할 수 있는 설계야.
//근데 이제 이거를 만들어서 다른 고객사에다가 제공한다고 해 보자.
//그니까 JDBC 기술에 한정해서만 봤을 때는 꽤 좋은 코드지만,
//이제 다른 기술을 적용해 봤을 때는 어떨지 확장성을 고려하는 거지.
//예를 들어서 어떤 애플리케이션은 DB 서버를 하나만 쓰지 않아.
//이쪽 DB 에도 요청 날리고, 저쪽 DB에도 같이 수정해줘야 하고, 어쩌구 저쩌구.
//DB 여러 개를 묶어서 트랜잭션을 관리해야 해.
//이렇게 어떤 DB 하나 안에만 종속되지 않고 트랜잭션 관리자를 따로 두어서 관리하는 방식을 '글로벌 트랜잭션'이라고 해.
//그런 기술은 JTA(Java Transaction API) 라고 자바가 이미 제공하고 있기는 해.
//또는 어떤 애플리케이션은 DB 서버뿐만 아니라 메시징 서버도 있어.
//예를 들어서 유저 레벨이 업그레이드 됐으면 알림 문자를 보내는 거지.
//또는 어떤 데서는 JDBC가 아니라 하이버네이트를 쓴대.
//하이버네이트는 우리가 구현한 것처럼 Connection 이 아니라 Session 이라는 걸 사용하거든.
//그럼 이게 코드를 계속 바꿔야 하는 건가.
//UserService 에서는 Connection 썼다가, Session 썼다가, JTA 썼다가...
//계속 바꿔야 하느냐는 문제가 생기지.
//
////
//이런 식으로 트랜잭션 관리자 기술이 바뀔 수 있다면 어떻게 해야 할까?
//기술이나 서버에 따라서 트랜잭션 관리자도 바뀌는데
//어떤 거를 가져다 쓸지 모르는 경우 어떻게 해야 할까...?
//이럴 때 바로 '추상화'를 활용하는 거야.
//"추상화란 하위 시스템의 공통점을 뽑아내서 분리하는 것"을 말해. (p.369)
//서로 다른 서버고 다른 기술이지만 다들 비슷한 패턴을 가지고 있어.
//뭔가 연결을 일단 열고, 트랜잭션을 시작하고, 작업을 진행하고,
//트랜잭션 커밋하거나 트랜잭션 롤백을 하고, 트랜잭션을 닫고, 연결도 닫고.
//구체적인 문법 자체는 기술마다 다 다르긴 해.
//tx.begin() / connection.setAutoCommit() / transactionManager.getTransaction()
//...
//근데 기능이나 패턴이 비슷하다는 거지.
//이것들을 한데 묶어서 추상화하는 거야.
//JDBC 기술도 추상화가 있었지.
//DB 가 Oracle 인지 MySql 인지 PostgreSQL 인지... 에 따라서 예외 종류도 다르고 예외 코드도 달랐어.
//그래서 성격이 비슷한 예외들끼리 묶어서 한 예외로 추상화했었잖아.
//이것도 비슷한 원리로 만들어 둔 거지.
//어떤 기술이든 간에 비슷한 기능들이 비슷한 패턴을 보이니까 추상화해 버린 거야.
////
//이 편리한 기능을 바로 스프링 SpringFramework 가 미리 만들어 줬다는 것...
//PlatformTransactionManager 인터페이스야.
//이제 이 인터페이스 안에 원하는 기술로 구현한 트랜잭션 매니저를 넣어주면 돼.
//JDBC 를 사용할 경우에는 DataSourceTransactionManager 를 사용하면 되고
//JTA 를 쓴다고 하면 JTATransactionManager 를 사용하면 돼.
//UserService 에서는 어떤 기술을 사용할지 모르니 PlatformTransactionManager 인터페이스로 받아.
//일단 인터페이스로 받아서 인터페이스를 구현한 commit 이나 rollback 같은 걸 실행해.
//UserService 는 이제 트랜잭션 기술에 의존하지 않는 채로 트랜잭션 관리 기능을 사용할 수 있게 됐어.
//JDBC 를 쓰든 하이버네이트를 쓰든 JTA 를 쓰든 UserService 입장에서는 코드를 수정할 필요가 없게 됐어.
//스프링 만세....!!





