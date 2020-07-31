package org.codebeneath.lyrics.authn;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.codebeneath.lyrics.impactedapi.ImpactedUser;
import org.codebeneath.lyrics.impactedapi.ImpactedClient;
import org.codebeneath.lyrics.impactedapi.ImpactedUserDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

/**
 * Enrich and upsert OIDC user metadata in the impacted database. Return the resulting user object that will be injected as
 * an AuthenticatedPrincipal with all roles and claims.
 */
@Service
public class CustomOidcUserService implements OAuth2UserService<OidcUserRequest, OidcUser> {

    private final Counter newUserCounter = Metrics.counter("impacted.created");
    private final Counter updatedUserCounter = Metrics.counter("impacted.updated");
    private static final String KEYCLOAK_REALM_ACCESS = "realm_access";
    private static final String KEYCLOAK_ROLES = "roles";
    private static final String KEYCLOAK_SOURCE = "keycloak";
    private final ModelMapper modelMapper = new ModelMapper();
    
    @Autowired
    private ImpactedClient impactedClient;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUserService delegate = new OidcUserService();
        OidcUser oidcUser = delegate.loadUser(userRequest);
        OidcIdToken idToken = oidcUser.getIdToken();

        String name = oidcUser.getName();  // attrib[sub]
        String userSource = userRequest.getClientRegistration().getRegistrationId(); // "okta" | "google"
        String displayName = idToken.getFullName() != null ? idToken.getFullName() : ImpactedUser.ANONYMOUS_DISPLAY_NAME;        
        ImpactedUser impactedOidcUser = new ImpactedUser(name, userSource, displayName);
        impactedOidcUser.setEmail(idToken.getEmail());
        impactedOidcUser.setPicture(idToken.getPicture());
        impactedOidcUser.setLocale(idToken.getLocale());
        impactedOidcUser.setAttributes(idToken.getClaims());
        setRolesFromUserSource(userSource, idToken, impactedOidcUser);
        
        ImpactedUser impactedLocal = lookupImpactedUser(impactedOidcUser);
        return impactedLocal;
    }

    // only honor mapped roles from local Keycloak IdP
    private void setRolesFromUserSource(String userSource, OidcIdToken idToken, ImpactedUser impactedOidcUser) {
        if (userSource.equals(KEYCLOAK_SOURCE) && idToken.containsClaim(KEYCLOAK_REALM_ACCESS)) {
            Map<String, Object> keycloakRealmAccess = idToken.getClaimAsMap(KEYCLOAK_REALM_ACCESS);
            List<String> keycloakRealmRoles = (List)keycloakRealmAccess.get(KEYCLOAK_ROLES);
            impactedOidcUser.setRoles(String.join(",", keycloakRealmRoles));
        }
    }
    
    // user authenticated via external OIDC service, now we need a local user to FK verses to
    private ImpactedUser lookupImpactedUser(ImpactedUser impactedOidcUser) {
        ImpactedUser impactedLocal;
        Optional<ImpactedUser> impactedLookup = impactedClient.findByUniqueId(impactedOidcUser.getUniqueId());
        if (impactedLookup.isPresent()) {
            impactedLocal = impactedLookup.get();
            impactedLocal = updateExistingImpacted(impactedLocal, impactedOidcUser);
            updatedUserCounter.increment();
        } else {
            impactedLocal = createNewImpacted(impactedOidcUser);
            newUserCounter.increment();
        }
        impactedLocal.setAttributes(impactedOidcUser.getAttributes());
        return impactedLocal;
    }

    private ImpactedUser updateExistingImpacted(ImpactedUser impactedLocal, ImpactedUser impactedOidcUser) {
        // update existing local users with oidc details that could have changed
        impactedLocal.setDisplayName(impactedOidcUser.getDisplayName());
        impactedLocal.setPicture(impactedOidcUser.getPicture());
        impactedLocal.setLocale(impactedOidcUser.getLocale());
        impactedLocal.setRoles(impactedOidcUser.getRoles());
        impactedLocal.setLastLogin(Instant.now());
        impactedLocal = impactedClient.save(mapToDto(impactedLocal));
        return impactedLocal;
    }

    private ImpactedUser createNewImpacted(ImpactedUser impactedOidcUser) {
        impactedOidcUser.setLastLogin(Instant.now());
        return impactedClient.save(mapToDto(impactedOidcUser));
    }
        
    private ImpactedUserDto mapToDto(ImpactedUser impactedUser) {
        return modelMapper.map(impactedUser, ImpactedUserDto.class);
    }
}
