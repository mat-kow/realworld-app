package pl.teo.realworldstarterkit.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.teo.realworldstarterkit.model.entity.Tag;

@Repository
public interface TagRepo extends JpaRepository<Tag, Long> {
}
