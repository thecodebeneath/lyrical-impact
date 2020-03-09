package org.codebeneath.lyrics.lookup.poems.poetrydb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Poetry API : PoetryDB
 * http://poetrydb.org/lines/foe%20to%20my%20own%20friends
 */
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class PoetryDbEntry {

    private String title;
    
    private String author;
    
    // private List<String> lines;
}
