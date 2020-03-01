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
public class LookupRestController {

    @GetMapping(path = "/lookupSongLyrics", produces = MediaType.APPLICATION_JSON_VALUE)
    public String lookupSongLyrics(@RequestParam(name = "lyrics", required = true) String lyrics) {
        log.warn("not yet implemented... lyrics=" + lyrics);
        return "{\"result\" : \"not yet implemented...\"}";
    }

    @GetMapping(path = "/lookupPoemLyrics", produces = MediaType.APPLICATION_JSON_VALUE)
    public String lookupPoemLyrics(@RequestParam(name = "lyrics", required = true) String lyrics) {
        log.warn("not yet implemented... lyrics=" + lyrics);
        return "{\"result\" : \"not yet implemented...\"}";
    }

}
