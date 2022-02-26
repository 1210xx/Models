package demo.guessnum;

import java.sql.*;

/**
 * 1 使用数据库统计（新建DButil类）
 * - 每次猜测的时间
 * - 猜测的数字
 * - 猜测的结果
 *
 * 2 采用JDBC访问数据库时，要求使用PreparedStatement并使用占位符方式绑定业务数据
 * 3 当用户猜中数字后，除了提示恭喜之外，还要输出猜数的历史记录（按时间倒序输出）
 */
public class DBUtil {

    private String url;
    private String user;
    private String password;

    public DBUtil() {
    }

    public DBUtil(String url, String use, String password) {
        this.url = url;
        this.user = use;
        this.password = password;
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
        try(Connection connection = DriverManager.getConnection(this.url, this.user, this.password)) {
            try(PreparedStatement ps = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){
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
        try(Connection connection = DriverManager.getConnection(this.url, this.user, this.password)) {

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
