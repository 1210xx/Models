package trysome.designpattern.factory;

public class FactoryTest {
    public static void main(String[] args) {
        NumberFactory numberFactory = NumberFactory.getFactory();
        Number result = numberFactory.parse("123.456");
        System.out.println(result);
    }
}


