package trysome.threadtest;

import java.io.File;

/**
 * <p>
 *     守护线程是为其他线程服务的线程；
 *     setDaemon(true);
 *     守护线程不能持有任何需要关闭的资源，
 *     例如打开文件等，因为虚拟机退出时，守护线程没有任何机会来关闭文件，这会导致数据丢失。
 *  </p>
 */
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
