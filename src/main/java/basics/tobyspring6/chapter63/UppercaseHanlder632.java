package basics.tobyspring6.chapter63;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class UppercaseHanlder632 implements InvocationHandler {
    Hello632 target;

    public UppercaseHanlder632(Hello632 target) {
        this.target = target;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String ret = (String) method.invoke(this.target, args);
        return ret.toUpperCase();
    }

}


// invoke 메소드는 리플렉션의 Method 인터페이스를 구현한 오브젝트를 파라미터로 받음.
//그리고 그 메소드를 실행할 때 필요한 파라미터도 args 로 받음.
//다이나믹 프록시 오브젝트는 클라이언트가 요청하는 모든 정보를 리플렉션 정보로 변환해서 invoke() 메소드로 넘기게 되는 것.
//타겟 메소드의 모든 요청을 하나의 메소드로 집중시킬 수 있어서 효율적이고 효과적이게 처리할 수 있음.

