package basics.tobyspring1.chapter13;


import java.sql.SQLException;

public class HelloSpringMain131 {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		// chapter 1.3.1 test
		UserDao131 dao = new UserDao131();
		//
		User111 user = new User111();
		user.setId("test131");
		user.setName("테스트131");
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
		//======================================================================//
		//
		// chapter 1.3.2 test
		UserDao132 dao132 = new UserDao132();
		//
		User111 user3 = new User111();
		user3.setId("test132");
		user3.setName("테스트132");
		user3.setPassword("myTest");
		//
		dao132.add(user3);
		//
		System.out.println(user3.getId() + "등록 성공");
		//
		User111 user4 = dao132.get(user3.getId());
		//
		System.out.println(user4.getName());
		System.out.println(user4.getPassword());
		System.out.println(user4.getId() + "조회 성공");
	}

}
