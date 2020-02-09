package org.codebeneath.lyrics.verse;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.codebeneath.lyrics.impacted.Impacted;
import org.codebeneath.lyrics.impacted.ImpactedRepository;
import org.codebeneath.lyrics.tag.TagRepository;
import org.codebeneath.lyrics.impacted.ImpactedNotFoundException;
import org.codebeneath.lyrics.tag.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
//@SessionAttributes("myRequestObject")
@Controller
public class VerseController {

    private final Counter createdCounter = Metrics.counter("verses.created");
    private final Counter updatedCounter = Metrics.counter("verses.updated");
    private final Counter deletedCounter = Metrics.counter("verses.deleted");
    private final Counter createdFromGlobalCounter = Metrics.counter("verses.createdFromGlobal");
            
    private final ImpactedRepository impactedRepo;
    private final VerseRepository verseRepo;
    private final TagRepository tagRepo;

    public VerseController(ImpactedRepository iRepo, VerseRepository vRepo, TagRepository tRepo) {
        this.impactedRepo = iRepo;
        this.verseRepo = vRepo;
        this.tagRepo = tRepo;
    }

    @GetMapping("/verse")
    public String getVerseForm(Model model, Principal principal, @RequestParam Optional<Long> randomVerseId) {
        Impacted impactedUser = getImpactedUser(principal);
        model.addAttribute("impacted", impactedUser);

        Verse verseToPopulateWith = new Verse();
        if (randomVerseId.isPresent()) {
            Verse sourceVerse = verseRepo.findById(randomVerseId.get()).get();
            verseToPopulateWith = sourceVerse.anonymizeVerse();
        }
        model.addAttribute("verse", verseToPopulateWith);

        List<Tag> tags = (List<Tag>) tagRepo.findAll();
        model.addAttribute("allTags", tags);

        return "impacted/verseForm";
    }

    @GetMapping("/verse/{vid}")
    public String getVerseFormForVerseId(Model model, Principal principal, @PathVariable Long vid) {
        Impacted impactedUser = getImpactedUser(principal);
        model.addAttribute("impacted", impactedUser);

        Optional<Verse> verseToPopulateWith = verseRepo.findByIdAndImpactedId(vid, impactedUser.getId());
        if (verseToPopulateWith.isPresent()) {
            model.addAttribute("verse", verseToPopulateWith.get());
            List<Tag> tags = (List<Tag>) tagRepo.findAll();
            model.addAttribute("allTags", tags);
            return "impacted/verseForm";
        } else {
            return "redirect:/my";
        }
    }

    @GetMapping("/verse/global/{gvid}")
    public String getVerseFormFromGlobal(Model model, Principal principal, @PathVariable Long gvid) {
        Impacted impactedUser = getImpactedUser(principal);
        model.addAttribute("impacted", impactedUser);

        Optional<Verse> verseToPopulateWith = verseRepo.findById(gvid);
        if (verseToPopulateWith.isPresent()) {
            model.addAttribute("verse", verseToPopulateWith.get().anonymizeVerse());
            model.addAttribute("gvid", gvid);
            List<Tag> tags = (List<Tag>) tagRepo.findAll();
            model.addAttribute("allTags", tags);
            return "impacted/verseForm";
        } else {
            return "redirect:/global";
        }
    }

    @PostMapping("/verse")
    public String addVerse(@Valid Verse verse, BindingResult bindingResult, Model model, @RequestParam("gvid") Optional<Long> gvid, Principal principal) {
        Impacted impactedUser = getImpactedUser(principal);
        if (bindingResult.hasErrors()) {
            model.addAttribute("impacted", impactedUser);
            List<Tag> tags = (List<Tag>) tagRepo.findAll();
            model.addAttribute("allTags", tags);
            return "impacted/verseForm";
        }

        boolean isFromGlobalVerse = gvid.isPresent();
        boolean isNewVerse = verse.getId() == null;
        verse.setImpacted(impactedUser);
        verseRepo.save(verse);

        if (isFromGlobalVerse) {
            createdFromGlobalCounter.increment();
            createdCounter.increment();
        } else if (isNewVerse) {
            createdCounter.increment();
        } else {
            updatedCounter.increment();
        }

        return "redirect:/my";
    }

    @PostMapping("/verse/delete")
    public String deleteVerse(Verse verse, Principal principal) {
        Impacted impactedUser = getImpactedUser(principal);

        Optional<Verse> verseToDelete = verseRepo.findByIdAndImpactedId(verse.getId(), impactedUser.getId());
        if (verseToDelete.isPresent()) {
            verseRepo.delete(verse);
        }

        deletedCounter.increment();

        return "redirect:/my";
    }

    private Impacted getImpactedUser(Principal p) {
        Optional<Impacted> impacted = impactedRepo.findByUserName(p.getName());
        if (!impacted.isPresent()) {
            throw new ImpactedNotFoundException(p.getName());
        }
        return impacted.get();
    }
}
