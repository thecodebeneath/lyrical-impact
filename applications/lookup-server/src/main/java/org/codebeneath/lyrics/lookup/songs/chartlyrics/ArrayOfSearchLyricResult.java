package org.codebeneath.lyrics.lookup.songs.chartlyrics;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.ToString;

/**
 * ChartLyrics API
 * Ref: http://www.chartlyrics.com/api.aspx
 */
@Getter
@ToString
@XmlRootElement(name = "ArrayOfSearchLyricResult")
public class ArrayOfSearchLyricResult {
    @XmlElement(name = "SearchLyricResult")
    private List<SearchLyricResult> searchLyricResult;
}
