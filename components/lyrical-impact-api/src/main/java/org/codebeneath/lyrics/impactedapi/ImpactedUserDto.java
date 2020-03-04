package org.codebeneath.lyrics.impactedapi;

import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author black
 */
@Getter
@Setter
@ToString
public class ImpactedUserDto {
    
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
}
