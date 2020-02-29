package org.codebeneath.lyrics.home;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.codebeneath.lyrics.impactedapi.ImpactedNotFoundException;
import org.codebeneath.lyrics.impactedapi.ImpactedClient;
import org.codebeneath.lyrics.impactedapi.ImpactedUser;
import org.codebeneath.lyrics.tagsapi.VerseTag;
import org.codebeneath.lyrics.tagsapi.TagsClient;
import org.codebeneath.lyrics.versesapi.ImpactedVerse;
import org.codebeneath.lyrics.versesapi.VersesClient;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author black
 */
@Slf4j
@Controller
public class HomeController {
    
    private final ImpactedClient impactedClient;
    private final VersesClient versesClient;
    private final TagsClient tagsClient;

    public HomeController(ImpactedClient impactedClient, VersesClient versesClient, TagsClient tagsClient) {
        this.impactedClient = impactedClient;
        this.versesClient = versesClient;
        this.tagsClient = tagsClient;
    }

    @GetMapping("/my")
    public String impactedVerses(Model model, @AuthenticationPrincipal ImpactedUser impactedUser,
            @RequestParam(name = "p", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "tag", required = false) String tag,
            @RequestParam(name = "q", required = false) String query) {

        setImpactedInModel(impactedUser, model);

        List<ImpactedVerse> verses = versesClient.findByImpactedId(impactedUser.getId(), page, tag, query);
        List<VerseTag> tags = tagsClient.getVerseTags();
        model.addAttribute("verses", verses);
        model.addAttribute("allTags", tags);
        model.addAttribute("randomVerse", versesClient.getRandomVerse());
        model.addAttribute("newLineChar", '\n');

        return (page == 0) ? "impacted/my" : "impacted/myVersesScroll";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/my/{id}")
    public String impactedVerses(Model model, @AuthenticationPrincipal ImpactedUser impactedUser,
            @PathVariable(required = true) Long id,
            @RequestParam(name = "p", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "tag", required = false) String tag,
            @RequestParam(name = "q", required = false) String query) {

        ImpactedUser otherImpactedUser = getImpactedUser(id);
        setImpactedInModel(otherImpactedUser, model);

        List<ImpactedVerse> verses = versesClient.findByImpactedId(impactedUser.getId(), page, tag, query);
        Collections.reverse(verses);
        List<VerseTag> tags = tagsClient.getVerseTags();
        model.addAttribute("verses", verses);
        model.addAttribute("allTags", tags);
        model.addAttribute("randomVerse", versesClient.getRandomVerse());
        model.addAttribute("newLineChar", '\n');

        return "impacted/my";
    }

    private void setImpactedInModel(ImpactedUser impactedUser, Model model) {
        // https://github.com/spring-projects/spring-security/issues/3208
        if (impactedUser != null) {
            model.addAttribute("impacted", impactedUser);
        }
    }

    private ImpactedUser getImpactedUser(Long id) {
        Optional<ImpactedUser> impacted = impactedClient.findById(id);
        if (!impacted.isPresent()) {
            throw new ImpactedNotFoundException(id);
        }
        return impacted.get();
    }
}
