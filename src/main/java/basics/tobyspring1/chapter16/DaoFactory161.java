package basics.tobyspring1.chapter16;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;

@Configuration
public class DaoFactory161 {

    @Bean
    public UserDao161 userDao() throws ClassNotFoundException, SQLException {
        return new UserDao161(connectionMaker());
    }

    @Bean
    public ConnectionMaker161 connectionMaker() {
        return new SimpleConnectionMaker161();
    }
}

