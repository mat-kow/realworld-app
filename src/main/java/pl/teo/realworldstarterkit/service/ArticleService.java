package pl.teo.realworldstarterkit.service;

import pl.teo.realworldstarterkit.model.dto.ArticleCreateDto;
import pl.teo.realworldstarterkit.model.entity.Article;

public interface ArticleService {
    Article save(ArticleCreateDto articleDto);
}
