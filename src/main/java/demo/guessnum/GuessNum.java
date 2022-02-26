package demo.guessnum;

/**
 * 1 使用数据库统计（新建DButil类）
 * - 每次猜测的时间
 * - 猜测的数字
 * - 猜测的结果
 *
 * 2 采用JDBC访问数据库时，要求使用PreparedStatement并使用占位符方式绑定业务数据
 * 3 当用户猜中数字后，除了提示恭喜之外，还要输出猜数的历史记录（按时间倒序输出）
 */
public class GuessNum {
    String JDBC_URL = "jdbc:mysql://localhost:3306/guessnum?useSSL=false&characterEncoding=utf8&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    //        String JDBC_USER = "root";
//        String JDBC_PASSWORD = "password";
    String JDBC_USER = "guessnum";
    String JDBC_PASSWORD = "guessnumpassword";

    public static void main(String[] args) {
        GuessNum guessNum = new GuessNum();
        guessNum.guessNumApp();
    }

    public boolean guessNumApp(){

        return true;
    }
}
