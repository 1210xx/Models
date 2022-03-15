package trysome.springjdbc.springhibernate.entity;


import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {
    //映射使用的JavaBean，所有属性都使用包装类型而不是基本类型。
    private Long id;
    private String email;
    private String password;
    private String name;
    private Long createAt;


    public User() {
    }

    public User(Long id, String email, String password, String name, Long createAt) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.createAt = createAt;
    }


    //@Id 表明主键
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(nullable = false,unique = true,length = 100)
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
    @Column(nullable = false,updatable = false)
    public Long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Long createAt) {
        this.createAt = createAt;
    }
}
