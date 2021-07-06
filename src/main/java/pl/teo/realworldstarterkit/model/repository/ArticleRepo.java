package pl.teo.realworldstarterkit.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.teo.realworldstarterkit.model.entity.Article;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepo extends JpaRepository<Article, Long> {
    Optional<Article> getArticleBySlug(String slug);
    @Query(
            value = "SELECT * FROM articles WHERE id IN " +
                    "(SELECT articles_tags.article_id FROM tags INNER JOIN articles_tags " +
                    "ON tags.id = articles_tags.tag_id WHERE tags.name = ?1) ORDER BY created_at DESC OFFSET ?2 LIMIT ?3",
            nativeQuery = true
    )
    List<Article> getByTag(String tag, int offset, int limit);

    @Query(
            value = "SELECT * FROM articles WHERE author_id = (SELECT id FROM users WHERE username = ?1) " +
                    "ORDER BY created_at DESC OFFSET ?2 LIMIT ?3",
            nativeQuery = true
    )
    List<Article> getByAuthor(String author, int offset, int limit);

    @Query(
            value = "SELECT * FROM articles WHERE id IN (SELECT users_fav_articles.article_id " +
                    "FROM users INNER JOIN users_fav_articles ON users.id = users_fav_articles.user_id " +
                    "WHERE username = ?1) " +
                    "ORDER BY created_at DESC OFFSET ?2 LIMIT ?3",
            nativeQuery = true
    )
    List<Article> getByFavorited(String username, int offset, int limit);

    @Query(
            value = "SELECT * FROM articles ORDER BY created_at DESC OFFSET ?1 LIMIT ?2",
            nativeQuery = true
    )
    List<Article> getAll(int offset, int limit);

    @Query(
            value = "SELECT * from articles WHERE author_id IN " +
                    "(SELECT id FROM users WHERE id IN " +
                    "(SELECT fallowed_id FROM users_fallowing WHERE user_id = ?3)) " +
                    "ORDER BY created_at DESC OFFSET ?1 LIMIT ?2",
            nativeQuery = true
    )
    List<Article> getAllFallowedByUserId(int offset, int limit, long userId);
}

