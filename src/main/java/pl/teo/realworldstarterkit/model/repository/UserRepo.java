package pl.teo.realworldstarterkit.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.teo.realworldstarterkit.model.entity.User;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    boolean existsByEmailOrUsername(String email, String username);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmailIgnoreCase(String email);
}
