package demo.guessnum;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.*;

/**
 * 使用数据库连接池创建连接
 *
 * datasource是javax.sql.DataSource标准库的接口。
 * 要使用JDBC连接池，我们必须选择一个JDBC连接池的实现。常用的JDBC连接池有：
 * HikariCP
 * C3P0
 * BoneCP
 * Druid
 *
 * 首先创建datasource是一个非常昂贵的资源。
 *
 */
public class DBUtilPool {

    private String url;
    private String user;
    private String password;

    HikariConfig config = new HikariConfig();
    DataSource ds = null;

    public DBUtilPool() {
    }

    public DBUtilPool(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
        config.setJdbcUrl(this.url);
        config.setUsername(this.user);
        config.setPassword(this.password);
        config.addDataSourceProperty("connectionTimeout", "1000"); // 连接超时：1秒
        config.addDataSourceProperty("idleTimeout", "60000"); // 空闲超时：60秒
        config.addDataSourceProperty("maximumPoolSize", "10"); // 最大连接数：10
        ds = new HikariDataSource(config);
    }

    /**
     * - 每次猜测的时间
     * - 猜测的数字
     * - 猜测的结果
     * @param time
     */
    public int reconrdState(String time, int inputNum, int targetNum, int result){
        int numrecord = -1;
        String sql = "INSERT INTO guessnumtable (time, inputNum, targetNum, result) VALUES (?, ?, ?, ?)";
        try(Connection connection = ds.getConnection()) {
            try(PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
                ps.setObject(1,time);
                ps.setObject(2,inputNum);
                ps.setObject(3,targetNum);
                ps.setObject(4,result);
                numrecord = ps.executeUpdate();
                try (ResultSet resultSet = ps.getGeneratedKeys()){
                    if (resultSet.next()){
                        long id = resultSet.getLong(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return numrecord;
    }

    /**
     * 输出猜数的历史记录（按时间倒序输出）
     */
    public void printRecord(){
        try(Connection connection = ds.getConnection()) {
            String sql = "SELECT * FROM guessnumtable ORDER BY time DESC";
            try(PreparedStatement ps = connection.prepareStatement(sql);) {
                try(ResultSet rs = ps.executeQuery()) {
                    System.out.println("    时间    |   输入数   |    目标数  |   结果  ");
                    while (rs.next()){
                        String time = rs.getString(2);
                        int inputNum = rs.getInt(3);
                        int targetNum = rs.getInt(4);
                        int result = rs.getInt(5);

                        System.out.println(" "+time + "        " + inputNum + "         " + targetNum + "         " + result );
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int clearTable(){
        int numChanged = -1;
        try(Connection connection = ds.getConnection()) {
            String sql = "TRUNCATE TABLE guessnumtable";
            try(PreparedStatement ps = connection.prepareStatement(sql)) {
                numChanged = ps.executeUpdate();
                System.out.println("清空表   执行    " + numChanged);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return numChanged;
    }


}
