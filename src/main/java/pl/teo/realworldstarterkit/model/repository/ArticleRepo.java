package pl.teo.realworldstarterkit.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.teo.realworldstarterkit.model.entity.Article;

@Repository
public interface ArticleRepo extends JpaRepository<Article, Long> {
}
