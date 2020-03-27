package org.codebeneath.lyrics.lookup.songs.musixmatch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.xml.bind.annotation.XmlElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Song API : MusixMatch
 * https://developer.musixmatch.com/documentation
 */
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Lyrics {

    @XmlElement(name = "lyrics_body")
    private String lyricsBody;

}
