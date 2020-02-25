package org.codebeneath.lyrics.home;

import java.security.Principal;
import lombok.extern.slf4j.Slf4j;
import org.codebeneath.lyrics.impacted.Impacted;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Experimental endpoint for testing differ OAuth2 types as implemented by identity providers.
 * - Github is OAuth2 and makes use of .userInfoEndpoint() > .userService()
 * - Okta is OpenID Connect (OIDC) and makes use of .userInfoEndpoint() > .oidcUserService()
 * - Facebook ...
 * - Google ...
 */
@Slf4j
@Controller
public class OAuthTypesController {

    @GetMapping("/oathTypesOidc")
    public @ResponseBody String impactedVersesGlobal(@AuthenticationPrincipal OidcUser oidcPrincipal, Principal principal) {
        
        if (oidcPrincipal != null) {
            log.info("...[]...OIDC Principal:{}", oidcPrincipal.getName());
            log.info("...[]...OIDC Principal:{}", oidcPrincipal.getLocale());
        }
        if (principal != null) {
            log.info("...[]...Principal:{}", principal.getName());
        }
        
        return "oathTypes";
    }

    @GetMapping("/oathTypes")
    public @ResponseBody String impactedVersesGlobal(@AuthenticationPrincipal Impacted impactedUser, Principal principal) {
        
        if (impactedUser != null) {
            log.info("...[]...Oauth2 Principal:{}", impactedUser.getId());
        }
        if (principal != null) {
            log.info("...[]...Principal:{}", principal.getName());
        }
        
        return "oathTypes";
    }
}
