package org.codebeneath.lyrics.tagsapi;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 */
@Getter
@Setter
@ToString
public class VerseTag {
    
    private Long id;
    private String label;

    public VerseTag() {}
    
    public VerseTag(String label) {
        this.label = label;
    }
}
