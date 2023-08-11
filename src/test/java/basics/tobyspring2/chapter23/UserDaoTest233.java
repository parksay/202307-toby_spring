package basics.tobyspring2.chapter23;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;

import java.sql.SQLException;

public class UserDaoTest233 {

    @Test
    public void addAndGet() throws SQLException {

        // 오브젝트 생성
        ApplicationContext context = new GenericXmlApplicationContext("chapter23/applicationContext233.xml");
        UserDao233 dao = context.getBean("userDao", UserDao233.class);

        // 테스트 데이터 만들어 두기
        User232 user1 = new User232("id232-1", "name232-1", "psw232-1");
        User232 user2 = new User232("id232-2", "name232-2", "psw232-2");

        // deleteAll() / getCount() 테스트
        dao.deleteAll();
        Assertions.assertEquals(0, dao.getCount());

        // add() / get() 테스트 > add
        dao.add(user1);
        Assertions.assertEquals(1, dao.getCount());
        dao.add(user2);
        Assertions.assertEquals(2, dao.getCount());

        // add() / get() 테스트 > get
        User232 user3 = dao.get(user1.getId());
        Assertions.assertEquals(user3.getId(), user1.getId() );
        Assertions.assertEquals(user3.getName(), user1.getName() );
        Assertions.assertEquals(user3.getPassword(), user1.getPassword() );
        User232 user4 = dao.get(user2.getId());
        Assertions.assertEquals(user4.getId(), user2.getId() );
        Assertions.assertEquals(user4.getName(), user2.getName() );
        Assertions.assertEquals(user4.getPassword(), user2.getPassword() );

        // deleteAll() / getCount() 테스트
        dao.deleteAll();
        Assertions.assertEquals(0, dao.getCount());


    }

    @Test
    public void getCount() throws SQLException{
        // 오브젝트 생성
        ApplicationContext context = new GenericXmlApplicationContext("chapter23/applicationContext232.xml");
        UserDao232 dao = context.getBean("userDao", UserDao232.class);

        // user 만들어 두기
        User232 user1 = new User232("id232-1", "name232-1", "psw232-1");
        User232 user2 = new User232("id232-2", "name232-2", "psw232-2");
        User232 user3 = new User232("id232-3", "name232-3", "psw232-3");

        // 0개 부터 시작해서 하나씩 올려보고 마지막에 다시 0개로 끝내기
        dao.deleteAll();
        Assertions.assertEquals(0, dao.getCount());
        dao.add(user1);
        Assertions.assertEquals(1, dao.getCount());
        dao.add(user2);
        Assertions.assertEquals(2, dao.getCount());
        dao.add(user3);
        Assertions.assertEquals(3, dao.getCount());
        dao.deleteAll();
        Assertions.assertEquals(0, dao.getCount());
    }


    @Test
    public void getUserFailure() throws SQLException {
        // 오브젝트 생성
        ApplicationContext context = new GenericXmlApplicationContext("chapter23/applicationContext232.xml");
        UserDao232 dao = context.getBean("userDao", UserDao232.class);

        // 혹시나 일치하는 데이터가 우연히 있을지 모르니 일단 모두 지우고
        dao.deleteAll();
        // 엉뚱한 데이터로 uesr 찾아보기
        Assertions.assertThrows(EmptyResultDataAccessException.class, ()->dao.get("emptyId"));

    }
}


//p.170 - chapter2.3.3
//우리가 한 가지 놓치고 있는 점이 있었어
//dao.get() 해서 user 가져올 때 만약에 디비에 없는 사용자면?
//근데 어떤 사용자를 DB에서 꺼내오려고 했는데 만약에 DB 에 그런 사용자가 없다면 어떻게 동작할지?
//두 가지 방법이 있을 거야.
//하나는 그냥 약속된 특정한 값을 보내는 거야.
//null 을 보내거나 비어 있는 사용자를 보내거나 임시 사용자를 보내거나 등등...
//다른 하나는 예외를 던지는 거야.
//여기서는 예외를 던지는 방식으로 처리해 보자.
//테스트 방법은 혹시라도 우연히 DB에 일치하는 데이터가 있을 수 있으니 일단 DB에 데이터를 모두 지우고
//엉뚱한 id 로 사용자 찾아달라고 get() 호출해 보기.
//그랬을 때 우리가 예상했던 예외가 잘 나오는지.
//
//스프링 창시자 '로드 존슨'은 말했다.
//"항상 네거티브 테스트를 먼저 만들라."
//그만큼 실패하는 테스트는 중요하다.
//개발자 입장에서는 '이런 상태에서 이런 값이 입력됐을 때 이렇게 동작하는 기능을 만들어야지'
//하고 바로 그 기능을 상상하면서 만들기 마련이다.
//그리고 그 생각했던 기능이 생각했던 조건 안에서 잘 작동하는 걸 보면 개발이 완료됐다고 생각하고 다음 개발로 넘어가는 게 자연스럽다.
//그러다 보면 실패하는 테스트, 네거티브 테스트는 허술하게 생각하고 넘어가는 경우가 많다.
//따라서 성공하는 테스트를 만들기 전에 실패하는 테스트를 먼저 작성하는 습관을 들이는 게 좋다.
//'이렇게 했을 때 이렇게 되는 기능'을 생각하면서 만들다 보면 저렇게 했을 때는 생각을 잘 못한다.
//하지만 실제 서비스가 운영되면 수 많은 경우가 발생하고 이런 경우도 있고 저런 경우도 있다.
//어떤 경우에서도 안정적으로 작동하는 서비스를 만들려면 항상 예외 경우까지 잘 테스트해야 한다.


// p.175 - chapter 2.3.4
// TDD (Test-Driven Development) 에 대하여
// TDD 란?
//앞에서 예외 케이스 테스트를 만들 때 UserDao 부터 수정했는가, getUserFailure 부터 만들었는가?
//기능 코드를 수정하기 전에 테스트부터 만들었다.
//기능을 먼저 만들고 그 기능이 잘 작동하는지 확인하려는 테스트 코드를 만든 게 아니다.
//반대로, 이런 조건에서 이렇게 작동했을 때 통과하는 테스트부터 먼저 만들고 나서 그에 맞춰서 기능을 만들었다.
//이렇게 테스트부터 먼저 작성하는 개발 방식을 TDD(Test-Driven Development) 라고 한다.
//앞에서 개발자가 개발하려고 앉았을 때 이런 생각을 한다고 했다.
//'이런 상태에서 이런 값이 입력됐을 때 이렇게 동작하는 기능을 만들어야지'
//이때 우리는 어쩌면 머릿속으로 테스트 케이스를 이미 만든 것일 수도 있다.
//그리고 기능을 다 구현하고 나면 이런 상황에서 이렇게 작동하는지 확인해 보고 기대했던 값이 안 나온다면 머릿속에 있던 테스트에 실패한 것과 다름 없다.
//테스트에 실패하면 원인을 찾고 수정하고 다시 머릿속에 있던 테스트를 확인해 본다.
//이렇게 머릿속에 있던 테스트를 통과할 때까지 수정을 거듭하다가 마침내 기대했던 결과가 나오면 테스트 통과다.
//머릿속에 있던 테스트가 통과했을 때 비로소 개발을 완료했다고 생각하고 개발을 멈춘다.
//그런 점에서 우리는 머릿속으로 이미 TDD 방법론으로 개발을 진행하고 있던 것일 수도 있다.
//다만 머릿속에서 진행하는 테스트는 문제가 있다.
//머릿속에서는 테스트 조건을 너무 성공하는 쪽으로만 조건을 좁게 설정하게 되고,
//오류가 나는지 아닌지 확실하게 보이지 않고 추정만 하게 되고,
//무엇보다 나중에 반복해서 다시 실행하기가 어렵다.
//기능 수정 사항 들어오면 수정 마치고 나서 다른 기존 기능들에 영향 없는지 확인해야 하는데 그게 안 되지.
//그래서 머릿속에만 있던 테스트 케이스를 실제 코드로 작성해 놓기.
//개발을 시작하기 전에 일단 테스트부터 만들고 나서, 그 테스트 케이스를 성공하기 위한 코드를 만드는 것.
//이러한 순서로 개발하는 게 바로 TDD 야.
//어려울 것 없지, 이미 우리가 머릿속으로 해오던 방식이지, 단지 그걸 코드로 짤 뿐.
//// TDD 의 효과
//TDD 방식을 사용하면 좋은 점?
//TDD 의 기본 방침은 "실패한 테스트를 성공하려는 목적이 아닌 코드는 만들지 않는다."
//즉, 내가 짜는 모든 기능에는 그에 대응되는 테스트 케이스가 있어.
//즉, 그만큼 테스트 케이스를 꼼꼼하고 촘촘하게 작성해두었다는 거지.
//개발을 먼저 해놓고 테스트를 만들다 보면 점점 허술하고 드문드문 만들게 마련이야.
//개발을 여기부터 여기까지 묶어서 한 번에 개발하고, 다 개발하고 나서 테스트 한 번에 하고.
//개발한 시점으로부터 테스트하는 시점까지 시간적 간격이 넓을수록 테스트도 허술해지겠지.
//귀찮기도 하고, 까먹기도 하고, 어쨌든 꼼꼼한 테스트 케이스를 빼먹기 일쑤야.
//근데 일단 테스트부터 만들고 나면 그럴 일이 없지.
//// TDD 시간 단축
//이렇게 개발을 시작하기 전에 일일이 테스트 케이스부터 만들려고 하면 오래걸리지 않을까 걱정할 수도 있어.
//오히려 시간도 단축된다.
//사실 테스트 코드를 짜는 데에는 시간이 그렇게 오래 걸리지 않아.
//어떤 기능을 만들어 놓은 결과만 가지고 와서 사용하니까.
//반면에 테스트 코드부터 짜놓는 순서로 인해 오는 이점은 더 많아.
//일단 테스트를 만들면서 내가 원하는 기능이 무엇인지, 어떻게 작동해야 하는지, 그 결과를 명확하게 다듬을 수가 있어.
//기능 정의서를 작성하는 느낌이지.
//개발을 하는 도중에 이만큼 짜고 생각하고, 저만큼 짜고 다음은 어떻게 할지 고민하고, 이러다가 저까지 지웠다가 다시 짜고..
//테스트 케이스부터 짜면 이렇게 시간을 낭비하는 것을 방지해줘.
//내가 앞으로 어떤 기능을 짤 것인지 목표를 또렷하게 하고 개발을 시작하면 당연히 구현도 더 빠르고 확실하게 나아가지.
//그리고 여러 가지 경우를 한 번에 고려할 수 있어서 처음부터 설계를 어느 정도 완성도 높게 하게 돼.
//이 기능은 이런 경우에는 이렇게 동작하고 저런 경우에는 저렇게 동작해야 하는데
//그렇게까지 넓게 생각하지 못하고 이런 경우 한 가지만 좁게 보고 개발하는 경우도 있어.
//그냥 기능 추가 정도면 그나마 나은데, 막 처음부터 경우 설정을 다시 했어야 했든가,
//따로 메소드로 빼 놓고 작성했어야 했다든가, 데이터 타입을 콜렉션으로 했어야 했다든가,
//등등 전체적인 구조를 다 바꿔야 하면 그것도 시간 낭비야.
//테스트 케이스부터 짜면 그런 걸 방지해주지.
//또 장기적으로 봤을 때도 테스트 케이스부터 짜면 이득이야.
//테스트 케이스부터 짜면 모든 기능들에 꼼꼼하게 테스트 케이스가 작성된다고 했어.
//테스트 드문드문 작성해 놨다가 나중에 개발 다 끝나고 나서 에러를 뒤늦게 발견해
//서버 띄우고 화면 띄우고 DB 연결하고 다 작동시키다가 에러 발생했어.
//그럼 이게 어느 부분에서 에러 요인이 있었는지 범위가 너무 커지잖아.
//꼼꼼하게 테스트를 짜 두었으면 에러를 발생했을 때 어디에서 발생했는지 명확하게 포착할 수 있다는 것이고,
//수정 사항이나 추가 기능이 발생했을 때 어디에서 영향도가 발생할지 안 할지 다 한 번에 꼼꼼하게 체크할 수 있지.
//
//이렇게 좋은데 외 않함?
//그냥... 귀찮으니까 ㅡㅡ
//아니 일단 해보라고 이 개발자들아
//일단 해보고 나면 훨씬 효율적이고 효과적이라고.
//한 번 익숙해지고 나면 TDD 가 아닌 방식으로는 개발하기 어렵다고 하는 사람도 있음.
//귀찮음만 극복하면 됨.