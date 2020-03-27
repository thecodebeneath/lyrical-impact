package org.codebeneath.lyrics.lookup.songs.chartlyrics;

import java.io.StringReader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import lombok.extern.slf4j.Slf4j;
import org.codebeneath.lyrics.lookup.LookupSuggestion;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * ChartLyrics API Ref: http://www.chartlyrics.com/api.aspx
 */
@Slf4j
@Component
public class ChartLyricsClient {

    public static final String URL = "http://api.chartlyrics.com/apiv1.asmx/SearchLyricText?lyricText={lyricText}";
    private final RestTemplate restTemplate = new RestTemplate();

    public static void main(String... args) throws Exception {
        ChartLyricsClient e = new ChartLyricsClient();
        log.info("" + e.lookup("dark side of the moon"));
    }

    public LookupSuggestion lookup(String lyrics) {
//        lyrics = "dark side of the moon";        
        // unmarshalls to null because namespace in xml document root element...
        // ArrayOfSearchLyricResult chartLyrics = restTemplate.getForObject(URL, ArrayOfSearchLyricResult.class, lyrics);
        // log.info("  ====== ChartLyrics as Obj: {}", chartLyrics.toString());

        String chartLyricsResults = restTemplate.getForObject(URL, String.class, lyrics);
        SearchLyricResult searchLyricResult = getFirstMatchingResult(chartLyricsResults);
        
        LookupSuggestion suggestion = new LookupSuggestion();
        if (searchLyricResult != null) {
            suggestion.setTitle(searchLyricResult.getSong());
            suggestion.setAuthor(searchLyricResult.getArtist());
        }
        return suggestion;
    }

    private SearchLyricResult getFirstMatchingResult(String chartLyricsResults) {
        SearchLyricResult result = null;
        // strip out problematic namespaces...
        chartLyricsResults = chartLyricsResults.replaceFirst(" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://api.chartlyrics.com/\"", "");
        chartLyricsResults = chartLyricsResults.replaceAll("xsi:nil=\"true\"", "");
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(ArrayOfSearchLyricResult.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            ArrayOfSearchLyricResult arrayResult = (ArrayOfSearchLyricResult) jaxbUnmarshaller.unmarshal(new StringReader(chartLyricsResults));
            result = arrayResult.getSearchLyricResult().get(0);
        } catch (Exception e) {
            log.warn("couldn't handle xml response", e);
        }
        return result;
    }

}
