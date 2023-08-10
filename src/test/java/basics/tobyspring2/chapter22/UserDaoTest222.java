package basics.tobyspring2.chapter22;

import basics.tobyspring1.chapter18.User111;
import basics.tobyspring1.chapter18.UserDao184;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import java.sql.SQLException;

public class UserDaoTest222 {
    public static void main(String[] args) {

    }

    @Test
    public void addAndGet() throws SQLException {

        // 오브젝트 생성
        ApplicationContext context = new GenericXmlApplicationContext("applicationContext184.xml");
        UserDao184 dao = context.getBean("userDao", UserDao184.class);
        User111 user = new User111();
        user.setId("test184");
        user.setName("테스트184");
        user.setPassword("myTest");
        //
        dao.add(user);
        //
        User111 user2 = dao.get(user.getId());
        //


    }

}
