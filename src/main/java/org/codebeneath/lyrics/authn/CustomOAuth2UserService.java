package org.codebeneath.lyrics.authn;

import java.util.Map;
import java.util.Optional;
import org.codebeneath.lyrics.impacted.Impacted;
import org.codebeneath.lyrics.impacted.ImpactedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

/**
 *
 * @author black
 */
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest,OAuth2User> {

    @Autowired
    private ImpactedRepository iRepo;

    @Override
    public Impacted loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oauth2User = delegate.loadUser(userRequest);
        Map<String, Object> attributes = oauth2User.getAttributes();
        
        String name = oauth2User.getName(); // attrib[id]
        String userSource = userRequest.getClientRegistration().getRegistrationId(); // "github"        
        String displayName = attributes.get("name") != null ? (String)attributes.get("name") : null;
        String email = attributes.get("email") != null ? (String)attributes.get("email") : null;
        Impacted impactedOAuth2User = new Impacted(name, userSource, displayName);
        impactedOAuth2User.setEmail(email);
        impactedOAuth2User.setAttributes(attributes);
        
        Impacted impactedLocal = lookupImpactedUser(impactedOAuth2User);
        return impactedLocal;
    }
    
    // user authenticated via external OAauth2 service, now we need a local user to FK verses to
    private Impacted lookupImpactedUser(Impacted impactedOAuth2User) {
        Impacted impactedLocal;
        Optional<Impacted> impactedLookup = iRepo.findByUniqueId(impactedOAuth2User.getUniqueId());
        if (impactedLookup.isPresent()) {
            // autoupdate existing local users with OAuth2User details??
            impactedLocal = impactedLookup.get();
        } else {
            impactedLocal = iRepo.save(impactedOAuth2User);
        }
        // System.out.println(impactedLocal);
        impactedLocal.setAttributes(impactedOAuth2User.getAttributes());        
        return impactedLocal;
    }
}