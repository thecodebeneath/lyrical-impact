package org.codebeneath.lyrics.service.songs.musixmatch;

import lombok.extern.slf4j.Slf4j;
import org.codebeneath.lyrics.service.api.ExternalApi;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.jmusixmatch.MusixMatch;
import org.jmusixmatch.MusixMatchException;
import org.jmusixmatch.entity.track.Track;
import org.jmusixmatch.entity.track.TrackData;

/**
 * MusixMatch API
 * Ref: https://developer.musixmatch.com/
 */
@Slf4j
public class MusixMatchClient implements ExternalApi {

    public static final String URL = "https://api.musixmatch.com/ws/1.1/matcher.lyrics.get?format={format}&q_track={track}&q_artist={artist}&apikey={apiKey}";
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void call() throws RestClientException {
        MusixMatchResponse mm = restTemplate.getForObject(URL, MusixMatchResponse.class, "json", "Suicide Blonde", "INXS", "e934fcee28b5acaef679537a54fc9e19");
        log.info("MusixMatch obj: {}", mm.toString());

        MusixMatch musixMatch = new MusixMatch("inject.real.key.here");
        String trackName = "Don't stop the Party";
        String artistName = "The Black Eyed Peas";
        
        try {
            // Track Search [ Fuzzy ]
            Track track = musixMatch.getMatchingTrack(trackName, artistName);
            TrackData data = track.getTrack();
            System.out.println("AlbumID : " + data.getAlbumId());
            System.out.println("Album Name : " + data.getAlbumName());
            System.out.println("Artist ID : " + data.getArtistId());
            System.out.println("Album Name : " + data.getArtistName());
            System.out.println("Track ID : " + data.getTrackId());
        } catch (MusixMatchException mme) {
            log.info("MusixMatch exception", mme);
        }
    }
}
