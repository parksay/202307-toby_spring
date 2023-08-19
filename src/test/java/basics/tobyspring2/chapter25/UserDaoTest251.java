package basics.tobyspring2.chapter25;

import basics.tobyspring2.chapter23.User232;
import basics.tobyspring2.chapter23.UserDao233;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.SQLException;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations={"file:src/main/resources/chapter23/applicationContext233.xml"})
public class UserDaoTest251 {

    // 픽스처 dao 테스트 오브젝트 DI
    @Autowired
    private UserDao233 dao;
    // 픽스처 user 테스트 데이터 선언
    private User232 user1;
    private User232 user2;
    private User232 user3;


    @BeforeEach
    public void setUp() {

        // 픽스처 user 테스트 데이터 초기화
        this.user1 = new User232("id242-1", "name242-1", "psw242-1");
        this.user2 = new User232("id242-2", "name242-2", "psw242-2");
        this.user3 = new User232("id242-3", "name242-3", "psw242-3");
    }

    @Test
    public void addAndGet() throws SQLException {

        // deleteAll() / getCount() 테스트
        this.dao.deleteAll();
        Assertions.assertEquals(0, this.dao.getCount());

        // add() / get() 테스트 > add
        this.dao.add(this.user1);
        Assertions.assertEquals(1, this.dao.getCount());
        this.dao.add(this.user2);
        Assertions.assertEquals(2, this.dao.getCount());

        // add() / get() 테스트 > get
        User232 user3 = this.dao.get(this.user1.getId());
        Assertions.assertEquals(user3.getId(), this.user1.getId() );
        Assertions.assertEquals(user3.getName(), this.user1.getName() );
        Assertions.assertEquals(user3.getPassword(), this.user1.getPassword() );
        User232 user4 = this.dao.get(this.user2.getId());
        Assertions.assertEquals(user4.getId(), this.user2.getId() );
        Assertions.assertEquals(user4.getName(), this.user2.getName() );
        Assertions.assertEquals(user4.getPassword(), this.user2.getPassword() );

        // deleteAll() / getCount() 테스트
        this.dao.deleteAll();
        Assertions.assertEquals(0, this.dao.getCount());


    }

    @Test
    public void getCount() throws SQLException{

        // 0개 부터 시작해서 하나씩 올려보고 마지막에 다시 0개로 끝내기
        this.dao.deleteAll();
        Assertions.assertEquals(0, this.dao.getCount());
        this.dao.add(this.user1);
        Assertions.assertEquals(1, this.dao.getCount());
        this.dao.add(this.user2);
        Assertions.assertEquals(2, this.dao.getCount());
        this.dao.add(this.user3);
        Assertions.assertEquals(3, this.dao.getCount());
        this.dao.deleteAll();
        Assertions.assertEquals(0, this.dao.getCount());
    }


    @Test
    public void getUserFailure() throws SQLException {

        // 혹시나 일치하는 데이터가 우연히 있을지 모르니 일단 모두 지우고
        this.dao.deleteAll();
        // 엉뚱한 데이터로 uesr 찾아보기
        Assertions.assertThrows(EmptyResultDataAccessException.class, ()->this.dao.get("emptyId"));

    }
}

//p.197 - chapter 2.5.1
//새로운 기술을 배울 때는 테스트부터 만들어 보자
//그랬을 때 장점은?
//1. 빠르다.
//실제로 애플리케이션에 적용해가면서 학습을 하려면 미리 준비해야 할 게 많다.
//서버 올리고 화면 띄우고 DB 연동하고 비지니스 로직 태우고...
//값을 이거에서 저걸로 바꿨을 때 결과는 어떻게 바뀌는지?
//또는 환경을 이렇게 했다가 저렇게 바꿨을 때 결과는 뭐가 달라지는지?
//이렇게 입력값을 바꾸면 출력값이 어떻게 바뀌는지 마구마구 해보면서 익혀나가는 건데.
//테스트 코드로 짜보면 값을 바꾸고 나서부터 결과를 보기까지 시간적 간격이 짧음.
//애플리케이션 다 띄워가면서 하려면 너무 오래 걸림.
//2. 개발 중에 테스트 코드를 샘플 코드로써 참조할 수 있다.
//애플리케이션에 적용하면 결과적으로 제대로 작동하는 최종 버전만 남음.
//근데 테스트 코드는 처음에 설정 파일 만든 거, 초기화 하는 과정, API 호출 방법, 결과는 어떻게 받아야 하는지...
//다 샘플 코드로 남아 있음.
//개발하다가 언제든지 들어가서 확인해 봐도 됨.
//3. 기술에 버전이 바뀌면 테스트 코드로 안정성을 높일 수 있다
//요즘은 오픈 소스든 상용 제품이든 빠르게 바뀜.
//내가 기존에 쓰던 코드가 버전이 바뀌면서 안 쓰게 됐으면...?
//또는 부르는 방법이나 받아오는 방법이 바꼈으면..?
//그런 과정들을 다 샘플로써 테스트 코드에 남겨 놨어.
//테스트 코드 한 번 돌려 보면 어디는 그대로고 어디는 우리한테 영향이 있고 없고...
//한 번에 알기 편함.
//4. 테스트 코드 짜는 실력이 는다.
//이 기술 저 기술 가져다가 테스트 코드 짜보면서 테스트 코드를 만드는 실력 자체가 늘게 됨.

