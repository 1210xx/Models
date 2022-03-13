package trysome.designpattern.proxy;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Supplier;

/**
 * 继承我们之前写的的AbstractConnectionProxy
 *
 */
public class LazyConnectionProxy extends AbstractConnectionProxy{

    //创建Connection对象的容易，只有在用get方法的时候才会新建对象。
    private Supplier<Connection> supplier;

    private Connection target = null;

    public LazyConnectionProxy(Supplier<Connection> supplier) {
        this.supplier = supplier;
    }

    @Override
    public void close() throws SQLException{
        if (target != null){
            System.out.println("Close connection " + target);
            super.close();
        }
    }

    /**
     * 只有第一次执行SQL语句时（即调用任何类似prepareStatement()方法时，触发getRealConnection()调用）
     * @return 创建的Connection对象
     */
    @Override
    protected Connection getRealConnection() {
        if (target == null){
            //supplier的创建对象方法
            target = supplier.get();
        }
        return target;
    }
}
