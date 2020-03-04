package org.codebeneath.lyrics.versesapi;

import java.util.Optional;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.codebeneath.lyrics.impactedapi.ImpactedUser;
import org.codebeneath.lyrics.tagsapi.VerseTagsService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
@Controller
public class VersesController {
        
    private final VersesService versesService;
    private final VerseTagsService tagsService;

    public VersesController(VersesService versesService, VerseTagsService tagsService) {
        this.versesService = versesService;
        this.tagsService = tagsService;
    }

    @GetMapping("/verse")
    public String getVerseForm(Model model, @AuthenticationPrincipal ImpactedUser impactedUser, @RequestParam Optional<Long> randomVerseId) {
        model.addAttribute("impacted", impactedUser);

        ImpactedVerse verseToPopulateWith = new ImpactedVerse();
        if (randomVerseId.isPresent()) {
            ImpactedVerse sourceVerse = versesService.findById(randomVerseId.get()).get();
            verseToPopulateWith = sourceVerse.anonymizeVerse();
        }
        model.addAttribute("verse", verseToPopulateWith);
        model.addAttribute("allTags", tagsService.getVerseTags());
        return "impacted/verseForm";
    }

    @GetMapping("/verse/{vid}")
    public String getVerseFormForVerseId(Model model, @AuthenticationPrincipal ImpactedUser impactedUser, @PathVariable Long vid) {
        model.addAttribute("impacted", impactedUser);

        Optional<ImpactedVerse> verseToPopulateWith = versesService.findByIdAndImpactedId(vid, impactedUser.getId());
        if (verseToPopulateWith.isPresent()) {
            model.addAttribute("verse", verseToPopulateWith.get());
            model.addAttribute("allTags", tagsService.getVerseTags());
            return "impacted/verseForm";
        } else {
            return "redirect:/my";
        }
    }

    @GetMapping("/verse/global/{gvid}")
    public String getVerseFormFromGlobal(Model model, @AuthenticationPrincipal ImpactedUser impactedUser, @PathVariable Long gvid) {
        model.addAttribute("impacted", impactedUser);

        Optional<ImpactedVerse> verseToPopulateWith = versesService.findById(gvid);
        if (verseToPopulateWith.isPresent()) {
            model.addAttribute("verse", verseToPopulateWith.get().anonymizeVerse());
            model.addAttribute("gvid", gvid);
            model.addAttribute("allTags", tagsService.getVerseTags());
            return "impacted/verseForm";
        } else {
            return "redirect:/global";
        }
    }

    @PostMapping("/verse")
    public String addVerse(@Valid @ModelAttribute("verse") ImpactedVerse verse, BindingResult bindingResult, Model model, @RequestParam("gvid") Optional<Long> gvid, @AuthenticationPrincipal ImpactedUser impactedUser) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("impacted", impactedUser);
            model.addAttribute("allTags", tagsService.getVerseTags());
            return "impacted/verseForm";
        }

        boolean isFromGlobalVerse = gvid.isPresent();
        boolean isNewVerse = verse.getId() == null;
        verse.setImpactedId(impactedUser.getId());
        versesService.save(verse, isFromGlobalVerse, isNewVerse);
        return "redirect:/my";
    }

    @PostMapping("/verse/delete")
    public String deleteVerse(ImpactedVerse verse, @AuthenticationPrincipal ImpactedUser impactedUser) {
        Optional<ImpactedVerse> verseToDelete = versesService.findByIdAndImpactedId(verse.getId(), impactedUser.getId());
        if (verseToDelete.isPresent()) {
            versesService.deleteVerseId(verse.getId());
        }
        return "redirect:/my";
    }

}
