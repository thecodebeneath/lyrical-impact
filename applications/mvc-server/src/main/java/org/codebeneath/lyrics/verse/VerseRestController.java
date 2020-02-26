package org.codebeneath.lyrics.verse;

import java.util.ArrayList;
import java.util.List;
import org.codebeneath.lyrics.tagsapi.TagDto;
import org.codebeneath.lyrics.tagsapi.TagsClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController
public class VerseRestController {

    private final VerseRepository vRepo;
    private final TagsClient tagsClient;

    public VerseRestController(VerseRepository vRepo, TagsClient tagsClient) {
        this.vRepo = vRepo;
        this.tagsClient = tagsClient;
    }

    @RequestMapping("/verse/metrics/tags")
    public ChartVerseTagCount metricTagCount() {
        return allTagCounts();
    }

    private ChartVerseTagCount allTagCounts() {
        List<String> labels = new ArrayList<>();
        List<Long> series = new ArrayList<>();

        List<TagDto> tags = tagsClient.getTags();
        tags.stream().forEach(tag -> {
            labels.add(tag.getLabel());
            series.add(vRepo.countByTags(tag.getLabel()));
        });
        return new ChartVerseTagCount(labels, series);
    }
}
