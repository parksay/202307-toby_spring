package basics.tobyspring1.chapter11;


import java.sql.SQLException;

public class HelloSpringMain113 {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		//
		UserDao112 dao = new UserDao112();
		//
		User111 user = new User111();
		user.setId("test113");
		user.setName("테스트113");
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
