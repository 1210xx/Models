package demo.guessnum;

import org.junit.jupiter.api.Test;

import java.sql.Time;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class DBUtilTest {
    String JDBC_URL = "jdbc:mysql://localhost:3306/guessnum?useSSL=false&characterEncoding=utf8&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    //        String JDBC_USER = "root";
//        String JDBC_PASSWORD = "password";
    String JDBC_USER = "guessnum";
    String JDBC_PASSWORD = "guessnumpassword";
    @org.junit.jupiter.api.Test
    void reconrdState() {
        Date time = new Time(System.currentTimeMillis());
        String stringTime = time.toString();
        int n = new DBUtil(JDBC_URL, JDBC_USER, JDBC_PASSWORD).reconrdState(stringTime,50,50,1);
        assertEquals(1,n);
    }

    @org.junit.jupiter.api.Test
    void printRecord() {
        new DBUtil(JDBC_URL, JDBC_USER, JDBC_PASSWORD).printRecord();
    }


    @Test
    void clearTable() {
        int n = new DBUtil(JDBC_URL, JDBC_USER, JDBC_PASSWORD).clearTable();
        assertEquals(0,n);
    }
}