package basics.tobyspring6.chapter63;

import org.springframework.beans.factory.FactoryBean;

public class MessageFactoryBean634 implements FactoryBean {

    String text;

    public void setText(String text) {
        // 스프링 컨테이너 만들 때 <property> 로 넣어주는 값 받으려고
        this.text = text;
    }

    public Message634 getObject() throws Exception {
        // 여기서 return 해주는 오브젝트가 스프링 컨테이너에 bean 으로 등록됨.
        // 이 안에다가 빈 오브젝트를 생성하는 과정에서 필요한 복잡한 로직도 넣을 수 있음.
        return Message634.newMessage(this.text);
    }

    public Class<? extends Message634> getObjectType() {
        // 위에 getObject() 가 return 해주는 object 의 데이터 타입을 return 해줌
        return Message634.class;
    }

    public boolean isSingleton() {
        // 위에 getObject() 가 return 해주는 object 가 항상 같은 오브젝트인지? 즉 싱글톤인지?
        // 여기서는 getObject() 부를 때마다 새로운 오브젝트 생성해서 넘겨주므로 false 로 해둠.
        // 스프링 빈에서는 당연히 싱글톤으로 관리해주겠지.
        return false;
    }

}

//p.451
//사실 FactoryBean 인터페이스 소스를 까서 보면 내용은 간단해.
//안에 있는 메소드 사실 3개밖에 없음.
//....
//T getObject() throws Exception; // 빈 오브젝트를 생성해서 되돌려줌.
//Class<?> getObjectType();   // 생성되는 오브젝트의 타입을 되돌려줌
//boolean isSingleton();  // getObject() 로 던져주는 오브젝트가 항상 같은 오브젝트인지, 즉 싱글톤인지 알려줌.
//```
//실제 소스 코드는 아래.
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
//public interface FactoryBean<T> {
//    T getObject() throws Exception;
//    Class<?> getObjectType();
//    default boolean isSingleton() {
//        return true;
//    }
//}
//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
// 그럼 이렇게 만든 MessageFactoryBean634.class 가 진짜로
// 스프링 컨테이너에 등록되는 과정에서 우리가 기대하는 대로 작동하는지 학습 테스트를 만들어 보자.
// MessageFactoryBeanTest634.class 로 가보자.