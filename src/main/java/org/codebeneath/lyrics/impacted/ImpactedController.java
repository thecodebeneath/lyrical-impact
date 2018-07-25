package org.codebeneath.lyrics.impacted;

import org.codebeneath.lyrics.tag.Tag;
import org.codebeneath.lyrics.tag.TagRepository;
import org.codebeneath.lyrics.verse.Verse;
import org.codebeneath.lyrics.verse.VerseRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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

    @GetMapping("/impacted/{id}")
    public String impactedVerses(@PathVariable(required = true) Long id, Model model) {
        validateImpacted(id);

        Impacted jeff = impactedRepo.findById(id).get();
        List<Verse> verses = verseRepo.findByImpactedId(jeff.getId());
        Collections.reverse(verses);
        Verse randomVerse = verses.get(5);
        List<Tag> tags = tagRepo.findByImpacted(jeff);
        model.addAttribute("impacted", jeff);
        model.addAttribute("verses", verses);
        model.addAttribute("tags", tags);
        model.addAttribute("randomVerse", randomVerse);

        return "impacted";
    }

    private void validateImpacted(Long id) {
        if (!impactedRepo.findById(id).isPresent()) {
            throw new ImpactedNotFoundException(id);
        }
    }
}
