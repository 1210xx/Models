package demo.guessnum;

import static org.junit.jupiter.api.Assertions.*;

class DBUtilTest {

    @org.junit.jupiter.api.Test
    void reconrdState() {
        String JDBC_URL = "jdbc:mysql://localhost:3306/guessnum?useSSL=false&characterEncoding=utf8&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        //        String JDBC_USER = "root";
//        String JDBC_PASSWORD = "password";
        String JDBC_USER = "guessnum";
        String JDBC_PASSWORD = "guessnumpassword";
        int n = new DBUtil(JDBC_URL, JDBC_USER, JDBC_PASSWORD).reconrdState("202202261435",50,50,1);
        assertEquals(1,n);
    }

    @org.junit.jupiter.api.Test
    void printRecord() {
    }
}