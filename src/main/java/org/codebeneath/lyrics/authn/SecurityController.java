package org.codebeneath.lyrics.authn;

import java.security.Principal;
import java.util.Optional;
import org.codebeneath.lyrics.impacted.Impacted;
import org.codebeneath.lyrics.impacted.ImpactedNotFoundException;
import org.codebeneath.lyrics.impacted.ImpactedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author thomas.black
 */
@Controller
public class SecurityController {

    @Autowired
    ImpactedRepository impactedRepo;
        
    @GetMapping("/")
    public String root() {
        return "redirect:/my";
    }

    @GetMapping("/about")
    public String aboutPage(Model model, Principal principal) {
        if (isUserAuthenticated(principal)) {
            Impacted impactedUser = getImpactedUser(principal);
            model.addAttribute("impacted", impactedUser);
        }
        return "about";
    }

    @GetMapping("/user")
    public String userIndex(Model model, Principal principal) {
        Impacted impactedUser = getImpactedUser(principal);
        model.addAttribute("impacted", impactedUser);
        return "user/profile";
    }
    
    @GetMapping("/admin")
    public String adminIndex(Model model, Principal principal) {
        Impacted impactedUser = getImpactedUser(principal);
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
    
    private boolean isUserAuthenticated(Principal p) {
        return p != null ? true : false;
    }
    
    private Impacted getImpactedUser(Principal p) {
        Optional<Impacted> impacted = impactedRepo.findByUserName(p.getName());
        if (!impacted.isPresent()) {
            throw new ImpactedNotFoundException(p.getName());
        }
        return impacted.get();
    }
}
