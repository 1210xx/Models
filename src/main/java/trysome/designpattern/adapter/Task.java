package trysome.designpattern.adapter;

import java.util.concurrent.Callable;

/**
 * Callable 和 Runnable 相比多了一个返回值，并且返回Future类型的值。
 *
 * 而且Callable是一个泛型接口，可以返回指定类型的结果
 */
public class Task implements Callable {
    private long num;

    public Task(long num){
        this.num = num;
    }
    @Override
    public Object call() throws Exception {
        long r = 0;
        for (long n = 1; n < this.num; n++) {
            r += n;
        }
        System.out.println("Result " + r);
        return r;
    }
}
