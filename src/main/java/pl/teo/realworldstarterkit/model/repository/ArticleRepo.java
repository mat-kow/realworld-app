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
            value = "SELECT * FROM article WHERE id IN " +
                    "(SELECT article_tag_list.article_id FROM tag INNER JOIN article_tag_list " +
                    "ON tag.id = article_tag_list.tag_list_id WHERE tag.name = ?1) ORDER BY created_at DESC OFFSET ?2 LIMIT ?3",
            nativeQuery = true
    )
    List<Article> getByTag(String tag, int offset, int limit);

    @Query(
            value = "SELECT * FROM article WHERE author_id = (SELECT id FROM users WHERE username = ?1) " +
                    "ORDER BY created_at DESC OFFSET ?2 LIMIT ?3",
            nativeQuery = true
    )
    List<Article> getByAuthor(String author, int offset, int limit);

    @Query(
            value = "SELECT * FROM article WHERE id IN (SELECT users_favourite_list.favourite_list_id " +
                    "FROM users INNER JOIN users_favourite_list ON users.id = users_favourite_list.user_id " +
                    "WHERE username = ?1) " +
                    "ORDER BY created_at DESC OFFSET ?2 LIMIT ?3",
            nativeQuery = true
    )
    List<Article> getByFavorited(String username, int offset, int limit);

    @Query(
            value = "SELECT * FROM article ORDER BY created_at DESC OFFSET ?1 LIMIT ?2",
            nativeQuery = true
    )
    List<Article> getAll(int offset, int limit);
}
