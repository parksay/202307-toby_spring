package basics.tobyspring4;

import basics.tobyspring2.chapter23.User232;
import basics.tobyspring4.chapter42.UserDao42;
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
@ContextConfiguration(locations = {"file:src/main/resources/chapter42/test-applicationContext42.xml"})
public class UserDaoTest42 {
    // 픽스처 선언
    @Autowired
    private UserDao42 dao;
    @Autowired
    private DataSource dataSource;
    private User232 user1;
    private User232 user2;
    private User232 user3;

    @BeforeEach
    public void setUp() {
        //
//        this.dao = new UserDaoJdbc42();
//        DataSource dataSource = new SingleConnectionDataSource("jdbc:mysql://localhost:3306/testdb", "root", "0000", true);
//        dao.setDataSource(dataSource);
        //
        this.user1 = new User232("id42-1", "name42-1", "psw42-1");
        this.user2 = new User232("id42-2", "name42-2", "psw42-2");
        this.user3 = new User232("id42-3", "name42-3", "psw42-3");
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

    @Test
    public void getAllTest() throws SQLException {
        this.dao.deleteAll();
        //
        this.dao.add(this.user1);
        List<User232> result1 = this.dao.getAll();
        Assertions.assertEquals(1, result1.size());
        checkSameUser(user1, result1.get(0));
        //
        this.dao.add(this.user2);
        List<User232> result2 = this.dao.getAll();
        Assertions.assertEquals(2, result2.size());
        checkSameUser(user1, result2.get(0));
        checkSameUser(user2, result2.get(1));
        //
        this.dao.add(this.user3);
        List<User232> result3 = this.dao.getAll();
        Assertions.assertEquals(3, result3.size());
        checkSameUser(user1, result3.get(0));
        checkSameUser(user2, result3.get(1));
        checkSameUser(user3, result3.get(2));
        //
        this.dao.deleteAll();

    }

    public void checkSameUser(User232 user1, User232 user2) {
        Assertions.assertEquals(user1.getId(), user2.getId());
        Assertions.assertEquals(user1.getName(), user2.getName());
        Assertions.assertEquals(user1.getPassword(), user2.getPassword());
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