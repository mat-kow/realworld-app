package pl.teo.realworldstarterkit.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter @Setter
public class Author {
    private String username;
    private String bio;
    private String image;
    private boolean following;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return following == author.following && Objects.equals(username, author.username) && Objects.equals(bio, author.bio) && Objects.equals(image, author.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, bio, image, following);
    }
}
