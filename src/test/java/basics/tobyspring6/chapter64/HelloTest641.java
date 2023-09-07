package basics.tobyspring6.chapter64;

import basics.tobyspring6.chapter63.*;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

import java.lang.invoke.WrongMethodTypeException;
import java.lang.reflect.Proxy;

public class HelloTest641 {


    @Test
    public void helloTargetTest() {
        //
        Hello632 helloTarget = new HelloTarget632();
        //
        Assertions.assertEquals("Hello John", helloTarget.sayHello("John"));
        Assertions.assertEquals("Hi John", helloTarget.sayHi("John"));
        Assertions.assertEquals("Thank you John", helloTarget.sayThankYou("John"));
    }

    @Test
    public void simpleProxyTest() {
        //
        Hello632 helloTarget = new HelloTarget632();
        Hello632 proxiedHello = new HelloUppercase632(helloTarget);
        //
        Assertions.assertEquals("HELLO JOHN", proxiedHello.sayHello("John"));
        Assertions.assertEquals("HI JOHN", proxiedHello.sayHi("John"));
        Assertions.assertEquals("THANK YOU JOHN", proxiedHello.sayThankYou("John"));

    }

    @Test
    public void dynamicProxyTest() {
        Hello632 proxiedHello = (Hello632) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[] { Hello632.class },
                new UppercaseHanlder632(new HelloTarget632())
        );
        Assertions.assertEquals("HELLO JOHN", proxiedHello.sayHello("John"));
        Assertions.assertEquals("HI JOHN", proxiedHello.sayHi("John"));
        Assertions.assertEquals("THANK YOU JOHN", proxiedHello.sayThankYou("John"));
    }

    @Test
    public void dynamicProxyFail() {
        Hello632 proxiedHello = (Hello632) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[] { Hello632.class },
                new UppercaseHanlderEx632(new HelloTarget632())
        );
        Assertions.assertEquals("HELLO JOHN", proxiedHello.sayHello("John"));
        Assertions.assertEquals("HI JOHN", proxiedHello.sayHi("John"));
        Assertions.assertThrows(WrongMethodTypeException.class,
                ()->{proxiedHello.sayThankYou("John");}
        );
    }


    static class UpperCaseAdvice641 implements MethodInterceptor {
        public Object invoke(MethodInvocation invocation) throws Throwable {
            String ret = (String)invocation.proceed();
            return ret.toUpperCase();
        }
    }

    @Test
    public void proxyFactoryBeanTest() {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(new HelloTarget632());
        pfBean.addAdvice(new UpperCaseAdvice641());

        Hello632 proxiedHello = (Hello632) pfBean.getObject();
        //
        Assertions.assertEquals("HELLO JOHN", proxiedHello.sayHello("John"));
        Assertions.assertEquals("HI JOHN", proxiedHello.sayHi("John"));
        Assertions.assertEquals("THANK YOU JOHN", proxiedHello.sayThankYou("John"));
    }

    @Test
    public void pointcutAdvisor() {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(new HelloTarget632());
        //
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedName("sayH*");
        //
        pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UpperCaseAdvice641()));
        //
        Hello632 proxiedHello = (Hello632) pfBean.getObject();
        //
        Assertions.assertEquals("HELLO JOHN", proxiedHello.sayHello("John"));
        Assertions.assertEquals("HI JOHN", proxiedHello.sayHi("John"));
        Assertions.assertEquals("Thank you John", proxiedHello.sayThankYou("John"));
    }
}


//앞에서 FactoryBean 을 구현한 클래스를 빈으로 등록하면 스프링 컨테이너가 조금 다르게 다룬다고 했어.
//FactoryBean 을 구현한 클래스 자체를 빈으로 등록하지 않고, 그 클래스 안에 있는 getObject() 메소드의 결과값을 빈으로 등록한다고 했어.
//이런 FactoryBean 특징을 활용해서 우리는 프록시 오브젝트를 빈으로 등록했지.
//FactoryBean 인터페이스를 구현할 때 getObject() 메소드 안에 프록시를 만들어서 반환했어.
////
//FactoryBean 하나 등록하려면 반복되는 요소가 많아.
//FactoryBean 인터페이스 구현 -> getObject() 에서 프록시 생성 구현 -> 빈 오브젝트 등록
//근데 스프링에서는 이러한 과정 자체를 쉽게 구현할 수 있도록 기능을 제공해 줘.
//ProxyFactoryBean 이라고 함.
//스프링의 ProxyFactoryBean 는 프록시를 생성해서 빈 오브젝트로 등록해주는 팩토리 빈이야.
//JDK 에서는 다이나믹 프록시 기술을 제공해주지만 자바에는 프록시를 편리하게 만들어주는 다양한 기술들이 있어.
//스프링은 일관된 방법으로 프록시를 만들 수 있도록 추상 레이어를 제공해.
//그렇게 프록시를 만들어서 생성해주는 기능을 추상화한 게 바로 ProxyFactoryBean 이야.
//그걸로 Hello 기능 만든 게 위에 있는 테스트 코드.
////
//다이나믹 프록시 만들 때 InvocationHandler 안에다가 덧붙이고 싶은 부가기능을 구현했었지.
//그 InvocationHandler 역할을 하는 게 바로 MethodInterceptor 야.
//비슷한 역할을 하지만 아예 같지는 않아.
//둘의 차이점을 이해하다 보면 작동 방식까지 이해할 수 있을 거야.
////
////InvocationHandler 와 MethodInterceptor 사이의 차이
//구현 클래스 안에 target 오브젝트의 존재 여부.
//InvocationHandler 인터페이스를 구현할 때는 클래스 안에 target 을 들고 있었어.
//그래서 완전히 작동하는 프록시 오브젝트를 하나 만들려면 프록시 하나당 InvocationHanlder 도 하나씩 필요했지.
//그렇다고 InvocationHandler 까지 빈 오브젝트로 일일이 등록하기는 번거로우니까 프록시 안에서 직접 만들어서 바로 썼어.
//프록시가 먼저 target 주입받아서 그 안에서 handler 만들면서 setTarget() 해서 주입해줬지.
//그래서 예를 들어서 트랜잭션 기능을 추가해주는 InvocationHandler 를 하나 만들어서 재사용한다고는 해도 그 오브젝트를 프록시마다 하나씩 만들어서 쓰고 있기 때문에 여러 개가 생성될 거야.
//반면에 MethodInterceptor 는 어때.
//target 정보가 없어.
//MethodInterceptor 는 target 정보까지도 파라미터로 받아오거든.
//MethodInterceptor 안에 구현하는 invoke() 메소드 안에 보면 MethodInvocation 을 파라미터로 받고 있어.
//invocation.proceed(); 하면 알아서 타겟 오브젝트의 메소드를 내부적으로 실행해주는 기능이 있어.
//그니까 MethodInterceptor 는 일종의 템플릿처럼 쓰면서 MethodInvocation 를 콜백 오브젝트처럼 쓰고 있는 거지.
//이 덕분에 MethodInterceptor 구현 클래스는 target 정보로부터 자유로울 수 있게 됐어.
//MethodInterceptor 구현 클래스는 싱글톤으로 두고서 여러 프록시에서 재활용할 수 있게 되는 거지.
////
////ProxyFactoryBean 이랑 다이나믹 프록시 (Proxy.newInstance()) 차이.
//프록시 생성 메소드를 호출할 때 인터페이스 파라미터의 존재 여부.
//다이나믹 프록시로 프록시를 만들 때는 안에 인터페이스 클래스를 파라미터로 넣어줬어.
//프록시를 오브젝트로 생성해서 줄 때 어떤 인터페이스를 구현한 오브젝트로 만들지 알아야 할 거 아냐.
//근데 ProxyFactoryBean 만들 때는 인터페이스를 따로 안 넣어줘.
//위에 HelloTest 봐봐.
//그냥 기본 생성자로 생성해 놓고 setTarget() / addAdvice() 로 의존성만 주입해주고 있어.
//target 만 넣어주면 그 target 으로부터 인터페이스 싹 다 추출해서 그걸 다 구현해 줘.
//다는 아니고 특정 인터페이스만 구현했으면 좋겠다 싶으면 setInterfaces() 로 직접 넣어줘도 되긴 해.
//사실 ProxyFactoryBean 도 내부적으로는 다이나믹 프록시를 이용하는 기술이기는 해.
////
//// 어드바이스
//위에 ProxyFacotryBean 에서는 addAdvice() 라는 메소드가 있어.
//그 안에 MethodInterceptor 구현체를 파라미터로 넣어주고 있지.
//MethodInterceptor 는 사실 어드바이스 Advice 의 서브 인터페이스이기 때문이야.
//MethodInterceptor 인터페이스는 Advice 인터페이스를 상속받고 있어.
//왜냐하면 Advice 가 조금 더 큰 개념이야.
//프록시에다가 어떤 기능을 덧붙일 때 그 방식이 여러 방식이 있어.
//그중에서 MethodInterceptor 는 메소드 실행 시점에서 순서를 가로채는 방식으로 만든 거고.
//그 외에도 여러 방식으로 기능을 덧붙일 수가 있지.
//그걸 다 통틀어서 Advice 라고 부름.
//또 setAdvice() 가 아니라 addAdvice() 인 걸 보면 어드바이스를 여러 개 넣을 수 있겠다고 짐작할 수 있지.
//프록시 오브젝트 하나만 가지고도 여러 기능들을 덧붙일 수 있음.
////
//// 포인트컷
//InvocationHandler 와 MethodInterceptor 간의 차이가 하나 더 있어.
//InvocationHandler 를 구현했던 TransactionHandler 안에서는 pattern 을 들고 있었지.
//setPattern("upgrade") 이런 식으로 넣었어.
//그래서 메소드 이름이 "upgrade" 로 시작되는 메소드에만 우리가 원하는 기능이 적용되도록 했어.
//그러니까 어떤 메소드에만 부가 기능을 덧붙일지 필터링하는 기능이 있었어.
//InvocationHandler 에는 그런 기능이 있었는데, MethodInterceptor 에는 그런 역할을 해주는 게 없는가?
//생각해 보면 MethodInterceptor 에는 pattern 같은 값을 주입해서 들고 있으라고 할 수가 없어.
//왜냐하면 MethodInterceptor 는 싱글톤으로 두고서 여기 저기서 템플릿/콜백처럼 불러다 쓴다고 했잖아.
//싱글톤으로 쓰는 오브젝트에 pattern 이라는 필터링 값을 들고 있으라고 하면 상태값이 생기는 거지.
//싱글톤 오브젝트는 stateless 하게 만들어야 하니까 그런 방법을 쓸 수가 없어.
//이러한 문제를 또 스프링의 ProxyFactoryBean 에서는 어떻게 해결했느냐?
//그냥 (기능을 담당하는 프록시 / 필터링을 담당하는 프록시) 이렇게 나누어 버림.
//그리고 이렇게 (기능+필터링) 을 하나로 묶어서 한 단위로 만들어 버림.
//기능을 담당하는 프록시는 어드바이스 Advice 라고 함.
//메소드 선정을 담당하는 프록시는 포인트컷 Pointcut 이라고 함.
//기능 프록시와 필터링 프록시를 하나로 묶어서 어드바이저 Advisor 라고 함.
//포인트컷은 Pointcut 이라는 인터페이스를 구현해서 만들어.
//어드바이스는 메소드를 실행하기 전에 포인트컷한테 이 메소드에 부가기능 붙일지 말지 확인 받고 실행함.
//이런 구조로 만들어 놓고 어드바이스와 포인트컷은 DI 로 넣어서 활용함.
//이전에 만들었던 HelloTest641 에 있는 proxyFactoryBeanTest() 테스트를 pointcut 넣어서 바꾼 게 pointcutAdvisor()
// 그러면 이제 우리가 다이나믹 프록시 기술로 구현했던 트랜직션 기능을 ProxyBeanFactory 방식으로 바꿔보자.
// InvocationHandler 인터페이스를 구현했던 TransactionHandler 를 MethodInterceptor 인터페이스를 구현하도록 바꿔보자.
// 바꿔서 완성한 게 TransactionAdvice642.class
// 이렇게 구현한 클래스가 잘 작동하는지 테스트한 게 UserServiceTest642.class




