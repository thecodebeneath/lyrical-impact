package org.codebeneath.lyrics.lookupapi;

import org.codebeneath.lyrics.lookupapi.LookupClient.LookupClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author black
 */
@FeignClient(name = "lyrical-impact-lookup", fallback = LookupClientFallback.class)
public interface LookupClient {

    @GetMapping(path = "/lookupSongLyrics", produces = MediaType.APPLICATION_JSON_VALUE)
    LookupSuggestion lookupSongLyrics(@RequestParam(name = "lyrics", required = true) String lyrics);

    @GetMapping(path = "/lookupPoemLyrics", produces = MediaType.APPLICATION_JSON_VALUE)
    LookupSuggestion lookupPoemLyrics(@RequestParam(name = "lyrics", required = true) String lyrics);
    
    @Component
    static class LookupClientFallback implements LookupClient {
        @Override
        public LookupSuggestion lookupSongLyrics(@RequestParam(name = "lyrics", required = true) String lyrics) {
            return LookupSuggestion.noSuggestionResult();
        }        
        @Override
        public LookupSuggestion lookupPoemLyrics(@RequestParam(name = "lyrics", required = true) String lyrics) {
            return LookupSuggestion.noSuggestionResult();
        }
    }
}
