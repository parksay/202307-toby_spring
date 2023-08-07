package basics.tobyspring1.chapter15;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;

@Configuration
public class DaoFactory151 {

    @Bean
    public UserDao151 userDao() throws ClassNotFoundException, SQLException {
        return new UserDao151(connectionMaker());
    }

    @Bean
    public ConnectionMaker151 connectionMaker() {
        return new NaverConnectionMaker151();
    }
}

//p.96
//우리 애플리케이션에서 IoC 컨테이너 역할을 했던 DaoFactory 를
//이제 스프링 프레임워크 안에서 IoC 컨테이너 역할을 하도록 바꿔주는 방법.
//@Configuration
//=> 클래스에다가 붙여주기. 스프링 프레임워크가 빈 팩토리를 만들 때 이 클래스를 바탕으로 만들라고.
//번역하자면 설정 파일이지. DaoFactory 가 설계도 역할을 한다고 했어.
//이 오브젝트는 언제 생성돼서, 다른 어떤 오브젝트랑 조합해서, 누구랑 사용되고 언제 소멸되고...
//이게 애플리케이션 전체에서 일어나면 그 애플리케이션의 설계도 같은 구실을 하게 될 거라고 했지.
//그런 의미에서 이 클래스는 설계도 역할을 하는 거고, 설정 파일인 셈이고, 그걸 알려주려고 @Configuration 을 붙였음.
//그러니까 스프링 프레임워크가 애플리케이션을 띄울 때 이 설정 파일을 바탕으로 빈 팩토리를 만들라고.
//빈 팩토리고 프레임워크 안에서 설계도 역할, 오브젝트의 제어, 설정 클래스 역할을 하겠지.
//@Bean
//=> 오브젝트를 생성해서 되돌려주는 메소드에다가 @Bean 애노테이션 붙여주기.
//Bean 이란 무엇이냐? 오브젝트 단위의 컴포넌트.
//원래는 옛날에 EJB 나 UI툴 같은 데서 쓰는 한 단위의 컴포넌트를 말했음.
//이게 이제 자바까지 와서 오브젝트 단위의 애플리케이션 컴포넌트를 가리키는 말이 됨.
//이 @Configuration 이 붙은 클래스 안에서, @Bean 이 붙은 메소드가 내뱉는 오브젝트들은
//스프링이 어떤 통에다가 모아 놓고 한 번에 관리함.
//이런 통을 빈 팩토리(Bean Factory) 라고 함.
//우리가 설정 파일에서 생성해둔 오브젝트들은 모두 이 빈 팩토리 안에 들어 있으니까
//혹시 애플리케이션을 실행하다가 어떤 오브젝트가 필요하다 싶으면 이 빈 팩토리로부터 꺼내다 쓰면 됨.
//
//1. 내부 알맹이는 빈 팩토리가 맞긴 한데, 그 알맹이에 기능을 좀 더 확장시켜서 구현한 애플리케이션 컨텍스트(Application Context) 를 보통 사용함.
//2. 그냥 우리가 만들어 둔거 DaoFactory 이런 데서 가져다 쓰면 되지 굳이 빈 팩토리니, 애플리케이션 컨텍스트니 이런 걸 거쳐서 쓰는 이유는?
//조금 복잡해지기는 하지만 장점이 훨씬 많음. 오브젝트가 여기 저기서 여러 개 생성되는 걸 방지해주고 싱글톤 패턴을 유지해준다든가,
//생성, 사용, 소멸 등의 라이프사이클을 관리해준다든가, 등등...
//3. Bean 을 만들 때는 메소드 이름이 빈의 이름이 되므로 이름을 막 지어서는 안 됨.