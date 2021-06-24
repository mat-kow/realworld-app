package pl.teo.realworldstarterkit.model.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity @Data
public class Tag {
    @Id
    private String name;
}
