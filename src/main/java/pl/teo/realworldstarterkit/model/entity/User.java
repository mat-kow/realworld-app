package pl.teo.realworldstarterkit.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

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

}
