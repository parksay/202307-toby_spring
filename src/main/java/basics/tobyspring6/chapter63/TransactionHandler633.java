package basics.tobyspring6.chapter63;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TransactionHandler633 implements InvocationHandler {

    private Object target;
    private String pattern;
    private PlatformTransactionManager transactionManager;

    public void setTarget(Object target) {
        this.target = target;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //
        if(method.getName().startsWith(this.pattern)) {
            return invokeInTransaction(method, args);
        } else {
            return method.invoke(this.target, args);
        }
    }

    private Object invokeInTransaction(Method method, Object[] args) throws Throwable {
        //
        TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            Object ret = method.invoke(this.target, args);
            this.transactionManager.commit(status);
            return ret;
        } catch (InvocationTargetException e) {
            this.transactionManager.rollback(status);
            throw e.getTargetException();
        }
    }
}

// p.446 - chapter 6.3.3
// 이렇게 InvocationHandler 를 확장했어.
//트랜잭션 경계를 설정해주는 InvocationHandler 를 만들었어.
//이 Handler 를 파라미터로 넣어서 Proxy 를 newInstance 해서 만들잖아?
//그 Proxy 로 실행하는 메소드들은 모두 이 InvocationHandler 를 거쳐가.
//실행하려는 메소드 이름이 pattern 에 안 맞으면 그냥 target 한테 invoke 실행을 위임해버려.
//반대로 메소드 이름이 pattern 에 맞으면 invokeInTransaction 를 실행해.
//invokeInTransaction 가 실행되면 transactionManager 를 가지고 트랜잭션을 관리해.
//transaction 을 생성하고 작업이 잘 끝나면 commti 하고 안 되면 rollback 해주긔.
//이게 근데 좋은 게, target 을 어떤 인터페이스로든 받을 수 있어.
//우리가 예전에 만들었던 UserServiceTx612.class 는 어때.
//UserService 를 상속했지.
//그리고 UserService 에서
//UserService 안에서 upgradeLevels() 에 트랜잭션 적용이 필요해서 따로 만든 거야.
//근데 UserService 의 add() 메소드에도 트랜잭션이 필요해지면?
//delete() 에도 필요해지면?
//필요한 메소드 늘어날 때마다 트랜잭션 구문 계속 중복해서 만들어 줘?
//뭐 또 그거 중복되는 부분 공통으로 빼서 템플릿/콜백 패턴으로 만들까?
//그럼 BookService / CarService / ChairService 계속 나오면?
//서비스마다 인터페이스 또 만들고, 트랜잭션 클래스 또 만들고 계속 반복...?
//그러다가 수정이라도 일어나면 다 일일이 찾아가서 수정하고?
//쉽지 않은 일이야.
//근데 이제 transaction 관리해주는 InvocationHandler 를 하나 뙇 만들어 놓잖아?
//그럼 그때 그때 어떤 인터페이스로든 그 자리에서 Proxy 로 만들면 돼.
//Proxy 로 만들 때 InvocationHandler 파라미터만 TransactionHandler 로 넣어주면 됨.
//메소드 이름만 패턴 잘 맞춰가지고 통일성 있게 해가지고 넣어주면 어떤 메소드든 트랜잭션 관리해 줌.
// 아 패턴도 그때 그때 파라미터로 넣어 만들어 줄 수 있네, 프록시 만들 때.
// UserServiceTest633.class 로 가 보자.