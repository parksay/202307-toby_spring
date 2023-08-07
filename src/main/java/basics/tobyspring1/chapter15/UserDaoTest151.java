package basics.tobyspring1.chapter15;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;

public class UserDaoTest151 {
    public static void main(String args[]) throws ClassNotFoundException, SQLException {
        //
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory151.class);
        UserDao151 dao = context.getBean("userDao", UserDao151.class);
        //
        User111 user = new User111();
        user.setId("test151");
        user.setName("테스트151");
        user.setPassword("myTest");
        //
        dao.add(user);
        //
        System.out.println(user.getId() + "등록 성공");
        //
        User111 user2 = dao.get(user.getId());
        //
        System.out.println(user2.getName());
        System.out.println(user2.getPassword());
        System.out.println(user2.getId() + "조회 성공");
    }
}
//p.97
// >> AnnotationConfigApplicationContext...?
//빈 팩토리 Bean Factory 가 IoC 컨테이너 역할을 한다고 했지.
//그리고 그 빈 팩토리에 이런 저런 기능을 확장시켜서 만든 게 ApplicationContext 라고 했고.
//그래서 이 ApplicationContext 를 생성할 때는 설정 정보가 필요해.
//IoC 컨테이너가 설계도라고 했잖아.
//그럼 ApplicationContext 오브젝트를 만들려면 빈 팩토리를 구성해야 하고, 빈 팩토리를 구성하려면 오브젝트들의 설계도가 있어야 하겠지.
//그래서 설계도를 넣어주는 법은 여러 가지가 있어.
//XML 로 해도 되고, Java class 로 해도 되고, ...
//그 중에서 우리는 어노테이션이 붙은 자바 클래스로 만들 거임.
//어노테이션(Annotation)으로 설정(Config) 파일을 바탕으로 ApplicationContext 만들어 달라고. AnnotationConfigApplicationContext.
// >> AnnotationConfigApplicationContext 오브젝트를 생성하면서 생성자로 클래스 하나를 던져줌.
//이 클래스는 설정 정보로 활용할 클래스임.
//설정 정보라고 하면, @Configuration 애노테이션이 붙어 있는 클래스.
//오브젝트들을 어떻게 생성하고 어떤 인터페이스에는 어떤 오브젝트를 심어넣을 거고... 오브젝트들의 관계를 설정해주는 클래스.
//>> context.getBean() 하면서 넣어주는 파라미터는?
//첫째 파라미터는 빈의 이름 문자열.
//빈의 이름은 @Bean 이 붙어 있는 메소드의 메소드 이름.
//그 메소드가 생성해준 오브젝트는 그 이름으로 저장됨.
//두 번째 파라미터는 클래스 타입.
//이거는 굳이 안 넣겠다면 안 넣어줘도 됨.
//근데 대신 getBean() 메소드의 return type 이 Object 라서 매번 캐스팅을 수동으로 해줘야 함.
//근데 두 번째 파라미터로 클래스를 넣어주면 return type 도 그 클래스로 던져주기 때문에 편함.
//편한 것도 있지만, 에러 방지, 컴파일 단계에서 한 번 더 검증, 캐스팅 자동 등으로 웬만하면 쓰는 걸 추천.
