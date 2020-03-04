package org.codebeneath.lyrics.tagsapi;

import java.util.List;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 *
 * @author black
 */
@Service
public class VerseTagsService {

    private final TagsClient tagsClient;

    public VerseTagsService(TagsClient tagsClient) {
        this.tagsClient = tagsClient;
    }

    @Cacheable(value = "tags", unless="#result?.size() < 5")
    public List<VerseTag> getVerseTags() {
        return tagsClient.getVerseTags();
    }

    @CacheEvict(value = "tags", allEntries = true)
    public void flushCache() {
    }

}
