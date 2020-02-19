package org.codebeneath.lyrics.authn;

import org.codebeneath.lyrics.impacted.Impacted;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author thomas.black
 */
@Controller
public class SecurityController {
        
    @GetMapping("/")
    public String root() {
        return "redirect:/my";
    }

    @GetMapping("/about")
    public String aboutPage(Model model, @AuthenticationPrincipal Impacted impactedUser) {
        if (impactedUser != null) {
            model.addAttribute("impacted", impactedUser);
        }
        return "about";
    }

    @GetMapping("/privacy")
    public String privacyPage(Model model, @AuthenticationPrincipal Impacted impactedUser) {
        if (impactedUser != null) {
            model.addAttribute("impacted", impactedUser);
        }
        return "about";
    }

    @GetMapping("/user")
    public String userIndex(Model model, @AuthenticationPrincipal Impacted impactedUser) {
        model.addAttribute("impacted", impactedUser);
        return "user/profile";
    }
    
    @GetMapping("/admin")
    public String adminIndex(Model model, @AuthenticationPrincipal Impacted impactedUser) {
        model.addAttribute("impacted", impactedUser);
        return "admin/profile";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "/error/access-denied";
    }
    
}
