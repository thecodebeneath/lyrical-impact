package org.codebeneath.lyrics.lookup;

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
@RequestMapping
public class LookupPoetryController {

    LookupPoetryService service;
    
    public LookupPoetryController(LookupPoetryService service) {
        this.service = service;
    }
    
    @GetMapping(path = "/lookupPoemLyrics", produces = MediaType.APPLICATION_JSON_VALUE)
    public LookupSuggestion lookupPoemLyrics(@RequestParam(name = "lyrics", required = true) String lyrics) {
        return service.lookupPoemLyrics(lyrics);
    }

}
