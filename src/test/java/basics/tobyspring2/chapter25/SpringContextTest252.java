package basics.tobyspring2.chapter25;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"file:src/main/resources/chapter23/applicationContext233.xml"})
public class SpringContextTest252 {


    public static ApplicationContext testObjects;
    @Autowired
    private ApplicationContext context;


    @Test
    public void test1() {
        System.out.println(this.context);
        if(SpringContextTest252.testObjects == null) {
            SpringContextTest252.testObjects = this.context;
        }
        Assertions.assertEquals(SpringContextTest252.testObjects, this.context);
        Assertions.assertSame(SpringContextTest252.testObjects, this.context);
        Assertions.assertTrue(SpringContextTest252.testObjects == this.context);
    }

    @Test
    public void test2() {
        System.out.println(this.context);
        if(SpringContextTest252.testObjects == null) {
            SpringContextTest252.testObjects = this.context;
        }
        Assertions.assertEquals(SpringContextTest252.testObjects, this.context);
        Assertions.assertSame(SpringContextTest252.testObjects, this.context);
        Assertions.assertTrue(SpringContextTest252.testObjects == this.context);
    }

    @Test
    public void test3() {
        System.out.println(this.context);
        if(SpringContextTest252.testObjects == null) {
            SpringContextTest252.testObjects = this.context;
        }
        Assertions.assertEquals(SpringContextTest252.testObjects, this.context);
        Assertions.assertSame(SpringContextTest252.testObjects, this.context);
        Assertions.assertTrue(SpringContextTest252.testObjects == this.context);
    }
}




// p.200 -chapter 2.5.2
//이번에는 SpringFramework 의 ApplicationContext 를 테스트해보자.
//ApplicationContext 는 스프링 컨테이너가 싱글톤으로 관리하며 DI 해준다고 했다.
//그러면 테스트 메소드마다 모두 같은 context 를 쓰고 있어야 한다.
//이걸 어떻게 테스트할 수 있을까?
//일단 context 오브젝트를 담아둘 변수를 클래스 변수에 static 으로 선언해 둔다.
//테스트 메소드를 실행할 때마다 this 가 DI 받은 context 와 static 에 담아둔 context 가 모두 같은지 확인한다.
//스프링 컨테이너가 싱글톤으로 관리하는 오브젝트를 DI 해줬다면 모든 context 가 같은 오브젝트여야 정상이다.
//다른 context 오브젝트가 발견된다면 테스트 실패다.
//단, 테스트 메소드가 최초로 실행되는 때에는 static 변수가 null 인 상태이므로 확인해보고 넣어줘야 한다.
//JUnit 테스트와 ApplicationContext 를 가지고 학습 테스트를 만들어 봤다.
//JUnit 은 메소드 실행마다 테스트 오브젝트를 새로 생성하는지 확인했고,
//ApplicationContext 는 메소드 실행마다 모두 같은 context 오브젝트를 DI 받는지 확인했다.
//이렇게 학습 테스트를 만들어 보면서 해당 기술의 작동 방식을 익히기도 하고 테스트하는 방법도 고민해 보면서 테스트 코드를 짜는 실력도 는다.



