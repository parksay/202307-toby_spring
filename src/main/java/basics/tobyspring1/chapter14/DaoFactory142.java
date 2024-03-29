package basics.tobyspring1.chapter14;

import java.sql.SQLException;

public class DaoFactory142 {

    public UserDao141 userDao() throws ClassNotFoundException, SQLException {
        return new UserDao141(connectionMaker());
    }

    public ConnectionMaker141 connectionMaker() {
        return new NaverConnectionMaker141();
    }
}

//    public AccountDao userDao() throws ClassNotFoundException, SQLException {
//        return new AccountDao(connectionMaker());
//    }
//
//    public MeesageDao userDao() throws ClassNotFoundException, SQLException {
//        return new MeesageDao(connectionMaker());
//    }
//      // ... 이런 식으로 Dao 가 계속 더해져도 connectionMaker 오브젝트를 생성하는 부분은 한 곳만 수정하면 됨.


//// IoC 개념 설명 p.92
//IoC 란? Inversion of Control 제어의 역전이다.
//역전은 뭔가 뒤바뀐다는 뜻이지.
//그럼 여기서 제어라는 것이 무엇이냐?
//보통 애플리케이션을 생각해 보자.
//Java 라면 main() 메소드가 있겠지.
//애플리케이션은 main() 메소드가 가장 먼저 실행되므로 거기서부터 흐름이 시작돼.
//main() 메소드는 내가 어떤 오브젝트를 사용해서 어떤 기능을 실행할지 알아서 스스로 정해.
//main() 메소드가 A오브젝트를 불러서 a 메소드 기능을 불러다 썼어.
//A오브젝트는 다시 B오브젝트를 불렀고, B는 C와 D를 부르고....
//이렇게 흐름이 점점 흘러가는 게 순차적인 흐름이겠지.
//아이스버킷 챌린지 하듯이, 폭탄 돌리기 하듯이,
//내가 가지고 있던 흐름으로 누구를 부르고,
//그 부름을 받은 애가 다시 다음 사용할 애를 찾아서 부르고...
//근데 우리 UserDaoTest 를 봐.
//자기는 테스트하는 역할만 수행할 뿐, 자기가 뭘 가져다가 쓰게 될지 몰라.
//우리가 만든 UserDao 들도 봐 봐.
//거기 안에는 DB로부터 데이터를 꺼내오는 로직이 있기는 하지만,
//DB 연결 정보를 받아올 때 어떤 오브젝트를 활용해서 받아올지는 자기도 몰라.
//자기가 어떤 오브젝트를 생성해서 사용하게 될지, 누구한테 흐름을 넘겨주게 될지 자기도 모르는 거지.
//심지어 UserDao 본인이 어떤 흐름 안에서 오브젝트로 생성이 될지, 누가 호출할지 본인조차 몰라.
//이렇게 다른 애들도 거시적으로 봐 보자.
//각자 클래스들은 로직이 들어 있고 기능을 하고 있지만,
//어떤 흐름 속에서 자기가 생성될지, 어떤 오브젝트를 자기가 생성하게 될지, 아무것도 몰라.
//그런 책임을 DaoFactory 한테 넘겨줬거든.
//UserDao 는 제어권을 자기가 가지고 있다가 DaoFactory 한테 넘겨준 거야.
//그리고 사령관처럼, 코치처럼, 제갈량처럼 어떤 오브젝트를 어디에 쓸지, 어디에 배치할지,
//이런 흐름을 총 관리하는 역할을 하는 게 바로 DaoFactory
//UserDao 는 자기가 제어를 하는 입장이었다가, 자기가 DaoFactory 로부터 제어를 받는 입장이 됐어.
//제어권이 UserDao 한테 있다가 DaoFactory 한테로 역전됐어.
//이게 바로 '제어의 역전'.
//영화 <컨트롤러> 봤지.
//거기에는 인간의 삶을 설계하고, 누가 몇 시에 무슨 일을 겪고 어디서 누구를 만나게 될지 다 정해져 있어.
//사람들은 모두 자기 삶을 주체적으로 살아가고 있다고 착각하지만 사실은 뒤에서 누군가 조종하는 단체가 있었지.
//우리가 개발하는 컴포넌트들도 모두 이와 같은 것.
//자기네들이 언제 어떻게 사용되고 누구를 사용할지 본인도 몰라.
//뒤에서 누군가 조종하는 책임자가 있을 뿐, 자기네들은 그대로 움직이기만 하는 장기말과도 같아.
//DaoFactory 도 아주 간단하기는 하지만 IoC 컨테이너 또는 IoC 프레임워크라고 할 수도 있지.

// p.93
//IoC 프레임워크..? 프레임워크란 뭔지 설명하기에 아주 적절한 시점이야.
//바로 제어권이 어디에 있느냐를 기준으로 보면 됨.
//흐름을 내가 제어하고 제어권이 나에게 있고 그 흐름 안에서 내가 원할 때 원하는 라이브러리를 가져다 써.
//그게 라이브러리야.
//반대로 흐름을 시작하고 끝맺고 제어를 프레임워크가 하고 내가 만든 코드는 그 프레임워크가 흘러가는 중에 어딘가에서 사용될 뿐이야.
//내가 만든 코드를 내가 직접 실행하는 게 아니라 프레임워크가 실행되면 그 흐름 안에서 내가 만든 코드가 쓰이는 것.
//그게 프레임워크야.

//p,94
//스프링도 엄연한 프레임워크지.
//스프링도 프레임워크고, 자기가 흐름을 가지고 있는 채로 내가 짠 코드를 가져다 쓰는 거야.
//우리는 오브젝트들을 생성하고 관리하는 오브젝트 팩토리로써 DaoFactory 를 만들었어.
//그리고 그 DaoFactory 는 IoC 컨테이너 역할을 하면서 제어 책임을 가져오고 프레임워크가 되었지.
//스프링이 애플리케이션의 흐름을 제어하는 방법도 똑같아.
//IoC 컨테이너를 만들고 그 안에서 오브젝트들을 생성하고 조합하고 사용하고 소명시키고...
//오브젝트들을 제어함으로써 제어권을 가지고 있어.
//우리가 만든 DaoFactory 도 조금만 손보면 스프링이 사용하는 IoC 컨테이너로 탈바꿈시킬 수 있어.
//DaoFactory151.java 로 가 보자.