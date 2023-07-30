package basics.tobyspring1.chapter13;

import java.sql.SQLException;

public class UserDaoTest133 {
    public static void main(String args[]) throws ClassNotFoundException, SQLException {
        //
        ConnectionMaker132 connectionMaker = new NaverConnectionMaker132();
        // ConnectionMaker132 connectionMaker = new DaumConnectionMaker132();
        //
        UserDao133 dao = new UserDao133(connectionMaker);
        //
        User111 user = new User111();
        user.setId("test133");
        user.setName("테스트133");
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
