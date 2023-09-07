package basics.tobyspring6.chapter63;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"file:src/main/resources/chapter63/MessageFactoryBeanTest634-context.xml"})
public class MessageFactoryBeanTest634 {

    @Autowired
    ApplicationContext context;

    @Test
    public void factoryBeanTest() {
        Object message = context.getBean("message");
        Assertions.assertEquals(message.getClass(), Message634.class);
        Assertions.assertEquals(((Message634)message).getText(), "hello world");
    }


    @Test
    public void getFactoryBeanTest() {
        Object factory = context.getBean("&message");
        Assertions.assertEquals(factory.getClass(), MessageFactoryBean634.class);
    }
}


// 자 우리가 만든 MessageFactoryBean 이 생각한 대로 작동하는지 테스트를 만들어 보자.
// 원래 @ContextConfiguration 이렇게만 해두려고 했음.
// 파일 위치 지정 안 하고 어노테이션만 달아 두면 default 파일 이름으로 xml 불러옴.
// default 값은 resources 디렉토리에서 (클래스 이름) + ('-context.xml') 찾음.
// 일단 getBean 해서 던져주는 오브젝트의 데이터 타입이 뭔지 궁금해.
// 우리가 스프링 컨테이너 설정 파일에 등록한 클래스는 MessageFactoryBean.class 였어.
//그럼 등록된 bean 도 MessageFactoryBean.class 의 오브젝트가 되어야 정상이야.
//하지만 그 클래스가 FactoryBean.class 인터페이스를 구현했다면 다르다고 했지.
//그 클래스의 getObject() 함수를 실행해서 그 return 값을 bean 으로 등록한다고 했어.
//이게 진짜냐고.
//그걸 지금 확인해보고 싶은 거잖아.
//그래서 테스트 짜고 있는 거잖아.
//과연 빈이 MessageFactoryBean 랑 Message 중에 어떤 클래스의 오브젝트가 등록되는지 알고 싶은 거잖아.
//그러면 이거를 어떻게 테스트해봐야겠어.
//스프링 context 로부터 getBean() 해가지고 그 Object 의 class 가 뭔지 확인해 보자고.
//@Autowired 하려면 할 수 있었겠지만 지금 테스트 해보려는 게 그거니까 일부러 getBean() 으로 받음.
//return type 도 파라미터로 안 넘기고, return 받은 결과도 캐스팅 안 함.
//그냥 그 return 받은 오브젝트에다가 getClass() 찍어 봄.
//ㅇㅋ 통과.
//우리가 설정 파일에서 등록한 빈이 진짜 맞는지?
//property 로 넣어준 값이 Message 오브젝트까지 잘 들어갔는지?
//message.getText() 로 확인해 보기.
//ㅇㅋ 통과.
////
//테스트가 하나 더 있어.
//getFactoryBean() 메소드.
//이런 경우가 있어.
//내가 MessageFactoryBean.class 클래스를 빈으로 등록했어.
//근데 FactoryBean.class 인터페이스를 구현한 클래스는 해당 클래스가 빈으로 등록되는 게 아니라고 했지.
//그 안에 구현해 놓은 getObject() 메소드를 실행해서 그 결과값을 빈으로 등록한다고 했지.
//근데 나는 그게 아닌데?
//나는 진짜로 MessageFactoryBean.class 를 빈으로 등록하고 싶은 건데...?
//이럴 때는 getBean() 할 때 빈 이름 앞에다가 "&" 를 붙여주면 됨.
//그런 경우가 또 필요해.
//뒤에 나와.
//어디서 나오냐?
//UserService634.class 로 가보기.