package org.codebeneath.lyrics.home;

import org.codebeneath.lyrics.impactedapi.ImpactedUser;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author thomas.black
 */
@Controller
public class SecurityController {
    
    private static final String ATTR_IMPACTED = "impacted";
    
    @GetMapping("/")
    public String root() {
        return "redirect:/my";
    }

    @GetMapping(value = "/about", produces = MediaType.TEXT_HTML_VALUE)
    public String aboutPage(Model model, @AuthenticationPrincipal ImpactedUser impactedUser) {
        if (impactedUser != null) {
            model.addAttribute(ATTR_IMPACTED, impactedUser);
        }
        return "about";
    }

    @GetMapping(value = "/privacy", produces = MediaType.TEXT_HTML_VALUE)
    public String privacyPage(Model model, @AuthenticationPrincipal ImpactedUser impactedUser) {
        if (impactedUser != null) {
            model.addAttribute(ATTR_IMPACTED, impactedUser);
        }
        return "about";
    }

    @GetMapping(value = "/user", produces = MediaType.TEXT_HTML_VALUE)
    public String userIndex(Model model, @AuthenticationPrincipal ImpactedUser impactedUser) {
        model.addAttribute(ATTR_IMPACTED, impactedUser);
        return "user/profile";
    }
    
    @GetMapping(value = "/admin", produces = MediaType.TEXT_HTML_VALUE)
    public String adminIndex(Model model, @AuthenticationPrincipal ImpactedUser impactedUser) {
        model.addAttribute(ATTR_IMPACTED, impactedUser);
        return "admin/profile";
    }

    @GetMapping(value = "/login", produces = MediaType.TEXT_HTML_VALUE)
    public String login() {
        return "login";
    }

    @GetMapping(value = "/access-denied", produces = MediaType.TEXT_HTML_VALUE)
    public String accessDenied() {
        return "/error/access-denied";
    }
    
    @GetMapping(value={"/robots.txt", "/robot.txt"}, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String getRobotsTxt() {
    return "User-agent: * \n" +
           "Disallow: /my \n" +
           "Disallow: /admin \n" +
           "Disallow: /user \n" +
           "Disallow: /logout \n" +
           "Disallow: /error \n" +
           "Disallow: /access-denied \n";
    }
}
