package trysome.something;

public class StaticTest {
    public void unStaticMethod(){
        System.out.println("unStatic....");
    }

    public static void staticMethod(){
        System.out.println("Static method....");
    }

    public void callTest(){
        staticMethod();
        unStaticMethod();
    }


    public static void callTest02(){
        staticMethod();

        StaticTest staticTest = new StaticTest();
        staticTest.unStaticMethod();
    }

    public static void main(String[] args) {
        callTest02();
        StaticTest staticTest = new StaticTest();
        staticTest.callTest();
    }
}
