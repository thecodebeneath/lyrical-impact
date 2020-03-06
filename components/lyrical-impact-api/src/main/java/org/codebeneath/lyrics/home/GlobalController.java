package org.codebeneath.lyrics.home;

import lombok.extern.slf4j.Slf4j;
import org.codebeneath.lyrics.impactedapi.ImpactedUser;
import org.codebeneath.lyrics.tagsapi.VerseTagsService;
import org.codebeneath.lyrics.versesapi.VersesService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author black
 */
@Slf4j
@Controller
public class GlobalController {

    private final VersesService versesService;
    private final VerseTagsService tagsService;

    public GlobalController(VersesService versesService, VerseTagsService tagsService) {
        this.versesService = versesService;
        this.tagsService = tagsService;
    }

    @GetMapping("/global")
    public String impactedVersesGlobal(Model model, @AuthenticationPrincipal ImpactedUser impactedUser,
            @RequestParam(name = "p", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "tag", required = false) String tag,
            @RequestParam(name = "q", required = false) String query) {
        
        setImpactedInModel(impactedUser, model);

        model.addAttribute("verses", versesService.global(page, tag, query));
        model.addAttribute("allTags", tagsService.getVerseTags());
        if (tag != null) {
            model.addAttribute("tag", tag);
        }
        model.addAttribute("newLineChar", '\n');

        return (page == 0) ? "impacted/global" : "impacted/globalVersesScroll";
    }
    
    private void setImpactedInModel(ImpactedUser impactedUser, Model model) {
        // https://github.com/spring-projects/spring-security/issues/3208
        if (impactedUser != null) {
            model.addAttribute("impacted", impactedUser);
        }
    }
}
