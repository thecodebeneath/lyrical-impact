package org.codebeneath.lyrics.verse;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import org.codebeneath.lyrics.tag.Tag;

/**
 * The section of a song that impacted someone. This could be a single line, chorus or verse.
 */
@Entity
public class Verse {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String lyrics;
    private String song;
    private String artist;
    private String reaction;
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

    public Verse(String lyrics, String song, String artist, String reaction, Set<Tag> tags) {
        this.lyrics = lyrics;
        this.song = song;
        this.artist = artist;
        this.reaction = reaction;
        this.tags = tags;
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getReaction() {
        return reaction;
    }

    public void setReaction(String reaction) {
        this.reaction = reaction;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return String.format("Verse[id=%d, lyrics='%s', song='%s', artist='%s']",
                id, getLyrics(), getSong(), getArtist());
    }
}
