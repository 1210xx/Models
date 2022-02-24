package standalonedemo;

import java.util.Random;
import java.util.Scanner;

public class Mode001 {
    public static void main(String[] args) {
        int destNum = new Random().nextInt(50);
        System.out.println(destNum);
        int srcNum = -1;
        Scanner scanner = new Scanner(System.in);
        srcNum = scanner.nextInt();
        while (destNum != srcNum){
            if (srcNum < destNum){
                System.out.println("Oops, small,please try another");
                srcNum = scanner.nextInt();
            }else{
                System.out.println("Oh, big,please try another");
                srcNum = scanner.nextInt();
            }
        }
        System.out.println("Congratulation!!!");
    }
}
