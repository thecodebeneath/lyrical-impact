package org.codebeneath.lyrics.tagsapi;

import static java.util.Arrays.asList;
import java.util.List;
import org.codebeneath.lyrics.tagsapi.TagsClient.TagsClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author black
 */
@FeignClient(name = "lyrical-impact-tags", fallback = TagsClientFallback.class)
public interface TagsClient {

    @GetMapping(path = "/tags", produces = MediaType.APPLICATION_JSON_VALUE)
    List<VerseTag> getVerseTags();
    
    @Component
    static class TagsClientFallback implements TagsClient {
        @Override
        public List<VerseTag> getVerseTags() {
            return asList(new VerseTag("north"), new VerseTag("south"), new VerseTag("east"), new VerseTag("west"));
        }
    }
}
