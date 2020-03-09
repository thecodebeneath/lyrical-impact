package org.codebeneath.lyrics.lookup;

import org.codebeneath.lyrics.lookup.songs.cajunlyrics.CajunLyricsClient;
import org.codebeneath.lyrics.lookup.songs.chartlyrics.ChartLyricsClient;
import org.codebeneath.lyrics.lookup.songs.musixmatch.MusixMatchClient;
import org.springframework.stereotype.Service;

/**
 *
 * @author black
 */
@Service
public class LookupSongService {

    CajunLyricsClient cajunLyricsClient;
    ChartLyricsClient chartLyricsClient;
    MusixMatchClient musixMatchClient;

    public LookupSongService(CajunLyricsClient cajunLyricsClient, ChartLyricsClient chartLyricsClient, MusixMatchClient musixMatchClient) {
        this.cajunLyricsClient = cajunLyricsClient;
        this.chartLyricsClient = chartLyricsClient;
        this.musixMatchClient = musixMatchClient;
    }

    public LookupSuggestion lookupSongLyrics(String lyrics) {
        LookupSuggestion bestResult = null;

//        LookupSuggestion cajunResult = cajunLyricsClient.lookup(lyrics);
        LookupSuggestion chartResult = chartLyricsClient.lookup(lyrics);
//        LookupSuggestion musixResult = musixMatchClient.lookup(lyrics);

        if (chartResult != null) {
            bestResult = chartResult;
        }
        return bestResult;
    }
}
