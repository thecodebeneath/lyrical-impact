package org.codebeneath.lyrics.lookup;

import lombok.extern.slf4j.Slf4j;
import org.codebeneath.lyrics.lookup.poems.poetrydb.PoetryDbClient;
import org.codebeneath.lyrics.lookup.songs.cajunlyrics.CajunLyricsClient;
import org.codebeneath.lyrics.lookup.songs.chartlyrics.ChartLyricsClient;
import org.codebeneath.lyrics.lookup.songs.musixmatch.MusixMatchClient;
import org.springframework.stereotype.Component;

/**
 * Ideas for external API calls to help users lookup title/author given the verse text.
 */
@Slf4j
@Component
public class ExternalApiExperiments { // implements CommandLineRunner {

    PoetryDbClient poetryDbClient = new PoetryDbClient();
    CajunLyricsClient cajunLyricsClient = new CajunLyricsClient();
    ChartLyricsClient chartLyricsClient = new ChartLyricsClient();
    MusixMatchClient musixMatchClient = new MusixMatchClient();

//    public static void main(String... args) throws Exception {
//        ExternalApiExperiments e = new ExternalApiExperiments();
//        e.run(args);
//    }
    
//    @Override
    public void run(String... args) throws Exception {
        poetryDbClient.call();
//        cajunLyricsClient.call();
//        chartLyricsClient.call();
//        musixMatchClient.call();
    }
}
