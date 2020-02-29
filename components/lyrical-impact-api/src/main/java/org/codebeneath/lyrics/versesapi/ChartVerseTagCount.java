package org.codebeneath.lyrics.versesapi;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * POJO that is used to create the RestController json response for chartist graphs. 
 */
@Getter
@Setter
@ToString
public class ChartVerseTagCount {

    private List<String> labels;
    private List<String> series;
    
    public ChartVerseTagCount() {
    }

    public ChartVerseTagCount(List<String> labels, List<String> series) {
        this.labels = labels;
        this.series = series;
    }

}
