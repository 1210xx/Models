package trysome.reflection;

import java.lang.reflect.Field;
import java.util.Arrays;

public class ReflectionTest {
    public static void main(String[] args) throws NoSuchFieldException {
        Person person = new Person("xxxxx");
        Object o = person;
        Class oClass = o.getClass();

        Class stdClass = Student.class;
        System.out.println(stdClass.getField("score"));
        System.out.println(stdClass.getDeclaredField("grade"));
        System.out.println(stdClass.getField("name"));
        System.out.println(Arrays.toString(stdClass.getFields()));
        System.out.println(Arrays.toString(stdClass.getDeclaredFields()));
        System.out.println(Arrays.toString(oClass.getFields()));
        System.out.println(oClass.getFields().getClass());
        Field name = stdClass.getField("name");
        System.out.println(name.getModifiers());

    }
}

class Student extends Person{
    public int score;
    private int grade;

    public Student(String name) {
        super(name);
    }
}


class Person{
    public String name;

    public Person(String name) {
        this.name = name;
    }
}