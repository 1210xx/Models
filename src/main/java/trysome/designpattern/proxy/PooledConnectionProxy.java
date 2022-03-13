package trysome.designpattern.proxy;

import java.sql.Connection;
import java.util.Queue;

/**
 * 使用代理模式，创建可复用的Connection连接池
 */
public class PooledConnectionProxy extends AbstractConnectionProxy{
    //实际的Connection
    Connection realConnection;

    //空闲的连接池队列
    Queue<PooledConnectionProxy> idleQueue;

    //构造方法
    public PooledConnectionProxy(Queue<PooledConnectionProxy> idleQueue,Connection target) {
        this.realConnection = target;
        this.idleQueue = idleQueue;
    }

    /**
     * 覆写close方法
     */
    @Override
    public void close(){
        System.out.println("Fake close and released to idle queue for future reuse: " + realConnection);
        //并没有把实际的Connection关闭，即调用close()方法
        //而是把自己放入空闲队列
        idleQueue.offer(this);
    }
    @Override
    protected Connection getRealConnection() {
        return realConnection;
    }
}
