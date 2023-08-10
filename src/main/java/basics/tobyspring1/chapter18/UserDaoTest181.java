package basics.tobyspring1.chapter18;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import java.sql.SQLException;

public class UserDaoTest181 {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        // 오브젝트 생성
         ApplicationContext context = new GenericXmlApplicationContext("applicationContext181.xml");
        // ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext182.xml", UserDao181.class);
        UserDao181 dao1 = context.getBean("userDao", UserDao181.class);
        UserDao181 dao2 = context.getBean("userDao", UserDao181.class);
        // 참조값 주소 비교
        System.out.println(dao1);
        System.out.println(dao2);
        // 동일성 비교
        System.out.println( "dao1 === dao2 ? " + (dao1 == dao2) );
        // 동등성 비교
        System.out.println( "dao1 === dao2 ? " + (dao1.equals(dao2)) );


        // 똑같은 설정 정보로 만든 스프링 컨테이너의 똑같은 클래스로 만든 오브젝트라도 참조값은 다름
        ApplicationContext context2 = new GenericXmlApplicationContext("applicationContext181.xml");
        System.out.println(context.getBean("userDao"));
        System.out.println(context2.getBean("userDao"));
        UserDao181 dao3 = context.getBean("userDao", UserDao181.class);
        UserDao181 dao4 = context.getBean("userDao", UserDao181.class);
        // 참조값 주소 비교
        System.out.println(dao3);
        System.out.println(dao4);
        // 동일성 비교
        System.out.println( "dao3 === dao4 ? " + (dao3 == dao4) );
        // 동등성 비교
        System.out.println( "dao3 === dao4 ? " + (dao3.equals(dao4)) );
    }
}
//p. 128
// 우리가 지금까지 오브젝트로 만들었던 AnnotationConfigApplicationContext 클래스는
// ApplicationContext 인터페이스를 구현한 것.
// 인터페이스 ApplicationContext 는 인터페이스 BeanFactoru 를 상속했음
// BeanFactory 를 구현하려면 오브젝트 의존 관계 설정 정보가 필요함
// 즉, ApplicationContext 를 구현하려면 의존 관계 설정 자료를 넣어줘야 함
// AnnotationConfigApplicationContext 에서는 그걸 Annotation 으로 나타낸 자바 클래스를 바탕으로 판단한 것
//꼭 그 방법만 있는 건 아니고 스프링에서는 여러 방법을 지원함.
//그중 하나가 xml 파일
//xml 파일은 텍스트 파일이라 다루기 편한 데다가 컴파일을 따로 할 필요도 없음
//자바 클래스였다면 수정 사항이 발생할 때마다 컴파일까지 다시 해줘야 하겠지
//xml 로 설정한다면 해당 파일만 바꿔치기 해줘도 패스로 읽어오니까 컴파일이 필요 없음
//이제 그럼 Annoctation 바탕의 자바 클래스를 xml 로 바꿔보는 작업을 할 거야
//스프링에서 Bean 을 만들 때는 세 가지 정보가 들어간다고 했어
//예를 들어 아래 빈 생성 메소드를 보면서 말해볼게
//@Bean
//public ConnectionMaker connectionMaker() {
//    return new SimpleConnectionMaker();
//}
//첫째, 생성되는 빈의 이름
//메소드 이름이 곧 빈의 이름이 된다고 했지
//나중에 빈 검색이나 autowired 할 때 변수 이름 등에 활용됨
//둘째, 구현 클래스
//return type 은 인터페이스 ConnectionMaker 야.
//근데 그 인터페이스를 구현한 클래스 중에 어떤 걸 오브젝트로 만들 거냐고
//DaumConnectionMaker 로 할지 NaverConnectionMaker 로 할지 어쩔지...
//셋째, 의존 관계 정보
//의존하고 있는 오브젝트들을 생성자에 넣어준다고 했지.
//아래처럼
//@Bean
//public UserDao userDao(){
//    return new UserDao(connectionMaker());
//}
//이런 정보를 xml 파일에도 그대로 넣어주는 구조로 만들어진다.
//먼저 전체를 <beans> 태그로 감싼다
//<beans> 테그 안에는 <bean> 태그가 여러 개 들어갈 수 있다
//<bean> 태그 하나당 빈이 하나씩 생성된다
//즉, 빈 하나를 생성하는 데에 필요한 정보들을 이 <bean> 태그 안에 다 넣어주면 된다
//의존 관계는 있을 수도 있고 없을 수도 있지만, 빈의 이름이랑 그 빈의 구현 클래스는 무조건 들어가는 기본값이다.
//그래서 빈의 이름, 빈의 구현 클래스는 <bean> 태그 안에 속성으로 넣어준다
//나머지 하나, 의존관계는 <bean> 태그 안에 <property> 라는 하위 태그를 만들어서 따로 넣어준다
//일단 의존 관계가 없는 connectionMaker 를 빈으로 만들면 아래처럼 된다
//<bean id="connectionMaker" class="basics.tobyspring1.chapter18.SimpleConnectionMaker181" />
//<bean> 태그 안에 id="" 속성은 빈의 이름이 된다.
//빈의 이름은 보통 인터페이스 이름으로 쓴다
//<bean> 태그 안에 class="" 속성은 그 인터페이스를 구현한 클래스가 된다.
//클래스를 넣어줄 때는 패키지 주소를 루트부터 전체를 넣어준다.
//빈을 생성할 때 필요한 세 가지 정보 즉, 생성되는 빈의 이름, 구현 클래스, 의존 관계 정보 중에 두 개를 알아봤다.
//이번에는 나머지 하나, 의존 관계를 설정하는 방법을 알아보자.
//<bean> 태그 안에 하위 태그로 <property> 를 만들어준다고 했다.
//<bean id="userDao" class="basics.tobyspring1.chapter18.UserDao181">
//    <property name="connectionMaker" ref="connectionMaker" />
//</bean>
//여기서 보면 <property> 태그 안에 name 속성와 ref 속성이 있다.
//name 속성은 의존 관계를 받아줄 클래스 변수의 이름이다.
//스프링 컨테이너가 의존 오브젝트를 주입해주면 클래스는 그걸 받아서 어떤 변수 안에다가 저장해둘 것이다.
//그 변수의 이름이다.
//ref 속성은 의존 관계로 넣어줄 빈의 이름이다.
//스프링 컨테이너가 가지고 있는 빈들 중에 어떤 빈을 의존 관계로 넣어줄지 그 이름을 넣어주면 된다.
//<property> 태그를 사용할 때는 알아두어야 할 점이 두 가지 있다.
//첫째, name 과 ref 속성을 헷갈리지 않아야 한다.
//쓰다 보면 name 과 ref 안에 같은 이름이 들어가는 경우가 많다.
//클래스 안에서 변수 이름을 정할 때는 구체적인 구현 클래스 이름을 넣지 않고 그 인터페이스 이름으로 정하는 게 보통이다.
//빈 이름을 정할 때도 구체적인 구현 클래스가 아니라 인터페이스의 이름으로 정한다.
//따라서 name 과 ref 안에는 둘 모두 인터페이스 이름이 들어가게 된다.
//이렇게 두 속성에는 같은 값이 들어가는 게 보통이므로 나중에 가면 헷갈리기 쉽다.
//둘이 각각 무엇을 나타내는지 알아 두어야 한다.
//둘째, 수정자를 만들어 둔다.
//생성자로 넣어줄 때는 그냥 생성자에서 받아서 바로 클래스 변수 안에 꽂아줬다.
//그런데 xml 파일로 설정 정보를 만들면 과정이 조금 달라진다.
//일단 기본 생성자 메소드를 이용해서 그 클래스를 오브젝트로 만든다.
//그 다음에 수정자 메소드를 이용해서 변수들을 하나씩 넣어준다.
//따라서 xml 파일로 applicationConfig 를 만들 때는
//빈이 될 클래스 안에는 기본 생성자가 있어야 하고 의존 관계를 주입 받을 변수마다 수정자가 있어야 한다.
//
//이렇게 해서 xml 파일을 만들었다.
//이번에는 이 xml 파일을 바탕으로 applicationContext 오브젝트를 만드는 법을 알아보자.
//ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
//ApplicationContext 인터페이스를 구현한 GenericXmlApplicationContext 클래스를 오브젝트로 만들면 된다.
//이때 의존 관계 설정 정보를 담고 있는 xml 파일의 위치를 생성자 메소드 안에 파라미터로 넣어주면 된다.
//xml 파일 위치는 프로젝트 루트 디렉토리로 두는 게 관습이다.
//그러면 클래스 패스 안에 파라미터를 넣어줄 때 간단하게 "applicationContext.xml" 만 넣어주어도 된다.
//
//덧붙여서, ClassPathXmlApplicationContext 라는 것도 있다.
//클래스패스를 클래스로 대신 넣어주는 기능이다.
//ApplicationContext context = new GenericXmlApplicationContext("basics/tobyspring1/chapter18/applicationContext.xml");
//예를 들어 위처럼 xml 파일이 있는 패키지 경로가 복잡한 경우도 있다.
//이런 경우에 아래처럼 첫 번째 파라미터로 xml 파일 이름만 넣어주고 두 번째 파라미터로 클래스를 하나 넣어준다.
//그러면 클래스가 있는 패키지 경로에서 xml 파일을 찾는다.
//위와 아래는 결국 같은 파일을 불러온다.
//ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml", UserDao181.class);
//
