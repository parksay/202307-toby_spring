package basics.tobyspring1.chapter14;

import java.sql.SQLException;

public class UserDaoTest141 {
    public static void main(String args[]) throws ClassNotFoundException, SQLException {
        //
        UserDao141 dao = new DaoFactory141().userDao();
        //
        User111 user = new User111();
        user.setId("test141");
        user.setName("테스트141");
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
