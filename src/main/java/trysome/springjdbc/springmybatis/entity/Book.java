package trysome.springjdbc.springmybatis.entity;

public class Book extends AbstractEntity{
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return String.format("Book[id=%s, title=%s, createdAt=%s, createdDateTime=%s]", getId(), getTitle(),
                getCreatedAt(), getCreatedDateTime());
    }

}
