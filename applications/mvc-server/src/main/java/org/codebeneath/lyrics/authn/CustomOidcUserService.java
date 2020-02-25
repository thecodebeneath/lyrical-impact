package org.codebeneath.lyrics.authn;

import java.util.Optional;
import org.codebeneath.lyrics.impacted.Impacted;
import org.codebeneath.lyrics.impacted.ImpactedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

/**
 *
 * @author black
 */
@Service
public class CustomOidcUserService implements OAuth2UserService<OidcUserRequest, OidcUser> {

    @Autowired
    private ImpactedRepository iRepo;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUserService delegate = new OidcUserService();
        OidcUser oidcUser = delegate.loadUser(userRequest);
        OidcIdToken idToken = oidcUser.getIdToken();

        String name = oidcUser.getName();  // attrib[sub]
        String userSource = userRequest.getClientRegistration().getRegistrationId(); // "okta" | "google"
        String displayName = idToken.getFullName() != null ? idToken.getFullName() : Impacted.ANONYMOUS_DISPLAY_NAME;        
        Impacted impactedOidcUser = new Impacted(name, userSource, displayName);
        impactedOidcUser.setEmail(idToken.getEmail());
        impactedOidcUser.setPicture(idToken.getPicture());
        impactedOidcUser.setLocale(idToken.getLocale());
        impactedOidcUser.setAttributes(idToken.getClaims());
        
        Impacted impactedLocal = lookupImpactedUser(impactedOidcUser);
        return impactedLocal;
    }
    

    // user authenticated via external OIDC service, now we need a local user to FK verses to
    private Impacted lookupImpactedUser(Impacted impactedOidcUser) {
        Impacted impactedLocal;
        Optional<Impacted> impactedLookup = iRepo.findByUniqueId(impactedOidcUser.getUniqueId());
        if (impactedLookup.isPresent()) {
            impactedLocal = impactedLookup.get();
            // update existing local users with oidc details
            if (userDetailsHaveChanged(impactedLocal, impactedOidcUser)) {
                impactedLocal.setDisplayName(impactedOidcUser.getDisplayName());
                impactedLocal.setPicture(impactedOidcUser.getPicture());
                impactedLocal.setLocale(impactedOidcUser.getLocale());
                impactedLocal = iRepo.save(impactedLocal);
            }
        } else {
            impactedLocal = iRepo.save(impactedOidcUser);
        }
        // System.out.println(impactedLocal);
        impactedLocal.setAttributes(impactedOidcUser.getAttributes());        
        return impactedLocal;
    }
    
    private boolean userDetailsHaveChanged(Impacted existing, Impacted latest) {
        // TODO: implement equals(O o) instead...
        if (existing.getDisplayName() != null ? existing.getDisplayName().equals(latest.getDisplayName()) : latest.getDisplayName() == null) return false;
        if (existing.getPicture() != null ? existing.getPicture().equals(latest.getPicture()) : latest.getPicture() == null) return false;
        return existing.getLocale() != null ? !existing.getLocale().equals(latest.getLocale()) : latest.getLocale() != null;
    }
}
