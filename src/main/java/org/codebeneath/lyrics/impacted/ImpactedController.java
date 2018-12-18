package org.codebeneath.lyrics.impacted;

import java.security.Principal;
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
import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@Slf4j
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

    @GetMapping("/impacted")
    public String impactedVerses(Model model, Principal principal,
                                 @RequestParam(name = "tag", required = false) String tag) {
        
        log.info("Principal: {}", principal);
        log.info("Name: {}", principal.getName());

        Impacted impactedUser = getImpactedUserForPrincipal(principal);

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
    
    // TODO: testing only to get other users by url
    @GetMapping("/impacted/{id}")
    public String impactedVerses(Model model, Principal principal,
                                 @PathVariable(required = true) Long id, 
                                 @RequestParam(name = "tag", required = false) String tag) {
        
        log.info("Principal: {}", principal);
        log.info("Name: {}", principal.getName());
        
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

    @GetMapping("/about")
    public String aboutPage(Model model) {
        return "about";
    }

    private Impacted getImpactedUserForPrincipal(Principal p) {
        Impacted impacted = null;
        if (p.getName().equals("jeff")) {
            impacted = getImpactedUser(1L);
        } else if (p.getName().equals("alan")) {
            impacted = getImpactedUser(2L);
        }
        return impacted;
    }

    private Impacted getImpactedUser(Long id) {
        if (!impactedRepo.findById(id).isPresent()) {
            throw new ImpactedNotFoundException(id);
        }
        return impactedRepo.findById(id).get();
    }
}
