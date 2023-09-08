package basics.tobyspring6.chapter65;

import basics.tobyspring6.chapter63.*;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

import java.lang.invoke.WrongMethodTypeException;
import java.lang.reflect.Proxy;

public class HelloTest651 {


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

    @Test
    public void classNamePointcutAdvisor() {
        // 포인트컷 준비
        NameMatchMethodPointcut classMethodPointcut = new NameMatchMethodPointcut() {
            @Override
            public ClassFilter getClassFilter() {
                return new ClassFilter() {
                    @Override
                    public boolean matches(Class<?> clazz) {
                        return clazz.getSimpleName().startsWith("HelloT");
                    }
                };
            }
        };
        classMethodPointcut.setMappedName("sayH*");
        // 테스트 데이터
        class HelloWorld651 extends HelloTarget632 {};
        class HelloTom651 extends HelloTarget632{};
        // 테스트
        checkAdviced(new HelloTarget632(), classMethodPointcut, true);
        checkAdviced(new HelloWorld651(), classMethodPointcut, false);
        checkAdviced(new HelloTom651(), classMethodPointcut, true);
    }

    private void checkAdviced(Object target, Pointcut pointcut, boolean isAdviced) {
        //
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(target);
        pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UpperCaseAdvice641()));
        Hello632 proxiedHello = (Hello632) pfBean.getObject();
        //
        if(isAdviced) {
            Assertions.assertEquals("HELLO JOHN", proxiedHello.sayHello("John"));
            Assertions.assertEquals("HI JOHN", proxiedHello.sayHi("John"));
            Assertions.assertEquals("Thank you John",proxiedHello.sayThankYou("John"));
        } else {
            Assertions.assertEquals("Hello John", proxiedHello.sayHello("John"));
            Assertions.assertEquals("Hi John", proxiedHello.sayHi("John"));
            Assertions.assertEquals("Thank you John", proxiedHello.sayThankYou("John"));
        }
    }
}


//p.475 - chapter6.5.1.
//
//이제 ProxyBeanFactory 방식을 도입하면서 한 가지 문제는 해결 됐어.
//프록시 오브젝트 하나마다 InvocationHandler 를 하나씩 가지고 있던 문제.
//이제는 프록시들이 MethodInterceptor 같은 Advice 를 싱글톤으로 두고 다같이 공용으로 쓰고 있어.
//같은 기능이라면 Advice 를 프록시마다 하나씩 생성하지 않고 하나만 생성해두고 다같이 쓰게 됐어.
//하지만 다른 문제 한 가지가 더 남았지.
//바로 중복 문제.
//같은 부가 기능을 덧붙여야 하는 프록시들이 Advice 는 하나를 쓰지만, 어쨌든 선언은 각각 따로 해줘야 해.
//ProxyBeanFactory 를 class 로 두고 property 만 바꿔가면서 여러 빈을 등록해야 하지.
//_________________________________________________________
//<bean id="myHello" class="ProxyBeanFactory">
//	<property name="target" ref="helloImpl" />
//	<property name="interceptorNames">
//		<list>
//			<value>myAdvisor</value>
//				....
//		</list>
//	</property>
//</bean>
//``````````````````````````````````````````````````````````````````````````````````````````````````````
//이게 프록시 하나마다 반복된다고 생각해 봐...;
//개많아. 개길어짐.
//자, 이제 이 문제를 어떻게 해결할 것이냐?
//지금까지 어떤 반복이 일어날 때 해결했던 방법을 떠올려 보자.
//메소드 추출/템플릿 메소드 패턴/전략 패턴/템플릿콜백 패턴/....
////
//그중에 데코레이터 패턴 쓸 때 일어났던 중복을 생각해 봐.
//한 클래스 안에서 이 메소드, 저 메소드 모두 트랜잭션 기능을 덧붙여야 했어.
//그래서 어떻게 했어?
//모든 메소드들이 한 처리기를 거쳐가도록 만들었어.
//그 클래스 안의 모든 메소드들이 InvocationHandler 를 지나가도록 만들었지.
//그렇게 해 놓고 InvocationHandler 안에 중복돼서 일어나는 처리들을 다 넣었고.
//부가 기능이 필요 없는 메소드들은 걸러지도록 필터링 기능도 넣었었지.
////
//이런 방법을 이번에도 적용해 보자고.
//이 빈에다가도, 저 빈에다가도 똑같은 어드바이스를 덧붙여야 해.
//그러면 모든 빈이 다 한 처리기를 거쳐서 지나가도록 만들면 되지 않을까?
//스프링 컨테이너에서 만들어지는 모든 빈들이 생성되고 나면 어떤 한 처리기를 지나도록 해.
//그 처리기 안에서 부가 기능을 덧붙이는, 우리가 원하는 어드바이저가 추가되는 프록시를 만들어.
//그렇게 만든 프록시를 실제 빈이랑 바꿔치기 해서 프록시 쪽이 빈으로 등록되도록 만들어.
//그리고 중간에 이 빈은 프록시 만들고, 저 빈은 프록시 안 만들고, 구분할 수 있게 하는 필터링 기능도 필요할 거고.
////
//그런 기능을 '빈 후처리기'라고 해.
//빈 후처리기를 만드는 법은 바로 BeanPostProcessor 인터페이스를 구현하는 거야.
//스프링 컨테이너에 BeanPostProcessor 가 구현되어서 빈으로 등록돼 있으면 이러한 기능을 제공해 줘.
//원래는 스프링 빈으로 등록돼 있으면 그 class 를 오브젝트로 만들어서 빈으로 등록해준다고 했지.
//그 중에서 ProxyBeanFactory 인터페이스를 구현한 클래스는 특별하게 취급해서 그 클래스 안의 getObject() 메소드를 실행한 결과를 빈으로 등록해준다고 했어.
//마찬가지로 BeanPostProcessor 인터페이스를 구현한 클래스도 그래.
//이 인터페이스를 구현한 클래스를 빈으로 등록하면 다른 모든 빈들이 생성된 다음에 이 처리기를 거쳐서 나가도록 관리해 줘.
////
//빈 후처리기에 필터링 기능은 어떻게 넣느냐?
//모든 빈들이 빈 후처리기를 지나간다고 해서, 모든 빈을 프록시 오브젝트로 만들 거는 아니잖아?
//그래서 포인트컷을 같이 등록해줘.
//포인트컷은 앞에서 배운 개념이지.
//어드바이스를 어떤 메소드에 적용할지, 메소드 필터링 기능이었어.
//하지만 사실은 포인트컷은 메소드 선정 기능뿐만 아니라 클래스 선정 기능도 있어.
//우리가 지금까지 쓴 포인트컷은 그중에서 클래스 선정을 모두 통과시키고 메소드 선정만 쓴 거고.
//결론적으로 포인트컷은 '클래스 필터 기준 + 메소드 필터 기준' 을 모두 통과해야만 기능을 적용해 줘.
//실제로 Pointcut 소스 코드 까서 보면 아래와 같음.
//public interface Pointcut {
//	ClassFilter getClassFilter();
//	MethodMatcher getMethodMatcher();
//	Pointcut TRUE = TruePointcut.INSTANCE;
//}
//자 그럼 실제로 여러 이름으로 클래스를 만들고 포인트컷이 진짜 클래스까지 걸러주는지 보자고.
//그 테스트가 HelloTest651 클래스의 classNamePointcutAdvisor() 테스트.
//포인트컷이 잘 작동하는지 확인했으니 이제 실제로 빈 후처리기 BeanPostProcessor 를 적용해 보자.
//UserServiceTest652.class 로 가기.


