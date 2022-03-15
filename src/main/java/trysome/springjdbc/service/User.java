package trysome.springjdbc.service;

/**
 * 这里写一个Bean，在Spring容器启动时自动创建一个users表
 */
public class User {
    private long id;
    private String email;
    private String password;
    private String name;

    //没有默认构造器 会报错BeanInstantiationException:
    public User() {
    }

    public User(long id, String email, String password, String name) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString(){
        return String.format("User[id=%s, email=%s, name=%s, password=%s]", this.id, this.email, this.name,
                this.password);
    }
}
