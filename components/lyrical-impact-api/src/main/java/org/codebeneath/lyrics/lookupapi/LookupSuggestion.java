package org.codebeneath.lyrics.lookupapi;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LookupSuggestion {

    private static final String NONE = "No suggestion";
    private String title;
    private String author;

    public LookupSuggestion(String title, String author) {
        this.title = title;
        this.author = author;
    }

    public static LookupSuggestion noSuggestionResult() {
        return new LookupSuggestion(NONE, NONE);
    }
}
