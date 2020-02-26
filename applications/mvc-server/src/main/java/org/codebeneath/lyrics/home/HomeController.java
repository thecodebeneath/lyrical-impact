package org.codebeneath.lyrics.home;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.codebeneath.lyrics.impacted.Impacted;
import org.codebeneath.lyrics.impacted.ImpactedNotFoundException;
import org.codebeneath.lyrics.impacted.ImpactedRepository;
import org.codebeneath.lyrics.tagsapi.TagDto;
import org.codebeneath.lyrics.tagsapi.TagsClient;
import org.codebeneath.lyrics.verse.Verse;
import org.codebeneath.lyrics.verse.VerseRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author black
 */
@Slf4j
@Controller
public class HomeController {

    private static final int PAGE_SIZE  = 25;
    
    private final ImpactedRepository impactedRepo;
    private final VerseRepository verseRepo;
    private final TagsClient tagsClient;

    public HomeController(ImpactedRepository iRepo, VerseRepository vRepo, TagsClient tagsClient) {
        this.impactedRepo = iRepo;
        this.verseRepo = vRepo;
        this.tagsClient = tagsClient;
    }

    @GetMapping("/my")
    public String impactedVerses(Model model, @AuthenticationPrincipal Impacted impactedUser,
            @RequestParam(name = "p", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "tag", required = false) String tag,
            @RequestParam(name = "q", required = false) String query) {

        setImpactedInModel(impactedUser, model);

        List<Verse> verses;
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by(Sort.Order.desc("id")));
        if (tag != null) {
            verses = verseRepo.findByImpactedIdAndTags(impactedUser.getId(), tag, pageable);
        } else if (query != null) {
            verses = (List<Verse>) verseRepo.findByImpactedIdAndTextContainsIgnoreCase(impactedUser.getId(), query.trim(), pageable);
        } else {
            verses = verseRepo.findByImpactedId(impactedUser.getId(), pageable);
        }
        List<TagDto> tags = tagsClient.getTags();
        model.addAttribute("verses", verses);
        model.addAttribute("allTags", tags);
        model.addAttribute("randomVerse", verseRepo.getRandomVerse());
        model.addAttribute("newLineChar", '\n');

        return (page == 0) ? "impacted/my" : "impacted/myVersesScroll";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/my/{id}")
    public String impactedVerses(Model model, @AuthenticationPrincipal Impacted impactedUser,
            @PathVariable(required = true) Long id,
            @RequestParam(name = "tag", required = false) String tag) {

        Impacted otherImpactedUser = getImpactedUser(id);
        setImpactedInModel(otherImpactedUser, model);

        List<Verse> verses;
        if (tag != null) {
            verses = verseRepo.findByImpactedIdAndTags(impactedUser.getId(), tag);
        } else {
            verses = verseRepo.findByImpactedId(impactedUser.getId());
        }
        Collections.reverse(verses);
        List<TagDto> tags = tagsClient.getTags();
        model.addAttribute("verses", verses);
        model.addAttribute("allTags", tags);
        model.addAttribute("randomVerse", verseRepo.getRandomVerse());
        model.addAttribute("newLineChar", '\n');

        return "impacted/my";
    }

    private void setImpactedInModel(Impacted impactedUser, Model model) {
        // https://github.com/spring-projects/spring-security/issues/3208
        if (impactedUser != null) {
            model.addAttribute("impacted", impactedUser);
        }
    }

    private Impacted getImpactedUser(Long id) {
        Optional<Impacted> impacted = impactedRepo.findById(id);
        if (!impacted.isPresent()) {
            throw new ImpactedNotFoundException(id);
        }
        return impacted.get();
    }
}
