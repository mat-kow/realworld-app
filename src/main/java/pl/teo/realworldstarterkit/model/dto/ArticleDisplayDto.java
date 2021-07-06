package pl.teo.realworldstarterkit.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Setter @Getter
public class ArticleDisplayDto {
    private String slug;
    private String title;
    private String description;
    private String body;
    private List<String> tagList;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Author author;
    private int favoritesCount;
    private boolean favorited;

}