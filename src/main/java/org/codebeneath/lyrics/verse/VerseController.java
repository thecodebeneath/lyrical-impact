package org.codebeneath.lyrics.verse;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import java.util.Optional;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.codebeneath.lyrics.impacted.Impacted;
import org.codebeneath.lyrics.impacted.ImpactedRepository;
import org.codebeneath.lyrics.tag.TagRepository;
import org.codebeneath.lyrics.impacted.ImpactedNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
@Controller
public class VerseController {
    
    private final Counter createdCounter = Metrics.counter("verses.created");
    private final Counter updatedCounter = Metrics.counter("verses.updated");

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
        model.addAttribute("impacted", getImpactedUser(1L));
        Verse verseToPopulateWith = new Verse();
        if (randomVerseId.isPresent()) {
            Verse sourceVerse = verseRepo.findById(randomVerseId.get()).get();
            verseToPopulateWith.setSong(sourceVerse.getSong());
            verseToPopulateWith.setArtist(sourceVerse.getArtist());
            verseToPopulateWith.setLyrics(sourceVerse.getLyrics());            
        }
        model.addAttribute("verse", verseToPopulateWith);
        return "verseForm";
    }

    @GetMapping("/verse/{id}")
    public String getVerseForm(Model model, @PathVariable Long id){
        model.addAttribute("impacted", getImpactedUser(1L));
        Optional<Verse> verseToPopulateWith = verseRepo.findById(id);
        if (verseToPopulateWith.isPresent()) {
            model.addAttribute("verse", verseToPopulateWith.get());
            return "verseForm";
        } else {
            return "redirect:/";
        }
    }
    
    @PostMapping("/verse")
    public String addVerse(@Valid Verse verse, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "verseForm";
        }
        Impacted impactedUser = getImpactedUser(1L);
        boolean newVerse = verse.getId() == null; 
        verse.setImpacted(impactedUser);
        verseRepo.save(verse);

        if (newVerse) {
            createdCounter.increment();
        } else {
            updatedCounter.increment();
        }
        
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
