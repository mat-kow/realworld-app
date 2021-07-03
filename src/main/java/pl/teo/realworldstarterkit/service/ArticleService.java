package pl.teo.realworldstarterkit.service;

import pl.teo.realworldstarterkit.model.dto.*;

import java.security.Principal;

public interface ArticleService {
    ArticleDisplayJsonWrapper saveArticle(ArticleCreateDto articleDto, Principal principal);
    ArticleDisplayJsonWrapper getBySlug(String slug, Principal principal);
    ArticleDisplayJsonWrapper updateArticle(String slug, ArticleUpdateDto updateDto, Principal principal);
    void deleteArticle(String slug, Principal principal);

    CommentDisplayJsonWrapper newComment(String slug, Principal principal, CommentCreateDto createDto);
    CommentMultipleJsonWrapper getComments(String slug, Principal principal);
    void deleteComment(String slug, Principal principal, long id);

    ArticleDisplayJsonWrapper favorite(String slug, Principal principal);
    ArticleDisplayJsonWrapper unFavorite(String slug, Principal principal);

    ArticleMultipleJsonWrapper getByTagName(String tagName, Principal principal, int offset, int limit);
    ArticleMultipleJsonWrapper getByAuthor(String authorName, Principal principal, int offset, int limit);
    ArticleMultipleJsonWrapper getByFavorited(String username, Principal principal, int offset, int limit);
    ArticleMultipleJsonWrapper getAll(Principal principal, int offset, int limit);
}
