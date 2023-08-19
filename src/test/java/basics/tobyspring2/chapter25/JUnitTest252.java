package basics.tobyspring2.chapter25;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

public class JUnitTest252 {


    private static Set<JUnitTest252> jUnitObjects;


    @Test
    public void test1() {
        System.out.println(this);
        System.out.println(JUnitTest252.jUnitObjects + "");
        if(JUnitTest252.jUnitObjects == null) {
            JUnitTest252.jUnitObjects = new HashSet<>();
        }
        Assertions.assertFalse(JUnitTest252.jUnitObjects.contains(this));
        JUnitTest252.jUnitObjects.add(this);
    }

    @Test
    public void test2() {
        System.out.println(this);
        System.out.println(JUnitTest252.jUnitObjects + "");
        if(JUnitTest252.jUnitObjects == null) {
            JUnitTest252.jUnitObjects = new HashSet<>();
        }
        Assertions.assertFalse(JUnitTest252.jUnitObjects.contains(this));
        JUnitTest252.jUnitObjects.add(this);
    }

    @Test
    public void test3() {
        System.out.println(this);
        System.out.println(JUnitTest252.jUnitObjects + "");
        if(JUnitTest252.jUnitObjects == null) {
            JUnitTest252.jUnitObjects = new HashSet<>();
        }
        Assertions.assertFalse(JUnitTest252.jUnitObjects.contains(this));
        JUnitTest252.jUnitObjects.add(this);
    }
}

// p.200 -chapter 2.5.2
//학습 테스트를 직접 만들어 보자.
//JUnit 은 테스트를 수행할 때 메소드마다 오브젝트를 새로 만든다고 했다.
//정말로 그런지 확인해 보자.
//방법은 어떻게 할 수 있을까?
//일단 클래스에다가 스태틱 변수로 콜렉션을 하나 만든다.
//종류가 딱히 중요한 건 아니므로 그냥 Set 으로 만든다.
//이 콜렉션 안에는 테스트 오브젝트들을 넣어둘 것이다.
//그리고 테스트 메소드를 하나 실행할 때마다 현재 테스트 오브젝트인 this 가 Set 안에 있는지 확인한다.
//없다면 Set 안에 담아준다.
//this 가 Set 안에 이미 있다면 테스트 오브젝트가 겹친다는 뜻이므로 테스트가 실패해야 맞다.
//단 테스트 메소드가 최초로 실행될 때는 Set 이 null 인 상태이므로 초기화해준다.
