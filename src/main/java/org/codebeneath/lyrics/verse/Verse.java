package org.codebeneath.lyrics.verse;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.codebeneath.lyrics.impacted.Impacted;
import org.codebeneath.lyrics.tag.Tag;

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
    private String lyrics;
    private String song;
    private String artist;
    private String reaction;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private Impacted impacted;
    
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Tag> tags = new HashSet<>();

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

}
