package pl.teo.realworldstarterkit.model.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Tag {
    @Id
    private String name;
}
