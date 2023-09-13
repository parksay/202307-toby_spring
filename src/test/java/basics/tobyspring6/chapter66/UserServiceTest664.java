package basics.tobyspring6.chapter66;

import basics.tobyspring6.chapter61.*;
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
public class UserServiceTest664 {

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



//// p.500 - chapter 6.5.4
//
///트랜잭션 서비스 추상화
///프록시와 데코레이터 패턴
///다이나믹 프록시와 프록시 팩토리 빈
///자동 프록시 생성과 포인트컷
///부가기능의 모듈화
///AOP - 애스펙트 지향 프로그래밍
//
//지금까지 트랜잭션 적용 코드를 개선한 과정을 정리해 보자.
//일단 트랜잭션 자체는 데이터베이스에서 완벽하게 지원해 준다.
//중간에 문제가 생겼다고 일부 로우만 적용되고 나머지는 그대로 두거나 하지 않는다.
//또는 일부 칼럼만 적용하고 나머지 칼럼은 적용 안 된 채로 두거나 하지 않는다.
//단, 그거는 SQL 하나에 대한 이야기다.
//애플리케이션을 만들다 보면 여러 SQL 을 한 트랜잭션으로 묶어야 할 때가 있다.
//예를 들어 송금과 출금, 결재 승인과 알림 메일 전송 등.
//그러면 데이터베이스의 AutoCommit 이라는 기능을 꺼야 한다.
//SQL 하나에 대해 트랜잭션이 적용되던 이유는 SQL 하나마다 저절로 커밋이 적용되고 있어서였다.
//AutoCommit 을 끄고 내가 원하는 만큼 SQL 을 날린 후에 commit 을 수동으로 해줘야 한다.
//로직상으로는 커넥션을 열고 한 트랜잭션으로 묶을 SQL 들을 날리고 commit 을 하고 커넥션을 닫는다.
//즉 한 트랜잭션은 한 커넥션이 생성되고 소멸되는 과정 안에서 이루어진다.
//따라서 커넥션은 서비스 계층에서 비지니스 로직 안에 들고 있어야 한다.
//한 비지니스 로직 안에서 커넥션을 열고 나면 어디부터 어디까지 한 트랜잭션으로 묶을지 결정하고 커넥션을 닫아야 하기 때문이다.
//문제는 SQL 을 날리려면 DAO 단에서도 커넥션을 들고 있어야 한다.
//당장 생각한 건 비지니스 로직에서 커넥션을 생성하고 DAO 로 넘겨주기?
//그러면 서비스 계층 코드가 특정 기술에 종속돼 버린다.
//이것을 분리해야 한다.
//방법은 추상화.
//하이버네이트이든, DB든, 메일 서비스든 비슷한 패턴이 반복되는 애들은 트랜잭션 매니저라는 녀석으로 관리하도록 묶는다.
//트랜잭션 매니저라는 제 3자에게 트랜잭션을 가지고 있으라고 함.
//만드는 애는 커넥션을 만들어서 걔한테 들고 있으라고 하고 쓰는 애들은 모두 그 친구한테 받아와서 쓰고 다시 제자리에 돌려주도록 한다.
//마치 원탁 가운데에 메인 디쉬를 두고 다같이 쓰고 제자리에 두는 느낌이다.
//그렇게 만드니 분리는 됐다.
//이제 문제는 반복.
//같은 클래스 안에 여러 메소드에 걸쳐서 트랜잭션 받아오고, 날리고, 닫고....
//이 반복을 해결하는 방법은 데코레이터 패턴.
//처음에는 템플릿/콜백 패턴도 생각했다.
//트랜잭션 열고 로직 수행하고 트랜잭션 닫고, 하는 틀을 템플릿으로 두고 그 틀 안에서 실행할 로직만 콜백으로 받는 거다.
//하지만 템플릿/콜백 패턴은 단순히 중복을 없애는 용도, 한 번 쓰고 버리면 그만인 경우에 쓰는 거 같다.
//반면 프록시를 만드는 데코레이터 패턴은 비지니스 로직단을 캡슐화할 수 있다는 점을 이용해서 모듈화하는 게 가능하다.
//우리가 쓰고 싶은 건 일회용보다는 모듈화 쪽에 조금 더 가까우니 데코레이터 쪽으로 간다.
//처음에는 그 자체로 구현 클래스였던 UserService 를 인터페이스로 만든다.
//구현체는 두 개로 분리한다.
//하나는 비지니스 로직만 담고, 다른 하나는 트랜잭션 관리 로직만 담는다.
//그리고 트랜잭션 관리 로직이 틀이 되어서 그 틀 안에서 실행한 비지니스 로직은 다른 구현체를 호출함으로써 기능을 위임해버린다.
//이렇게 중간에 껴서 자기 할 일 먼저 하고 다른 구현체한테 기능을 위임하는 애들을 프록시라고 해.
//실제 알맹이 기능은 다른 애가 들고 있고, 자기가 중간에 끼어들어서 다른 기능을 먼저 수행하고 넘겨주는 거야.
//클라이언트는 인터페이스로만 DI 받아서 UserService 를 사용하니까 내가 호출하는 게 트랜잭션 관리 구현체인지 비지니스 로직 구현체인지 모름.
//이게 데코레이터 패턴.
//클라이언트와 실제 구현체(타겟) 사이에 프록시들을 여러 개 끼워 넣어서 부가 기능들을 덧붙이는 패턴.
//가운데에 트랜잭션 매니저를 세워둠으로써 DAO 계층과 서비스 계층을 분리했고,
//서비스 계층 안에서는 인터페이스를 틀로 두고 구현체를 둘로 나눔으로써 비지니스 로직과 트랜잭션 관리 로직을 분리했다.
//분리시킨 거까지는 좋은데 귀찮음이 조금 생겼어.
//인터페이스에는 메소드가 1,2,3,4,5,6 있는데 트랜잭션 로직은 2,3만 있으면 돼.
//그런데도 트랜잭션 관리 구현체는 메소드를 1,2,3,4,5,6 모두 구현해야 하지.
//내용물은 없고 기능을 위임하는 코드만 넣어서.
//그래 한두 번이야 그렇다 쳐도 서비스 만들 때마다 이러면 골치아픔.
//그래서 다이나믹 프록시라는 걸 썼어.
//다이나믹 프록시는 우리가 직접 구현하는 게 아니라 런타임 시점에 동적으로 프록시를 제작해주는 애야.
//어떤 인터페이스를 구현체로 만들어주면서, 그 안에 있는 어떤 메소드를 실행하든 invokeHandler 라는 한 곳을 거치도록 만들어 줘.
//내가 추가하고 싶은 부가 기능은 다 invokeHandler에 넣어두고 실행하도록 만들고 대신 메소드 이름에 pattern 같은 걸 걸어서 부가 기능을 덧붙일지 말지 결정해주도록 함.
//우리가 일일이 구현체를 만들지 않아도 invokeHandler 하나만 만들어 두면 프록시를 알아서 채워서 만들어 줘.
//편하지.
//이제 invokeHandler 를 빈으로 등록해 두고 다른 프록시들이 참고해서 쓰기만 하면 되게끔 하면 됐어.
//이 과정에서 문제가 살짝 있었는데 런타임에서 만들어지는 프록시를 빈으로 등록하는 게 문제였어.
//왜냐하면 빈으로 등록할 때는 특정 클래스를 오브젝트로 만들어서 컨테이너에 담아두는데 우리가 만들고 싶은 프록시는 Proxy.newInstance() 를 실행해서 오브젝트를 받아와야 하거든.
//이거는 비교적 간단하게 해결했지.
//빈에 등록할 때 BeanFactory 인터페이스를 구현한 클래스는 그 클래스를 오브젝트로 만들어서 빈으로 등록하는 게 아니라 그 클래스 안에 있는 getObject() 라는 메소드를 실행해서 그 결과를 오브젝트로 등록했어.
//그래서 프록시 만드는 클래스를 하나 만들고 BeanFactory 인터페이스를 구현해서 getObject() 안에 Proxy.newInstace() 로 프록시 오브젝트 초기화하는 코드를 넣어주면 해결.
//서비스 계층과 DAO 계층을 분리하고, 서비스 계층에서도 트랜잭션 로직과 비지니스 로직을 분리하고, 그 사이에 생긴 프록시 구현 문제도 다이나믹 프록시로 해결했어.
//근데 이제는 반복 문제가 생겼음.
//서비스 클래스가 여러 개면 트랜잭션 관리가 필요한 클래스 모두를 BeanFactory 로 등록해서 프록시로 만들어야 함.
//그러려면 트랜잭션 관리하는 InvocationHandler 는 하나만 등록하더라도 그 안에 target 을 다르게 들고 있어야 해서 프록시마다 객체를 하나씩 들고 있게 됨.
//target 클래스 빈으로 등록하고, 그 target 가지고 Proxy 만들어주는 BeanFactory 도 빈으로 등록하고, 이걸 서비스 클래스 하나마다 반복....
//이 반복을 해결해준 게 스프링의 ProxyFactoryBean 이야.
//ProxyFactoryBean 은 아래 과정을 한 번에 처리해 줌.
//FactoryBean 인터페이스 구현 -> getObject() 에서 프록시 생성 구현 -> 빈 오브젝트 등록
//자바에는 다이나믹 프록시 말고도 프록시를 만들어주는 여러 기술이 있는 스프링이 이걸 추상화 레이어로 만들어서 제공해 줌.
//ProxyFactoryBean 은 InvocationHandler 대신에 MethodInterceptor 를 사용해.
//MethodInterceptor 는 target 을 들고 있지 않아.
//파라미터로 받아.
//그래서 MethodInterceptor 를 빈으로 등록해 두고 싱글톤으로 다같이 쓰면서 재활용할 수가 있었지.
//또 인터페이스도 파라미터로 받은 target 한테서 추출해냄으로써 일일이 설정해주지 않아도 돼.
//InvocationHandler 에 있던 메소드 필터링 기능은 Pointcut 이라는 애가 대신함.
//MethodInterceptor 인터페이스는 Advice 인터페이스를 내려받은 서브 인터페이스라서 이제 어드바이스라고 부름.
//Advice 하나만 빈으로 등록해 두고 트랜잭션이 필요한 애들은 ProxyFactoryBean 를 빈으로 등록하면서 타겟만 바꿔주면 됨.
//진짜 다 좋은 거 같은데 여러 서비스 클래스가 똑같은 Advice 를 똑같은 Pointcut 으로 프록시를 만드는 반복이 너무 싫음.
//이 반복을 해결한 건 스프링의 PostBeanProcessor 빈 후처리기.
//스프링 설정 파일에 등록된 모든 빈이 한 처리기를 거쳐서 등록되도록 만드는 기능.
//이런 문제 해결 방법은 전에도 한 번 있었어.
//프록시 만들 때야.
//처음에 프록시 만들 때 인터페이스에 있는 메소드들을 일일이 구현해야만 했어.
//내용이 텅텅 비어 있고 다른 구현체한테 위임하는 코드만 남기면 되는데도.
//그래서 만든 방법이 다이나믹 프록시를 이용하는 방법이었어.
//어떤 인터페이스를 일단 임시로 구현체로 만들어두고 그 안에서 실행하는 모든 메소드는 InvocationHandler 를 거치도록 만들었지.
//이거랑 같은 패턴이야.
//모든 빈이 똑같은 처리기를 지나도록 만든 것.
//그 처리기를 지나면서 Pointcut을 만족하는 애들은 프록시로 만들어서 빈으로 등록하고 Pointcut 만족 못하는 애는 그대로 해당 클래스를 오브젝트로 만들어서 빈으로 등록되게끔.
//PostBeanProcessor 를 사용하는 법은 BeanFactory 나 ProxyBeanFactory 처럼 그냥 스프링 설정 파일에 그 인터페이스를 구현한 클래스를 등록해두면 됨.
//이렇게까지 해서 트랜잭션 관리하는 코드를 서비스 계층으로부터도 분리했어.
//분리하고 트랜잭션 관련 Advice 한 곳으로 다 몰아 넣었어.
//그리고 빈 전체를 탐색하면서 적용할 애들은 일괄적으로 적용할 수 있었어.
//이렇게 애플리케이션 코드 전체에 걸쳐서 반복되고 흩어져 있던 트랜잭션 관리 코드를 한 곳으로 몰아 넣었어.
//이거는 기존의 OOP - Object Oriented Programming 객체지향프로그래밍 설계만으로는 해결할 수 없는 문제야.
//이런 프로그래밍 방식을 AOP - Aspect Oriented Programming 이라고 해.
//AOP 는 OOP 를 바탕에 두고 OOP 를 더 OOP 답게 만들어주는 보조 기술이야.
//서비스 계층은 더욱 비지니스 로직에만 집중할 수 있게 하고, 데이터 계층은 더욱 데이터베이스 로직에만 집중할 수 있게 해줘.
//클래스마다 로그를 남기는 기능, 트랜잭션 관리 기능, 권한 처리 기능 등등 반복돼서 등장하는 애들을 싹 긁어 모아서 분리시켜 주거든.
//그러면 클래스에는 본래 있어야 하는 핵심 기능만 남기고 구현할 수 있지.
//더욱 객체지향스러워짐.
//
//// p.506 - chapter 6.5.5
//사실 AOP 방식에는 여러 방식이 있음.
//우리가 만든 건 프록시를 사용하는 방식.
//그 외의 기술 중에 가장 강력하고 유명한 기술은 AspectJ 기술.
//우리가 트랜잭션 기술을 AOP 로 뽑아내기까지 많은 기술을 응용했지.
//IoC/DI 컨테이너 / 다이나믹 프록시 / 데코레이터 패턴 / 프록시 패턴 / 빈 오브젝트 후처리 기법 등등...
//객체지향 설계 기술과 스프링이 지원해주는 기술 여러 가지를 응용했지.
//그중에서 가장 핵심은 프록시야.
//부가기능을 담은 Advice 어드바이스를 만들어 두고 핵심 기능에다가 덧붙이는 방식이지.
//그럼 AspectJ 는 어떤가...?
//AspectJ 는 타깃 오브젝트 자체를 뜯어가지고 부가기능을 직접 꽂아 넣어주는 방식이야.
//그렇다고 직접 소스코드를 찾아서 수정하지는 못하겠지.
//그래서 JVM 에 로딩되는 시점을 중간에 가로채서 바이트코드를 조작하거나 컴파일된 타겟 클라스의 파일 자체를 수정하거나 함.
//꽤 어렵고 복잡한 기술이야.
//그럼 AspectJ 를 뭐하러 씀...?
//첫째, 스프링이 굳이 필요 없음.
//프록시를 이용한다면 DI 가 필수적으로 필요하게 되겠지.
//그런데 바이트코드를 조작하는 방식을 사용하면 스프링 컨테이너가 없는 환경에서도 AOP 를 적용할 수 있음.
//둘째, 프록시를 사용할 때보다 훨씬 세밀하고 유연한 AOP 가 가능해짐.
//예를 들어서 스태틱 클래스가 메모리에 올리갈 때...?
//또는 어떤 클래스의 필드 값에 접근하거나 조작할 때, 오브젝트를 생성할 때 등등...
//훨씬 더 다양한 시점에 자유자재로 조작할 수가 있음.
//반면에 프록시를 사용하는 스프링의 AOP 방식은 메소드를 호출하는 시점만이 조인포인트가 되겠지.
//하지만 보통의 경우에는 프록시를 사용하는 스프링의 AOP 만으로 충분해.
//따라서 우선은 스프링의 AOP 를 고려하고 그것 이상으로 필요하다면 그때 가서 AspectJ 사용하기.
//AspectJ 는 스프링도 차용해서 가져다 쓸 만큼 충분히 성숙하고 수준 높은 기술임.
//물론 스프링의 프록시 AOP 와 AspectJ 의 AOP 를 동시에 사용할 수도 있음.
//
//
//// p.510 - AOP 네임스페이스
//스프링 컨테이너에 AOP 관련 빈들이 가득해지겠지.
//Advice 빈으로 등록하고 Pointcut 등록하고 PostBeanProcessor 등록하고...
//똑같은 패턴이 계속해서 반복되는 코드야.
//그래서 이걸 차라리 한 번에 등록할 수 있도록 설정 파일에 aop 전용 네임스페이스가 제공돼.
//<aop:config>
//	<aop:pointcut id="transacitonPointcut" expression="execution(* *..*ServiceImpl.upgrade*(..))" />
//	<aop:advisor advice-ref="transactionAdvice" pointcut-ref="transactionPointcut" />
//</aop:config>
//
//<aop:config> 로 열고 pointcut 등록해주 pointcut 과 advice 조합한 advisor 등록해주고.
//근데 그냥 포인트컷을 속성값에 바로 넣어주는 문법도 가능.
//
//<aop:config>
//	<aop:advisor advice-ref="transactionAdvice" pointcut="execution(* *..*ServiceImpl.upgrade*(..))" />
//</aop:config>
//
//물론 pointcut 재활용하고 싶다고 하면 다른 데서 가져다 쓰면 됨.
//
//
//// p.512 - chapter6.6 트랜잭션 속성
//PlatformTransactionManager 만들 때 생각해 보자.
//this.transactionManager.getTransaction(new DefaultTransactionDefinition());
//여기서 파라미터로 넣어주는 new DefaultTransactionDefinition() 이게 뭐지?
//트랜잭션을 정의하는 속성값이 사실 몇 개 있어.
//> 트랜잭션 전파
//> 격리 수준
//> 제한 시간
//> 읽기 전용
/// 트랜잭션 전파
//트랜잭션을 관리해야 하는 메소드 여러 개가 계층을 가지고 작동할 떄 트랜잭션 구조는 어떻게 해야 할지?
//이걸 결정하는 게 트랜잭션 전파야.
//예를 들어서  A 메소드 안에서 B 메소드를 호출했어.
//근데 A 메소드도 트랜잭션이 있고 B 메소드도 나름 트랜잭션이 있어.
//A 메소드를 호출하면서 트랜잭션을 열었고, 그 상황에서 B 메소드를 실행했는데 B 메소드도 자체적으로 트랜잭션을 가지고 있어.
//이때 B 가 실패하면 어떻게 될 것인가?
//A 메소드가 이미 열어 놓은 트랜잭션에 B 메소드가 참여한다면, B 메소드가 실패했을 때 A 메소드도 따라서 rollback 됨.
//A 메소드가 이미 트랜잭션을 열어놓았지만 그거랑 별개로 B 메소드도 자체적으로 트랜잭션을 가지고 작동하면 B 가 실패해도 지금까지 성공했던 A 작업의 내용은 그대로 commit 되고 B 작업 내용만 rollback 됨.
//세 가지 방식이 있어.
//> PROPAGATION_REQUIRED
//DefaultTransactionDefinition 에서 사용하는 기본 설정값이며 가장 많이 사용되는 속성이다.
//이미 진행 중인 트랜잭션이 있다면 그 트랜잭션에 참여하고
//아직 진행 중인 트랜잭션이 없다면 새로운 트랜잭션을 만든다.
//> PROPAGATION_REQUIRES_NEW
//항상 새로운 트랜잭션을 연다.
//이미 앞에서 트랜잭션을 열었든 닫았든 그런 거 신경 안 쓰고 내가 트랜잭션을 여는 코드가 있으면 항상 독립적인 새로운 트랜잭션을 연다.
//> PROPAGATION_NOT_SUPPORTED
//이건 좀 특이함.
//트랜잭션을 항상 열지 않는다.
//그니까, 트랜잭션이 없음.
//아니, 트랜잭션이 없으면 애초에 트랜잭션 관리자를 가져오지를 말든가, 트랜잭션 관리자 호출해놓고 막상 트랜잭션은 안 쓴다고 하는 건 뭐임?
//이게 또 필요한 게 있음...
//전체적으로, 기본값으로 트랜잭션이 작동되도록 해놓고 특정 몇 개 부분에서만 트랜잭션을 없애고 싶어.
//이렇게 했을 때 포인트컷이나 구현이 훨씬 간단해지는 경우가 있지.
//그럴 때 씀.
/// 격리 수준.
//트랜잭션이 100개 있다고 이거를 한 줄로 세워서 100개를 순서대로 실행한다?
//사실 그게 가장 안정적이고 좋긴 하겠지만 성능이 좀 떨어지겠지.
//너무 느릴 거야.
//따라서 가능한 한 많은 트랜잭션을 동시에 진행시키면서도 서로 충돌나거나 간섭하지 않도록 만들기.
//이게 격리 수준 설정이야.
//DefaultTransactionDefinition 에서 사용하는 기본 설정값은 ISOLATION_DEFAULT
/// 제한 시간
//그냥 뭐.. 말 그대로 제한 시간.
//트랜잭션 하나 날려놓고 하루 종일 기다릴 수는 없으니까.
//어느 정도 기다리다가 답 없으면 그냥 안 되나 보다 하고 취소하기.
//DefaultTransactionDefinition 에서 사용하는 기본 설정값은 제한 시간이 없음
/// 읽기 전용
//get 머시니가 find 머시기나 이런 애들은 읽기 전용으로 설정해두면 좋음.
//실수로 데이터를 조작하거나 하는 코드를 넣었을 때 예외 발생시키면서 막아줌.
//또 읽기 전용으로 트랜잭션을 열면 성능이 살짝 올라가기도 함.
//
//// p.516 chapter - 6.6.2.
//우리가 직접 만들었던 TransactionAdvice 는 그냥 학습용으로 만든 거야.
//위에서 트랜잭션 속성들을 설정할 수 있는 트랜잭션 어드바이스를 스프링이 미리 만들어 뒀음.
//이름은 TransactionInterceptor
//TransactionInterceptor 는 두 가지 속성을 넣어줘야 해.
//하나는 PlatformTransactionManager 이고 다른 하나는 Properties 야.
//PlatformTransactionManager 는 이미 알고 있을 거고.
//Properties 쪽이 바로 방금 말한 트랜잭션 속성 정의 부분.
//그냥 문자열로 넣어주면 됨.
//PROPAGATION_NAME, ISOLATION_NAME, readOnly, timeout_NNNN, -Exception1, +Exception2
//여기서 전파 속성 PROPAGATION_NAME 만 필수 항목이고 나머지는 다 생략 가능.
//순서는 상관 없음.
//+ 나 - 예외는 기본 원칙을 따르지 않는 예외들.
//모든 런타임 예외를 rollback 하도록 만들어 놓고서 +XXXRuntimeException 해놓으면 런타임 예외라도 commit 하게 만듦.
//반대로 모든 체크 예외는 모두 commit 하도록 만들어 놓고서는 - 를 붙여주면 그 트랜잭션은 rollback 되게 만듦.
//예를 들어서 작성해보자면 이렇게 됨.
//<bean id="transactionAdvice" class="org.springframework.transcation.interceptor.TransactionInterceptor">
//	<property name="transactionManager" ref="transactionManager" />
//	<property name="transactionAttributes">
//		<props>
//			<prop key="get*">PROPAGATION_REQUIRED, readOnly, timeout_30</prop>
//			<prop key="upgrade*">PROPAGATION_REQUIRES_NEW, ISOLATION_SERIALIZABLE</prop>
//			<prop key="*">PROPAGATION_REQUIED</prop>
//		</props>
//	</property>
//</bean>
//이렇게 해 두면 get 으로 시작하는 메소드는 트랜잭션 전파 PROPAGATION_REQUIRED 에 시간제한 30초에 읽기전용.
//upgrade 로 시작하는 메소드는 PROPAGATION_REQUIRES_NEW 에 격리 수준은 최고수준인 ISOLATION_SERIALIZABLE 로 둠.
//그리고 그 외의 나머지 모든 메소드는 PROPAGATION_REQUIED 를 따르게 함.
//한 메소드가 여러 패턴에 적용되는 경우에는 가장 정확하고 세밀하게 일치하는 쪽을 따름.
//근데 만약에 읽기전용이 아닌 트랜잭션에서 읽기전용 메소드인 get~~ 을 호출하면 어떻게 될까?
//get 메소드는 전파 속성이 PROPAGATION_REQUIED 이기 때문에 이미 열려 있는 트랜잭션에 참여함.
//근데 이렇게 빈 설정하는 방법은 단순히 문자열로 넣어주는 거기 때문에 오타가 있을 수도 있고 계층도 많아서 복잡해 보임.
//조금 더 간단하게 정의할 수 있도록 스프링에서는 tx 스키마를 제공함.
//그리고 웬만하면 트랜잭션 속성 정의는 이 tx 스키마를 이용하도록 권장함.
//<tx:advice id="transactionAdvice" transaction-manager="transactionManager">
//	<tx:attributes>
//		<tx:method name="get*" propagation="REQUIRED" read-only="true" timeout="30" />
//		<tx:method name="upgrad*" propagation="REQUIRES_NEW" isolation="SERIALIZABLE" />
//		<tx:method name="*" propagation="REQUIED" />
//	</tx:attributes?
//</tx:advice>
//
//// p.521 - chapter.6.6.3
//포인트컷 표현식이나 트랜잭션 속성을 정의할 때 따르면 좋은 전략 몇 가지.
//> 트랜잭션 포인트컷 표현식은 클래스나 빈 이름을 패턴으로 이용한다.
//일반적으로 트랜잭션을 적용할 어떤 타겟 클래스가 있으면 그 안에 있는 메소드는 모두 트랜잭션이 적용되는 후보가 돼는 것이 바람직하다.
//반대로, 트랜잭션 포인트컷을 작성할 때는 메소드 이름이나 파라미터, 에외 등에는 패턴을 적용하지 않는 게 좋다.
//단순한 add() 이런 것도 그 자체로만 보면 딱히 트랜잭션이 필요없어 보이기도 하지만 이런 기능은 다른 비지니스 로직 안에서 호출되어 불려다닐 가능성이 높으므로 트랜잭션 속성을 걸어 주는 게 좋다.
//쓰기 작업 없는 단순한 조회 기능이라도 트랜잭션 설정을 해 두면 읽기 전용 메소드에서 실수로 데이터를 조작하는 일을 방지할 수 있고 성능도 조금 올라가니 트랜잭션 속성을 걸어 두는 편이 좋다.
//트랜잭션 대상이 되는 클래스를 찾았다면 그 클래스가 들어 있는 패키지 전체를 통째로 선택거나 이름 패턴으로 정하는 게 바람직하다.
//예를 들어 서비스 게층 클래스면 비슷한 기능과 계층의 클래스들이 한 패키지 안에 모여 있도록 구성하는 게 좋을 테니 그 패키지 전체를 선정하게 되는 게 바람직하다.
//또 비지니스 로직을 담당하는 클래스 이름은 관계상 ServiceImpl 로 끝나도록 하는 경우가 많으므로 아래처럼 포인트컷을 작성하면 된다.
//execution(* *..*ServiceImpl.*(..))
//메소드의 시그니처를 이용하는 execution 대신에 간단하게 스프링에서 사용하는 빈 이름을 가지고 패턴을 결정하는 bean() 을 사용해도 좋다.
//> 클래스나 메소드 이름에 공통된 규칙을 정하는 게 좋다
//한 애플리케이션에서 사용하는 트랜잭션 속성 조합은 그리 많지 않다.
//따라서 어떤 패턴을 두고 트랜잭션 속성 설정을 최소화하는 게 좋다.
//이런 트랜잭션 속성을 사용할 애들은 이런 패턴 이름으로,
//저런 트랜잭션으로 사용할 애들은 저런 패턴으로.
//이름을 누구는 get~~ / 누구는 find~~ / 누구는 select ~~ 이렇게 중구난방으로 해놓으면 트랜잭션을 설정할 때 일일이 찾아서 패턴을 작성해줘야 한다.
//예를 들어서 get 이나 find 로 시작하는 메소드는 조회 전용으로 만들고
//> 프록시 방식 AOP 에서는 한 타겟 오브젝트 안에서 서로 다른 메소드끼리 호출할 때는 어드바이스 부가 기능이 적용되지 않는다.
//이거는 주의사항.
//프록시를 사용하는 스프링의 AOP 방식은 클라이언트가 타겟을 부를 때 그 사이에 프록시를 끼워넣어서 부가 기능을 실행해버리는 것.
//근데 한 타겟 안에서 자기가 자기 자신의 메소드를 부를 때는 당연히 프록시가 끼어들 틈이 없음.
//그래서 어드바이스 패턴이 맞든 어떻든 어드바이스 기능들이 적용이 안 됨.
//꼭 만들어야 하는 경우에는 프록시를 사용하도록 강제할 수 있기는 한데 소스코드도 더러워지고 복잡함.
//이런 경우에는 AspectJ 가 제공하는 AOP 기술을 사용하는 방법이 나음.