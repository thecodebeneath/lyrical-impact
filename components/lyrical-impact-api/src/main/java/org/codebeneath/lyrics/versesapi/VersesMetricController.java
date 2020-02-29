package org.codebeneath.lyrics.versesapi;

import java.util.ArrayList;
import java.util.List;
import org.codebeneath.lyrics.tagsapi.VerseTag;
import org.codebeneath.lyrics.tagsapi.TagsClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController
@RequestMapping("/verse/metrics")
public class VersesMetricController {

    private final VersesClient versesClient;
    private final TagsClient tagsClient;

    public VersesMetricController(VersesClient versesClient, TagsClient tagsClient) {
        this.versesClient = versesClient;
        this.tagsClient = tagsClient;
    }

    @GetMapping("/tags")
    public ChartVerseTagCount metricTagCount() {
        return allTagCounts();
    }

    private ChartVerseTagCount allTagCounts() {
        List<String> labels = new ArrayList<>();
        List<String> series = new ArrayList<>();

        List<VerseTag> tags = tagsClient.getVerseTags();
        tags.stream().forEach(tag -> {
            labels.add(tag.getLabel());
            series.add(versesClient.countByTags(tag.getLabel()));
        });
        return new ChartVerseTagCount(labels, series);
    }
}
