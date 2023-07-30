package basics.tobyspring1.chapter12;

import java.sql.*;

public abstract class UserDao123 {

    public void add(User111 user) throws ClassNotFoundException, SQLException {
        //
        Connection c = getConnection();
        //
        PreparedStatement ps = c.prepareStatement(
                "insert into users(id, name, password) values (?, ?, ?)");
        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());
        //
        ps.executeUpdate();
        //
        ps.close();
        c.close();

    }

    public User111 get(String id) throws ClassNotFoundException, SQLException {
        //
        Connection c = getConnection();
        //
        PreparedStatement ps = c.prepareStatement(
                "select * from users where id = ?");
        ps.setString(1, id);
        //
        ResultSet rs = ps.executeQuery();
        rs.next();
        User111 user = new User111();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));
        //
        rs.close();
        ps.close();
        c.close();
        //
        return user;
    }

    public abstract Connection getConnection() throws ClassNotFoundException, SQLException;

}


// p.67
//자 이렇게 해서 UserDao112.java 에서 제기했던 문제는 해결됐어.
//고객사(네이버나 다음)가 우리 소스코드를 가져다 쓸 수 있도록 하면서,
//네이버나 다음이 각자 자기네 기술로 만든 getConnection() 을 구현할 수 있도록 열어줬어.
//그러면서도 우리 기술이 담긴 소스코드는 노출하지 않을 수 있지.
//이런 틀은 자주 쓰는 방식이라서 '템플릿 메소드 패턴' 이라고 이름 붙여서 사용해.
//'템플릿 메소드 패턴'이란 뭐냐?
//슈퍼클래스가 있고, 그 슈퍼클래스를 상속 받아서 구현하는 서브클래스가 있어.
//슈퍼클래스에서는 기본적인 로직의 흐름, 큰 틀은 미리 짜놓는 거야.
//그리고 그중에서 일부분은 추상 메소드로 만들어 두거나 override 할 수 있도록 protected 로 만들어 두는 거야.
//그럼 그 슈퍼클래스를 상속받은 서브클래스에서는 이 메소드들을 자기네 필요에 맞게 구현해서 쓰는 거지.
//우리가 방금 만든 UserDao123.java 에는 '팩토리 메소드 패턴'도 들어가 있어.
//'팩토리 메소드 패턴'은 뭐냐?
//슈퍼클래스가 있고, 그 슈퍼클래스를 상속받은 서브클래스가 있는 경우에 대해 이야기하고 있었지.
//이때 서브클래스는 추상 메소드를 자기네가 직접 구현해서 써야 하는데, 이 메소드가 어떤 특정한 기능을 할 때 '팩토리 메소드 패턴'이라고 말해.
//무슨 기능을 하는 메소드냐?
//오브젝트를 만들어주는 메소드야.
//구체적인 오브젝트를 어떤 기술을 써서 만들지, 어떤 로직으로, 어떤 흐름으로, 어떤 정보로 만들지 결정해야 하지.
//이렇게 슈퍼클래스가 서브클래스한테 오브젝트를 어떻게 생성할지 알아서 정하라고, 메소드를 떠넘기는 패턴을 '팩토리 메소드 패턴'이라고 해.
////
//이렇게 끝나면 좋은데 아직도 문제가 남았어.
//아까 UserDao112.java 에서 제기했던 문제는 abstract 추상 클래스 추상 메소드 기술로 해결했지.
//간단하게 템플릿 메소드 패턴과 팩토리 메소드 패턴이라고 했어.
//그런데 가장 큰 문제는 바로 상속을 이용한다는 점이야.
//왜 이게 문제가 되냐?
//1. 자바에서는 추상 클래스를 하나밖에 상속받지 못해. (extends)
//NaverDao123 이 이미 UserDao123 을 상속받아버렸는데, 다른 class 도 상속받아야 한다면..?
//2. 슈퍼클래스와 서브클래스가 생각보다 밀접한 관계를 가진다는 점이야.
//서브클래스는 슈퍼클래스의 기능을 그대로 가져다가 사용할 수 있어.
//그런데 우리 기술에 변경이 일어나서 UserDao123.java 에 수정 사항이 일어났다면?
//UserDao123 을 상속해서 쓰는 NaverDao123.java 도 수정해야 하고 DaumDao123.java 도 수정해야 하고 .... 너무 의존적이지.
//3. 다른 Dao 를 만든다면 가져다 쓸 수가 없어.
//NaverDao123.java 에서 getConnection() 을 구현했다 쳐.
//그런데 NaverMailDao / NaverBlogDao / NaverNewsDao... 계속 만들어야 한다면...?
//우리가 만든 UserDao123 을 계속 상속받아야 하고 계속 getConnection 을 구현해야겠지.
//중복이 일어날 거야.
////
//자 그럼 이건 어떻게 해결해야 할까?
// UserDao131.java 로 가보자.