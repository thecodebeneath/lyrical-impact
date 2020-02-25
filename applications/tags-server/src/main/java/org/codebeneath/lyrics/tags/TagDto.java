package org.codebeneath.lyrics.tags;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 */
@Getter
@Setter
@ToString
public class TagDto {
    
    private String label;

    public TagDto() {}
    
    public TagDto(String label) {
        this.label = label;
    }
}
