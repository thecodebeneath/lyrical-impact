package org.codebeneath.lyrics.verse;

import org.codebeneath.lyrics.impacted.Impacted;
import org.codebeneath.lyrics.impacted.ImpactedRepository;
import org.codebeneath.lyrics.tag.TagRepository;
import org.codebeneath.lyrics.impacted.ImpactedNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;
import javax.validation.Valid;
import org.springframework.validation.BindingResult;

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
    public String addVerse(@Valid Verse verse, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "verseForm";
        }
        Impacted impactedUser = getImpactedUser(1L);
        verse.setImpacted(impactedUser);
        verseRepo.save(verse);

        return "redirect:/";
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
