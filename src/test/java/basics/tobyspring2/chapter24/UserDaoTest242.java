package basics.tobyspring2.chapter24;

import basics.tobyspring2.chapter23.User232;
import basics.tobyspring2.chapter23.UserDao233;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;

public class UserDaoTest242 {

    // 픽스처 dao 테스트 오브젝트 생성
    private UserDao233 dao;
    // 테스트 데이터 만들어 두기
    private User232 user1;
    private User232 user2;
    private User232 user3;


    @BeforeEach
    public void setUp() {
        // 픽스처 dao 테스트 오브젝트 생성
        this.dao = new UserDao233();
        DataSource dataSource = new SingleConnectionDataSource(
                "jdbc:mysql://localhost:3306/testdb", "root", "0000", true);
        this.dao.setDataSource(dataSource);

        // 픽스처 user 테스트 데이터 생성
        this.user1 = new User232("id242-1", "name242-1", "psw242-1");
        this.user2 = new User232("id242-2", "name242-2", "psw242-2");
        this.user3 = new User232("id242-3", "name242-3", "psw242-3");
        System.out.println(this);
        System.out.println(this.dao);
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

// p.189
//앞에서 DI 생각해 봤지.
//클래스 변수에 있는 UserDao dao 를 DI 로 넣어준다고 했어.
//어딘가에서 JUnit 이라는 프레임워크를 기동시킬 거고,
//그 전에 SpringFramework 를 띄워서 ApplicationContext 만들고 빈 팩토리 만들어서 dao 빈을 꺼내서 JUnit 에 넣어준다고 했어.
//근데 DI 방법이 꼭 이런 방법만 있는 건 아니지.
//우리가 처음에 Spring 빈 컨테이너를 사용해서 DI 해주기 전에는 어떻게 했어?
//이런 저런 방법이 많았지.
//그냥 객체지향형 언어의 특성을 그대로 살리기만 해도 DI 방식은 여러 가지가 있었어.
//스프링은 단지 자바가 객체지향 언어로서 가지는 그런 특성을 좀 더 손쉽게 사용할 수 있도록 미리 틀을 짜둔 것 뿐이었어.
//여기서도 마찬가지야.
//JUnit 에서 DI 가 필요한데, 그걸 굳이 스프링 컨테이너를 써야 한다거나 @ExtendWith 어노테이션을 써야 한다거나 하지 않아.
//JUnit 테스트를 할 때 DI 고려하는 방법
//첫째, 스프링 컨테이너 없이 짜 보기.
//가능하다면 스프링 없이 테스트할 수 있는 방법을 가장 먼저 생각해 보기.
//스프링 없이 그 기술 자체만 가지고 테스트할 때 가장 간단하고 간결하고 빠르고 개입하는 기술이 적기 때문에 테스트 결과도 알기 쉬움.
//UserDao 를 구현해 놓은 클래스 내부를 보면 어디에서 스프링 기술을 직접 사용하는 곳은 없어.
//그냥 DB 접속해서 원하는 데이터 제대로 가져올 수 있는지, 그 기능만 있을 뿐이야.
//테스트도 딱 그거만 하면 되지, 뭘 굳이 스프링까지 띄우고 컨테이너에서 어쩌구 저쩌구 할 필요 없어.
//그런 방식으로 구현한 게 바로 지금 이 파일 - UserDaoTest242.java
//근데 이건 오브젝트 초기화가 간단할 경우겠지.
//초기화 해야 하는 오브젝트가 많거나 서로 관계가 복잡하거나 초기화 과정 자체가 복잡하거나 그러면 그냥 스프링으로 하는 게 나아.
//이때 쓰는 방법이 다음 방법.
//둘째, applicationContext.xml 파일을 테스트용으로 하나 더 만들기.
//어쨌든 스프링을 띄워야 하는 경우가 생기면 조금 번거롭거나 복잡하더라도 applicationContext.xml 파일을 테스트용으로 하나 더 만들기.
//예를 들어서 운영할 때는 NaverDataSource.class 쓰고 테스트할 때는 SimpleDriverDataSource.class 쓴다고 해 봐.
//운영할 때는 "springbook" 데이터베이스로 접근하고 테스트할 때는 "testdb" 쓴다고 해 봐.
//운영할 때는 "admin" 계정으로 접속하고 테스트할 때는 "test" 계정으로 접속한다고 해 봐.
//이런 거 수도 없이 많을 텐데...
//테스트할 때 싹다 바꿨다가 테스트 끝나면 원래대로 되돌리고, 나중에 또 테스트할 때는 다시 바꾸고...
//그러다가 하나 빼먹어서 실수라도 하면...?
//운영 DB 에 deleteAll() 날려버리면...?
//그냥 차라리 test-applicationContext.xml 하나 더 만드는 게 낫다.
//셋째, @DirtiesContext 활용하기
//그러다가도 정 필요한 경우에는 @DirtiesContext 를 사용하는 방법도 있음.
//이거는 다른 메소드에서는 다 미리 세팅해 둔 거 쓰는데 어떤 한두 메소드만 특정하게 바꿔야 할 때.
//클래스 전체가 공유하는 오브젝트로 다같이 쓰다가, 어떤 녀석들은 강제로 좀 바꿔서 테스트해봐야 할 때.
//그럴 때 @DirtiesContext 쓰면 해당 메소드에서만 수정해서 사용하고 그 다음부터는 계속해서 새 context 만들어서 씀.
//원래 context 같은 거는 한 번 생성되면 수정하지 않는 게 원칙이고,
//stateful 하지 않도록 하는 게 좋고, 그 이후로 새 context 만들어서 쓴다는 게 좀 걸리기는 함.
//그래도 정 방법이 없을 때는 활용하면 좋음.


