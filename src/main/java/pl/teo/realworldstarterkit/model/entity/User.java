package pl.teo.realworldstarterkit.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity @Data
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
