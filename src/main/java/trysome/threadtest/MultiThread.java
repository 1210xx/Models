package trysome.threadtest;

/**
 * <head>
 *     多线程问题
 * </head>
 *
 * <p>
 *
 * </p>
 */
public class MultiThread {
    public static void main(String[] args) throws InterruptedException {
        Thread add = new AddThread();
        Thread dec = new DecThread();
        add.start();
        dec.start();
        add.join();
        dec.join();
        System.out.println(Counter.count);
    }
}

class Counter{
    public static int count = 0;
}

class AddThread extends Thread{
    @Override
    public void run() {
        super.run();
        for (int i = 0; i < 10000; i++) {
            Counter.count += 1;
        }
    }
}

class DecThread extends Thread{
    @Override
    public void run() {
        super.run();
        for (int i = 0; i < 10000; i++) {
            Counter.count -= 1;
        }
    }
}