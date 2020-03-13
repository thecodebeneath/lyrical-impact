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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/verses")
public class VersesController {
        
    private final VersesService versesService;
    private final VerseTagsService tagsService;

    public VersesController(VersesService versesService, VerseTagsService tagsService) {
        this.versesService = versesService;
        this.tagsService = tagsService;
    }

    @GetMapping
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

    @GetMapping("/{vid}")
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

    @GetMapping("/global/{gvid}")
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

    @PostMapping
    public String createVerse(@Valid @ModelAttribute("verse") ImpactedVerse verse, BindingResult bindingResult, Model model, @RequestParam("gvid") Optional<Long> gvid, @AuthenticationPrincipal ImpactedUser impactedUser) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("impacted", impactedUser);
            model.addAttribute("allTags", tagsService.getVerseTags());
            return "impacted/verseForm";
        }

        boolean isFromGlobalVerse = gvid.isPresent();
        verse.setImpactedId(impactedUser.getId());
        versesService.create(verse, isFromGlobalVerse);
        return "redirect:/my";
    }

    @PutMapping("/{vid}")
    public String updateVerse(@Valid @ModelAttribute("verse") ImpactedVerse verse, BindingResult bindingResult, Model model, @PathVariable Long vid, @AuthenticationPrincipal ImpactedUser impactedUser) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("impacted", impactedUser);
            model.addAttribute("allTags", tagsService.getVerseTags());
            return "impacted/verseForm";
        }

        verse.setImpactedId(impactedUser.getId());
        versesService.update(verse, vid);
        return "redirect:/my";
    }

    @DeleteMapping("/{vid}/delete")
    public String deleteVerse(ImpactedVerse verse, @AuthenticationPrincipal ImpactedUser impactedUser, @PathVariable Long vid) {
        Optional<ImpactedVerse> verseToDelete = versesService.findByIdAndImpactedId(vid, impactedUser.getId());
        if (verseToDelete.isPresent()) {
            versesService.deleteVerseId(vid);
        }
        return "redirect:/my";
    }

}
