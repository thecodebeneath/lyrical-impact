package org.codebeneath.lyrics.tags;

import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author black
 */
@RestController
@RequestMapping("/tags")
public class TagsRestController {

    private final TagsRepository repo;

    public TagsRestController(TagsRepository repo) {
        this.repo = repo;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Tag> getTags() {
        return repo.findAll();
    }

}
