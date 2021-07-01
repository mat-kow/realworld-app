package pl.teo.realworldstarterkit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.teo.realworldstarterkit.model.dto.ArticleCreateDto;
import pl.teo.realworldstarterkit.model.entity.Article;
import pl.teo.realworldstarterkit.service.ArticleService;

import java.util.List;

@RestController
//@RequestMapping("/api/articles") todo uncomment
public class ArticleController {
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping
    public Article create(@RequestBody ArticleCreateDto article) {
        return articleService.save(article);
    }

    @GetMapping
    public List<Article> getArticles(@RequestParam(required = false) String tag,
                                     @RequestParam(required = false) String author,
                                     @RequestParam(required = false) String favorited,
                                     @RequestParam(defaultValue = "20") int limit,
                                     @RequestParam(defaultValue = "0") String offset) {
        //todo get articles
        return null;


    }
    @GetMapping("/hello")
    public String hello() {
        //todo delete this
        return "hello";
    }
}
