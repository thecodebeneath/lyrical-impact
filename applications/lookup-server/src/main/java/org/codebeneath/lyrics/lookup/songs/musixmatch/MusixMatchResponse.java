package org.codebeneath.lyrics.lookup.songs.musixmatch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Song API : MusixMatch
 * https://developer.musixmatch.com/documentation
 * Note: does not work because response mimetype is text/plain instead of application/json
 */
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class MusixMatchResponse {

    private Message message;
    
}
