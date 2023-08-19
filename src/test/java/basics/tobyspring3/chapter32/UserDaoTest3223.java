package basics.tobyspring3.chapter32;

import basics.tobyspring2.chapter23.User232;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;

public class UserDaoTest3223 {

    private UserDao3223 dao;
    private User232 user1;
    private User232 user2;
    private User232 user3;

    @BeforeEach
    public void setUp() {
        //
        this.dao = new UserDao3223();
        DataSource dataSource = new SingleConnectionDataSource("jdbc:mysql://localhost:3306/testdb", "root", "0000", true);
        this.dao.setDataSource(dataSource);
        //
        this.user1 = new User232("id3223-1", "name3223-1", "psw3223-1");
        this.user2 = new User232("id3223-2", "name3223-2", "psw3223-2");
        this.user3 = new User232("id3223-3", "name3223-3", "psw3223-3");
    }

    @Test
    public void add() throws SQLException {
        // deleteAll() /
        this.dao.deleteAll();

        // add()  테스트 > add
        this.dao.add(this.user1);
        this.dao.add(this.user2);

        // deleteAll() /
        this.dao.deleteAll();
    }

}
