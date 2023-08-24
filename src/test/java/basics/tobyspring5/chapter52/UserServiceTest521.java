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
        try {
            testUserService.upgradeLevels();
            Assertions.fail("TestUserServiceException expected");
            // try/catch 블록으로 감쌌을 때 동작 =>
            // 실행 중간에 예외가 발생했음 ->
            // catch 에서 받아주려고 기다리고 있나? ->
            // 기다리고 있었으면 catch 로 받아서 흐름이 넘어감 ->
            // 받아주려고 기다리는 애들이 없었으면 그냥 원래 흐름에서 계속 진행함.
            // 우리가 의도했던 예외가 발생한다면 정상 작동이니까 catch 에서 그대로 받아서 아래로 진행시킴.
            // 그게 아니면 try 에서 fail 시켜버리기.
        } catch (TestUserServiceException e) {

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

// p.350 - chapter5.2
//구현은 얼추 끝났어.
//UserDao 만들고 UserDaoJdbc 만들고 UserService 만들고 Level 이랑 User 에다가 필요한 기능 덧붙이고...
//근데 이제 이런 말이 나와.
//"만약에 유저 레벨 업그레이드 하다가 네트워크가 끊기든 시스템에 문제가 생기든 중간에 실패하면 이미 업데이트 된 애들은 그대로 둡니까 아니면 되돌립니까?"
//당연히 되돌려야지.
//똑같이 조건을 충족했는데 누구는 업그레이드 되고, 누구는 안 되고.
//나중에 말 나오지.
//차라리 모두 다 되돌려놓고 사용자들한테 공지 띄우고 다음 날이나 해서 다시 시도하는 게 낫지.
//이런 걸 트랜잭션이라고 해.
 //한 작업이기 때문에 그 작업을 쪼개서 일부분만 성공한다거나 여러 번에 걸쳐서 진행하거나 할 수가 없어.
//될 거면 다같이 되고, 안 될 거면 다같이 안 돼야 해.
//이런 성질을 가지는 작업을 트랜잭션이라고 해.
//화학에서 말하는 원자와 느낌이 비슷하지.
////
//자 그럼 일단 현재 우리가 만들어 놓은 UserService 에서는 어떻게 작동하는지 볼까?
//작업하다가 중간에 취소되면 이미 업그레이드 된 애들은 그대로 둘까? 아니면 되돌려놓을까?
//궁금하면 직접 테스트 돌려서 확인해 보면 되지!
//자 근데 테스트를 짜는 방법을 조금 고민해봐야 해.
////
//왜냐하면 일반 테스트랑 조금 다르거든.
//테스트를 진행하다가 중간에 일부러 작업을 중단시켜야 해.
//그렇다고 프로그램 돌려놓고 진짜로 코드를 뽑을 수는 없잖아.
//그럴 때는 예외를 발생시키면 돼.
//[1,2,3,4,5] 얘네들을 다 넣어두고, 4번쯤에 왔다 하면 일부러 예외를 발생시키도록 하는 거야.
//그러고 들어가 봐서 [1,2,3] 애들이 어떻게 돼 있는지 확인해보는 거지.
//근데 또 문제가 있어.
//그런 코드를 그럼 어디에 어떻게 만들지 문제.
//그러려면 UserService 에서 upgradeLevels() 메소드 코드를 직접 수정해야 해.
//아니면 UserService 에다가 upgradeLevelsTest() 같은 테스트용 메소드를 하나 더 만들어...?
//둘 다 비추야.
//애초에 테스트를 한다는 이유로 멀쩡하게 작동하는 소스 코드를 수정하는 건 좋지 않아.
//그러면 앞으로 계속 테스트 돌려볼 때마다 소스 찾아서 수정할래?
//그럼 테스트 메소드를 만들까?
//당연히 안 돼지.
//이유도 이제는 알지.
//단지 구현하는 것만 중요한 게 아니라, 어디에 구현하는가, 어떻게 설계하는가도 중요하다는 걸 배웠지.
//UserService 가 책임지고 있는 기능에 테스트 코드가 포함돼 있지는 않지.
//그러면, UserServiceTest 클래스를 하나 따로 만들면?
//그건 괜찮을지도.
//그렇다고 복붙해서 만들면 안 되겠지.
//앞으로 계속 테스트 돌려봐야 하는데, 수정사항이 발생하면 두 곳을 일일이 찾아 들어가서 수정해야 하니까.
//그러면 자바 언어가 기본적으로 제공하는 상속을 이용해보는 건?
//UserService 를 상속받아서 UserServiceTest 를 만들고, 우리가 바꾸고 싶은 메소드만 오버라이드 하는 거야.
//이 방법이 가장 좋겠어.
//단, 이렇게 진짜 애플리케이션에서 쓰는 클래스도 아니고, 테스트에서만 오직 한 곳에서만 쓸 거기 때문에 파일을 따로 분리해서 만들기보다는 테스트 클래스 안에다가 static 으로 선언하는 게 더 좋을 것 같아.
//테스트할 때 필요한 예외도 따로 만들어 두자.
//이미 있는 거 써도 되긴 되는데 이게 다른 과정에서 발생한 건지 우리가 정확하게 의도한 그 장소에서 발생한 예외인지 구분하기 위해서.
//마찬가지로 예외 클래스도 테스트 파일에다가 같이 만들어 두기.


// UserDao 나 UserService 로 옮겨
// JDBC 에서 만든 트랜잭션
//그럼 JDBC 에서는 트랜잭션 작업을 어떻게 관리하느냐?
//일단 코드부터 볼까
//>>>>>>>>>>>>>>>>>>>>>
//Connection c= dataSource.getConnection();
//c.setAutoCommit(false);
//try {
//PreparedStatement st1 = c.prepareStatement("update users ...");
//st1.executeUpdate();
//PreparedStatement st2 = c.prepareStatement("delete users ...");
//st2.executeUpdate();
//c.commit(); // => 트랜잭션 커밋
//} catch(Exception e) {
//c.rollback(); // => 트랜잭션 롤백
//}
//c.close();
//<<<<<<<<<<<<<<<<<<<
//원래 DB 에서는 한 SQL 을 단위로 작업을 완료하거나 취소해.
//한 SQL 단위로 트랜잭션을 관리하고 있어.
//그게 기본값이야.
//그거를 이제 우리가 원하는 대로 바꿀 거야.
//일단 한 SQL 을 실행할 때마다 트랜잭션을 commit 하도록 설정되어 있는데 이걸 일부러 끌 거야.
//이걸 AutoCommit 이라고 하거든.
//이 AutoCommit 기능을 끄면 한 SQL 요청이 성공하더라도 바로 commit 이 이루어지지는 않는 상태가 되겠지.
//이 상태로 내가 한 묶음으로 실행하고 싶은 작업들을 요청해.
//그러다가 실행할 작업이 모두 성공했다는 걸 확인하고 나면 그때 가서 수동으로 commit 을 하는 거지.
//근데 만약에 중간에 예외가 발생했다?
//그러면 작업했던 거 다 rollback 해버려.
//이렇게 트랜잭션을 시작하는 방법은 하나지만, 그 트랜잭션이 끝나는 방법은 두 가지야.
//하나는 commit 이고 다른 하나는 rollback.
//그리고 코드를 보면 알겠지만 setAutoCommit 이나 commit/rollback 은 connection 이 관리해.
//즉 한 트랜잭션 묶음은 한 connection 이 만들어지고 닫히는 범위 안에 존재한다는 것.
//또 애플리케이션 코드 안에서도 트랜잭션이 시작되고 끝나는 지점이 있겠지.
//모든 트랜잭션은 시작 지점과 끝 지점이 있어.
//이걸 트랜잭션의 경계라고 해.