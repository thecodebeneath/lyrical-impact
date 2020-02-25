package org.codebeneath.lyrics.tag;

import java.util.ArrayList;
import java.util.List;
import org.codebeneath.lyrics.tag.TagsClient.TagsClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author black
 */
@FeignClient(name = "lyrical-impact-tags", fallback = TagsClientFallback.class)
public interface TagsClient {

    @GetMapping("/tags")
    List<Tag> getTags();

    @Component
    static class TagsClientFallback implements TagsClient {
        @Override
        public List<Tag> getTags() {
            List<Tag> fallbackTags = new ArrayList<>();
            fallbackTags.add(new Tag("fallback1"));
            fallbackTags.add(new Tag("fallback2"));
            return fallbackTags;
        }
    }
}
