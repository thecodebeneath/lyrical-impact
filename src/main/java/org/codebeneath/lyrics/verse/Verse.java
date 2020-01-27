package org.codebeneath.lyrics.verse;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.codebeneath.lyrics.impacted.Impacted;
import org.springframework.web.util.HtmlUtils;

/**
 * The section of a song that impacted someone. This could be a single line, chorus or verse.
 */
@Entity
@Getter
@Setter
@ToString
public class Verse {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    @Size(min = 1, max = 1000)
    @Column(columnDefinition = "TEXT")
    private String lyrics;
    @Size(min = 0, max = 100)
    private String song;
    @Size(min = 0, max = 100)
    private String artist;
    @Size(min = 0, max = 500)
    private String reaction;

    @ManyToOne(fetch = FetchType.LAZY)
    private Impacted impacted;

    @ElementCollection
    private List<String> tags = new ArrayList<>();

    protected Verse() {
    }

    public Verse(String lyrics, String song, String artist, String reaction) {
        this.lyrics = replaceLinefeed(lyrics);
        this.song = song;
        this.artist = artist;
        this.reaction = replaceLinefeed(reaction);
    }

    public Verse(String lyrics, String song, String artist, String reaction, Impacted impacted) {
        this.lyrics = replaceLinefeed(lyrics);
        this.song = song;
        this.artist = artist;
        this.reaction = replaceLinefeed(reaction);
        this.impacted = impacted;
    }

    public Verse(String lyrics, String song, String artist, String reaction, Impacted impacted, List<String> tags) {
        this.lyrics = replaceLinefeed(lyrics);
        this.song = song;
        this.artist = artist;
        this.reaction = replaceLinefeed(reaction);
        this.impacted = impacted;
        this.tags = tags;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = replaceLinefeed(lyrics);
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
        verse.setLyrics(this.getLyrics());
        verse.setArtist(this.getArtist());
        verse.setSong(this.getSong());
        return verse;
    }
}
