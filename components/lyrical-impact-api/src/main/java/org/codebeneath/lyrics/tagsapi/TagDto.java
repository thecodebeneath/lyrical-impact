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
public class TagDto {
    
    private Long id;
    private String label;

    public TagDto() {}
    
    public TagDto(String label) {
        this.label = label;
    }
}
