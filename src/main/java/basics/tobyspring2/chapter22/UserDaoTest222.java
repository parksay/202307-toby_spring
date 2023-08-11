package basics.tobyspring2.chapter22;

import basics.tobyspring1.chapter18.User111;
import basics.tobyspring1.chapter18.UserDao184;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import java.sql.SQLException;

public class UserDaoTest222 {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        // 오브젝트 생성
        ApplicationContext context = new GenericXmlApplicationContext("chapter18/applicationContext184.xml");
        UserDao184 dao = context.getBean("userDao", UserDao184.class);
        User111 user = new User111();
        user.setId("test222");
        user.setName("테스트222");
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

//p.156
//우리가 main() 메소드를 이용해서 만들었던 테스트는 문제점이 여러 개 있어.
//첫째, 결과를 인간이 확인해야 함
//둘째, 갯수가 많아지면 불편함
//첫째, 결과를 여전히 인간이 확인해야 한다.
//무슨 말이냐면, 테스트를 하고 나면 결과가 있을지 경우의 수를 따져보자.
//가장 먼저, 테스트 성공.
//뭐 다른 거 없지 그냥 성공임.
//반대로 테스트 실패.
//근데 테스트 실패했을 때는 다시 나누자면 두 가지로 나눌 수 있어.
//하나는 테스트를 수행하는 도중에 에러가 나서 실패한 경우.
//이런 건 테스트 에러라고 해.
//다른 하나는 테스트를 끝까지 수행했고 에러도 나지는 않았지만 기대했던 대로 작동하지는 않아.
//이런 건 테스트 실패라고 해.
//에러가 났으면 콘솔에 어쩌구 저쩌구 나오니까 바로 알 수 있겠지.
//하지만 테스트 실패는 인간이 들어가서 일일이 인간이 확인해야 하지.
//main 테스트의 단점 둘째, 갯수가 많아지면 불편함.
//테스트 해야 하는 기능이 수백, 수천 개면 어떡해...?
//또는 비슷한 메소드나 클래스가 여러 개 있으면 그거 다 일일이 돌려봐야 하나..?
//main 메소드는 하나인데 그 메소드 안에다가 다 때려박아야 하는가?
//갯수가 많아지면 다 다루기가 어려워져.
//
//자, 이런 문제를 해결하기 위해서 자바 진영에서는 테스트 프레임워크가 이미 여러 가지 존재해.
//그중에서도 자바 프로그래머를 위한 자바 테스팅 프레임워크라고 불리는 JUnit 이 있어.
//이름대로 자바로 단위 테스트를 수행할 때 유용한 프레임워크야.
//우리가 main() 를 사용해서 임시로 만들었던 테스트 메소드를 JUnit 프레임워크를 사용하는 테스트로 바꿔보자.
//JUnit 프레임워크를 사용하는 방법은 두 가지.
//하나, 메소드 접근 제어자를 public 으로 선언해 줄 것.
//둘, 메소드 앞에 @Test 라는 어노테이션을 붙여줄 것.
//일단, JUnit 은 테스트 '프레임워크'라고 했어.
//프레임워크 가장 큰 특징이 뭐라고 했어.
//IoC라고 했지.
//제어권을 자기가 가지고 있고, 우리가 짜는 코드는 그 제어권 흐름 안에서 어딘가에서 불려서 사용되는 거야.
//근데 우리가 만든 main() 메소드는 자기가 main 기동 메소드니까 제어권을 가지고 있다고.
//이걸 일반 메소드로 일단 바꿔줄 거야.
//그리고 말한 대로 public 으로 바꿔주고 @Test 를 붙여주지.
//테스트 메소드 만들 때는 이름도 신경 써서 만들면 좋지.
//어떤 기능을 테스트하는 메소드인지 이름만 봐도 알 수 있도록 이름 붙여주기.
//다음은 검증 코드 부분도 JUnit 기능으로 바꾸기.
//>> 변경 이전 코드 (UserDaoTest221.class)
//>    // 테스트 실패 시
//>    if(!user.getName().equals(user2.getName())) {
//>    System.out.println("테스트 실패 (name)");
//>    } else if(!user.getPassword().equals(user2.getPassword())) {
//>    System.out.println("테스트 실패 (password)");
//>    } else {
//>    // 테스트 성공 시
//>    System.out.println("조회 테스트 성공");
//>    }
//>> JUnit 변경 이후 코드
//>    assertThat(user2.getName(), is(user.getName()));
//>    assertThat(user2.getPassword(), is(user.getPassword()));
//
//assertThat 설명
//JUnit 이 제공해주는 스태틱 메소드 중에 assertThat 이라는 메소드가 있음.
//이걸로 if 문을 대체할 수 있다.
//위에서 보면 assertThat() 메소드에 파라미터가 두 개가 들어가지.
//첫째는 비교하고 싶은 값, 둘째는 '매처(matcher)' 라고 부르는 조건.
//is() 나 not() 같은 애들이 있음.
//위에서 방금 썼던 매처 is()는 첫째 파라미터와 비교해 보고 일치하면 다음으로 넘어가고 다르면 테스트가 실패하도록 만들어준다.
//
//
//자, 이제 이렇게 테스트 메소드를 만들었어.
//근데 이걸 어떻게 수행할 거냐?
//스프링 프레임워크에서 IoC/DI 컨테이너에서도 그랬듯이 프레임워크라도 어쨌든 어딘가에서 한 번은 호출을 해줘야 해.
//JUnit 프레임워크도 마찬가지고, 흐름은 자기가 제어하지만 기동은 직접 해줘야 해.
//main() 메소드를 만들고 아래처럼 수행하기.
//> public static void main(String[] args) {
//>     JUnitCore.main("springbook.user.dao.UserDaoTest");
//>         }
//JUnitCore.main() 안에 테스트를 실행할 클래스의 패키지 경로는 넣어주면 됨.
//그러면 그 클래스 안에 @Test 어노테이션이 붙은 메소드를 모두 실행해보고 결과를 알려줌.
//결과 로그를 보면 테스트를 몇 개 수행했는지, 시간은 얼마나 걸렸는지, 몇 개 실패했는지 등등 알려줌.



