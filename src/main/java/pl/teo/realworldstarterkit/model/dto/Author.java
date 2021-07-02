package pl.teo.realworldstarterkit.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Author {
    private String username;
    private String bio;
    private String image;
    private boolean following;

}
