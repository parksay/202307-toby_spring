package basics.tobyspring3.chapter35;

import basics.tobyspring2.chapter23.User232;
import basics.tobyspring3.chapter33.StatementStrategy331;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDao352 {

    private DataSource dataSource;
    private JdbcContext352 jdbcContext;

    public UserDao352() {
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcContext = new JdbcContext352();
        this.jdbcContext.setDataSource(this.dataSource);
    }

    public void add(final User232 user) throws SQLException {
        this.jdbcContext.executeSql("insert into users(id, name, password) values (?, ?, ?)");
    }

    public void deleteAll() throws SQLException {
        this.jdbcContext.executeSql("delete from users");
    }



}



// p.240 - chapter 3.5.1
//와.. 진짜 깔끔해졌다.
//이제 UserDao / BookDao / ChairDao / DeskDao / ...
//막 만들어도 진짜 편하게 갖다 쓰겠다.


