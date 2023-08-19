package basics.tobyspring3.chapter32;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;

public class UserDaoTest3222 {

    private UserDao3222 dao;

    @BeforeEach
    public void setUp() {
        //
        this.dao = new UserDaoDeleteAll3222();
        DataSource dataSource = new SingleConnectionDataSource("jdbc:mysql://localhost:3306/testdb", "root", "0000", true);
        this.dao.setDataSource(dataSource);
    }

    @Test
    public void deleteAll() throws SQLException {
        this.dao.deleteAll();
    }
}
