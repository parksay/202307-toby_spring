package basics.tobyspring2.chapter23;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao232 {

    private DataSource dataSource;

    public UserDao232() {
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void add(User232 user) throws SQLException {
        //
        Connection c = dataSource.getConnection();
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

    public User232 get(String id) throws SQLException {
        //
        Connection c = dataSource.getConnection();
        //
        PreparedStatement ps = c.prepareStatement(
                "select * from users where id = ?");
        ps.setString(1, id);
        //
        ResultSet rs = ps.executeQuery();
        rs.next();
        User232 user = new User232();
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

    public int getCount() throws SQLException {
        //
        Connection c = dataSource.getConnection();
        //
        PreparedStatement ps = c.prepareStatement("select count(*) from users");
        //
        ResultSet rs = ps.executeQuery();
        rs.next();
        int cnt = rs.getInt(1);
        //
        rs.close();
        ps.close();
        c.close();
        //
        return cnt;
    }

    public void deleteAll() throws SQLException {
        //
        Connection c = dataSource.getConnection();
        //
        PreparedStatement ps = c.prepareStatement("delete from users");
        //
        ps.execute();
        //
        ps.close();
        c.close();
        //
    }

}

// deleteAll() 에서 ps.executeQuery() 하면 아래 에러 남
// Statement.executeQuery() cannot issue statements that do not produce result sets.
// 무슨 뜻이냐면 ResultSet 을 만들어줄 수가 없대
// 당연하지 delete 하는 거라서 결과가 없는데;
// executeQuery()
//        > ResultSet 반환해 줌
//        > SELECT 문에서 사용
// executeUpdate()
//        > 영향 받은 레코드의 갯수를 int 값으로 반환해 줌
//        > INSERT / CREATE / UPDATE / DELETE / DROP / ALTER 문에서 사용
// execute()
//        > ResultSet 개체가 있으면 true, ResultSet 이 없으면 false
//        > executeQuery 랑 executeUpdate 에 쓰는 쿼리 다 가능
//        > 무쓸모... executeQuery 나 executeUpdate 가 상위 호환


