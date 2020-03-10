package org.codebeneath.lyrics.lookupapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author black
 */
@Slf4j
@RestController
@RequestMapping("/lookup")
public class LookupController {

    LookupClient lookupClient;
    
    public LookupController(LookupClient lookupClient) {
        this.lookupClient = lookupClient;
    }
    
    @GetMapping(path = "/song", produces = MediaType.APPLICATION_JSON_VALUE)
    public LookupSuggestion lookupSongLyrics(@RequestParam(name = "lyrics", required = true) String lyrics) {
        return lookupClient.lookupSongLyrics(lyrics);
    }

    @GetMapping(path = "/poem", produces = MediaType.APPLICATION_JSON_VALUE)
    public LookupSuggestion lookupPoemLyrics(@RequestParam(name = "lyrics", required = true) String lyrics) {
        return lookupClient.lookupPoemLyrics(lyrics);
    }

}