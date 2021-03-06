package org.codebeneath.lyrics.lookup.songs.musixmatch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class Body {

    private Lyrics lyrics;
    
}
