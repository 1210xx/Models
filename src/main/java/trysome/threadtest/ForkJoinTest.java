package trysome.threadtest;

import com.sun.corba.se.spi.ior.iiop.IIOPFactories;

import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * Fork/Join线程池，它可以执行一种特殊的任务：把一个大任务拆成多个小任务并行执行。
 *
 * 我们举个例子：
 *
 * 如果要计算一个超大数组的和，最简单的做法是用一个循环在一个线程内完成
 *
 * 还有一种方法，可以把数组拆成两部分，分别计算，最后加起来就是最终结果，这样可以用两个线程并行执行：
 *
 */
public class ForkJoinTest {
    public static void main(String[] args) {
        long[] array = new long[2000];
        long expectNum = 0;
        for (int i = 0; i <array.length; i++) {
            array[i] = random();
            expectNum += array[i];
        }

        System.out.println("expectNum :　　" + expectNum);

        //fork/join

        ForkJoinTask<Long> task = new Sumtask(array, 0 ,array.length);
        long startTime = System.currentTimeMillis();
        long result = ForkJoinPool.commonPool().invoke(task);
        long endTime = System.currentTimeMillis();
        System.out.println("Fork/Join:  sum = " + result + "  in " + (endTime - startTime) +" ms");
    }

    static Random random = new Random(14);

    static long random(){
        return random.nextInt(1000);
    }

}

class Sumtask extends RecursiveTask<Long> {
    static final int THRESHOLD = 500;
    long[] array;
    int start;
    int end;

    Sumtask(long[] array, int start, int end){
        this.array = array;
        this.start = start;
        this.end = end;
    }


    @Override
    protected Long compute() {
        if (end - start <= THRESHOLD){
            long sum = 0;
            for (int i = start; i < end; i++) {
                sum += this.array[i];
                try {
                    Thread.sleep(1);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
            return sum;
        }
        int middle = (start + end) / 2;
        System.out.println(String.format("split %d ~ %d ==> %d ~ %d, %d ~ %d",start, end, start, middle, middle, end));
        Sumtask subTask1 = new Sumtask(this.array, start, middle);
        Sumtask subTask2 = new Sumtask(this.array, middle, end);
        invokeAll(subTask1,subTask2);
        long subResult1 = subTask1.join();
        long subResult2 = subTask2.join();
        long result = subResult1 + subResult2;
        System.out.println("Result : " + subResult1 + " +　" + subResult2 + " ==> " + subResult1 + subResult2);

        return result;
    }
}