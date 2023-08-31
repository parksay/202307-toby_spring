package basics.tobyspring6.chapter63;

import java.lang.invoke.WrongMethodTypeException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class UppercaseHanlderEx632 implements InvocationHandler {
    Object target;

    public UppercaseHanlderEx632(Object target) {
        this.target = target;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object ret = method.invoke(this.target, args);
        if(ret instanceof String) {
            if(method.getName().startsWith("sayH")) {
                return ((String)ret).toUpperCase();
            } else {
                throw new WrongMethodTypeException();
            }
        } else {
            return ret;
        }
    }

}


// 이거는 UppercaseHanlder632 와 비교해서 두 가지 기능이 확장됨.
// 첫째, 그전에는 Hello 인터페이스를 구현한 오브젝트만 받을 수 있었음.
//이제는 그런 거 상관 없이 범용적으로, 모두 다 받게 해 놨음.
//그냥 Object 로 받게 해 뒀음.
// 어떠한 인터페이스를 구현한 타겟이든 상관없이 재사용 가능해짐.
//둘째, 필터링 기능이 있음.
//메소드 이름이 "sayH" 로 시작할 때에만 되돌려주게 해 놓음.
//return type 이 String 인데 "sayH" 로 시작하는 메소드가 아닌 경우에는 exception 던지게 해 놓음.