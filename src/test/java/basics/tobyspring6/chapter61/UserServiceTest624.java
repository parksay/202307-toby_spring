package basics.tobyspring6.chapter61;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"file:src/main/resources/chapter61/test-applicationContext612.xml"})
public class UserServiceTest624 {

    @Autowired
    private UserDao611 userDao;
    @Autowired
    private PlatformTransactionManager transactionManager;

    private List<User611> userList;

    @BeforeEach
    public void setUp() {
        this.userList = Arrays.asList(
                new User611("id622-1", "name622-1", "psw622-1", Level611.BASIC, UserService611.MIN_LOGCOUNT_FOR_SILVER - 1, 0),
                new User611("id622-2", "name622-2", "psw622-2", Level611.BASIC, UserService611.MIN_LOGCOUNT_FOR_SILVER, 0),
                new User611("id622-3", "name622-3", "psw622-3", Level611.SILVER, 60, UserService611.MIN_RECOMMEND_FOR_GOLD - 1),
                new User611("id622-4", "name622-4", "psw622-4", Level611.SILVER, 60, UserService611.MIN_RECOMMEND_FOR_GOLD),
                new User611("id622-5", "name622-5", "psw622-5", Level611.GOLD, 100, 100)
        );
    }

    @Test
    public void upgradeAllOrNothing() throws Exception {
        //
        TestUserService testUserService = new TestUserService(this.userList.get(3).getId());
        testUserService.setUserDao(this.userDao);
        UserServiceTx612 txUserService = new UserServiceTx612();
        txUserService.setTransactionManager(this.transactionManager);
        txUserService.setUserService(testUserService);
        //
        this.userDao.deleteAll();
        for(User611 user : this.userList) {
            txUserService.add(user);
        }
        try {
            txUserService.upgradeLevels();
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

    static class TestUserService extends UserServiceImpl612 {
        private String stopId;
        public TestUserService(String id) {
            this.stopId = id;
        }
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
        // 딱히 Mockito 오브젝트를 생성하거나 하지는 않아.
        // 대부분 Mockito 의 스태틱 메소드로 선언되어 있어.
        // Mockito.mock(MyInterface.class) 하면 MyInterface 라는 인터페이스를 가지고 목 오브젝트를 구현해서 반환해 줘.
        // 하지만 이 자체로는 텅 비어 있어서 쓸모가 없어
        UserDao611 mockUserDao = Mockito.mock(UserDao611.class);
        // 이제 위에서 만든 목 오브젝트에 스텁 기능을 달아주자.
        // mockUserDao.getAll() 메소드를 호출했을 때 userList 를 반환해주게끔 만들어야지.
        // Mockito.when(myMockObj.myMethod()).thenReturn(myParam);
        // Mockito 의 스태틱 메소드 when 을 호출해.
        // 그 안에 스텁 기능을 추가할 목 오브젝트의 메소드를 넣어줘.
        // 그리고 나와서 다시 thenReturn() 메소드로 연결하면서 그 안에 파라미터 넣어줘.
        // 그러면 목 오브젝트의 myMethod() 가 호출될 때 파라미터를 반환해줌.
        Mockito.when(mockUserDao.getAll()).thenReturn(this.userList);
        UserServiceImpl612 userServiceImpl = new UserServiceImpl612();
        userServiceImpl.setUserDao(mockUserDao);
        //
        try {
            userServiceImpl.upgradeLevels();
        } catch (Exception e) {
            throw e;
        }

        // 이제 이렇게 만든 목 오브젝트를 검증해봐야겠지.
        // Mockito.verify(myMockObj, Mockito.times(num)).myMethod(Mockito.any(MyType.class));
        // Mockito 에 verify 라는 스태틱 메소드가 있어.
        // 어떤 목 오브젝트의 어떤 메소드가 몇 번 불렸는지, 그때 파라미터를 어땠는지 확인해 보는 거야.
        // myMethod 가 2번 불렸는지, 그때 안에 파라미터가 들어온 데이터 타입은 MyType.class 이 맞는지?
        // 밑에는, mockUserDao 목 오브젝트의 update() 메소드를 2번 부른 적 있는지? 그리고 그 안의 파라미터는 User611 클래스가 맞는지?
        Mockito.verify(mockUserDao, Mockito.times(2)).update(Mockito.any(User611.class));
        // Mockito.verify(myMockObj).myMethod(myParam);
        // 똑같이 Mockito 의 verify 스테틱 메소드이긴 한데 파라미터 갯수가 다르지.
        // myMockObj 의 myMethod() 메소드가 myParam 을 파라미터로 호출된 적이 있는지 확인하기.
        Mockito.verify(mockUserDao).update(this.userList.get(1));
        Assertions.assertEquals(this.userList.get(1).getLevel(), Level611.SILVER);
        // mockUserDao 의 update 메소드를 this.userList.get(3) 파라미터로 부른 적이 있는지?
        Mockito.verify(mockUserDao).update(this.userList.get(3));
        Assertions.assertEquals(this.userList.get(3).getLevel(), Level611.GOLD);
    }

}

//p.425 - chapter 6.2.4
//테스트를 작성하는 건 프로그래밍할 때 굉장히 중요한 일이야.
//어쩌면 테스트를 짜는 것까지가 개발의 일부라고 할 정도야.
//실제로 요즘은 TDD 가 일반적인 개발 방법론으로 사용되는 중.
//테스트도 중요하게 여기는 문화가 이제는 좀 보편화되고 있지.
//스프링이 추구하는 것처럼 깔끔하고 유연한 코드를 짜다 보면 테스트도 그만큼 쉬워질 거야.
//테스트가 쉬우면 테스트도 적극적으로 만들게 되고.
//테스트를 많이 자주 하게 되면 리팩토링도 더 활발하게 하게 되고.
//리팩토링을 많이 할 수 있으면 다시 좋은 코드가 되고.
//좋은 코드는 또 테스트하기 좋고.
//반대로 복잡하고 얼기설기 뒤엉킨 코드를 짜다 보면...?
//테스트를 짜기도 복잡하고 어려울 거야.
//테스트 만드는 게 어려우니까 귀찮아지고 잘 안 만들게 돼.
//테스트를 안 하니까 리팩토링도 손을 안 대게 되고.
//리팩토링이 일어나지 않으니까 코드는 고여서 썩어가고.
//코드가 안 좋으니까 다시 테스트를 안 만들게 되고.
//악순환 반복.
//이렇게 좋은 코드와 테스트는 밀접한 관계야.
//좋은 코드는 테스트하기 좋은 코드고,
//반대로 테스트하기 좋은 코드가 좋은 코드야.
////
//이렇게 중요한 테스트!
//그중에서도 단위 테스틑 가장 먼저 고려하라고 했지.
//일단은 단위 테스트가 기본이야.
//단위 테스트라는 기준은 의존 오브젝트나 외부 리소스 없이 대상 오브젝트만 가지고 테스트하는 것.
//물론 DAO 같은 애들은 예외일 수 있지.
//DB 접근 자체가 기능인데 DB 없이 테스트할 수가 없으니까.
//어쨌든 단위 테스트가 기본인 만큼 자주 많이 해야 하는데 가장 큰 걸림돌이 뭐냐.
//바로 테스트 대역을 만드는 일이야.
//이전에도 MockUserDao 만들면서 해봤지.
//사용하지 않는 interface 메소드들 모두 다 구현해야 해.
//또 같은 인터페이스의 다른 검증 기능으로 여러 개 필요하다면...?
//테스트 클래스가 온통 목 오브젝트로 가득찰 거야...
//이런 불편함을 해결해 주는 목 프레임워크가 또 있지.
//바로 Mockito 라는 프레임워크야.
//사용법은 여기서는 테스트 코드에 각주 달아가면서 간단하게만 다룰게.
//어떻게 사용하는지는 나중에 더 공부해야 할듯!
//자주 쓰게 될 테니까.


