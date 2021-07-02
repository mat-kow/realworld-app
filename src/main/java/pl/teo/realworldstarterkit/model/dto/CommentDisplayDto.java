package pl.teo.realworldstarterkit.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Setter @Getter
public class CommentDisplayDto {
    private long id;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String body;
    private Author author;

}
