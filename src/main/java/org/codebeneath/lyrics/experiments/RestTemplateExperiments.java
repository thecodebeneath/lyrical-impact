package org.codebeneath.lyrics.experiments;

import org.codebeneath.lyrics.service.poems.poetrydb.PoetryDbEntry;
import org.codebeneath.lyrics.service.songs.json.MusixMatchResponse;
import org.codebeneath.lyrics.service.songs.xml.CajunGetLyricResult;
import org.codebeneath.lyrics.service.songs.xml.GetLyricResult;
import org.codebeneath.lyrics.service.songs.xml.User;
import org.jmusixmatch.MusixMatch;
import org.jmusixmatch.entity.track.Track;
import org.jmusixmatch.entity.track.TrackData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 *
 */
@Component
public class RestTemplateExperiments {

    private static final Logger LOG = LoggerFactory.getLogger(RestTemplateExperiments.class);

    public void experiment() throws Exception {

        RestTemplate restTemplate = new RestTemplate();

//        final String chartLyricsSearch = "http://api.chartlyrics.com/apiv1.asmx/SearchLyricDirect?artist={artist}&song={song}";
//        String lyricsAsString = restTemplate.getForObject(chartLyricsSearch, String.class, "inxs", "blond");
//        LOG.info("Chart as String: " + lyricsAsString);

//        GetLyricResult chartLyrics = restTemplate.getForObject(chartLyricsSearch, GetLyricResult.class, "fred", "gun");
//        LOG.info("Chart as Obj: " + chartLyrics.toString());
//        final String cajunLyricsSearch = "http://api.cajunlyrics.com/LyricDirectSearch.php?artist={artist}&title={title}";
//        CajunGetLyricResult cajunLyrics = restTemplate.getForObject(cajunLyricsSearch, CajunGetLyricResult.class, "feufollet", "Blues De Dix Ans");
//        LOG.info("Cajun as Obj: " + cajunLyrics.toString());

//        final String userUrl = "https://randomapi.com/api/6de6abfedb24f889e0b5f675edc50deb?fmt=xml&sole";
//        User users = restTemplate.getForObject(userUrl, User.class);
//        LOG.info(users.toString());

//        final String musixmatchUrl = "https://api.musixmatch.com/ws/1.1/matcher.lyrics.get?format={format}&q_track={track}&q_artist={artist}&apikey={apiKey}";
//        MusixMatchResponse mm = restTemplate.getForObject(musixmatchUrl, MusixMatchResponse.class, "json", "Suicide Blonde", "INXS", "e934fcee28b5acaef679537a54fc9e19");
//        LOG.info("mm message: " + mm.toString());

        final String poetryDbUrl = "http://poetrydb.org/lines/{lines}";
        PoetryDbEntry[] p = restTemplate.getForObject(poetryDbUrl, PoetryDbEntry[].class, "foe to my own friends");
        for (int x = 0; x < p.length; x++) {
            // LOG.info("poetrydb: " + p[x].toString());
        }

        // --------------
//        MusixMatch musixMatch = new MusixMatch("inject.real.key.here");
//        String trackName = "Don't stop the Party";
//        String artistName = "The Black Eyed Peas";
//
//        // Track Search [ Fuzzy ]
//        Track track = musixMatch.getMatchingTrack(trackName, artistName);
//        TrackData data = track.getTrack();
//        System.out.println("AlbumID : " + data.getAlbumId());
//        System.out.println("Album Name : " + data.getAlbumName());
//        System.out.println("Artist ID : " + data.getArtistId());
//        System.out.println("Album Name : " + data.getArtistName());
//        System.out.println("Track ID : " + data.getTrackId());
    }

}
