package org.codebeneath.lyrics.lookup.songs.chartlyrics;

import lombok.extern.slf4j.Slf4j;
import org.codebeneath.lyrics.lookup.api.ExternalApi;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * ChartLyrics API
 * Ref: http://www.chartlyrics.com/api.aspx
 */
@Slf4j
public class ChartLyricsClient implements ExternalApi {

    public static final String URL = "http://api.chartlyrics.com/apiv1.asmx/SearchLyricDirect?artist={artist}&song={song}";
    private final RestTemplate restTemplate = new RestTemplate();

    public static void main(String... args) throws Exception {
        ChartLyricsClient e = new ChartLyricsClient();
        e.call();
    }
        
    @Override
    public void call() throws RestClientException {
        String lyricsAsString = restTemplate.getForObject(URL, String.class, "inxs", "blond");
//        log.info("ChartLyrics as String: {}", lyricsAsString);

        GetLyricResult chartLyrics = restTemplate.getForObject(URL, GetLyricResult.class, "fred", "gun");
        log.info("ChartLyrics as Obj: {}", chartLyrics.toString());
    }

}
