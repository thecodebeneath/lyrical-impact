package org.codebeneath.lyrics.tag;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Tags that an impacted user might use to remember or group verses. Global to all users.
 */
@Entity
@Getter
@Setter
@ToString
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String label;

    protected Tag() {
    }

    public Tag(String label) {
        this.label = label;
    }

}
