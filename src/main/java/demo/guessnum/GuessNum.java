package demo.guessnum;

import java.sql.Time;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

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
    static String JDBC_URL = "jdbc:mysql://localhost:3306/guessnum?useSSL=false&characterEncoding=utf8&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    //        String JDBC_USER = "root";
//        String JDBC_PASSWORD = "password";
    static String JDBC_USER = "guessnum";
    static String JDBC_PASSWORD = "guessnumpassword";

    //结果标记
    final static int WRONG = 0;
    final static int RIGHT = 1;

    public static void main(String[] args) {
        DBUtil dbUtil = new DBUtil(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
        dbUtil.clearTable();
        GuessNum guessNum = new GuessNum();
        guessNum.guessNumApp();
    }

    public boolean guessNumApp(){
        //产生100以内的随机数
        int destNum = new Random().nextInt(100);
        //System.out.println(destNum);
        //初始化输入数
        int srcNum = -1;
        //结果标志
        int resultFlag = -1;
        //数据库工具
        DBUtil dbUtil = new DBUtil(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
        //开启接收
        try (Scanner scanner = new Scanner(System.in)) {
            srcNum = scanner.nextInt();
            while (destNum != srcNum) {
                resultFlag = WRONG;
                if (srcNum < destNum) {
                    Date time = new Time(System.currentTimeMillis());
                    String stringTime = time.toString();
                    dbUtil.reconrdState(stringTime,srcNum, destNum, resultFlag);
                    System.out.println("Oops, small,please try another");
                    System.out.println("有点小，重试一下呢");
                    srcNum = scanner.nextInt();
                } else {
                    Date time = new Time(System.currentTimeMillis());
                    String stringTime = time.toString();
                    dbUtil.reconrdState(stringTime, srcNum, destNum, resultFlag);
                    System.out.println("Oh, big,please try another");
                    System.out.println("大了，网小点试试？？？");
                    srcNum = scanner.nextInt();
                }
            }
        }
        resultFlag = RIGHT;
        Date time = new Time(System.currentTimeMillis());
        String stringTime = time.toString();
        dbUtil.reconrdState(stringTime, srcNum, destNum, resultFlag);
        System.out.println("\r\n");
        System.out.println("Congratulation!!!");
        System.out.println("！！！！恭喜！！！！");
        dbUtil.printRecord();
        return true;
    }
}
