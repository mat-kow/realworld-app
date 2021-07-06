package pl.teo.realworldstarterkit.model.entity;

import lombok.*;

import javax.persistence.*;

@Entity(name = "tags")
@Getter @Setter @NoArgsConstructor
public class Tag {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;

    public Tag(String name) {
        this.name = name;
    }
}
