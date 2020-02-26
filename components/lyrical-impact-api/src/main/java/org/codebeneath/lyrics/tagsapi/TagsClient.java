package org.codebeneath.lyrics.tagsapi;

import java.util.ArrayList;
import java.util.List;
import org.codebeneath.lyrics.tagsapi.TagsClient.TagsClientFallback;
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
    List<TagDto> getTags();
    
    @Component
    static class TagsClientFallback implements TagsClient {
        @Override
        public List<TagDto> getTags() {
            List<TagDto> fallbackTags = new ArrayList<>();
            fallbackTags.add(new TagDto("fallback1"));
            fallbackTags.add(new TagDto("fallback2"));
            return fallbackTags;
        }
    }
}
