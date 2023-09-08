package basics.tobyspring6.chapter65;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

public class PointcutExpressionTest653 {

    @Test
    public void methodSignaturePointcut() throws SecurityException, NoSuchMethodException {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("" +
                "execution(" +
                "public int basics.tobyspring6.chapter65.MyTarget653.minus(int, int) " +
                "throws java.lang.RuntimeException)");

        // MyTarget653.minus()
        Assertions.assertEquals(true,
                pointcut.getClassFilter().matches(MyTarget653.class)
                    && pointcut.getMethodMatcher().matches(MyTarget653.class.getMethod("minus", int.class, int.class), null)
                );

        // MyTarget653.plus()
        Assertions.assertEquals(false,
                pointcut.getClassFilter().matches(MyTarget653.class)
                && pointcut.getMethodMatcher().matches(MyTarget653.class.getMethod("plus", int.class, int.class), null)
                );

        // MyBean653.myMethod()
        Assertions.assertEquals(false,
                pointcut.getClassFilter().matches(MyBean653.class)
                && pointcut.getMethodMatcher().matches(MyTarget653.class.getMethod("myMethod"), null));
    }


}


// p.489 - chapter 6.5.3
//포인트컷을 조금 더 정교하게 만들어 보자.
//이전에 우리가 만들었던 포인트컷은 기껏해야 이름으로 비교하는 기능이었어.
//클래스 이름이 어떤 글자로 시작하는지, 어떤 글자로 끝나는지
//메소드 이름이 어떤 글자로 시작하는지, 어떤 글자로 끝나는지.
//그런데 리플렉션 API 를 이용하면 훨씬 많은 정보를 가져올 수 있지.
//클래스로부터 메타데이터를 가져오잖아.
//클래스나 메소드 이름, 상속한 클래스, 구현한 인터페이스, 패키지 주소, 파라미터 종류, 파라미터 갯수, 리턴 타입...
//물론 우리가 직접 구현할 수도 있겠지만 역시 짱짱맨 스프링이 다 미리 만들어 줬음.
////
//RegEx 정규식처럼 포인트컷을 작성할 때도 일종의 표현식이 있어.
//이러한 표현식을 '포인트컷 표현식 pointcut expression' 이라고 불러.
//포인트컷 표현식을 지원하는 포인트컷을 만들려면 AspectJExpressPointcut 클래스를 사용하면 돼.
//스프링에서 사용하는 포인트컷 표현식이 사실은 AspectJ 라는 유명한 프레임워크로부터 끌어다가 일부 문법을 확장해서 쓰고 있거든.
////
//AspectJ 포인트컷 표현식은 포인트컷 지시자를 사용해서 작성해.
//지시자도 여러 가지가 있긴 한데 그중에 가장 많이 사용하는 것이 execution() 지시자야.
//대괄호 "[]" 로 묶은 부분은 옵션 항목이라서 생략이 가능하다는 뜻이고 파이프 "|" 는 OR 조건으로 씀.
//execution([접근제한자 패턴] 타입패턴 [타입패턴.]이름패턴 (타입패턴 | "..", ...)
//구체적으로 예를 들면 아래처럼 나옴.
//public int springbook.learningtest.spring.pointcut.HelloTarget.sayHello(String, int) throws java.lang.RuntimeException
//하나씩 설명해 볼게.
//> 접근제한자 패턴
//위에 public 이 접근 제한자.
//public/protected/private 등이 올 수 있음.
//생략이 가능함.
//생략하면 접근제한자에 대해서는 조건을 부여하지 않겠다는 뜻.
//> 리턴 타입 패턴
//위에서 int 가 리턴 타입 패턴.
//리턴하는 값이 어떤 데이터 타입인지 조건을 넣음.
//이거는 생략 불가능함.
//리턴 타입에 대해서 조건을 부여하고 싶지 않으면 그냥 별표 "*" 넣어서 모든 타입을 허용하도록 만들면 됨.
//> 패키지 주소
//위에서 "springbook.learningtest.spring.pointcut.HelloTarget" 에 해당함.
//패키지 주소 + 클래스 이름까지
//주의할 거는 메소드 이름 나오기 전까지임.
//점 "." 까지만임.
//여기까지는 생략 가능함.
//중간에 쩜쩜 ".." 으로 여러 패키지 계층을 포함할 수도 있음.
//별 "*" 로 어떤 문자든 ok 할 수도 있음.
//> 메소드 이름
//위에서 sayHello
//생략할 수 없음.
//모든 메소드를 대상으로 하려면 그냥 별표 "*" 하면 됨.
//> 파라미터 타입
//위에서 (String, int)
//메소드 안에 넣어주는 파라미터의 타입 패턴.
//파라미터끼리 콤마 "," 로 구분하면서 여러 개를 순서대로 넣을 수도 있음.
//파라미터가 없는 거랑 모두 허용하는 거랑 다르니까 주의.
//파라미터가 없는 메소드만 대상으로 하려면 그냥 빈 소괄호 "()"
//파라미터가 뭐가 오든, 갯수도 상관 없고 타입도 상관 없으면 쩜쩜 ".."
//앞에 두 개까지만 조건 따지고 뒤에부터는 상관 없으면, 앞에 두 개까지만 적고 ".." 해도 됨.
//> 예외 타입 패턴
//위에서 throws java.lang.RuntimeException
//어떤 예외를 던지는 앤지.
//생략 가능.
////
//// 여러 가지 표현식 스타일
//실행하는 메소드의 시그니처를 비교하는 execution() 를 알아봤어.
//하지만 포인트컨 표현식은 꼭 메소드의 시그니처뿐만 아니라 다양한 방식을 제공해.
//스프링의 빈 이름을 비교하는 bean() 방법도 있고.
//예를 들어 bean(*Service) 이러면 Service 로 끝나는 모든 빈을 대상으로 함.
//또는 애노테이션만으로도 제어할 수가 있어.
//클래스/메소드/파라미터 등에 어떤 애노테이션이 붙어 있다면 그 메소드만 대상으로 하게 만들 수도 있지.
//상상해 봐.
//@Transactional 이렇게 붙어 있는 메소드면 다 트랜잭션 어드바이스 기능을 제공해주는 방식이라면?
//애노테이션 하나만으로 제어할 수 있으니 얼마나 편하겠어.
////