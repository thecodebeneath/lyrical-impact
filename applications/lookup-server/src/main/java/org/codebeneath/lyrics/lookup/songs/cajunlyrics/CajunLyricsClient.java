package org.codebeneath.lyrics.lookup.songs.cajunlyrics;

import lombok.extern.slf4j.Slf4j;
import org.codebeneath.lyrics.lookup.LookupSuggestion;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * CajunLyrics API
 * Ref: https://www.cajunlyrics.com/?page=api
 */
@Slf4j
@Component
public class CajunLyricsClient {

    public static final String URL = "http://api.cajunlyrics.com/LyricDirectSearch.php?artist={artist}&title={title}";
    private final RestTemplate restTemplate = new RestTemplate();

    public LookupSuggestion lookup(String lyrics) throws RestClientException {
        String cajun = restTemplate.getForObject(URL, String.class, "Buckwheat Zydeco", "Tee Nah Nah");
        log.info("Cajun as Str: {}", cajun);

        CajunGetLyricResult cajunLyrics = restTemplate.getForObject(URL, CajunGetLyricResult.class, "Buckwheat Zydeco", "Tee Nah Nah");
        log.info("Cajun as Obj: {}", cajunLyrics.toString());
        
        LookupSuggestion suggestion = new LookupSuggestion();
        if (cajunLyrics != null) {
            suggestion.setTitle(cajunLyrics.getTitle());
            suggestion.setAuthor(cajunLyrics.getArtist());
        }
        return suggestion;
    }

}
