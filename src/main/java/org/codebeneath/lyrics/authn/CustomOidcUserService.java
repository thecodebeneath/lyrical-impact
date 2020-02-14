package org.codebeneath.lyrics.authn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import org.codebeneath.lyrics.impacted.Impacted;
import org.codebeneath.lyrics.impacted.ImpactedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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

        String userProvider = userRequest.getClientRegistration().getRegistrationId(); // "okta"
        String userName = oidcUser.getName();  // attrib[sub]
        
        Impacted impactedOidcUser = new Impacted("jeff", (String) attributes.get("email"), (String) attributes.get("name"), "lastname");
        impactedOidcUser.setAttributes(attributes);
        Impacted impactedLocal = lookupImpactedUser(impactedOidcUser);
        return impactedLocal;
    }

    private Impacted lookupImpactedUser(Impacted impactedOidcUser) {
        Impacted impactedLocal;
        Optional<Impacted> impactedLookup = iRepo.findByUserName(impactedOidcUser.getUserName());
        if (impactedLookup.isPresent()) {
            // autoupdate existing local users with OAuth2User details??
            impactedLocal = impactedLookup.get();
            impactedLocal.setAttributes(impactedOidcUser.getAttributes());
            
            // TODO: add roles to Impacted user table
            Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            impactedLocal.setAuthorities(grantedAuthorities);
        } else {
            impactedLocal = null;
            // impactedLocal = createImpactedUser(impactedOAuth2User);
        }
        
        //System.out.println(impactedLocal);
        return impactedLocal;
    }
}
