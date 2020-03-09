package org.codebeneath.lyrics.lookup.songs.chartlyrics;

import javax.xml.bind.annotation.XmlElement;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Row {
//    @XmlElement(name = "county")
//    private String county;
    
    @XmlElement(name = "salestax")
    private String salestax;
    
}
