package org.codebeneath.lyrics.home;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.codebeneath.lyrics.impacted.Impacted;
import org.codebeneath.lyrics.tag.Tag;
import org.codebeneath.lyrics.tag.TagRepository;
import org.codebeneath.lyrics.verse.Verse;
import org.codebeneath.lyrics.verse.VerseRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    
    private final VerseRepository verseRepo;
    private final TagRepository tagRepo;

    public GlobalController(VerseRepository vRepo, TagRepository tRepo) {
        this.verseRepo = vRepo;
        this.tagRepo = tRepo;
    }

    @GetMapping("/global")
    public String impactedVersesGlobal(Model model, @AuthenticationPrincipal Impacted impactedUser,
            @RequestParam(name = "p", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "tag", required = false) String tag,
            @RequestParam(name = "q", required = false) String query) {
        
        setImpactedInModel(impactedUser, model);

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
    
    private void setImpactedInModel(Impacted impactedUser, Model model) {
        // https://github.com/spring-projects/spring-security/issues/3208
        if (impactedUser != null) {
            model.addAttribute("impacted", impactedUser);
        }
    }
}
