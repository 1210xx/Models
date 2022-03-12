package trysome.designpattern.proxy;

import java.sql.Connection;
import java.util.Queue;

public class PooledConnectionProxy extends AbstractConnectionProxy{
    Connection target;

    Queue<PooledConnectionProxy> idleQueue;

    public PooledConnectionProxy(Queue<PooledConnectionProxy> idleQueue,Connection target) {
        this.target = target;
        this.idleQueue = idleQueue;
    }

    @Override
    public void close(){
        System.out.println("Fake close and released to idle queue for future reuse: " + target);
        idleQueue.offer(this);
    }
    @Override
    protected Connection getRealConnection() {
        return target;
    }
}
