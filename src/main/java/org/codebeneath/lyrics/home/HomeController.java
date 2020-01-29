package org.codebeneath.lyrics.home;

import java.security.Principal;
import java.util.Collections;
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
import org.springframework.security.access.prepost.PreAuthorize;
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

    private final ImpactedRepository impactedRepo;
    private final VerseRepository verseRepo;
    private final TagRepository tagRepo;

    public HomeController(ImpactedRepository iRepo, VerseRepository vRepo, TagRepository tRepo) {
        this.impactedRepo = iRepo;
        this.verseRepo = vRepo;
        this.tagRepo = tRepo;
    }

    @GetMapping("/my")
    public String impactedVerses(Model model, Principal principal,
            @RequestParam(name = "tag", required = false) String tag) {

        Impacted impactedUser = getImpactedUser(principal);

        List<Verse> verses;
        if (tag != null) {
            verses = verseRepo.findByImpactedIdAndTags(impactedUser.getId(), tag);
        } else {
            verses = verseRepo.findByImpactedId(impactedUser.getId());
        }
        Collections.reverse(verses);
        List<Tag> tags = (List<Tag>) tagRepo.findAll();
        model.addAttribute("impacted", impactedUser);
        model.addAttribute("verses", verses);
        model.addAttribute("allTags", tags);
        model.addAttribute("randomVerse", verseRepo.getRandomVerse());
        model.addAttribute("newLineChar", '\n');

        return "impacted/my";
    }

    @GetMapping("/global")
    public String impactedVersesGlobal(Model model, Principal principal,
            @RequestParam(name = "tag", required = false) String tag) {

        Impacted impactedUser = getImpactedUser(principal);

        List<Verse> verses;
        if (tag != null) {
            verses = verseRepo.findByTags(tag);
        } else {
            verses = (List<Verse>) verseRepo.findAll();
        }
        Collections.reverse(verses);
        List<Tag> tags = (List<Tag>) tagRepo.findAll();
        model.addAttribute("impacted", impactedUser);
        model.addAttribute("verses", verses);
        model.addAttribute("allTags", tags);
        model.addAttribute("newLineChar", '\n');

        return "impacted/globalTag";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/my/{id}")
    public String impactedVerses(Model model, Principal principal,
            @PathVariable(required = true) Long id,
            @RequestParam(name = "tag", required = false) String tag) {

        Impacted impactedUser = getImpactedUser(id);

        List<Verse> verses;
        if (tag != null) {
            verses = verseRepo.findByImpactedIdAndTags(impactedUser.getId(), tag);
        } else {
            verses = verseRepo.findByImpactedId(impactedUser.getId());
        }
        Collections.reverse(verses);
        List<Tag> tags = (List<Tag>) tagRepo.findAll();
        model.addAttribute("impacted", impactedUser);
        model.addAttribute("verses", verses);
        model.addAttribute("allTags", tags);
        model.addAttribute("randomVerse", verseRepo.getRandomVerse());
        model.addAttribute("newLineChar", '\n');

        return "impacted/my";
    }

    @GetMapping("/about")
    public String aboutPage(Model model) {
        return "about";
    }

    private Impacted getImpactedUser(Principal p) {
        Optional<Impacted> impacted = impactedRepo.findByUserName(p.getName());
        if (!impacted.isPresent()) {
            throw new ImpactedNotFoundException(p.getName());
        }
        return impacted.get();
    }

    private Impacted getImpactedUser(Long id) {
        Optional<Impacted> impacted = impactedRepo.findById(id);
        if (!impacted.isPresent()) {
            throw new ImpactedNotFoundException(id);
        }
        return impacted.get();
    }
}
