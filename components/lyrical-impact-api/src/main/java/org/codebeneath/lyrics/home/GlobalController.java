package org.codebeneath.lyrics.home;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.codebeneath.lyrics.impactedapi.ImpactedUser;
import org.codebeneath.lyrics.tagsapi.VerseTag;
import org.codebeneath.lyrics.tagsapi.TagsClient;
import org.codebeneath.lyrics.versesapi.ImpactedVerse;
import org.codebeneath.lyrics.versesapi.VersesClient;
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

    private final VersesClient versesClient;
    private final TagsClient tagsClient;

    public GlobalController(VersesClient versesClient, TagsClient tagsClient) {
        this.versesClient = versesClient;
        this.tagsClient = tagsClient;
    }

    @GetMapping("/global")
    public String impactedVersesGlobal(Model model, @AuthenticationPrincipal ImpactedUser impactedUser,
            @RequestParam(name = "p", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "tag", required = false) String tag,
            @RequestParam(name = "q", required = false) String query) {
        
        setImpactedInModel(impactedUser, model);

        List<ImpactedVerse> verses = versesClient.global(page, tag, query);
        List<VerseTag> tags = tagsClient.getVerseTags();
        model.addAttribute("verses", verses);
        model.addAttribute("allTags", tags);
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
