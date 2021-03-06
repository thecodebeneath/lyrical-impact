package org.codebeneath.lyrics.versesapi.seed;

import lombok.extern.slf4j.Slf4j;
import org.codebeneath.lyrics.impactedapi.ImpactedUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author black
 */
@Slf4j
@Controller
@RequestMapping("/seed")
public class SeedController {

    private final VersesFixtures versesFixtures;

    public SeedController(VersesFixtures versesFixtures) {
        this.versesFixtures = versesFixtures;
    }

    @GetMapping("/my")
    public String seedMy(Model model, @AuthenticationPrincipal ImpactedUser impactedUser) {
        versesFixtures.loadMy(impactedUser);
        
        model.addAttribute("impacted", impactedUser);
        return "admin/profile";
    }

    @GetMapping("/random")
    public String seedRandom(Model model, @AuthenticationPrincipal ImpactedUser impactedUser) {
        versesFixtures.loadRandom();
        
        model.addAttribute("impacted", impactedUser);
        return "admin/profile";
    }

}
