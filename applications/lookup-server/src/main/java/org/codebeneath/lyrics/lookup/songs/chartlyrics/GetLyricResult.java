package org.codebeneath.lyrics.lookup.songs.chartlyrics;

//import javax.xml.bind.annotation.XmlAccessType;
//import javax.xml.bind.annotation.XmlAccessorType;
//import javax.xml.bind.annotation.XmlElement;
//import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Song API : Chart Lyrics
 * http://www.cajunlyrics.com/?page=api
 * Note: Does not marshal from XML to Object because of multiple namespaces:prefixes
 */
//@Getter
//@Setter
@ToString
//@XmlRootElement(name = "GetLyricResult", namespace = "http://api.chartlyrics.com/")
//@XmlAccessorType(XmlAccessType.FIELD)
public class GetLyricResult {

//    @XmlElement(name="TrackId")
    private String trackId;
   
//    @XmlElement(name="LyricChecksum")
//    private String lyricChecksum;
//    
//    @XmlElement(name="LyricId")
//    private String lyricId;
    
//    @XmlElement(name = "LyricSong")
    private String lyricSong;

//    @XmlElement(name="LyricArtist")
//    private String lyricArtist;
//
//    @XmlElement(name="LyricUrl")
//    private String lyricUrl;
//
//    @XmlElement(name="LyricCovertArtUrl")
//    private String lyricCoverArtUrl;
//
//    @XmlElement(name="LyricRank")
//    private String lyricRank;
//
//    @XmlElement(name="LyricCorrectUrl")
//    private String lyricCorrectUrl;
// 
//    @XmlElement(name="Lyric")
//    private String lyric;
    
 }
