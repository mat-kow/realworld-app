package pl.teo.realworldstarterkit.service.impl;

import org.springframework.stereotype.Service;
import pl.teo.realworldstarterkit.model.dto.TagMultipleJsonWrapper;
import pl.teo.realworldstarterkit.model.entity.Tag;
import pl.teo.realworldstarterkit.model.repository.TagRepo;
import pl.teo.realworldstarterkit.service.TagService;

import java.util.stream.Collectors;

@Service
public class TegServiceDefault implements TagService {
    private final TagRepo tagRepo;

    public TegServiceDefault(TagRepo tagRepo) {
        this.tagRepo = tagRepo;
    }

    @Override
    public TagMultipleJsonWrapper getAll() {
        return new TagMultipleJsonWrapper(tagRepo.findAll().stream().map(Tag::getName).collect(Collectors.toList()));
    }
}
