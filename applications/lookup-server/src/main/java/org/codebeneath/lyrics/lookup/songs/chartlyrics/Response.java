package org.codebeneath.lyrics.lookup.songs.chartlyrics;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@XmlRootElement(name = "response")
public class Response {
    @XmlElement(name = "row")
    private RowElement row;
}
