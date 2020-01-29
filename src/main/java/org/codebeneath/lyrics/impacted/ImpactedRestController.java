package org.codebeneath.lyrics.impacted;

import java.util.ArrayList;
import java.util.List;
import org.codebeneath.lyrics.tag.Tag;
import org.codebeneath.lyrics.tag.TagRepository;
import org.codebeneath.lyrics.verse.VerseRepository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController
public class ImpactedRestController {

    private final ImpactedRepository repo;
    private final VerseRepository vRepo;
    private final TagRepository tRepo;
    
    public ImpactedRestController(ImpactedRepository repo, VerseRepository vRepo, TagRepository tRepo) {
        this.repo = repo;
        this.vRepo = vRepo;
        this.tRepo = tRepo;
    }

    @RequestMapping("/impacted/{id}/json")
    public Impacted impactedVerses(@PathVariable(required = true) Long id) {
        return repo.findById(id).get();
    }
    
    @RequestMapping("/impacted/metrics/tags")
    public ChartTagCount metricTagCount() {
        return allTagCounts();
    }
    
    private ChartTagCount allTagCounts() {
        List<String> labels = new ArrayList<>();
        List<Long> series = new ArrayList<>();

        List<Tag> tags = (List<Tag>) tRepo.findAll();
        tags.stream().forEach(tag -> {
            labels.add(tag.getLabel());
            series.add(vRepo.countByTags(tag.getLabel()));
        });
        return new ChartTagCount(labels, series);
    }
}
