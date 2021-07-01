package pl.teo.realworldstarterkit.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter @Setter
@Table(name = "users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String email;
    private String username;
    private String bio;
    private String image;
    private String password;
    @ManyToMany
    private List<Article> favouriteList;
    @ManyToMany
    private List<User> fallowingList;

}
