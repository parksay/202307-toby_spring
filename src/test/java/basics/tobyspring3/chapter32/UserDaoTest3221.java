package basics.tobyspring3.chapter32;

import basics.tobyspring2.chapter23.User232;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;

public class UserDaoTest3221 {

    private UserDao3221 dao;
    private User232 user1;
    private User232 user2;
    private User232 user3;

    @BeforeEach
    public void setUp() {
        //
        this.dao = new UserDao3221();
        DataSource dataSource = new SingleConnectionDataSource("jdbc:mysql://localhost:3306/testdb", "root", "0000", true);
        this.dao.setDataSource(dataSource);
        //
        this.user1 = new User232("id3221-1", "name3221-1", "psw3221-1");
        this.user2 = new User232("id3221-2", "name3221-2", "psw3221-2");
        this.user3 = new User232("id3221-3", "name3221-3", "psw3221-3");
    }

    @Test
    public void add() throws SQLException {
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
