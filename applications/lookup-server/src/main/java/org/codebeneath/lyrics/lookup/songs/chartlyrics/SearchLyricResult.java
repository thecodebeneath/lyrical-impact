package org.codebeneath.lyrics.lookup.songs.chartlyrics;

import javax.xml.bind.annotation.XmlElement;
import lombok.Getter;
import lombok.ToString;

/**
 * ChartLyrics API
 * Ref: http://www.chartlyrics.com/api.aspx
 * Note: Does not marshal from XML to Object because of multiple namespaces:prefixes
 */
@Getter
@ToString
public class SearchLyricResult {

//    @XmlElement(name="TrackId")
//    private String trackId;
   
//    @XmlElement(name="LyricChecksum")
//    private String lyricChecksum;
//    
//    @XmlElement(name="LyricId")
//    private String lyricId;
    
//    @XmlElement(name = "SongUrl")
//    private String songUrl;

//    @XmlElement(name="ArtistUrl")
//    private String artistUrl;
//
    @XmlElement(name="Artist")
    private String artist;

    @XmlElement(name="Song")
    private String song;
//
//    @XmlElement(name="SongRank")
//    private String songRank;
    
 }
