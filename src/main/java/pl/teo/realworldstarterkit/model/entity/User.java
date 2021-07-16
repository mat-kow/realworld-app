package pl.teo.realworldstarterkit.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity(name = "users")
@Getter @Setter
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String email;
    private String username;
    private String bio;
    private String image;
    private String password;
    @ManyToMany
    @JoinTable(
            name = "users_fav_articles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "article_id")
    )
    private List<Article> favouriteList;
    @JoinTable(
            name = "users_fallowing",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "fallowed_id")
    )
    @ManyToMany
    private List<User> fallowingList;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(email, user.email) && Objects.equals(username, user.username) && Objects.equals(bio, user.bio) && Objects.equals(image, user.image) && Objects.equals(password, user.password) && Objects.equals(favouriteList, user.favouriteList) && Objects.equals(fallowingList, user.fallowingList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, username, bio, image, password, favouriteList, fallowingList);
    }
}
