package org.codebeneath.lyrics.impacted;

import org.codebeneath.lyrics.tag.Tag;
import org.codebeneath.lyrics.tag.TagRepository;
import org.codebeneath.lyrics.verse.Verse;
import org.codebeneath.lyrics.verse.VerseRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;

/**
 *
 */
@Controller
public class ImpactedController {

    private final ImpactedRepository impactedRepo;
    private final VerseRepository verseRepo;
    private final TagRepository tagRepo;

    public ImpactedController(ImpactedRepository iRepo, VerseRepository vRepo, TagRepository tRepo) {
        this.impactedRepo = iRepo;
        this.verseRepo = vRepo;
        this.tagRepo = tRepo;
    }

    @GetMapping("/")
    public String impactedVerses(Model model) {
        model.addAttribute("attribute", "forwardWithForwardPrefix");
        return "forward:/impacted/1";
    }
    
    @GetMapping("/impacted/{id}")
    public String impactedVerses(@PathVariable(required = true) Long id, Model model, @RequestParam(name = "tag", required = false) String tag) {
        Impacted impactedUser = getImpactedUser(id);
        
        List<Verse> verses;        
        if (tag != null) {
            verses = verseRepo.findByImpactedIdAndTagsLabel(impactedUser.getId(), tag);
        } else {
            verses = verseRepo.findByImpactedId(impactedUser.getId());
        }
        Collections.reverse(verses);
        List<Tag> tags = tagRepo.findByImpacted(impactedUser);
        model.addAttribute("impacted", impactedUser);
        model.addAttribute("verses", verses);
        model.addAttribute("tags", tags);
        model.addAttribute("randomVerse", verseRepo.getRandomVerse());

        return "impacted";
    }

    private Impacted getImpactedUser(Long id) {
        // TODO: hardcoded for now...
        id = 1L;
        
        if (!impactedRepo.findById(id).isPresent()) {
            throw new ImpactedNotFoundException(id);
        }
        return impactedRepo.findById(id).get();
    }
}
