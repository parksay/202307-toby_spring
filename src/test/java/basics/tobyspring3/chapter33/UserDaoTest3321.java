package basics.tobyspring3.chapter33;

import basics.tobyspring2.chapter23.User232;
import basics.tobyspring3.chapter32.UserDao3223;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;

public class UserDaoTest3321 {

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
        this.user1 = new User232("id332-1", "name332-1", "psw332-1");
        this.user2 = new User232("id332-2", "name332-2", "psw332-2");
        this.user3 = new User232("id332-3", "name332-3", "psw332-3");
    }

    @Test
    public void add() throws SQLException {
        // deleteAll() /
        this.dao.deleteAll();

        // add()  테스트 > add
        this.dao.add(this.user1);
        this.dao.add(this.user2);
        this.dao.add(this.user3);

        // deleteAll() /
        this.dao.deleteAll();
    }

}
