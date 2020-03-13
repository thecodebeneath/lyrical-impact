package org.codebeneath.lyrics.versesapi;

import static java.util.Arrays.asList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import org.codebeneath.lyrics.versesapi.VersesClient.VersesClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author black
 */
@FeignClient(name = "lyrical-impact-verses", fallback = VersesClientFallback.class)
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
    public ImpactedVerse create(@RequestBody ImpactedVerse verse);

    @PutMapping(path = "/verses/{vid}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ImpactedVerse update(@RequestBody ImpactedVerse verse, @PathVariable Long vid);

    @DeleteMapping(path = "/verses/{vid}")
    public void deleteVerseId(@PathVariable Long vid);
    
    @GetMapping(path = "/verses/random", produces = MediaType.APPLICATION_JSON_VALUE)
    public ImpactedVerse getRandomVerse();
            
    @GetMapping(path = "/verses/countBy/{tag}", produces = MediaType.TEXT_PLAIN_VALUE)
    public String countByTags(@PathVariable String tag);

    @Component
    static class VersesClientFallback implements VersesClient {

        private static final ImpactedVerse FALLBACK_VERSE = new ImpactedVerse("I know they're in here somewhere...", "Whoops!", "Verses are being located. Stand by!", "", -1L, asList(""));
        private static final Random rnd = new Random();
        
        @Override
        public List<ImpactedVerse> global(@RequestParam Integer p, @RequestParam String tag, @RequestParam String q) {
            return asList(FALLBACK_VERSE);
        }

        @Override
        public Optional<ImpactedVerse> findById(Long vid) {
            return Optional.empty();
        }

        @Override
        public Optional<ImpactedVerse> findByIdAndImpactedId(Long vid, Long impactedId) {
            return Optional.empty();
        }

        @Override
        public List<ImpactedVerse> findByImpactedId(Long impactedId, Integer p, String tag, String q) {
            return asList(FALLBACK_VERSE);
        }

        @Override
        public ImpactedVerse create(@RequestBody ImpactedVerse verse) {
            throw new UnsupportedOperationException("No fallback action available.");
        }

        @Override
        public ImpactedVerse update(@RequestBody ImpactedVerse verse, @PathVariable Long vid) {
            throw new UnsupportedOperationException("No fallback action available.");
        }

        @Override
        public void deleteVerseId(Long vid) {
            throw new UnsupportedOperationException("No fallback action available.");
        }

        @Override
        public ImpactedVerse getRandomVerse() {
            return FALLBACK_VERSE;
        }

        @Override
        public String countByTags(String tag) {
            return "" + rnd.nextInt(10)+1;
        }
    }
}
