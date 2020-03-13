package org.codebeneath.lyrics.verses;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@Slf4j
@RestController
@RequestMapping("/verses")
public class VersesRestController {

    private static final int PAGE_SIZE  = 25;
    
    private final VersesRepository vRepo;

    public VersesRestController(VersesRepository vRepo) {
        this.vRepo = vRepo;
    }

    @GetMapping(path = "/global", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Verse> findGlobal(
            @RequestParam(name = "p", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "tag", required = false) String tag,
            @RequestParam(name = "q", required = false) String query) {
        
        List<Verse> verses;
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by(Sort.Order.desc("id")));
        if (tag != null && query != null) {
            verses = (List<Verse>) vRepo.findByTagsAndTextContainsIgnoreCase(tag, query.trim(), pageable);
        } else if (tag != null) {
            verses = vRepo.findByTags(tag, pageable);
        } else if (query != null) {
            verses = (List<Verse>) vRepo.findByTextContainsIgnoreCase(query.trim(), pageable);
        } else {
            verses = (List<Verse>) vRepo.findAll(pageable);
        }
        return verses;
    }

    @GetMapping(path = "/{vid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Verse findById(@PathVariable Long vid) {
        return vRepo.findById(vid).get();
    }

    @GetMapping(path = "/{vid}/{impactedId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Verse findByIdAndImpactedId(@PathVariable Long vid,
            @PathVariable Long impactedId) {
        return vRepo.findByIdAndImpactedId(vid, impactedId).get();
    }
    
    @GetMapping(path = "/byImpacted/{impactedId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Verse> findByImpactedId(@PathVariable Long impactedId,
            @RequestParam(name = "p", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "tag", required = false) String tag,
            @RequestParam(name = "q", required = false) String query) {
        
        List<Verse> verses;
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by(Sort.Order.desc("id")));
        if (tag != null && query != null) {
            verses = (List<Verse>) vRepo.findByImpactedIdAndTagsAndTextContainsIgnoreCase(impactedId, tag, query.trim(), pageable);
        } else if (tag != null) {
            verses = vRepo.findByImpactedIdAndTags(impactedId, tag, pageable);
        } else if (query != null) {
            verses = (List<Verse>) vRepo.findByImpactedIdAndTextContainsIgnoreCase(impactedId, query.trim(), pageable);
        } else {
            verses = vRepo.findByImpactedId(impactedId, pageable);
        }
        return verses;
    }

    @GetMapping(path = "/byImpacted/{impactedId}/{tag}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Verse> findByImpactedIdAndTags(@PathVariable Long impactedId, @PathVariable String tag) {
        return vRepo.findByImpactedIdAndTags(impactedId, tag);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Verse create(@RequestBody Verse verse) {
        if (verse.getId() != null) {
            verse.setId(null);
        }
        return vRepo.save(verse);
    }

    @PutMapping(path = "/{vid}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Verse update(@RequestBody Verse verse, @PathVariable Long vid) {
        if (verse.getId() == null || !verse.getId().equals(vid)) {
            verse.setId(vid);
        }
        return vRepo.save(verse);
    }

    @DeleteMapping(path = "/{vid}")
    public void deleteVerseId(@PathVariable Long vid) {
        vRepo.deleteById(vid);
    }

    @GetMapping(path = "/random", produces = MediaType.APPLICATION_JSON_VALUE)
    public Verse getRandomVerse() {
        return vRepo.getRandomVerse();
    }
    
    @GetMapping(path = "/countBy/{tag}", produces = MediaType.TEXT_PLAIN_VALUE)
    public String countByTags(@PathVariable String tag) {
        return "" + vRepo.countByTags(tag);
    }
}
