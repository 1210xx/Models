package demo.guessnum;

import javafx.beans.binding.When;

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
    public String JDBC_URL = "jdbc:mysql://localhost:3306/guessnum?useSSL=false&characterEncoding=utf8&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    //        String JDBC_USER = "root";
//        String JDBC_PASSWORD = "password";
    public String JDBC_USER = "guessnum";
    public String JDBC_PASSWORD = "guessnumpassword";

    //结果标记
    final static int WRONG = 0;
    final static int RIGHT = 1;

    DBUtilPool dbUtilPool = new DBUtilPool(this.JDBC_URL, this.JDBC_USER, this.JDBC_PASSWORD);

    public static void main(String[] args) {
//        DBUtil dbUtil = new DBUtil(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
//        dbUtil.clearTable();
//        DBUtilPool dbUtilPool = new DBUtilPool(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
//        dbUtilPool.clearTable();
        //测试对内app
//        GuessNum guessNum = new GuessNum();
//        guessNum.guessNumApp();
        //测试对内appre
        GuessNum guessNum = new GuessNum();
        guessNum.guessNumAppRe();
        //测试对外API
//        Scanner scanner = new Scanner(System.in);
//        GuessNum guessNum = new GuessNum();
//        int targetNum = new Random().nextInt(100);
//        dbUtilPool.clearTable();
//        while (scanner.hasNext()){
//            int inputNum = scanner.nextInt();
//            if (guessNum.guessNumApp(inputNum,targetNum))
//                return;
//        }
    }

    /**
     * 控制台开启的完整程序
     *
     * @return 程序执行结果
     */
    public boolean guessNumApp(){
        //清楚之前的数据库信息
        dbUtilPool.clearTable();
        //产生100以内的随机数
        int destNum = new Random().nextInt(100);
        //System.out.println(destNum);
        //初始化输入数
        int srcNum = -1;
        //结果标志
        int resultFlag = -1;
        //数据库工具
//        DBUtil dbUtil = new DBUtil(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
        //数据连接池
//        DBUtilPool dbUtilPool = new DBUtilPool(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
        //开启接收
        try (Scanner scanner = new Scanner(System.in)) {
            srcNum = scanner.nextInt();
            while (destNum != srcNum) {
                resultFlag = WRONG;
                if (srcNum < destNum) {
                    Date time = new Time(System.currentTimeMillis());
                    String stringTime = time.toString();
//                    dbUtil.reconrdState(stringTime,srcNum, destNum, resultFlag);
                    dbUtilPool.reconrdState(stringTime,srcNum, destNum, resultFlag);
                    System.out.println("Oops, small,please try another");
                    System.out.println("有点小，重试一下呢");
                    srcNum = scanner.nextInt();
                } else {
                    Date time = new Time(System.currentTimeMillis());
                    String stringTime = time.toString();
//                    dbUtil.reconrdState(stringTime, srcNum, destNum, resultFlag);
                    dbUtilPool.reconrdState(stringTime, srcNum, destNum, resultFlag);
                    System.out.println("Oh, big,please try another");
                    System.out.println("大了，小点试试？？？");
                    srcNum = scanner.nextInt();
                }
            }
        }
        resultFlag = RIGHT;
        Date time = new Time(System.currentTimeMillis());
        String stringTime = time.toString();
//        dbUtil.reconrdState(stringTime, srcNum, destNum, resultFlag);
        dbUtilPool.reconrdState(stringTime, srcNum, destNum, resultFlag);
        System.out.println("!!!Congratulation!!!");
        System.out.println("！！！！恭喜！！！！");
//        dbUtil.printRecord();
        System.out.println("\n");
        System.out.println("---------------RESULT----------------");
        System.out.println("----------------结果----------------");
        dbUtilPool.printRecord();
        return true;
    }

    public boolean guessNumAppRe(){
        dbUtilPool.clearTable();
        int destNum = new Random().nextInt(100);
        int inputNum = -1;
        GuessNum guessNum = new GuessNum();
        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNext()){
                inputNum = scanner.nextInt();
                if (guessNum.guessNumApp(inputNum,destNum))
                    return true;
            }
        }
        return false;
    }

    /**
     * 对外API
     * @param inputNum 输入数
     * @param targetNum 目标数
     * @return 程序结果
     */
    public boolean guessNumApp(int inputNum, int targetNum){
//        dbUtilPool.clearTable();
        //产生100以内的随机数
//        int destNum = new Random().nextInt(100);
        int destNum = targetNum;
        //System.out.println(destNum);
        //初始化输入数
//        int srcNum = -1;
        int srcNum = inputNum;
        //结果标志
        int resultFlag = -1;
        //数据库工具
//        DBUtil dbUtil = new DBUtil(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
        //数据连接池
//        DBUtilPool dbUtilPool = new DBUtilPool(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
        //开启接收
//        try (Scanner scanner = new Scanner(System.in)) {
//            srcNum = scanner.nextInt();
            while (destNum != srcNum) {
                resultFlag = WRONG;
                if (srcNum < destNum) {
                    Date time = new Time(System.currentTimeMillis());
                    String stringTime = time.toString();
//                    dbUtil.reconrdState(stringTime,srcNum, destNum, resultFlag);
                    dbUtilPool.reconrdState(stringTime,srcNum, destNum, resultFlag);
                    System.out.println("Oops, small,please try another");
                    System.out.println("有点小，重试一下呢");
//                    srcNum = scanner.nextInt();
                    return false;
                } else {
                    Date time = new Time(System.currentTimeMillis());
                    String stringTime = time.toString();
//                    dbUtil.reconrdState(stringTime, srcNum, destNum, resultFlag);
                    dbUtilPool.reconrdState(stringTime, srcNum, destNum, resultFlag);
                    System.out.println("Oh, big,please try another");
                    System.out.println("大了，小点试试？？？");
//                    srcNum = scanner.nextInt();
                    return false;
                }
            }
//        }
        resultFlag = RIGHT;
        Date time = new Time(System.currentTimeMillis());
        String stringTime = time.toString();
//        dbUtil.reconrdState(stringTime, srcNum, destNum, resultFlag);
        dbUtilPool.reconrdState(stringTime, srcNum, destNum, resultFlag);
        System.out.println("!!!Congratulation!!!");
        System.out.println("！！！！恭喜！！！！");
//        dbUtil.printRecord();
        System.out.println("\n");
        System.out.println("---------------RESULT----------------");
        System.out.println("----------------结果----------------");
        dbUtilPool.printRecord();
        return true;
    }
}
