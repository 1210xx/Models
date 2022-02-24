package standalonedemo;

import java.util.Random;
import java.util.Scanner;

/**
 * 简单的单体应用
 * 只是实现了猜数过程的控制台程序
 */
public class Mode001 {
    public static void main(String[] args) {
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
    }
}
