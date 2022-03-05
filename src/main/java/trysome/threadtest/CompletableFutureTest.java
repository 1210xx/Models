package trysome.threadtest;

import com.sun.corba.se.spi.ior.iiop.IIOPFactories;

import java.util.concurrent.CompletableFuture;

public class CompletableFutureTest {

    public static void main(String[] args) throws InterruptedException {
        //创建异步任务
        CompletableFuture<Double> completableFuture = CompletableFuture.supplyAsync(CompletableFutureTest::fetchPrice);
        //如果执行成功
        //需要接受一个单接口的实例的参数，而其中Consumer实现的apply就是对参数操作
        //所以可以写成Lambda形式
        completableFuture.thenAccept((result) -> {
            System.out.println("price :" + result);
        });
        //如果异常执行
        completableFuture.exceptionally((e)->{
                e.printStackTrace();
                return null;
        });
        //主线程不立即结束，否则CompletableFuture默认的线程池会立即关闭
        Thread.sleep(200);
    }

    static Double fetchPrice(){
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (Math.random() < 0.3){
            throw new RuntimeException("fetch price failed");
        }
        return 5 + Math.random() * 20;
    }
}
