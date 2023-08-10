package basics.tobyspring1.chapter18;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
public class DaoFactory183 {

    @Bean
    public UserDao183 userDao() throws SQLException {
        return new UserDao183(dataSource());
    }

    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(com.mysql.cj.jdbc.Driver.class);
        dataSource.setUrl("jdbc:mysql://localhost:3306/springbook");
        dataSource.setUsername("root");
        dataSource.setPassword("0000");
        return dataSource;
    }

}

//위에서 값을 세팅하는 코드 중에 드라이버 클래스를 지정하는 부분에서 조금 애를 먹었다.
//dataSource.setDriverClass(com.mysql.cj.jdbc.Driver.class);


// implementation 'org.springframework.boot:spring-boot-starter-jdbc'
// implementation 'com.mysql:mysql-connector-j'
// runtimeOnly 'com.mysql:mysql-connector-j'
