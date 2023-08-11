package basics.tobyspring1.chapter18;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import java.sql.SQLException;

public class UserDaoTest184 {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        // 오브젝트 생성
        ApplicationContext context = new GenericXmlApplicationContext("chapter18/applicationContext184.xml");
        UserDao184 dao = context.getBean("userDao", UserDao184.class);
        User111 user = new User111();
        user.setId("test184");
        user.setName("테스트184");
        user.setPassword("myTest");
        //
        dao.add(user);
        //
        System.out.println(user.getId() + "등록 성공");
        //
        User111 user2 = dao.get(user.getId());
        //
        System.out.println(user2.getName());
        System.out.println(user2.getPassword());
        System.out.println(user2.getId() + "조회 성공");


    }
}

