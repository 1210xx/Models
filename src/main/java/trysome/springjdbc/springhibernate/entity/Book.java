package trysome.springjdbc.springhibernate.entity;

import javax.persistence.*;

@Entity
public class Book {
    private Long id;
    private String title;
    private Long CreateAt;

    public Book() {
    }

    public Book(Long id, String title, Long createAt) {
        this.id = id;
        this.title = title;
        CreateAt = createAt;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false,updatable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(nullable = false,length = 100)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    @Column(nullable = false, updatable = false)
    public Long getCreateAt() {
        return CreateAt;
    }

    public void setCreateAt(Long createAt) {
        CreateAt = createAt;
    }
}
