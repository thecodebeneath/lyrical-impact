package org.codebeneath.lyrics.verse;

import org.codebeneath.lyrics.impacted.Impacted;
import org.codebeneath.lyrics.impacted.ImpactedRepository;
import org.codebeneath.lyrics.tag.TagRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class VerseController {

    private static final Logger LOG = LoggerFactory.getLogger(VerseController.class);

    private final ImpactedRepository impactedRepo;
    private final VerseRepository verseRepo;
    private final TagRepository tagRepo;

    public VerseController(ImpactedRepository iRepo, VerseRepository vRepo, TagRepository tRepo) {
        this.impactedRepo = iRepo;
        this.verseRepo = vRepo;
        this.tagRepo = tRepo;
    }

    @GetMapping("/verse")
    public String getVerseForm(Model model, @RequestParam Optional<Long> randomVerseId) {
        Verse verseToPopulateWith = new Verse();
        if (randomVerseId.isPresent()) {
            verseToPopulateWith = verseRepo.findById(randomVerseId.get()).get();
        }
        model.addAttribute("verse", verseToPopulateWith);
        return "verseForm";
    }

    @PostMapping("/verse")
    public String addVerse(@ModelAttribute Verse verse, Model model) {
        Impacted jeff = impactedRepo.findById(1L).get();
        verse.setImpacted(jeff);
        verseRepo.save(verse);

        return "redirect:/impacted/" + jeff.getId();
    }
}
