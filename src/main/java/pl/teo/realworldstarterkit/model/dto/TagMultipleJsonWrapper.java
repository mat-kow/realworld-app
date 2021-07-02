package pl.teo.realworldstarterkit.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter @Getter @NoArgsConstructor @AllArgsConstructor
public class TagMultipleJsonWrapper {
    private List<String> tags;
}
