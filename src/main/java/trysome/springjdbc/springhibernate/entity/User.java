package trysome.springjdbc.springhibernate.entity;


import javax.persistence.*;

@NamedQueries(
        @NamedQuery(
                name = "login",
                query = "SELECT u FROM User u WHERE u.email=?0 AND u.password=?1"
        ))
@Entity
public class User extends AbstractEntity{
    //映射使用的JavaBean，所有属性都使用包装类型而不是基本类型。
    private String email;
    private String password;
    private String name;


    public User() {
    }

    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    @Column(nullable = false,updatable = false,length = 100)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(nullable = false,length = 100)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(nullable = false,length = 100)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
