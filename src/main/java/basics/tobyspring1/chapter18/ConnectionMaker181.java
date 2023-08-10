package basics.tobyspring1.chapter18;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionMaker181 {

    // DB 연결 받아오기 기능
    public abstract Connection makeConnection() throws ClassNotFoundException, SQLException;
    // interface 에서는 public abstract 선언자 생략 가능.

}