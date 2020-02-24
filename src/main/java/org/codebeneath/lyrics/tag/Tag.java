package org.codebeneath.lyrics.tag;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Tags that an impacted user might use to remember or group verses. Global to all users.
 */
@Getter
@Setter
@ToString
public class Tag {

    private String label;

    protected Tag() {}
        
    public Tag(String label) {
        this.label = label;
    }

}
