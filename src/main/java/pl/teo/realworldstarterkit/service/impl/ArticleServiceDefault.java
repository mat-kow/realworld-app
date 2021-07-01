package pl.teo.realworldstarterkit.service.impl;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import pl.teo.realworldstarterkit.model.dto.ArticleCreateDto;
import pl.teo.realworldstarterkit.model.entity.Article;
import pl.teo.realworldstarterkit.model.entity.User;
import pl.teo.realworldstarterkit.service.ArticleService;

@Service
public class ArticleServiceDefault implements ArticleService {
    @Override
    public Article save(ArticleCreateDto articleDto) {
        Article article = createDtoToArticle(articleDto);
        article.setAuthor(new User());//todo principal
        article.setSlug(generateSlug(article.getTitle()));
        return article;
    }

    private Article createDtoToArticle(ArticleCreateDto dto) {
        Article article = new Article();
        article.setBody(dto.getBody());
        article.setTitle(dto.getTitle());
        article.setDescription(dto.getDescription());
        article.setTagList(dto.getTagList());
        return article;
    }
    private String generateSlug(String title) {
        return title.replaceAll("\\s+", "-") + "-" + RandomStringUtils.randomAlphanumeric(6);
    }
}
