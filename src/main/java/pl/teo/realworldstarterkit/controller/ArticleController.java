package pl.teo.realworldstarterkit.controller;

import org.springframework.web.bind.annotation.*;
import pl.teo.realworldstarterkit.model.dto.*;
import pl.teo.realworldstarterkit.model.entity.Article;
import pl.teo.realworldstarterkit.service.ArticleService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping
    public ArticleDisplayJsonWrapper create(@RequestBody ArticleCreateDto article, Principal principal) {
        return articleService.saveArticle(article, principal);
    }

    @GetMapping
    public ArticleMultipleJsonWrapper getArticles(@RequestParam(required = false) String tag,
                                     @RequestParam(required = false) String author,
                                     @RequestParam(required = false) String favorited,
                                     @RequestParam(defaultValue = "20") int limit,
                                     @RequestParam(defaultValue = "0") String offset) {
        //todo get articles
        return null;
    }

    @GetMapping("/{slug}")
    public ArticleDisplayJsonWrapper getOne(@PathVariable String slug, Principal principal) {
        return articleService.getBySlug(slug, principal);
    }

    @DeleteMapping("/{slug}")
    public void delete(@PathVariable String slug, Principal principal) {
        articleService.deleteArticle(slug, principal);
    }

    @PutMapping("/{slug}")
    public ArticleDisplayJsonWrapper update(@PathVariable String slug, @RequestBody ArticleUpdateDto article, Principal principal) {
        return articleService.updateArticle(slug, article, principal);
    }

    @PostMapping("/{slug}/favorite")
    public ArticleDisplayJsonWrapper favoriteArticle(@PathVariable String slug, Principal principal) {
        return articleService.favorite(slug, principal);
    }

    @DeleteMapping("/{slug}/favorite")
    public ArticleDisplayJsonWrapper unFavoriteArticle(@PathVariable String slug, Principal principal) {
        return articleService.unFavorite(slug, principal);
    }


    // comments

    @PostMapping("/{slug}/comments")
    public CommentDisplayJsonWrapper addComment(@PathVariable String slug,
                                        Principal principal,
                                        @RequestBody CommentCreateDto createDto) {
        return articleService.newComment(slug, principal, createDto);
    }

    @GetMapping("/{slug}/comments")
    public CommentMultipleJsonWrapper getComments(@PathVariable String slug, Principal principal) {
        return articleService.getComments(slug, principal);
    }

    @DeleteMapping("/{slug}/comments/{id}")
    public void deleteComment(@PathVariable String slug, Principal principal, @PathVariable long id) {
        articleService.deleteComment(slug, principal, id);
    }
}
