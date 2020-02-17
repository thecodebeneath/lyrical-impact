package org.codebeneath.lyrics.authn;

import java.util.Map;
import java.util.Optional;
import org.codebeneath.lyrics.impacted.Impacted;
import org.codebeneath.lyrics.impacted.ImpactedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
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
        Map<String, Object> attributes = oidcUser.getAttributes();

        String name = oidcUser.getName();  // attrib[sub]
        String userSource = userRequest.getClientRegistration().getRegistrationId(); // "okta"
        String displayName = attributes.get("name") != null ? (String)attributes.get("name") : null;
        String email = attributes.get("email") != null ? (String)attributes.get("email") : null;
        Impacted impactedOidcUser = new Impacted(name, userSource, displayName);
        impactedOidcUser.setEmail(email);
        impactedOidcUser.setAttributes(attributes);
        
        Impacted impactedLocal = lookupImpactedUser(impactedOidcUser);
        return impactedLocal;
    }

    // user authenticated via external OAauth2 service, now we need a local user to FK verses to
    private Impacted lookupImpactedUser(Impacted impactedOidcUser) {
        Impacted impactedLocal;
        Optional<Impacted> impactedLookup = iRepo.findByUniqueId(impactedOidcUser.getUniqueId());
        if (impactedLookup.isPresent()) {
            // autoupdate existing local users with OAuth2User details??
            impactedLocal = impactedLookup.get();
        } else {
            impactedLocal = iRepo.save(impactedOidcUser);
        }
        // System.out.println(impactedLocal);
        impactedLocal.setAttributes(impactedOidcUser.getAttributes());        
        return impactedLocal;
    }
}
