package pl.teo.realworldstarterkit.service.impl;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.teo.realworldstarterkit.app.exception.ApiForbiddenException;
import pl.teo.realworldstarterkit.app.exception.ApiNotFoundException;
import pl.teo.realworldstarterkit.model.dto.*;
import pl.teo.realworldstarterkit.model.entity.Article;
import pl.teo.realworldstarterkit.model.entity.Comment;
import pl.teo.realworldstarterkit.model.entity.Tag;
import pl.teo.realworldstarterkit.model.entity.User;
import pl.teo.realworldstarterkit.model.repository.ArticleRepo;
import pl.teo.realworldstarterkit.model.repository.CommentRepo;
import pl.teo.realworldstarterkit.model.repository.UserRepo;
import pl.teo.realworldstarterkit.service.ArticleService;
import pl.teo.realworldstarterkit.service.UserService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleServiceDefault implements ArticleService {
    private final UserService userService;
    private final ArticleRepo articleRepo;
    private final CommentRepo commentRepo;
    private final UserRepo userRepo;

    public ArticleServiceDefault(UserService userService, ArticleRepo articleRepo, CommentRepo commentRepo, UserRepo userRepo) {
        this.userService = userService;
        this.articleRepo = articleRepo;
        this.commentRepo = commentRepo;
        this.userRepo = userRepo;
    }

    @Override
    @Transactional
    public ArticleDisplayJsonWrapper saveArticle(ArticleCreateDto articleDto, Principal principal) {
        Article article = createDtoToArticle(articleDto);
        article.setAuthor(userService.getCurrentUser(principal));
        article.setSlug(generateSlug(article.getTitle()));
        return mapArticleToDisplay(articleRepo.save(article), principal);
    }

    @Override
    public ArticleDisplayJsonWrapper getBySlug(String slug, Principal principal) {
        return mapArticleToDisplay(articleRepo.getArticleBySlug(slug)
                .orElseThrow(() -> new ApiNotFoundException("Article does not exist")), principal);
    }

    @Override
    @Transactional
    public ArticleDisplayJsonWrapper updateArticle(String slug, ArticleUpdateDto updateDto, Principal principal) {
        Article article = articleRepo.getArticleBySlug(slug)
                .orElseThrow(() -> new ApiNotFoundException("Article does not exist"));
        if (article.getAuthor().getId() != Long.parseLong(principal.getName())) {
            throw new ApiForbiddenException();
        }
        String body = updateDto.getBody();
        if (body != null && !body.isBlank()) {
            article.setBody(body);
        }
        String title = updateDto.getTitle();
        if (title != null && !title.isBlank()) {
            article.setTitle(title);
            article.setSlug(generateSlug(title));
        }
        String description = updateDto.getDescription();
        if (description != null && !description.isBlank()) {
            article.setDescription(description);
        }
        return mapArticleToDisplay(articleRepo.save(article), principal);
    }

    @Override
    @Transactional
    public void deleteArticle(String slug, Principal principal) {
        Article article = articleRepo.getArticleBySlug(slug)
                .orElseThrow(() -> new ApiNotFoundException("Article does not exist"));
        if (article.getId() != Long.parseLong(principal.getName())) {
            throw new ApiForbiddenException();
        }
        articleRepo.delete(article);
    }

    @Override
    @Transactional
    public CommentDisplayJsonWrapper newComment(String slug, Principal principal, CommentCreateDto createDto) {
        Article article = articleRepo.getArticleBySlug(slug)
                .orElseThrow(() -> new ApiNotFoundException("Article does not exist"));
        User author = userService.getCurrentUser(principal);
        Comment comment = new Comment(createDto.getBody(), author, article);
        return mapCommentToDisplay(commentRepo.save(comment), principal);
    }

    @Override
    public CommentMultipleJsonWrapper getComments(String slug, Principal principal) {
        Article article = articleRepo.getArticleBySlug(slug)
                .orElseThrow(() -> new ApiNotFoundException("Article does not exist"));
        return new CommentMultipleJsonWrapper(commentRepo.getAllByArticle(article).stream()
                .map(comment -> mapCommentToDisplay(comment, principal)).collect(Collectors.toList()));
    }

    @Override
    @Transactional
    public void deleteComment(String slug, Principal principal, long id) {
        Comment comment = commentRepo.findById(id)
                .orElseThrow(() -> new ApiNotFoundException("Article does not exist"));
        if (comment.getAuthor().getId() != Long.parseLong(principal.getName())) {
            throw new ApiForbiddenException();
        }
        commentRepo.delete(comment);
    }

    @Override
    @Transactional
    public ArticleDisplayJsonWrapper favorite(String slug, Principal principal) {
        Article article = articleRepo.getArticleBySlug(slug)
                .orElseThrow(() -> new ApiNotFoundException("Article does not exist"));
        User currentUser = userService.getCurrentUser(principal);
        //check if article is already on favorite list
        List<Article> favouriteList = currentUser.getFavouriteList();
        if (favouriteList.stream().noneMatch(f -> f.getId() == article.getId())) {
            article.setFavouriteCount(article.getFavouriteCount() + 1);
            articleRepo.save(article);
            favouriteList.add(article);
            currentUser.setFavouriteList(favouriteList);
            userRepo.save(currentUser);
        }
        return mapArticleToDisplay(article,principal);
    }

    @Override
    @Transactional
    public ArticleDisplayJsonWrapper unFavorite(String slug, Principal principal) {
        Article article = articleRepo.getArticleBySlug(slug)
                .orElseThrow(() -> new ApiNotFoundException("Article does not exist"));
        User currentUser = userService.getCurrentUser(principal);
        //check if article is on favorite list
        List<Article> favouriteList = currentUser.getFavouriteList();
        if (favouriteList.stream().anyMatch(f -> f.getId() == article.getId())) {
            article.setFavouriteCount(article.getFavouriteCount() - 1);
            articleRepo.save(article);
            currentUser.setFavouriteList(favouriteList.stream()
                    .filter(art -> art.getId() != article.getId())
                    .collect(Collectors.toList()));
            userRepo.save(currentUser);
        }
        return mapArticleToDisplay(article,principal);
    }

    private CommentDisplayJsonWrapper mapCommentToDisplay(Comment comment, Principal principal) {
        CommentDisplayJsonWrapper displayDto = new CommentDisplayJsonWrapper();
        displayDto.setAuthor(userService.getProfile(comment.getAuthor().getUsername(), principal));
        displayDto.setId(comment.getId());
        displayDto.setCreatedAt(comment.getCreatedAt());
        displayDto.setUpdatedAt(comment.getUpdatedAt());
        displayDto.setBody(comment.getBody());
        return displayDto;
    }

    private Article createDtoToArticle(ArticleCreateDto dto) {
        Article article = new Article();
        article.setBody(dto.getBody());
        article.setTitle(dto.getTitle());
        article.setDescription(dto.getDescription());
        if (dto.getTagList() != null) {
            List<Tag> tagList = dto.getTagList().stream().map(Tag::new).collect(Collectors.toList());
            article.setTagList(tagList);
        } else {
            article.setTagList(new ArrayList<>());
        }
        return article;
    }
    private String generateSlug(String title) {
        return title.toLowerCase().replaceAll("\\s+", "-") + "-" + RandomStringUtils.randomAlphanumeric(6);
    }

    private ArticleDisplayJsonWrapper mapArticleToDisplay(Article article, Principal principal) {
        ArticleDisplayJsonWrapper display = new ArticleDisplayJsonWrapper();
        display.setBody(article.getBody());
        display.setSlug(article.getSlug());
        display.setTitle(article.getTitle());
        display.setDescription(article.getDescription());
        display.setTagList(article.getTagList().stream().map(Tag::getName).collect(Collectors.toList()));
        display.setCreatedAt(article.getCreatedAt());
        display.setUpdatedAt(article.getUpdatedAt());

        if (principal == null) {
            display.setFavorited(false);
        } else {
            User currentUser = userService.getCurrentUser(principal);
            display.setFavorited(currentUser.getFavouriteList().stream().anyMatch(f -> f.getId() == article.getId()));
        }

        display.setFavouriteCount(article.getFavouriteCount());
        display.setAuthor(userService.getProfile(article.getAuthor().getUsername(), principal));
        return display;
    }
}
