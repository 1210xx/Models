package trysome.springjdbc.springhibernate.entity;

import javax.persistence.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

//用于继承
@MappedSuperclass
public abstract class AbstractEntity {
    private Long id;
    private Long createAt;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false,updatable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @Column(nullable = false,updatable = false)
    public Long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Long createAt) {
        this.createAt = createAt;
    }
    //@Transient方法，它返回一个“虚拟”的属性。
    //因为getCreatedDateTime()是计算得出的属性，而不是从数据库表读出的值，因此必须要标注@Transient，
    //否则Hibernate会尝试从数据库读取名为createdDateTime这个不存在的字段从而出错。
    @Transient
    public ZonedDateTime getCreateDateTime(){
        return Instant.ofEpochMilli(this.createAt).atZone(ZoneId.systemDefault());
    }

    //它表示在我们将一个JavaBean持久化到数据库之前（即执行INSERT语句），
    //Hibernate会先执行该方法，这样我们就可以自动设置好createdAt属性。
    @PrePersist
    public void preInsert(){
        setCreateAt(System.currentTimeMillis());
    }
}
