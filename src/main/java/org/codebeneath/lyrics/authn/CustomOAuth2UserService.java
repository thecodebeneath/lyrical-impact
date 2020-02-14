package org.codebeneath.lyrics.authn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.stereotype.Service;
import org.codebeneath.lyrics.impacted.Impacted;
import org.codebeneath.lyrics.impacted.ImpactedRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest,OAuth2User> {

    @Autowired
    private ImpactedRepository iRepo;

    @Override
    public Impacted loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oauth2User = delegate.loadUser(userRequest);
        Map<String, Object> attributes = oauth2User.getAttributes();
        
        String userProvider = userRequest.getClientRegistration().getRegistrationId(); // "github"
        String userName = oauth2User.getName(); // attrib[id]
        
        Impacted impactedOAuth2User = new Impacted("jeff", (String) attributes.get("email"), (String) attributes.get("name"), "lastname");
        impactedOAuth2User.setAttributes(attributes);
        Impacted impactedLocal = lookupImpactedUser(impactedOAuth2User);
        return impactedLocal;
    }

    private Impacted lookupImpactedUser(Impacted impactedOAuth2User) {
        Impacted impactedLocal;
        Optional<Impacted> impactedLookup = iRepo.findByUserName(impactedOAuth2User.getUserName());
        if (impactedLookup.isPresent()) {
            // autoupdate existing local users with OAuth2User details??
            impactedLocal = impactedLookup.get();
            impactedLocal.setAttributes(impactedOAuth2User.getAttributes());
            
            // TODO: add roles to Impacted user table
            Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            impactedLocal.setAuthorities(grantedAuthorities);
        } else {
            impactedLocal = createImpactedUser(impactedOAuth2User);
        }
        
        //System.out.println(impactedLocal);
        return impactedLocal;
    }
    
    // user authenticated via external OAauth2 service, now we need a local user to FK verses to
    private Impacted createImpactedUser(Impacted impactedOAuth2User) {
//        TODO: implement save logic..
//        TODO: figure out with attributes are available, populated, unique per OAuth2 provider...
//        Impacted impactedUserToSave = new Impacted("","","","");
//        impactedUserToSave.setEmail(impactedOidcUser.getEmail());
//        impactedUserToSave.setImageUrl(impactedOidcUser.getImageUrl());
//        impactedUserToSave.setName(impactedOidcUser.getName());
//        impactedUserToSave.setUserType(impactedOidcUser.google);
//        impactedUserToSave.setEmail((String) attributes.get("email"));
//        impactedUserToSave.setId((String) attributes.get("sub"));
//        impactedUserToSave.setImageUrl((String) attributes.get("picture"));
//        impactedUserToSave.setName((String) attributes.get("name"));
//        iRepo.save(impactedToSave);
         
        return impactedOAuth2User;
    }
}