package basics.tobyspring3.chapter34;

import basics.tobyspring2.chapter23.User232;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import java.sql.SQLException;

public class UserDaoTest342 {

    private UserDao342 dao;
    private User232 user1;
    private User232 user2;
    private User232 user3;

    @BeforeAll
    public static void setUpAll() {

    }

    @BeforeEach
    public void setUp() {
        //
        ApplicationContext context = new GenericXmlApplicationContext("chapter34/test-applicationContext342.xml");
        this.dao = context.getBean("userDao", UserDao342.class);
        //
        this.user1 = new User232("id342-1", "name342-1", "psw342-1");
        this.user2 = new User232("id342-2", "name342-2", "psw342-2");
        this.user3 = new User232("id342-3", "name342-3", "psw342-3");
    }

    @Test
    public void add() throws SQLException {
        // deleteAll() 테스트
        this.dao.deleteAll();

        // add()  테스트 > add
        this.dao.add(this.user1);
        this.dao.add(this.user2);
        this.dao.add(this.user3);

        // deleteAll() 테스트
        this.dao.deleteAll();
    }

}

