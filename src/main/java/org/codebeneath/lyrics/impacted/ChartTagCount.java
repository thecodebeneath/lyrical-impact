package org.codebeneath.lyrics.impacted;

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
public class ChartTagCount {

    private List<String> labels;
    private List<Long> series;
    
    public ChartTagCount() {
    }

    public ChartTagCount(List<String> labels, List<Long> series) {
        this.labels = labels;
        this.series = series;
    }

}
