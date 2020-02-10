package org.codebeneath.lyrics.home;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.codebeneath.lyrics.impacted.Impacted;
import org.codebeneath.lyrics.impacted.ImpactedNotFoundException;
import org.codebeneath.lyrics.impacted.ImpactedRepository;
import org.codebeneath.lyrics.tag.Tag;
import org.codebeneath.lyrics.tag.TagRepository;
import org.codebeneath.lyrics.verse.Verse;
import org.codebeneath.lyrics.verse.VerseRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author black
 */
@Slf4j
@Controller
public class GlobalController {

    private static final int PAGE_SIZE  = 25;
    
    private final ImpactedRepository impactedRepo;
    private final VerseRepository verseRepo;
    private final TagRepository tagRepo;

    public GlobalController(ImpactedRepository iRepo, VerseRepository vRepo, TagRepository tRepo) {
        this.impactedRepo = iRepo;
        this.verseRepo = vRepo;
        this.tagRepo = tRepo;
    }

    @GetMapping("/global")
    public String impactedVersesGlobal(Model model, Principal principal,
            @RequestParam(name = "p", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "tag", required = false) String tag,
            @RequestParam(name = "q", required = false) String query) {

        Optional<Impacted> impactedUser = getImpactedUser(principal);
        if (impactedUser.isPresent()) {
            model.addAttribute("impacted", impactedUser.get());
        }
        
        List<Verse> verses;
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by(Order.desc("id")));
        if (tag != null) {
            verses = verseRepo.findByTags(tag, pageable);
        } else if (query != null) {
            verses = (List<Verse>) verseRepo.findByTextContainsIgnoreCase(query.trim(), pageable);
        } else {
            verses = (List<Verse>) verseRepo.findAll(pageable);
        }
        List<Tag> tags = (List<Tag>) tagRepo.findAll();
        model.addAttribute("verses", verses);
        model.addAttribute("allTags", tags);
        model.addAttribute("newLineChar", '\n');

        return (page == 0) ? "impacted/global" : "impacted/globalVersesScroll";
    }

    private Optional<Impacted> getImpactedUser(Principal p) {
        Optional<Impacted> impacted = Optional.empty();
        if (p != null) {
            impacted = impactedRepo.findByUserName(p.getName());
            if (!impacted.isPresent()) {
                throw new ImpactedNotFoundException(p.getName());
            }
        }
        return impacted;
    }

}
