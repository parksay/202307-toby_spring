package basics.tobyspring2.chapter23;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import java.sql.SQLException;

public class UserDaoTest232 {

    @Test
    public void addAndGet() throws SQLException {

        // 오브젝트 생성
        ApplicationContext context = new GenericXmlApplicationContext("chapter23/applicationContext232.xml");
        UserDao232 dao = context.getBean("userDao", UserDao232.class);

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

}



// p.168
//UserDaoTest222.java 에서 이것 저것 설명했는데 정정해야 할 듯
//토비의 스프링 책이 벌써 10년도 더 된 책이라 버전이 옛날 버전
//지금은 버전이 JUnit5 인데 책에서 알려주는 내용은 JUnit4 시절 얘기라서
//책을 그대로 따라하면 에러가 남.
//import 할 때 패키지 주소나 메소드 이름, 사소한 문법 같은 거 주의하기
//import org.junit.jupiter.api.Assertions;
// Assertions.assertEuqals();
// Assertions.assertArrayEquals();
// Assertions.assertTrue();
// Assertions.assertFalse();
// Assertions.assertNull();
// Assertions.assertNotNull();
// Assertions.assertThrows();
// 이외에도 몇 개 더 있음...
//테스트할 때 중요하게 고려해야 할 점이 또 하나 더 있음.
//맥락으로부터 독립적일 것.
//예를 들어서, 이 시간에 하면 되고 저 시간에 하면 안 된다?
//이 테스트를 하고 나서 수행하면 통과하고 저 테스트를 하고 나서 수행하면 실패한다?
//저번에는 됐는데 이번에는 안 된다?
//올바르게 짠 테스트라면 수행할 때마다 똑같은 결과가 나와야 해.
//근데 우리가 여태까지 만들었던 테스트는 이런 조건을 충족하지 못해.
//테스트를 한 번 수행하면서 user1 을 users 테이블에 추가했어.
//그러고 나서 똑같은 테스트를 한 번 더 수행하면 user1 이 이미 있는 사용자라서 추가할 수 없다고 하지.
//primary key 가 겹친다면서.
//이건 제대로 된 코드가 아니야.
//따라서 새로 개발해보자.
//테스트를 수행하면 먼저 테이블 안에 있던 모든 데이터를 삭제하는 기능.
//그러면 삭제 기능이 제대로 수행됐는지 확인할 수 있는 기능도 하나 만들어야겠네.
//삭제 기능 수행할 때마다 DB 접속해서 테이블 열어보고 지워졌는지 어쩐지 확인할 순 없잖아.
//현재 테이블 안에 데이터가 몇 개 들어가 있는지 확인하는 기능도 함께 개발해야겠군.
//// => 기능 개발은 userDao232.java 에서 완료.
//그럼 이제 개발 완료한 기능들이 잘 수행되는지 테스트할 수 있는 테스트 케이스를 또 만들어야겠네.
//근데 deleteAll 이 제대로 작동하는지 알려면?
//데이터가 이미 있는 상태에서 deleteAll 실행 후에 데이터 갯수가 0개가 되는지
//그러면 getCount 가 필요함
//getCount 잘 되는지 확인하려면?
//deleteAll 해서 데이터가 0개인 상태에서 데이터 하나 추가하면 데이터 갯수가 1개가 되는지
//그래서 deleteAll 이나 getCount 는 서로 작동이 제대로 확인되지 않은 기능 2개이기 때문에
//그냥 기존에 만들었던 addAndGet 에다가 붙여서 테스트해보기
//처음에 deleteAll 돌리고 getCount 값이 0 인지 확인하고
//user1 추가하고 getCount 값이 1인지 확인하고
//user2 추가하고 getCount 값이 2인지 확인하고
//마지막으로 deleteAll 한 번 더 돌리고 getCount 값이 다시 0 이 되는지 확인하기



