package basics.tobyspring1.chapter12;


import java.sql.SQLException;

public class HelloSpringMain123 {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		// chapter 1.2.2 test
		UserDao122 dao = new UserDao122();
		//
		User111 user = new User111();
		user.setId("test122");
		user.setName("테스트122");
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

		// chapter 1.2.3 test
		NaverUserDao123 dao2 = new NaverUserDao123();
		// DaumUserDao123 dao2 = new DaumUserDao123();
		//
		User111 user3 = new User111();
		user3.setId("test123");
		user3.setName("테스트123");
		user3.setPassword("myTest");
		//
		dao2.add(user3);
		//
		System.out.println(user3.getId() + "등록 성공");
		//
		User111 user4 = dao2.get(user3.getId());
		//
		System.out.println(user4.getName());
		System.out.println(user4.getPassword());
		System.out.println(user4.getId() + "조회 성공");
	}

}
