package trysome.designpattern.template;


/**
 * 定义一个操作中的算法的骨架，而将一些步骤延迟到子类中，
 * 使得子类可以不改变一个算法的结构即可重定义该算法的某些特定步骤。
 *
 * 模板方法（Template Method）是一个比较简单的模式。
 * 它的主要思想是，定义一个操作的一系列步骤，对于某些暂时确定不下来的步骤，
 * 就留给子类去实现好了，这样不同的子类就可以定义出不同的步骤。
 */
public class Setting {
    public final String getSetting(String key){
        String value = readFromDataBase(key);
        return value;
    }

    private String readFromDataBase(String key){
        return new String("123");
    }
}
