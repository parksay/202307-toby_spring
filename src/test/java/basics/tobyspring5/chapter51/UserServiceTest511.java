package basics.tobyspring5.chapter51;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"file:src/main/resources/chapter51/test-applicationContext511.xml"})
public class UserServiceTest511 {

    @Autowired
    private UserDao511 userDao;
    @Autowired
    private UserService511 userService;

    private List<User511> userList;

    @BeforeEach
    public void setUp() {
        // 배열 형태로 주면 그걸로 Array 만들어주는 편리한 기능 Arrays.asList(a, b, c, d...);
        userList = Arrays.asList(
                new User511("id511-1", "name511-1", "psw511-1", Level511.BASIC, 49, 0),
                new User511("id511-2", "name511-2", "psw511-2", Level511.BASIC, 50, 0),
                new User511("id511-3", "name511-3", "psw511-3", Level511.SILVER, 60, 29),
                new User511("id511-4", "name511-4", "psw511-4", Level511.SILVER, 60, 30),
                new User511("id511-5", "name511-5", "psw511-5", Level511.GOLD, 100, 100)
        );
    }

    @Test
    public void beanTest() {
        Assertions.assertNotNull(this.userService);
    }

    @Test
    public void upgradeLevelsTest() {

        //
        this.userDao.deleteAll();
        for(User511 user : this.userList) {
            this.userDao.add(user);
        }
        //
        this.userService.upgradeLevels();
        //
        this.checkLevel(this.userList.get(0), Level511.BASIC);
        this.checkLevel(this.userList.get(1), Level511.SILVER);
        this.checkLevel(this.userList.get(2), Level511.SILVER);
        this.checkLevel(this.userList.get(3), Level511.GOLD);
        this.checkLevel(this.userList.get(4), Level511.GOLD);
    }


    public void checkLevel(User511 user, Level511 level) {
        User511 userUpdate = this.userDao.get(user.getId());
        Assertions.assertEquals(userUpdate.getLevel(), level);
    }

    @Test
    public void addTest() {
        //
        this.userDao.deleteAll();
        //
        User511 userWithLevel = this.userList.get(4);
        User511 userWithoutLevel = this.userList.get(0);
        userWithoutLevel.setLevel(null);
        //
        this.userService.add(userWithLevel);
        this.userService.add(userWithoutLevel);
        //
        User511 userWithLevelRead = this.userDao.get(userWithLevel.getId());
        User511 userWithoutLevelRead = this.userDao.get(userWithoutLevel.getId());
        //
        Assertions.assertEquals(userWithLevelRead.getLevel(), userWithLevelRead.getLevel());
        Assertions.assertEquals(userWithoutLevelRead.getLevel(), Level511.BASIC);
    }
}


//User 픽스처가 5개나 필요해.
//하나씩 하지 말고 그냥 리스트로 묶어서 가지고 다니기.
////
//이렇게 특정한 값들을 기준으로 삼아서 작동하는 기능은 테스트 할 때 기준의 경계가 되는 값들 앞뒤로 넣어서 체크해보는 게 좋음.
//로그인이 50 이상일 때 레벨이 업그레이드 된다, 라고 하면 그 경계인 50이랑 그보다 하나 모자란 49랑 해서 테스트하기.
////
//개발 요건 중에 이런 사항이 있어.
//"처음 가입하는 사용자는 기본적으로 레벨을 BASIC으로 넣어준다."
//사실 뭐 개발이 어렵지는 않아.
//근데 생각해볼 거는, 이 기능을 어디에다가 구현할지?
//일단 가장 먼저 생각하는 곳이 userDao 의 add() 지.
//"insert into users(id, name, password, level, login, recommend) values (?, ?, ?, ?, ?, ?)"
//이 쿼리에다가 박아버리는 거지.
//왜냐하면 INSERT 문은 최초에만 들어갈 거고, 처음 넣을 때 BASIC 으로 넣어달라고 했으니까.
//아니면 아예 DB단을 바꿔버려도 되지.
//default 로 level 칼럼 값은 basic 값으로 들어가도록.
////
//근데 생각해 봐.
//이게 맞아...?
//"처음 가입하는 사용자는 기본적으로 레벨이 BASIC이다" 이거는 비지니스 로직 아니야..?
//이거는 그냥 애플리케이션 상에서 정책인 거고, 정책은 바뀔 수도 있는 거잖아.
//뭐 연말 이벤트로 기본 레벨을 바꿔줄 수도 있는 거고, VIP 손님은 가입부터 GOLD 로 줄 수도 있는 거고,
//BASIC 아래에 GUEST 같은 것도 새로 생길 수 있는 거고...
//그거를 지금 DAO 단에다가 로직을 넣는 게 맞아?
//그거를 지금 DB 테이블까지 건드려가면서까지 구현하는 게 맞아..?
//UserDao 의 add() 메소드는 위에서 던져주는 user 객체를 그대로 DB 에 넣어주는 기능을 하는 거지.
//거기에 지금 비지니스 로직을 끼워 넣는 거는 아니지.
////
//그럼 어디에...?
//다음 생각해볼 거는 User 클래스 자체에 넣는 방법도 생각해볼 수 있지.
//User 생성자 메소드에다가 초기화할 때 Level 값을 기본적으로 BASIC 으로 넣기.
//나쁘지는 않아.
//하지만 User 오브젝트는 여기저기서 많이 쓰는 거고,
//"처음 가입자는 기본적으로 BASIC" 이거는 비지니스 로직 때 한 번 쓰는 건데..
////
//그럼 어디에...?
//답정너지 ㅎㅎㅎㅎ
//비지니스 로직이니까 어디에 넣어?
//UserService 에 넣어야지.
//이렇게 기능을 구현할 때 그냥 기능이 잘 되는지, 에러는 없는지, 그거만 생각할 게 아니야.
//구현이라는 관점에서만 볼 게 아니라, 설계라는 관점에서도 봐야 해.
//이 기능은 어디에 넣어야 가장 적절하겠는가?
//이 기능은 어떤 계층에서 작업이 이루어져야 가장 적절하겠는가?
//이 기능을 책임질 클래스/객체는 누가 되어야 적합하겠는가?
//그동안 그냥 감으로 "뭔가... 이건 여기다 두는 게 맞는 거 같아" 이러면서 개발했었는데 이거를 이제는 의식화하기.
//이제는 구현뿐만 아니라 설계까지 고려해서 이 기능을 어디에 구현해야 적합할지 판단하는 연습을 하고 구현하기.

