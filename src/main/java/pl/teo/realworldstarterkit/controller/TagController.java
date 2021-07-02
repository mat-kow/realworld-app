package pl.teo.realworldstarterkit.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.teo.realworldstarterkit.model.dto.TagMultipleJsonWrapper;
import pl.teo.realworldstarterkit.service.TagService;

@RestController
@RequestMapping("/api/tags")
public class TagController {
    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public TagMultipleJsonWrapper getAll() {
        return tagService.getAll();
    }
}
