package org.codebeneath.lyrics.lookup.poems.poetrydb;

import lombok.extern.slf4j.Slf4j;
import org.codebeneath.lyrics.lookup.LookupSuggestion;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * PoetryDB API Ref: https://github.com/thundercomb/poetrydb/blob/master/README.md
 */
@Slf4j
@Component
public class PoetryDbClient {

    public static final String URL = "http://poetrydb.org/lines/{lines}";
    private final RestTemplate restTemplate = new RestTemplate();

    public LookupSuggestion lookup(String lyrics) throws RestClientException {
//        lyrics = "foe to my own friends";
        PoetryDbEntry[] p = restTemplate.getForObject(URL, PoetryDbEntry[].class, lyrics);
        
        LookupSuggestion suggestion = new LookupSuggestion();
        if (p != null && p[0] != null) {
            suggestion.setTitle(p[0].getTitle());
            suggestion.setAuthor(p[0].getAuthor());
        }
        return suggestion;
    }
}
