package org.codebeneath.lyrics.lookup.songs.chartlyrics;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import lombok.Getter;

@Getter
public class RowElement {
    @XmlElement(name = "row")
    private List<Row> rows;
}
