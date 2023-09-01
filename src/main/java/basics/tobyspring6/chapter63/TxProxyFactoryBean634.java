package basics.tobyspring6.chapter63;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import java.lang.reflect.Proxy;

public class TxProxyFactoryBean634 implements FactoryBean<Object> {


    private Object target;
    private PlatformTransactionManager transactionManager;
    private String pattern;
    private Class<?> serviceInterface;


    public void setTarget(Object target) {
        this.target = target;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public void setServiceInterface(Class<?> serviceInterface) {
        this.serviceInterface = serviceInterface;
    }

    public Object getObject() throws Exception {
        TransactionHandler633 txHandler = new TransactionHandler633();
        txHandler.setTarget(this.target);
        txHandler.setPattern(this.pattern);
        txHandler.setTransactionManager(this.transactionManager);
        //
        return Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[] { this.serviceInterface },
                txHandler
        );
    }

    public Class<?> getObjectType() {
        return serviceInterface;
    }

    public boolean isSingleton() {
        return false;
    }
}

// p.460 - chapter6.3.4
//UserServcie / BookService / ChairService....
//트랜잭션 기능이 필요한 어떤 인터페이스가 오든 재활용할 수 있도록 Object 타입으로 주고 받도록 만들었어.
//이런 방식은 굉장히 큰 장점이야.
//우리가 데코레이터 패턴 만들 때 겪었던 문제들을 해결해 줘.
//데코레이터 패턴 적용할 때 어떤 문제점이 있었어?
//첫째, 인터페이스를 모두 구현해야 하는 번거로움.
//한 클래스 안에 메소드가 엄청 많아.
//그 메소드들 중에서도 어떤 한두 메소드에 부가 기능을 덧붙이고 싶어.
//그러면 그 클래스를 인터페이스로 추상화시키고 프록시를 만들어야겠지.
//내가 원하던 메소드들에 부가 기능 덧붙이고, 나머지는 그대로 타겟한테 위임해.
//근데 한두 메소드에 부가 기능 적용하려고 인터페이스의 나머지 모든 메소드들을 다 구현해야만 해.
//프록시는 그 인터페이스를 구현해야 하니까.
//우리가 새로 도입한 '다이나믹 프록시' 방식은 이러한 문제점을 해결해 줘.
//일부 메소드에 추가 기능 덧붙이겠다고 나머지까지 모두 구현해야 하는 번거로움이 사라졌어.
//둘째, 여러 메소드에 같은 부가 기능이 반복되는 문제.
//UserService 클래스 안에서 트랜잭션 기능이 add() / upgradeLevels() / delete() .... 등등 여러 메소드에 필요하다고 해 봐.
//그럼 이걸 구현한 프록시는 메소드마다 같은 로직이 반복되겠지.
//우리가 만든 다이나믹 프록시는 invocationHandler 를 거쳐서 가기 때문에 모든 메소드에 일괄적으로 적용할 수 있어.
////
//이렇게 좋은 다이나믹 프록시 방식에도 사실 아직 한계점이 있어.
//첫째, 여러 클래스에 같은 부가 기능 추가하기.
//우리가 만든 다이나믹 프록시 방식으로는 한 클래스 안에 있는 여러 메소드에 일괄적으로 부가 기능을 덧붙이기에는 좋았어.
//모두 InvocationHandler 를 거쳐서 가니까.
//우리가 원하는 조작을 그 invoke 함수 안에다가 때려 넣으면 됐었어.
//근데 여러 클래스 안에 있는 메소드들에 부가 기능을 덧붙이는 일은...?
//TxProxyFactoryBean 을 재활용한다고는 해도, 안에 DI 해줘야 할 것들도 있고, 어쨌든 bean 으로 등록해야 해.
//적어도 한 4,5줄은 나올 텐데.
//클래스가 100개만 돼도, 설정파일이 4,500줄이나 돼.
//똑같은 TxProxyFactoryBean 클래스인데 DI 내용만 바껴서 몇 십 개, 몇 백 개씩 빈으로 등록돼 있는 게 맞을까..?
//둘째, 하나의 타겟에 여러 부가 기능을 덧붙이고 싶을 때.
//upgradeLevels() 에다가 트랜잭션 기능도 덧붙이고 싶고, 로깅 기능도 덧붙이고 싶고, 보안 기능도 덧붙이고 싶고...
//내가 원하는 타겟은 하나인데, 그 타겟이 이 팩토리 빈에, 저 팩토리 빈에, 여기 저기 불려다니면서 DI 당하고 있어.
//그렇게 해서 부가 기능 하나씩 얻어와.
//이것도 맞을까...?
//어려워 보이지만 스프링 컨테이너의 강력한 DI 기능은 가능성이 무한하다고.
//라고, 토비가 말함.
//
//
//
//
//
