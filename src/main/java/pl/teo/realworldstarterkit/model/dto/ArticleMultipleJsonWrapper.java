package pl.teo.realworldstarterkit.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter @NoArgsConstructor
public class ArticleMultipleJsonWrapper {
    private List<ArticleDisplayDto> articles;
    private int articlesCount;

    public ArticleMultipleJsonWrapper(List<ArticleDisplayDto> articles) {
        setArticles(articles);
    }

    public void setArticles(List<ArticleDisplayDto> articles) {
        this.articles = articles;
        this.articlesCount = articles.size();
    }
}
