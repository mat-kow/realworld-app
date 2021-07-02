package pl.teo.realworldstarterkit.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.teo.realworldstarterkit.model.entity.Article;
import pl.teo.realworldstarterkit.model.entity.Comment;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepo extends JpaRepository<Comment, Long> {
    List<Comment> getAllByArticle(Article article);
    Optional<Comment> findById(Long id);
}
