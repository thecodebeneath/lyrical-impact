package org.codebeneath.lyrics.versesapi;

import java.util.List;
import java.util.Optional;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author black
 */
@FeignClient(name = "lyrical-impact-verses")
public interface VersesClient {

    @GetMapping(path = "/verses/global", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ImpactedVerse> global(@RequestParam Integer p, @RequestParam String tag, @RequestParam String q);

    @GetMapping(path = "/verses/{vid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<ImpactedVerse> findById(@PathVariable Long vid);

    @GetMapping(path = "/verses/{vid}/{impactedId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<ImpactedVerse> findByIdAndImpactedId(@PathVariable Long vid,
            @PathVariable Long impactedId);

    @GetMapping(path = "/verses/byImpacted/{impactedId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ImpactedVerse> findByImpactedId(@PathVariable Long impactedId, @RequestParam Integer p, @RequestParam String tag, @RequestParam String q);
    
    @PostMapping(path = "/verses", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ImpactedVerse save(ImpactedVerse verse);

    @DeleteMapping(path = "/verses/{vid}")
    public void deleteVerseId(@PathVariable Long vid);
    
    @GetMapping(path = "/verses/random", produces = MediaType.APPLICATION_JSON_VALUE)
    public ImpactedVerse getRandomVerse();
            
    @GetMapping(path = "/verses/countBy/{tag}", produces = MediaType.TEXT_PLAIN_VALUE)
    public String countByTags(@PathVariable String tag);

}
