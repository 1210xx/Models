package trysome.springjdbc.springmybatis.entity;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * 把一些共同的属性抽象出来
 */
public abstract class AbstractEntity {
    private Long id;
    private long createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getCreatedDateTime(){
        return Instant.ofEpochMilli(this.createdAt).atZone(ZoneId.systemDefault());
    }
}
