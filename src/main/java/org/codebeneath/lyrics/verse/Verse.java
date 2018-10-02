package org.codebeneath.lyrics.verse;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.codebeneath.lyrics.impacted.Impacted;
import org.codebeneath.lyrics.tag.Tag;
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
    @Size(min = 1, max = 5000)
    @Column(columnDefinition = "TEXT")
    private String lyrics;
    private String song;
    private String artist;
    private String reaction;

    @ManyToOne(fetch = FetchType.LAZY)
    private Impacted impacted;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Tag> tags = new ArrayList<>();

    protected Verse() {
    }

    public Verse(String lyrics, String song, String artist, String reaction) {
        this.lyrics = lyrics;
        this.song = song;
        this.artist = artist;
        this.reaction = reaction;
    }

    public Verse(String lyrics, String song, String artist, String reaction, Impacted impacted) {
        this.lyrics = lyrics;
        this.song = song;
        this.artist = artist;
        this.reaction = reaction;
        this.impacted = impacted;
    }

    public Verse(String lyrics, String song, String artist, String reaction, Impacted impacted, List<Tag> tags) {
        this.lyrics = lyrics;
        this.song = song;
        this.artist = artist;
        this.reaction = reaction;
        this.impacted = impacted;
        this.tags = tags;
    }

    public String getLyricsAsHtml() {
        return HtmlUtils.htmlEscape(lyrics);
    }
}
