package trysome.threadtest;

import java.util.concurrent.CompletableFuture;

/**
 * <p>
 * 使用Future获得异步执行结果时，要么调用阻塞方法get()，
 * 要么轮询看isDone()是否为true，这两种方法都不是很好，因为主线程也会被迫等待。
 * </p>
 *
 * <p>
 * 从Java 8开始引入了CompletableFuture，它针对Future做了改进，
 * 可以传入回调对象，当异步任务完成或者发生异常时，自动调用回调对象的回调方法。
 * </p>
 *
 *
 * <p>
 * 只是实现了异步回调机制，我们还看不出CompletableFuture相比Future的优势。
 * CompletableFuture更强大的功能是，多个CompletableFuture可以串行执行，
 * 例如，定义两个CompletableFuture，
 * 第一个CompletableFuture根据证券名称查询证券代码，
 * 第二个CompletableFuture根据证券代码查询证券价格，
 * </p>
 */

public class CompletableFuturePointTest {
    public static void main(String[] args) throws InterruptedException {
//        serialExe();
        parallelExe();
    }

    public static void parallelExe() throws InterruptedException {
        //创建一个CompletableFuture
        CompletableFuture<String> cfQueryFromSina = CompletableFuture.supplyAsync(() -> {
            return queryCode("中国石油", "https://finance.sina.com.cn/code/");
        });
        //创建第二个CompletableFuture,并行执行
        CompletableFuture<String> cfQueryFrom163 = CompletableFuture.supplyAsync(() -> {
            return queryCode("中国石油", "https://money.163.com/code");
        });
        //任意一个执行结束，都可以进行下一步
        CompletableFuture<Object> cfQuery = CompletableFuture.anyOf(cfQueryFrom163, cfQueryFromSina);
        //CompletableFuture获取价格
        CompletableFuture<Double> cfFetchFromSina = cfQuery.thenApplyAsync((code) -> {
            return fetchPrice((String) code, "https://finance.sina.com.cn/price/");
        });
        //第二个CompletableFuture获取价格
        CompletableFuture<Double> cfFetchFrom163 = cfQuery.thenApplyAsync((code) -> {
            return fetchPrice((String) code, "https://money.163.com/price/");
        });


        CompletableFuture<Object> cfFetch = CompletableFuture.anyOf(cfFetchFrom163, cfFetchFromSina);

        cfFetch.thenAccept((result) -> System.out.println("price : " + result));

        cfFetch.exceptionally((e) -> {
            e.printStackTrace();
            return null;
        });

        Thread.sleep(200);

    }


    public static void serialExe() throws InterruptedException {
        CompletableFuture<String> cfQuery = CompletableFuture.supplyAsync(() -> {
            return queryCode("中国石油");
        });

        CompletableFuture<Double> cfFetch = cfQuery.thenApply((code) -> {
            return fetchPrice(code);
        });

        cfFetch.thenAccept((result) -> {
            System.out.println("price: " + result);
        });

        Thread.sleep(2000);
    }

    static Double fetchPrice(String code) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 5 + Math.random() * 20;
    }

    static String queryCode(String name) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "601857";
    }

    static Double fetchPrice(String code, String url) {
        System.out.println("query price from " + url + "...");
        try {
            Thread.sleep((long) (Math.random() * 100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 5 + Math.random() * 20;
    }

    static String queryCode(String name, String url) {
        System.out.println("query code from " + url + "....");
        try {
            Thread.sleep((long) (Math.random() * 100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "601857";
    }
}
