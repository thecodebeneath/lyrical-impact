package org.codebeneath.lyrics.home;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.codebeneath.lyrics.impactedapi.ImpactedNotFoundException;
import org.codebeneath.lyrics.impactedapi.ImpactedClient;
import org.codebeneath.lyrics.impactedapi.ImpactedUser;
import org.codebeneath.lyrics.tagsapi.VerseTagsService;
import org.codebeneath.lyrics.versesapi.VersesService;
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
    private final VersesService versesService;
    private final VerseTagsService tagsService;

    public HomeController(ImpactedClient impactedClient, VersesService versesService, VerseTagsService tagsService) {
        this.impactedClient = impactedClient;
        this.versesService = versesService;
        this.tagsService = tagsService;
    }

    @GetMapping("/my")
    public String impactedVerses(Model model, @AuthenticationPrincipal ImpactedUser impactedUser,
            @RequestParam(name = "p", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "tag", required = false) String tag,
            @RequestParam(name = "q", required = false) String query) {

        setImpactedInModel(impactedUser, model);

        model.addAttribute("verses", versesService.findByImpactedId(impactedUser.getId(), page, tag, query));
        model.addAttribute("allTags", tagsService.getVerseTags());
        if (tag != null) {
            model.addAttribute("tag", tag);
        }
        model.addAttribute("randomVerse", versesService.getRandomVerse());
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

        model.addAttribute("verses", versesService.findByImpactedId(impactedUser.getId(), page, tag, query));
        model.addAttribute("allTags", tagsService.getVerseTags());
        if (tag != null) {
            model.addAttribute("tag", tag);
        }
        model.addAttribute("randomVerse", versesService.getRandomVerse());
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
