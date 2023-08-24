package basics.tobyspring5.chapter51;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"file:src/main/resources/chapter51/test-applicationContext511.xml"})
public class UserDaoTest511 {
    // 픽스처 선언
    @Autowired
    private UserDao511 dao;
    @Autowired
    private DataSource dataSource;
    private User511 user1;
    private User511 user2;
    private User511 user3;

    @BeforeEach
    public void setUp() {
        //
        this.user1 = new User511("id511-1", "name511-1", "psw511-1", Level511.BASIC, 1, 0);
        this.user2 = new User511("id511-2", "name511-2", "psw511-2", Level511.SILVER, 55, 10);
        this.user3 = new User511("id511-3", "name511-3", "psw511-3", Level511.GOLD, 103, 47);
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
        User511 user3 = this.dao.get(this.user1.getId());
        this.checkSameUser(user3, user1);
        User511 user4 = this.dao.get(this.user2.getId());
        this.checkSameUser(user4, user2);

        // deleteAll() / getCount() 테스트
        this.dao.deleteAll();
        Assertions.assertEquals(0, this.dao.getCount());

    }

    @Test
    public void update() {
        //
        this.dao.deleteAll();
        //
        this.dao.add(this.user1);   // 업데이트 돼야 할 유저
        this.dao.add(this.user2);   // 압데이트 되면 안 되는 유저
        //
        user1.setName("updated_name");
        user1.setPassword("updated");
        user1.setLevel(Level511.SILVER);
        user1.setLogin(101);
        user1.setRecommend(36);
        this.dao.update(user1);
        //
        User511 user1update = this.dao.get(this.user1.getId());
        checkSameUser(this.user1, user1update);
        User511 user2same = this.dao.get(this.user2.getId());
        checkSameUser(this.user2, user2same);
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

    @Test
    public void getAllTest() throws SQLException {
        //
        this.dao.deleteAll();
        //
        this.dao.add(this.user1);
        List<User511> result1 = this.dao.getAll();
        Assertions.assertEquals(1, result1.size());
        checkSameUser(user1, result1.get(0));
        //
        this.dao.add(this.user2);
        List<User511> result2 = this.dao.getAll();
        Assertions.assertEquals(2, result2.size());
        checkSameUser(user1, result2.get(0));
        checkSameUser(user2, result2.get(1));
        //
        this.dao.add(this.user3);
        List<User511> result3 = this.dao.getAll();
        Assertions.assertEquals(3, result3.size());
        checkSameUser(user1, result3.get(0));
        checkSameUser(user2, result3.get(1));
        checkSameUser(user3, result3.get(2));
        //
        this.dao.deleteAll();

    }

    public void checkSameUser(User511 user1, User511 user2) {
        Assertions.assertEquals(user1.getId(), user2.getId());
        Assertions.assertEquals(user1.getName(), user2.getName());
        Assertions.assertEquals(user1.getPassword(), user2.getPassword());
        Assertions.assertEquals(user1.getLevel(), user2.getLevel());
        Assertions.assertEquals(user1.getLogin(), user2.getLogin());
        Assertions.assertEquals(user1.getRecommend(), user2.getRecommend());
    }

    @Test
    public void addDuplicatedKey() {
        //
        this.dao.deleteAll();
        //
        Executable callback = ()-> {
            this.dao.add(this.user1);
            this.dao.add(this.user1);
        };
        //
        Assertions.assertThrows(DuplicateKeyException.class, callback);
    }

    @Test
    public void sqlExceptionTranslate() {
        this.dao.deleteAll();

        try {
            this.dao.add(this.user1);
            this.dao.add(this.user1);
        } catch (DuplicateKeyException ex) {
            SQLException sqlEx = (SQLException)ex.getRootCause();
            SQLExceptionTranslator set = new SQLErrorCodeSQLExceptionTranslator(this.dataSource);
            Assertions.assertInstanceOf(DuplicateKeyException.class, set.translate(null, null, sqlEx));
        }
    }
}


//UserDaoJdbc42.class 보면 스프링에서 예외 전환을 잘 활용하고 있다고 했지.
//그걸 직접 만들어 보는 테스트가 sqlExceptionTranslate()
//SQLExceptionTranslator 를 만들려면 DB 기술 정보를 알고 있어야 하는데, 이때 dataSource 를 넣어줘야 해.
//그래서 dataSource 를 클래스 변수로 선언해 두고 Autowired 로 받아와서 직접 넣어주고 있지.