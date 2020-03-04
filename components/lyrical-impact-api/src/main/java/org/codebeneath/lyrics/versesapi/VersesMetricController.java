package org.codebeneath.lyrics.versesapi;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController
@RequestMapping("/verse/metrics")
public class VersesMetricController {

    private final VersesService versesService;

    public VersesMetricController(VersesService versesService) {
        this.versesService = versesService;
    }

    @GetMapping("/tags")
    public ChartVerseTagCount metricTagCount() {
        return versesService.allTagCounts();
    }
}
