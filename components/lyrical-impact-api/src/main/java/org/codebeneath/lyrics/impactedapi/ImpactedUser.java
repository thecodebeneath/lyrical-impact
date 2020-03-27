package org.codebeneath.lyrics.impactedapi;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

/**
 * The person that was impacted by a verse and wants to express why.
 */
@Getter
@Setter
@ToString
public class ImpactedUser implements OidcUser, Serializable {

    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_USER = "ROLE_USER";
    public static final String ANONYMOUS_DISPLAY_NAME = "Anonymous User";
    
    private Long id;
    private String uniqueId;
    private String name;
    private String userSource;
    private String displayName;
    private String email;
    private String picture;
    private String locale;
    private String roles;
    private Instant lastLogin;
    private transient Map<String, Object> attributes;
    
    protected ImpactedUser() {
        this.roles = ROLE_USER;
    }

    public ImpactedUser(String name, String userSource, String displayName) {
        this.name = name;
        this.userSource = userSource;
        this.displayName = displayName;
        this.uniqueId = userSource + "-" + name;
        this.roles = ROLE_USER;
    }

    @Override
    public String getName() {
        return name; 
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>(1);
        for (String role : roles.split(",")) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }
    
    /**
     * These are injected via OAuth2 principal user attributes.
     * @return oAuth2Attributes
     */
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    /**
     * These are injected via OIDC principal ID token attributes.
     * @param oAuth2Attributes
     */
    public void setAttributes(Map<String, Object> oAuth2Attributes) {
        attributes = oAuth2Attributes;
    }    

    @Override
    public Map<String, Object> getClaims() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public OidcUserInfo getUserInfo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public OidcIdToken getIdToken() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
