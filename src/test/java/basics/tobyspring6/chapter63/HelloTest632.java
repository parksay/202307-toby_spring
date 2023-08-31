package basics.tobyspring6.chapter63;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.invoke.WrongMethodTypeException;
import java.lang.reflect.Proxy;

public class HelloTest632 {


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
}
