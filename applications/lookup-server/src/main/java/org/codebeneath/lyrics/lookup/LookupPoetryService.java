package org.codebeneath.lyrics.lookup;

import lombok.extern.slf4j.Slf4j;
import org.codebeneath.lyrics.lookup.poems.poetrydb.PoetryDbClient;
import org.springframework.stereotype.Service;

/**
 *
 * @author black
 */
@Slf4j
@Service
public class LookupPoetryService {
    
    PoetryDbClient poetryDbClient;
    
    public LookupPoetryService(PoetryDbClient poetryDbClient) {
        this.poetryDbClient = poetryDbClient;
    }
    
    public LookupSuggestion lookupPoemLyrics(String lyrics) {
        return poetryDbClient.lookup(lyrics);
    }
}
