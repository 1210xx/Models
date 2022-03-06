package trysome.webtest.mvc.bean;

public class User {
    public  String email;

    public String password;

    public String name;
    public String description;

    public User() {
    }

    public User(String emil, String password, String name, String description) {
        this.email = emil;
        this.password = password;
        this.name = name;
        this.description = description;
    }
}
