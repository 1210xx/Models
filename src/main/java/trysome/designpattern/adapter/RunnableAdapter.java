package trysome.designpattern.adapter;

import java.util.concurrent.Callable;

public class RunnableAdapter implements Runnable{
    //带转接的接口
    private Callable<?> callable;

    public RunnableAdapter(Callable<?> callable){
        this.callable = callable;
    }
    //实现指定接口
    @Override
    public void run() {
        try {
            //将指定接口调用委托至Adapter调用
            callable.call();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
