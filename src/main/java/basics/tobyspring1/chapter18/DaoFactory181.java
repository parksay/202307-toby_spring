package basics.tobyspring1.chapter18;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;

@Configuration
public class DaoFactory181 {

    @Bean
    public UserDao181 userDao() throws ClassNotFoundException, SQLException {
        return new UserDao181(connectionMaker());
    }

    @Bean
    public ConnectionMaker181 connectionMaker() {
        return new SimpleConnectionMaker181();
    }

}

