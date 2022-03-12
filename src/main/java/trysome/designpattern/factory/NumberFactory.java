package trysome.designpattern.factory;

/**
 * 工厂方法可以隐藏创建产品的细节，且不一定每次都会真正创建产品，
 * 完全可以返回缓存的产品，从而提升速度并减少内存消耗。
 */
public interface NumberFactory {
    Number parse(String s);

    //静态方法
    static NumberFactory getFactory(){
        return impl;
    }
    //静态变量
    static NumberFactory impl = new NumberFactoryImpl();
}
