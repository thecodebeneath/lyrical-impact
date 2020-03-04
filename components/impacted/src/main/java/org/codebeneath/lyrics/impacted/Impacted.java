package org.codebeneath.lyrics.impacted;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * The person that was impacted by a verse and wants to express why.
 */
@Entity
@Getter
@Setter
@ToString
public class Impacted implements Serializable {

    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_USER = "ROLE_USER";
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    
    protected Impacted() {
    }

    public Impacted(String name, String userSource, String displayName) {
        this.name = name;
        this.userSource = userSource;
        this.displayName = displayName;
        this.uniqueId = userSource + "-" + name;
        this.roles = ROLE_USER;
    }
    
}
