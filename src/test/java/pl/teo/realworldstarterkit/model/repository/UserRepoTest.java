package pl.teo.realworldstarterkit.model.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pl.teo.realworldstarterkit.model.entity.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepoTest {
    @Autowired
    UserRepo underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void shouldReturnTrueWhenUsernameOrEmailExistOtherwiseFalse() {
        String username = "Tom";
        String email = "tom@gmail.com";
        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        underTest.save(user);

        assertAll(
                () -> assertTrue(underTest.existsByEmailOrUsername(email, username)),
                () -> assertTrue(underTest.existsByEmailOrUsername("bob@gmail.com", username)),
                () -> assertTrue(underTest.existsByEmailOrUsername(email, "Bob")),
                () -> assertFalse(underTest.existsByEmailOrUsername("bob@gmail.com", "Bob"))
        );
    }

    @Test
    void shouldFindUserByUsernameIgnoringCase() {
        String username = "Tom";
        String email = "tom@gmail.com";
        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user = underTest.save(user);

        Optional<User> expected = Optional.of(user);

        assertAll(
                () -> assertEquals(expected, underTest.findByUsernameIgnoreCase(username.toLowerCase())),
                () -> assertEquals(expected, underTest.findByUsernameIgnoreCase(username.toUpperCase()))
        );

    }

    @Test
    void shouldFindUserByEmailIgnoringCase() {
        String username = "Tom";
        String email = "Tom@gmail.com";
        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user = underTest.save(user);

        Optional<User> expected = Optional.of(user);

        assertAll(
                () -> assertEquals(expected, underTest.findByEmailIgnoreCase(email.toLowerCase())),
                () -> assertEquals(expected, underTest.findByEmailIgnoreCase(email.toUpperCase()))
        );

    }

    @Test
    void shouldFindUserById() {
        String username = "Tom";
        String email = "Tom@gmail.com";
        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user = underTest.save(user);

        long id = user.getId();
        Optional<User> expected = Optional.of(user);

        assertAll(
                () -> assertEquals(expected, underTest.findById(id)),
                () -> assertEquals(Optional.empty(), underTest.findById(id + 1))
        );


    }
}