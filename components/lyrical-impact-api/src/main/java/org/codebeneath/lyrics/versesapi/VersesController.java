package org.codebeneath.lyrics.versesapi;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.codebeneath.lyrics.impactedapi.ImpactedUser;
import org.codebeneath.lyrics.tagsapi.VerseTag;
import org.codebeneath.lyrics.tagsapi.TagsClient;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
@Controller
public class VersesController {

    private final Counter createdCounter = Metrics.counter("verses.created");
    private final Counter updatedCounter = Metrics.counter("verses.updated");
    private final Counter deletedCounter = Metrics.counter("verses.deleted");
    private final Counter createdFromGlobalCounter = Metrics.counter("verses.createdFromGlobal");
        
    private final VersesClient versesClient;
    private final TagsClient tagsClient;

    public VersesController(VersesClient versesClient, TagsClient tagsClient) {
        this.versesClient = versesClient;
        this.tagsClient = tagsClient;
    }

    @GetMapping("/verse")
    public String getVerseForm(Model model, @AuthenticationPrincipal ImpactedUser impactedUser, @RequestParam Optional<Long> randomVerseId) {
        model.addAttribute("impacted", impactedUser);

        ImpactedVerse verseToPopulateWith = new ImpactedVerse();
        if (randomVerseId.isPresent()) {
            ImpactedVerse sourceVerse = versesClient.findById(randomVerseId.get()).get();
            verseToPopulateWith = sourceVerse.anonymizeVerse();
        }
        model.addAttribute("verse", verseToPopulateWith);

        List<VerseTag> tags = tagsClient.getVerseTags();
        model.addAttribute("allTags", tags);

        return "impacted/verseForm";
    }

    @GetMapping("/verse/{vid}")
    public String getVerseFormForVerseId(Model model, @AuthenticationPrincipal ImpactedUser impactedUser, @PathVariable Long vid) {
        model.addAttribute("impacted", impactedUser);

        Optional<ImpactedVerse> verseToPopulateWith = versesClient.findByIdAndImpactedId(vid, impactedUser.getId());
        if (verseToPopulateWith.isPresent()) {
            model.addAttribute("verse", verseToPopulateWith.get());
            List<VerseTag> tags = tagsClient.getVerseTags();
            model.addAttribute("allTags", tags);
            return "impacted/verseForm";
        } else {
            return "redirect:/my";
        }
    }

    @GetMapping("/verse/global/{gvid}")
    public String getVerseFormFromGlobal(Model model, @AuthenticationPrincipal ImpactedUser impactedUser, @PathVariable Long gvid) {
        model.addAttribute("impacted", impactedUser);

        Optional<ImpactedVerse> verseToPopulateWith = versesClient.findById(gvid);
        if (verseToPopulateWith.isPresent()) {
            model.addAttribute("verse", verseToPopulateWith.get().anonymizeVerse());
            model.addAttribute("gvid", gvid);
            List<VerseTag> tags = tagsClient.getVerseTags();
            model.addAttribute("allTags", tags);
            return "impacted/verseForm";
        } else {
            return "redirect:/global";
        }
    }

    @PostMapping("/verse")
    public String addVerse(@Valid ImpactedVerse verse, BindingResult bindingResult, Model model, @RequestParam("gvid") Optional<Long> gvid, @AuthenticationPrincipal ImpactedUser impactedUser) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("impacted", impactedUser);
            List<VerseTag> tags = tagsClient.getVerseTags();
            model.addAttribute("allTags", tags);
            return "impacted/verseForm";
        }

        boolean isFromGlobalVerse = gvid.isPresent();
        boolean isNewVerse = verse.getId() == null;
        verse.setImpactedId(impactedUser.getId());
        versesClient.save(verse);

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
    public String deleteVerse(ImpactedVerse verse, @AuthenticationPrincipal ImpactedUser impactedUser) {
        Optional<ImpactedVerse> verseToDelete = versesClient.findByIdAndImpactedId(verse.getId(), impactedUser.getId());
        if (verseToDelete.isPresent()) {
            versesClient.deleteVerseId(verse.getId());
        }

        deletedCounter.increment();

        return "redirect:/my";
    }

}
