package basics.tobyspring1.chapter18;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import java.sql.SQLException;

public class UserDaoTest183 {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        // 오브젝트 생성
         ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory183.class);
        UserDao183 dao = context.getBean("userDao", UserDao183.class);
        User111 user = new User111();
        user.setId("test183");
        user.setName("테스트183");
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

//p.136
//지금까지 SimpleConnectionMaker 를 우리가 직접 만들어서 썼었지
//그런데 사실 자바에서 기본적으로 제공해주는 인터페이스가 있어
//DataSource 라는 인터페이스야
//그렇다고 해서 뭐가 엄청 대단한 것도 아니고 그냥 우리가 만든 거랑 똑같아
//Connection 만들어 주고 DB 접근하는 기능일 뿐이지
//다만 내부가 복잡해서 우리가 직접 이 인터페이스를 구현해서 쓸 일은 별로 없을 거야
//우리가 만들었던 SimpleConnectionMaker 는 JDBC 를 이용해서 간단하게 만들었었지
//그것처럼 DataSource 인터페이스 구현 클래스 중에서도 JDBC 를 이용하는 간단한 구현 클래스가 있어.
//SimpleDriverDataSource
//이걸 사용하는 방식으로 바꿔보자.
//두 가지 버전으로 만들 거야.
//하나는 AnnotationConfigApplicationContext 로 구현한 ApplicationContext 로,
//다른 하나는 GenericXmlApplicationContext 로 구현한 ApplicationContext 로 만들 거야.
//그러니까, 하나는 자바 클래스를 설정 파일로 만들 거고, 다른 하나는 xml 파일을 설정 파일로 만들 거야.
//UserDaoTest183.class / UserDao183.class / DaoFactory183.class / 는 annotation 버전
//UserDaoTest184.class / UserDao184.class / applicationContext184.xml / 은 xml 버전


