package trysome.designpattern.adapter;

import java.util.concurrent.Callable;

public class AdapterMain {

    public static void main(String[] args) {
        Callable<Long>  callable = new Task(123450000L);
        //thread需要接受runnable的接口。此时编译不通过。
        //Thread thread = new Thread(callable);
        //有两种做法，直接改为runnable
        //Thread thread = new Thread(runnable);
        //还有就是使用Adapter
        Thread thread = new Thread(new RunnableAdapter(callable));
        thread.start();
    }
}
