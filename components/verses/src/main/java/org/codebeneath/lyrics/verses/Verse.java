package org.codebeneath.lyrics.verses;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * The section of song/poem that impacted someone. This could be a single line, chorus or verse.
 */
@Entity
@Getter
@Setter
@ToString
public class Verse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Size(min = 1, max = 1000)
    @Column(columnDefinition = "TEXT")
    private String text;
    @Size(min = 0, max = 100)
    private String title;
    @Size(min = 0, max = 100)
    private String author;
    @Size(min = 0, max = 500)
    private String reaction;
    @ElementCollection
    private List<String> tags = new ArrayList<>();
    public Long impactedId;

    protected Verse() {
    }

    public Verse(String text, String title, String author, String reaction) {
        this.text = replaceLinefeed(text);
        this.title = title;
        this.author = author;
        this.reaction = replaceLinefeed(reaction);
    }

    public Verse(String text, String title, String author, String reaction, Long impactedId) {
        this.text = replaceLinefeed(text);
        this.title = title;
        this.author = author;
        this.reaction = replaceLinefeed(reaction);
        this.impactedId = impactedId;
    }

    public Verse(String text, String title, String author, String reaction, Long impactedId, List<String> tags) {
        this.text = replaceLinefeed(text);
        this.title = title;
        this.author = author;
        this.reaction = replaceLinefeed(reaction);
        this.impactedId = impactedId;
        this.tags = tags;
    }

    public void setText(String text) {
        this.text = replaceLinefeed(text);
    }

    public void setReaction(String reaction) {
        this.reaction = replaceLinefeed(reaction);
    }

    // intent is to replace \r\n with just \n, such as when text comes from a <textarea> form element
    private String replaceLinefeed(String txt) {
        return txt.replaceAll("[\r]", "");
    }   
    
    public Verse anonymizeVerse() {
        Verse verse = new Verse();
        verse.setText(this.getText());
        verse.setAuthor(this.getAuthor());
        verse.setTitle(this.getTitle());
        return verse;
    }
}
