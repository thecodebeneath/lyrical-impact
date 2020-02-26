package org.codebeneath.lyrics.tags;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author black
 */
@RestController
public class TagsRestController {

    private final TagsRepository repo;

    public TagsRestController(TagsRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/tags")
    public List<Tag> getTags() {
        List<Tag> tags = repo.findAll();
        return tags;
    }

}
