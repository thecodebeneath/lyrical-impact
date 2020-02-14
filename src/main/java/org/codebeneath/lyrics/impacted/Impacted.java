package org.codebeneath.lyrics.impacted;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

/**
 * The person that was impacted by a verse and wants to express why.
 */
@Entity
@Getter
@Setter
@ToString
public class Impacted implements OAuth2User, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userName;
    private String email;
    private String firstName;
    private String lastName;
    @Transient 
    private Map<String, Object> attributes;
 
    protected Impacted() {
    }

    public Impacted(String userName, String email, String firstName, String lastName) {
        this.userName = userName;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String getName() {
        return userName; 
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // TODO: set actual roles for users... probably in OAuth2UserService/OIDCUserService imple services...
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        return grantedAuthorities;
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
     * These are injected via OAuth2 principal user attributes.
     */
    public void setAttributes(Map<String, Object> oAuth2Attributes) {
        attributes = oAuth2Attributes;
    }    

}
