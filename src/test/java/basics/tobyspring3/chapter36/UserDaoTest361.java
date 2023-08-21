package basics.tobyspring3.chapter36;

import basics.tobyspring2.chapter23.User232;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

public class UserDaoTest361 {
    // 픽스처 선언
    private UserDao361 dao;
    private User232 user1;
    private User232 user2;
    private User232 user3;


    @BeforeEach
    public void setUp() {
        //
        this.dao = new UserDao361();
        DataSource dataSource = new SingleConnectionDataSource("jdbc:mysql://localhost:3306/testdb", "root", "0000", true);
        dao.setDataSource(dataSource);
        //
        this.user1 = new User232("id361-1", "name361-1", "psw361-1");
        this.user2 = new User232("id361-2", "name361-2", "psw361-2");
        this.user3 = new User232("id361-3", "name361-3", "psw361-3");
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
}
