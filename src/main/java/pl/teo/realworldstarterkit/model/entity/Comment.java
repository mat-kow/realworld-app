package pl.teo.realworldstarterkit.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity(name = "comments")
@Setter @Getter @NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @CreationTimestamp
    private Timestamp createdAt;
    @UpdateTimestamp
    private Timestamp updatedAt;
    private String body;
    @ManyToOne
    private User author;
    @ManyToOne
    private Article article;

    public Comment(String body, User author, Article article) {
        this.body = body;
        this.author = author;
        this.article = article;
    }
}
