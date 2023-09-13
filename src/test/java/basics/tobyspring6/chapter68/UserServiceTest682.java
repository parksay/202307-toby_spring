package basics.tobyspring6.chapter68;

import basics.tobyspring6.chapter61.Level611;
import basics.tobyspring6.chapter61.User611;
import basics.tobyspring6.chapter61.UserDao611;
import basics.tobyspring6.chapter61.UserService611;
import basics.tobyspring6.chapter66.UserService664;
import basics.tobyspring6.chapter66.UserServiceImpl664;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"file:src/main/resources/chapter66/test-applicationContext664.xml"})
public class UserServiceTest682 {

    @Autowired
    ApplicationContext context;
    @Autowired
    private UserDao611 userDao;
    @Autowired
    private UserService664 userService;
    @Autowired
    private UserService664 testUserService;

    private List<User611> userList;

    @BeforeEach
    public void setUp() {
        this.userList = Arrays.asList(
                new User611("id664-1", "name664-1", "psw664-1", Level611.BASIC, UserService611.MIN_LOGCOUNT_FOR_SILVER - 1, 0),
                new User611("id664-2", "name664-2", "psw664-2", Level611.BASIC, UserService611.MIN_LOGCOUNT_FOR_SILVER, 0),
                new User611("id664-3", "name664-3", "psw664-3", Level611.SILVER, 60, UserService611.MIN_RECOMMEND_FOR_GOLD - 1),
                new User611("id664-4", "name664-4", "psw664-4", Level611.SILVER, 60, UserService611.MIN_RECOMMEND_FOR_GOLD),
                new User611("id664-5", "name664-5", "psw664-5", Level611.GOLD, 100, 100)
        );
    }

    @Test
    @DirtiesContext
    public void upgradeAllOrNothing() throws Exception {
        //
        this.userDao.deleteAll();
        for(User611 user : this.userList) {
            this.testUserService.add(user);
        }
        try {
            this.testUserService.upgradeLevels();
            Assertions.fail("TestUserServiceException expected");
        } catch (TestUserServiceException e) {
            // on test
        } catch (Exception e) {
            throw e;
        }

        checkLevelUpgraded(this.userList.get(1), false);

    }

    private void checkLevelUpgraded(User611 user, boolean upgraded) {
        User611 userRead = this.userDao.get(user.getId());
        if(upgraded) {
            Assertions.assertEquals(user.getLevel().nextLevel(), userRead.getLevel());
        } else {
            Assertions.assertEquals(user.getLevel(), userRead.getLevel());
        }
    }

    static class TestUserServiceImpl extends UserServiceImpl664 {
        private String stopId = "id664-4";
        public void upgradeLevel(User611 user) {
            if(user.getId().equals(this.stopId)) throw new TestUserServiceException();
            super.upgradeLevel(user);
        }
        public List<User611> getALl() {
            for(User611 user : super.getAll()) {
                super.update(user);
            }
            return null;
        }
    }

    static class TestUserServiceException extends RuntimeException {
    }

    @Test
    public void upgradeLevelsTest() throws Exception {
        //
        UserDao611 mockUserDao = Mockito.mock(UserDao611.class);
        Mockito.when(mockUserDao.getAll()).thenReturn(this.userList);
        UserServiceImpl664 userServiceImpl = new UserServiceImpl664();
        userServiceImpl.setUserDao(mockUserDao);
        //
        try {
            userServiceImpl.upgradeLevels();
        } catch (Exception e) {
            throw e;
        }

        Mockito.verify(mockUserDao, Mockito.times(2)).update(Mockito.any(User611.class));
        Mockito.verify(mockUserDao).update(this.userList.get(1));
        Assertions.assertEquals(this.userList.get(1).getLevel(), Level611.SILVER);
        Mockito.verify(mockUserDao).update(this.userList.get(3));
        Assertions.assertEquals(this.userList.get(3).getLevel(), Level611.GOLD);
    }

    @Test
    public void advisorAutoProxyCreator() {
        Assertions.assertInstanceOf(java.lang.reflect.Proxy.class, this.testUserService);
    }

    @Test
    public void readOnlyTransactionAttribute() {
        this.testUserService.getAll();
    }
}


//// p.532 - chapter.6.7.1
////
//어드바이스와 포인트컷을 활용해서 애플리케이션 전반에 걸쳐서 일괄적으로 트랜잭션을 적용하는 방식은 편리하다.
//복잡하지 않은 트랜잭션 속성을 정의할 때 알맞다.
//그런데 세밀하게 트랜잭션을 정의해야 하는 경우도 있다.
//클래스나 메소드마다 다르게 트랜잭션 속성을 다르게 정의해야 한다면...?
//포인트컷 방식을 사용하려면 패턴이 복잡해지고 스프링 설정 파일도 지저분해질 것이다.
//이럴 때 사용하면 좋은 방법은 어노테이션 방식이다.
////
//@Transactional 이라는 어노테이션을 붙이면 그 대상에 트랜잭션을 적용할 수 있다.
//어노테이션을 붙일 수 있는 대상은 클래스와 메소드다.
//스프링은 어노테이션이 붙어 있는 모든 대상을 싹 긁어 모아서 그 대상만을 포함하는 포인트컷을 자동으로 생성해서 돌려준다.
//이때 사용되는 포인트컷은 TransactionAttributeSourcePointcut 이다.
////
//@Transactional 은 어디에 붙느냐에 따라서 우선순위가 다르다.
//이 어노테이션을 붙일 수 있는 대상은 클래스와 메소드라고 했다.
//조금 더 추상적인 계층까지 생각한다면 총 네 곳이 된다.
//인터페이스 클래스, 인터페이스 메소드, 타겟 클래스, 타겟 메소드.
//우선순위는 구체적일수록 높고 추상적일수록 낮다.
//즉 타겟 메소드, 타겟 클래스, 인터페이스 메소드, 인터페이스 클래스 순서로 적용된다.
//따라서 조금 더 복잡하고 다양한 경우의 수까지 고려해서 트랜잭션 속성을 정의할 수 있다.
//이 인터페이스를 구현하는 애들은 이런 트랜잭션을 가져야 하고,
//대신 그중에서 이 기술 클래스로 구현한 타겟 클래스들은 이런 트랜잭션을 가져야 하고,
//다시 그중에서 이 메소드만큼은 저런 트랜잭션을 가져야 한다.
//이런 식으로 추상에서 구체화하는 단계별로 다르게 적용할 수 있기 떄문이다.
//다만 복잡하지 않은 경우라면 타겟 클래스에 붙이는 방법을 권장한다.
//이유는 그 방법이 좀 더 확실하기 때문.
//사실 한 곳에만 트랜잭션을 적용한다면 인터페이스에 걸어 두는 게 바람직하다고 볼 수 있기는 하다.
//왜냐하면 클라이언트는 인터페이스로 선언하고 스프링 컨테이너로부터 DI 받아서 쓰는 거기 때문이다.
//하지만 이러한 방식은 스프링 AOP 처럼 프록시를 사용하는 방법에 한해서만 작동한다.
//AspectJ 처럼 프록시 방식이 아닌 경우에는 트랜잭션이 적용되지 않을 수 있다.
//그러니까 내가 진짜 AOP 를 정교하게 잘 다룬다 하는 게 아니라면 차라리 마음 편하게 타겟 클래스에 직접 꽂아 박는 게 안전하다.
////
//스프링에서 @Transactional 어노테이션을 사용하는 방법은?
//스프링 설정 파일에 아래 태그를 추가하면 된다.
//이거 하나만 추가해 두면 어드바이스, 포인트컷, 어드바이저 등등 알아서 등록을 해준다.
//너무 편리하다.
//<tx:annotation-driven />
//
//
//// p.539 - chapter.6.8.1
//트랜잭션 전파라는 기능이 얼마나 유용한 기능인지 생각해 보자.
//이 기능 덕분에 우리는 메소드들은 재사용할 수 있고, 모듈화, 컴포넌트화 할 수 있다.
//무슨 말이냐?
//트랜잭션 전파라는 기능이 없었다면 우리는 같은 코드를 트랜잭션 단위가 다른 곳마다 똑같이 복붙해서 만들었어야 할 것이다.
//예를 들어서 add() 메소드를 보자.
//add() 메소드는 user 데이터 하나를 DB 에 등록하는 메소드다.
//add() 메소드는 그 자체로 하나의 트랜잭션을 가지고 있는 게 맞다.
//그런데 꼭 단독으로만 쓰이지는 않을 것이다.
//비지니스 로직을 짜다 보면 다른 기능들을 한참 진행하다가 add() 를 부르기도 할 것이다.
//이때 트랜잭션은 add() 단독으로 가져가면 안 되고 비지니스 로직 전체가 가지고 있어야 한다.
//이벤트 신청자를 일괄적으로 처리하고 있는데 앞에 20명만 add() 완료되고 뒤에 80명은 add() 실패하면 말이 안 되지 않나.
//그러면 이 이벤트 신청자를 처리하는 비지니스 로직이 트랜잭션을 가져야 하므로 add() 를 포함하는 코드를 새로 짜야 한다.
//여기서 add() 는 똑같은 내용이 중복되어 등장하게 된다.
//근데 이곳에만 중복된다면 다행이겠지.
//애플리케이션 전반에 걸쳐서 복붙으로 계속 만들어뒀을 텐데 칼럼 이름이라도 바뀌면 죄다 찾아가면서 바꿔줘야 한다.
//그런데 트랜잭션 전파라는 기능이 있음으로써 이 모든 문제가 해결 된다.
//add() 메소드가 자체적인 트랜잭션을 가지고 있더라도 먼저 열려 있는 트랜잭션이 있다면 우선은 거기에 참여하도록 만들 수가 있다.
//이러면 한 비지니스 로직 안에서 어떤 메소드를 몇 개를 부르든 한 트랜잭션으로 묶어서 수행할 수가 있다.
//중간에 어디에서 실패를 하든 전체를 롤백하거나 성공하면 커밋할 수 있게 됐다.
////
//이런 트랜잭션 전파라는 기능이 가능했던 일등공신은 바로 트랜잭션 매니저와 트랜잭션 동기화 기술이다.
//트랜잭션 매니저가 트랜잭션을 총괄해서 관리해주고 있기 때문에 다른 곳에서는 이미 열려 있는 트랜잭션이 있는지 확인할 때 매니저한테만 물어보면 된다.
//이미 열려 있는 트랜잭션이 있다면 트랜잭션 매니저한테 받아오고, 없다면 새로 만들어서 보관한다.
//그리고 이러한 방식은 트랜잭션을 어노테이션으로 구현하든 어드바이저로 구현하든 똑같다.
//우리가 지금까지 애플리케이션 전반에 일괄적으로 트랜잭션을 적용하느라 어드바이저를 사용하는 AOP 방식을 배운 것이다.
//사실은 스프링 컨테이너로부터 트랜잭션 매니저를 직접 받아와서 트랜잭션을 열어주기만 하면 된다.
//그러면 그 트랜잭션을 닫아주기까지 모두 한 트랜잭션으로 묶어줄 수가 있다.
//이런 장점을 가장 잘 활용할 수 있는 곳이 바로 테스트다.
////
//테스트에서는 특히 DB 와 데이터를 주고 받는 기능을 단위 테스트로 짜기가 어렵다.
//DB 데이터에 영향을 주기 때문이다.
//데이터를 지우든, 넣든, 수정하든, 다른 테스트에 영향이 갈 수밖에 없다.
//또는 한 테스트 데이터베이스를 여러 개발자가 함께 쓰는 경우에도 서로 겹치고 덮어쓰고 충돌하면서 테스트가 정상적으로 수행되지 않을 수도 있다.
//그런데 한 테스트 안에서 트랜잭션을 관리한다면?
//테스트가 시작될 때 트랜잭션을 열고, DB에 어떤 작업을 한 다음에, 테스트가 끝날 때 모두 강제로 rollback 시켜버리면...?
//한 테스트 메소드와 다른 테스트 메소드 사이에 영향을 주고 받지 않는다.
//개발자들끼리도 영향을 주고 받지 않는다.