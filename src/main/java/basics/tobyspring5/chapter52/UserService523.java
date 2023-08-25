package basics.tobyspring5.chapter52;


import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserService523 {

    private UserDao521 userDao;
    private DataSource dataSource;
    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;


    public void setUserDao(UserDao521 userDao) {
        this.userDao = userDao;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void upgradeLevel(User521 user) {
        user.upgradeLevel();
        this.userDao.update(user);
    }

    public void upgradeLevels() throws SQLException {
        //
        TransactionSynchronizationManager.initSynchronization();
        Connection c = DataSourceUtils.getConnection(this.dataSource);
        c.setAutoCommit(false);
        try {
            List<User521> userList = userDao.getAll();
            for (User521 user : userList) {
                if (this.canUpgradeLevel(user)) {
                    this.upgradeLevel(user);
                }
            }
            c.commit();
        } catch (Exception e) {
            c.rollback();
            throw e;
        } finally {
            DataSourceUtils.releaseConnection(c, this.dataSource);
            TransactionSynchronizationManager.unbindResource(this.dataSource);
            TransactionSynchronizationManager.clearSynchronization();
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

// p.360 - chapter 5.2
//이제 어떻게 해야 하나?
//트랜잭션을 만들려면 Connection 을 DAO 로부터 Service 로 가져와야 하고,
//Connection 을 Service 로 가져오자니 설계상 문제가 이것 저것 생기고...
//해답은 바로 스프링...!
//스플링에서는 이러한 문제를 꽤 창의적으로 해결해 줌.
//바로 Connection 저장소를 따로 만들어 두는 거지.
//Connection 을 놓아두는 탁자를 가운데다가 놓고
//트랜잭션에 참여하는 애들은 다 그 탁자를 둘러싸고 앉아서
//connection 이 필요하면 쓰고 다시 가운데 제자리에 놓는 거지.
//이렇게 Connection 을 다같이 공유해서 쓰는 방법이라면
//여러 가지 문제가 해결 돼.
//첫째로 피라미드 구조로 최상위 메소드에서 Connection 을 만들고
//모든 하위 메소드에 폭포처럼 하나하나 거쳐서 전달할 필요가 없음.
//수직 구조가 아니라 원형 구조로 짰기 때문.
//또 singleton 으로 관리하면 멀티쓰레드를 이용하더라도
//한 쓰레드 안에서 connection 이 꼬일 일이 없음.
//이렇게 어느 수준 넘어가면 어느 분야든 창의력이 필요함.
//
//
//DataSource.getConnection() 하지 않고
//DataSourceUtils.getConnection(this.dataSource) 을 썼어.
//DataSource 는 그냥 java 에서 제공해주는 기본 클래스지만
//DataSourceUtils 는 SpringFramework 가 미리 만들어둔 기능성 DataSource 야.
//우리가 직접 만든 dataSource 를 넣어주면 그 정보를 바탕으로 이런 저런 작업을 알아서 해주지.
//어떤 기능이 추가돼 있느냐?
//Connection 오브젝트를 생성해주는 건 똑같아.
//다른 점은 트랜잭션 동기화까지 관리해 준다는 점.
//Connection 오브젝트를 생성하고 나면 트랜잭션 동기화에 사용할 수 있도록 저장소에다가 바인딩까지 해줌.
//c.setAutoCommit(flase);
//c.commit();
//c.rollback();
//이런 거는 다 똑같지.
//다른 점은 연결 닫아줄 때.
//connection.close();
//했었지 원래는.
//이제는 아래처럼.
//DataSourceUtils.releaseConnection(c, dataSource);
//닫아줄 때도 DataSourceUtils 통해서 닫아주기.
//다 끝났으면 동기화 작업 종료하고 정리까지 해주기.
//TransactionSynchronizationManager.unbindResource(this.dataSource);
//TransactionSynchronizationManager.clearSynchronization();
//
//근데 생각해 보면 우리가 JdbcTemplate 을 사용할 때 뭐 설정을 따로 바꿔준 건 없었어.
//JdbcTemplate 에서는 트랜잭션 관련된 처리를 따로 해주지 않아도 알아서 작동해.
//jdbcTemplate 내부 설계는 이미 connection 을 공용 저장소에서 가져다 쓰도록 구현돼 있어.
//그럼 지금까지 트랜잭션 동기화 관리를 사용하지 않을 때는 왜 잘 됐느냐?
//일단 공유 저장소에서 connection 찾아보고, 있으면 그거 가져다 쓰고
//없으면 자기가 새로 만들어서 한 connection 가지고서 작업함.
//
// 자 사실 이제 코드나 기능 자체는 거의 흠잡을 데가 없을 정도야
//JDBC 기술을 이용하면서 이보다 더 깔끔하게 설계하고 구현하기 어려울 정도.
//Service 계층과 DAO 계층으로 나누고,
//비지니스 로직은 Service 계층이 책임지고,
//DB 접근 기술은 DAO 가 책임지고,
//그러면서 트랜잭션은 공유 저장소에 두고서 같이 쓰도록 했어.
//책임은 책임대로 나누고 트랜잭션까지 포기하지 않았어.
//근데 이제 문제는 확장성...
//현재는 JDBC 를 쓸 때에 한정해서 구현을 한 거고.
//만약 JDBC 가 아닌 다른 기술을 쓴다면 어떻게 할지....?
//그 부분에 대한 문제와 해결은 아래
//UserService524.class 로 가 보기.


