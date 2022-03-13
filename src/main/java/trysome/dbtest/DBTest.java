package trysome.dbtest;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.*;

public class DBTest {
    public static void main(String[] args) throws SQLException {
        // JDBC连接的URL, 不同数据库有不同的格式:
        String JDBC_URL = "jdbc:mysql://localhost:3306/learnjdbc?useSSL=false&characterEncoding=utf8&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        String JDBC_USER = "root";
        String JDBC_PASSWORD = "password";

        jdbcConnPool(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
//        jdbcPreparedStatement(JDBC_URL, JDBC_USER, JDBC_PASSWORD);

    }
    public static void jdbcConnPool(String JDBC_URL, String JDBC_USER, String JDBC_PASSWORD){
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(JDBC_URL);
        config.setUsername(JDBC_USER);
        config.setPassword(JDBC_PASSWORD);
        config.addDataSourceProperty("connectionTimeout", "1000"); // 连接超时：1秒
        config.addDataSourceProperty("idleTimeout", "60000"); // 空闲超时：60秒
        config.addDataSourceProperty("maximumPoolSize", "10"); // 最大连接数：10
        DataSource ds = new HikariDataSource(config);

        try (Connection conn = ds.getConnection()) { // 在此获取连接
            try (PreparedStatement ps = conn.prepareStatement("SELECT id, grade, name, gender FROM students WHERE gender=? AND grade=?")) {
                ps.setObject(1, 0); // 注意：索引从1开始
                ps.setObject(2, 3);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        long id = rs.getLong("id");
                        long grade = rs.getLong("grade");
                        String name = rs.getString("name");
                        String gender = rs.getString("gender");
                        System.out.println("id: " + id + " grade: " + grade +" name: "+ name +" gender: "+ gender);
                    }
                }
            }
            conn.close();
        } // 在此“关闭”连接
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void jdbcPreparedStatement(String JDBC_URL, String JDBC_USER, String JDBC_PASSWORD){
        //使用PreparedStatement。使用PreparedStatement可以完全避免SQL注入的问题，因为PreparedStatement始终使用?作为占位符，
        // 并且把数据连同SQL本身传给数据库，这样可以保证每次传给数据库的SQL语句是相同的，只是占位符的数据不同，
        // 还能高效利用数据库本身对查询的缓存。
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            //try（resource） 自动关闭打开资源，需要创建的对象实现了autocloseable接口
            try (PreparedStatement ps = conn.prepareStatement("SELECT id, grade, name, gender FROM students WHERE gender=? AND grade=?")) {
                ps.setObject(1, 0); // 注意：索引从1开始
                ps.setObject(2, 3);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        long id = rs.getLong("id");
                        long grade = rs.getLong("grade");
                        String name = rs.getString("name");
                        String gender = rs.getString("gender");
                        System.out.println("id: " + id + " grade: " + grade +" name: "+ name +" gender: "+ gender);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    
    public static void jdbcStatementTest(String JDBC_URL, String JDBC_USER, String JDBC_PASSWORD){

// 获取连接:
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            //创建statement执行sql,statement 容易sql注入.statement 从参数接收数据。
            try (Statement stmt = conn.createStatement()) {
                try (ResultSet rs = stmt.executeQuery("SELECT id, grade, name, gender FROM students WHERE gender=1")) {
                    while (rs.next()) {
                        long id = rs.getLong(1); // 注意：索引从1开始
                        long grade = rs.getLong(2);
                        String name = rs.getString(3);
                        int gender = rs.getInt(4);
                        System.out.println("id: " + id + " grade: " + grade +" name: "+ name +" gender: "+ gender);
                    }
                }catch (SQLException e){
                    e.getErrorCode();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }

    }
}
