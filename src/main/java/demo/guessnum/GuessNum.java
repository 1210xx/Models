package demo.guessnum;

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
        //产生100以内的随机数
        int destNum = new Random().nextInt(100);
        System.out.println(destNum);
        //初始化输入数
        int srcNum = -1;
        //开启接收
        try (Scanner scanner = new Scanner(System.in)) {
            srcNum = scanner.nextInt();
            while (destNum != srcNum) {
                if (srcNum < destNum) {
                    System.out.println("Oops, small,please try another");
                    srcNum = scanner.nextInt();
                } else {
                    System.out.println("Oh, big,please try another");
                    srcNum = scanner.nextInt();
                }
            }
        }
        System.out.println("Congratulation!!!");

        return true;
    }
}
