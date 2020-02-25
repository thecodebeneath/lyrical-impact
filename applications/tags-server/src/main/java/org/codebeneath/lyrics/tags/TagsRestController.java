package org.codebeneath.lyrics.tags;

import java.lang.reflect.Type;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author black
 */
@RestController
public class TagsRestController {

    @Autowired
    private ModelMapper mapper;
    
    private Type listType = new TypeToken<List<TagDto>>() {}.getType();
    
    private final TagsRepository repo;

    public TagsRestController(TagsRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/tags")
    public List<TagDto> getTags() {
        List<Tag> tags = repo.findAll();
        List<TagDto> result = mapper.map(tags, listType);
        return result;
    }

}
