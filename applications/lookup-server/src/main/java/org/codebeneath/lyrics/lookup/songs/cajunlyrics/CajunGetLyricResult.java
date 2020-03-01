package org.codebeneath.lyrics.lookup.songs.cajunlyrics;

//import javax.xml.bind.annotation.XmlAccessType;
//import javax.xml.bind.annotation.XmlAccessorType;
//import javax.xml.bind.annotation.XmlElement;
//import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Song API : Cajun Lyrics
 * http://www.cajunlyrics.com/?page=api
 */
@Getter
@Setter
@ToString
//@XmlRootElement(name = "GetLyricResult", namespace = "http://api.cajunlyrics.com/")
//@XmlAccessorType(XmlAccessType.NONE)
public class CajunGetLyricResult {

//    @XmlElement(name = "Id")
    private String id;

//    @XmlElement(name = "LyricsUrl")
    private String lyricsUrl;

//    @XmlElement(name = "ArtistUrl")
    private String artistUrl;

//    @XmlElement(name = "Artist")
    private String artist;

//    @XmlElement(name = "Title")
    private String title;

//    @XmlElement(name = "Lyric")
    private String lyric;
}
