package trysome.threadtest;

import java.io.File;

public class ThreadTest {
//    public static void main(String[] args) {
//        System.out.println("main start...");
//        Thread t = new Thread() {
//            public void run() {
//                System.out.println("thread run...");
//                System.out.println("thread end.");
//            }
//        };
//        t.start();
//        System.out.println("main end...");
//    }
public static void main(String[] args) {
    System.out.println(File.separator);
    System.out.println("main start...");
    Thread t = new Thread() {
        public void run() {
            System.out.println("thread run...");
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {}
            System.out.println("thread end.");
        }
    };
    t.start();
    try {
        Thread.sleep(20);
    } catch (InterruptedException e) {}
    System.out.println("main end...");
}
}
