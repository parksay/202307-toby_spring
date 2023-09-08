package basics.tobyspring6.chapter65;

import basics.tobyspring6.chapter61.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"file:src/main/resources/chapter65/test-applicationContext652.xml"})
public class UserServiceTest652 {

    @Autowired
    ApplicationContext context;
    @Autowired
    private UserDao611 userDao;
    @Autowired
    private UserService612 userService;
    @Autowired
    private UserService612 testUserService;

    private List<User611> userList;

    @BeforeEach
    public void setUp() {
        this.userList = Arrays.asList(
                new User611("id652-1", "name652-1", "psw652-1", Level611.BASIC, UserService611.MIN_LOGCOUNT_FOR_SILVER - 1, 0),
                new User611("id652-2", "name652-2", "psw652-2", Level611.BASIC, UserService611.MIN_LOGCOUNT_FOR_SILVER, 0),
                new User611("id652-3", "name652-3", "psw652-3", Level611.SILVER, 60, UserService611.MIN_RECOMMEND_FOR_GOLD - 1),
                new User611("id652-4", "name652-4", "psw652-4", Level611.SILVER, 60, UserService611.MIN_RECOMMEND_FOR_GOLD),
                new User611("id652-5", "name652-5", "psw652-5", Level611.GOLD, 100, 100)
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

    static class TestUserServiceImpl extends UserServiceImpl612 {
        private String stopId = "id652-4";
        public void upgradeLevel(User611 user) {
            if(user.getId().equals(this.stopId)) throw new TestUserServiceException();
            super.upgradeLevel(user);
        }
    }

    static class TestUserServiceException extends RuntimeException {
    }

    @Test
    public void upgradeLevelsTest() throws Exception {
        //
        UserDao611 mockUserDao = Mockito.mock(UserDao611.class);
        Mockito.when(mockUserDao.getAll()).thenReturn(this.userList);
        UserServiceImpl612 userServiceImpl = new UserServiceImpl612();
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
}


//p.481 - chapter6.5.2
////
//이제 우리가 만들어 놓은 어드바이스와 포인트컷이 스프링 컨테이너에 빈으로 등록됐을 때 우리가 기대한 대로 잘 작동하는지 확인해 보자.
//BeanPostProcessor 인터페이스를 구현한 클래스가 빈으로 등록돼 있으면 자동으로 빈 후처리기가 적용된다고 했지.
//BeanPostProcessor 인터페이스를 구현한 클래스 중에 간단한 기능인 DefaultAdvisorProxyCreator 클래스를 빈으로 등록해 보자.
//이 클래스가 빈으로 등록돼 있으면 컨테이너에 빈으로 등록된 다른 모든 빈을 쭉 한 번씩 훑어보면서 Advisor 인터페이스를 구현한 클래스를 모두 긁어와.
//그렇게 해놓고 다른 모든 빈들이 생성될 때마다 미리 찾아놨던 Advisor 에 갖다 대보면서  advice 를 적용할지 말지 pointcut 에 물어봄.
//그러다가 프록시 적용 대상이라고 판단되면 원래 등록되어야 하는 빈 대신에 새로 만든 프록시 빈을 바꿔치기해서 등록함.
//자 그럼 일단 DefaultAdvisorProxyCreator 를 빈으로 등록해야겠지.
//test-applicationContext652.xml 파일을 보자.
//<bean class="org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator" />
////
//빈 후처리기 BeanPostProcessor 를 등록했으니까 이제 이 빈 후처리기가 탐색해서 가져갈 advisor 를 빈으로 등록할 차례야.
//우리가 원하는 advisor 기능은 트랜잭션 기능이지.
//transactin 기능을 덧붙여주는 advice 랑 "~~ServiceImpl" 클래스 안에 있는 "upgrade~~" 를 걸러주는 pointcut 이랑 둘 다 빈으로 등록.
//그리고 이 advice 와 pointcut 을 한 묶음으로 포장해주는 advisor 까지 빈으로 등록.
//그러면 이 advisor 를 이제 빈 후처리기 BeanPostProcessor 가 탐색해서 들고 있는다고.
////
//이렇게 하면 userService 로 등록하는 UserServiceImpl 클래스는 빈으로 등록될 때 자동으로  프록시가 만들어짐.
//프록시를 만들어서 트랜잭션 기능이 있는 advisor 가 덧붙여진 상태로 원래 빈 대신 프록시가 빈으로 등록됨.
//이렇게 되면 이제 다른 애플리케이션들이 스프링 컨테이너에서 userService 빈을 받아다가 쓸 때는 자기도 모르게 트랜잭션 기능이 붙어 있겠지.
////
//근데 문제는 TestUserServiceImpl.
//기존에는 어떻게 했어.
//ProxyFactoryBean 을 빈으로 등록해뒀다가 프록시가 아닌 ProxyFactoryBean 클래스 자체를 빈으로 받아왔어.
//그 빈에다가 getObject() 한 다음에 그 오브젝트에 setter 이용해서 수동으로 DI 해줬지.
//근데 이제는 그게 안 돼.
//빈 후처리기로 이미 프록시로 등록돼버렸어.
//getBean("&~~") 해서 받아올 수가 없어.
//그냥 직점 TestUserServiceImpl 을 빈으로 등록하고 Advisor 까지 적용되도록 만드는 수밖에 없을 듯?
//TestUserServiceImpl 는 UserServiceTest 클래스 안에 있는 스태틱 클래스야.
//이런 걸 등록하는 방법은 "$" 를 붙여서 "SuperClass$SubClass"이렇게 하면 됨.
//한 클래스 안에서만 쓸 거면 그냥 이렇게 내부 클래스로 만들어 두고 쓰는 게 좋음.
//<bean id="testUserService" parent="userService" class="basics.tobyspring6.chapter65.UserServiceTest652$TestUserServiceImpl"/>
//근데 보면 <bean> 태그 안에 parent 라는 속성도 있어.
//이건 뭐지?
////
//<bean> 을 등록할 때는 parent 라는 속성도 넣을 수 있어.
//이거는 자바 클래스 선언할 때 extends 랑 똑같아.
//다른 빈을 선언할 때 적용했던 설정들을 그대로 따라가겠다는 뜻이야.
//property 든 scope 든 그런 거 다 가져와.
//물론 override 할 수도 있어.
//그냥 겹치는 속성 이름으로 쓰면 됨.
//위에서는 testUserService 빈을 등록하면서 class 속성만 override 하고 나머지는 다 userService 빈을 따라갔지.
////
//이제 TestUserService 까지 @Autowired 로 받아오도록 Test 클래스를 수정하자.
//잘 작동되는지 확인하기.
//거기에 테스트할 사항 하나만 더 추가하자.
//빈 후처리기가 진짜로 작동하는지?
//빈으로 받아온 오브젝트의 클래스가 뭔지 확인해 보자.
//우리가 지금 빈으로 등록한 클래스는 UserServiceImpl 클래스야.
//그런데 빈 후처리기가 제대로 작동했다면 UserServiceImpl 클래스에 트랜잭션 advisor 를 덧붙이면서 프록시로 만들었을 거야.
//그러면 userService 빈의 클래스를 확인해 보면, UserServiceImpl 클래스가 아니라 Proxy 클래스가 나와야겠지.
//이렇게 결과물 클래스를 확인하는 게 advisorAutoProxyCreator() 테스트.
