package trysome.threadtest;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 使用synchronizedl构建多线程任务管理器
 */
public class TaskQueueTest {
    public static void main(String[] args) throws InterruptedException {

        //创建任务队列
        TaskQueue taskQueue = new TaskQueue();
        //执行任务（加入任务队列）线程列表
        List<Thread> exeThreadList = new LinkedList<Thread>();

        //创造五个线程，获取线程并执行。模拟排队等待获取线程
        for (int i = 0; i < 5; i++) {
            //创建提取任务线程
            Thread getTaskThread = new Thread(){
                @Override
                public void run() {
                    super.run();
                    while (true){
                        try {
                            String s = taskQueue.getTask2();
                            System.out.println(Thread.currentThread().getName() + "  ---- execute task " + s);
                        }catch (InterruptedException e){
                            return;
                        }
                    }
                }
            };
            //启动获取任务线程，5个不同的线程开始获取任务队列中的任务
            getTaskThread.start();
            //加入线程列表（执行获取任务队列的线程列表）
            exeThreadList.add(getTaskThread);
        }
        //创建放入任务队列的线程
        Thread putThread = new Thread(){
            @Override
            public void run() {
                super.run();
                //由一个放入线程，放入10个不同的任务
                for (int i = 0; i < 10; i++) {
                    String s = "t-" + Math.random();
                    System.out.println("add task：" + s);
                    taskQueue.addTask2(s);
                    try {
                        //放入线程每隔暂停0.1s，执行一次
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        };
        //放入线程开启
        putThread.start();
        //插入main线程
        putThread.join();
        //主线程暂停一段时间，等待提取线程执行完，然后通过打断结束所有执行任务的线程
        Thread.sleep(100);
        //将执行任务的列表打断
        for (Thread thread : exeThreadList){
            System.out.println("中断前 ： " + thread.getName() + "  ======  " + thread.getState());
            thread.interrupt();
            System.out.println("中断后：" + thread.getName() + "  ======  " + thread.getState());
        }

        System.out.println("====================================");
        for (Thread thread : exeThreadList){
            System.out.println("中断后：" + thread.getName() + "  ======  " + thread.getState());
        }
    }

}


class TaskQueue{
    Queue<String> queue = new LinkedList<>();

    public synchronized void addTask(String s){
        this.queue.add(s);
    }

    public synchronized void addTask2(String s){
        this.queue.add(s);
        this.notifyAll();
    }
    /**
     * while()循环永远不会退出。因为线程在执行while()循环时，
     * 已经在getTask()入口获取了this锁，其他线程根本无法调用addTask()，
     * 因为addTask()执行条件也是获取this锁。
     * @return
     */
    public synchronized String getTask(){
        while (queue.isEmpty()){}
        return queue.remove();
    }

    public synchronized String getTask2() throws InterruptedException {
//        Thread.sleep(2000);
        while (queue.isEmpty()){
            this.wait();
        }
        return queue.remove();
    }
}