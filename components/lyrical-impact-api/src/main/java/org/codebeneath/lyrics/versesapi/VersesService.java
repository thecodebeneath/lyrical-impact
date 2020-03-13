package org.codebeneath.lyrics.versesapi;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.codebeneath.lyrics.tagsapi.VerseTag;
import org.codebeneath.lyrics.tagsapi.VerseTagsService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

/**
 *
 * @author black
 */
@Service
public class VersesService {

    private final Counter createdCounter = Metrics.counter("verses.created");
    private final Counter updatedCounter = Metrics.counter("verses.updated");
    private final Counter deletedCounter = Metrics.counter("verses.deleted");
    private final Counter createdFromGlobalCounter = Metrics.counter("verses.createdFromGlobal");
    
    private final VersesClient versesClient;
    private final VerseTagsService tagsService;

    public VersesService(VersesClient versesClient, VerseTagsService tagsService) {
        this.versesClient = versesClient;
        this.tagsService = tagsService;
    }

    public List<ImpactedVerse> global(Integer p, String tag, String q) {
        return versesClient.global(p, tag, q);
    }
    
    public Optional<ImpactedVerse> findById(Long vid) {
        return versesClient.findById(vid);
    }
    
    public Optional<ImpactedVerse> findByIdAndImpactedId(Long vid, Long impactedId) {
        return versesClient.findByIdAndImpactedId(vid, impactedId);
    }
    
    public List<ImpactedVerse> findByImpactedId(Long impactedId, Integer p, String tag, String q) {
        return versesClient.findByImpactedId(impactedId, p, tag, q);
    }
    
    @CacheEvict(value = "tagCounts", allEntries = true)
    public ImpactedVerse create(ImpactedVerse verse, boolean isFromGlobalVerse) {
        ImpactedVerse saved = versesClient.create(verse);
        
        createdCounter.increment();
        if (isFromGlobalVerse) {
            createdFromGlobalCounter.increment();
        }
                
        return saved;
    }

    @CacheEvict(value = "tagCounts", allEntries = true)
    public ImpactedVerse update(ImpactedVerse verse, Long vid) {
        ImpactedVerse saved = versesClient.update(verse, vid);
        
        updatedCounter.increment();
                
        return saved;
    }

    public void deleteVerseId(Long vid) {
        versesClient.deleteVerseId(vid);
        deletedCounter.increment();
    }   
    
    public ImpactedVerse getRandomVerse() {
        return versesClient.getRandomVerse();
    }
    
    @Cacheable("tagCounts")
    public ChartVerseTagCount allTagCounts() {
        List<String> labels = new ArrayList<>();
        List<String> series = new ArrayList<>();

        List<VerseTag> tags = tagsService.getVerseTags();
        tags.stream().forEach(tag -> {
            labels.add(tag.getLabel());
            series.add(versesClient.countByTags(tag.getLabel()));
        });
        return new ChartVerseTagCount(labels, series);
    }
}
