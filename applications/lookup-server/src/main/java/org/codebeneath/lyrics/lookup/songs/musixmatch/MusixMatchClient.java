package org.codebeneath.lyrics.lookup.songs.musixmatch;

import lombok.extern.slf4j.Slf4j;
import org.codebeneath.lyrics.lookup.LookupSuggestion;
import org.springframework.web.client.RestTemplate;
import org.jmusixmatch.MusixMatch;
import org.jmusixmatch.MusixMatchException;
import org.jmusixmatch.entity.track.Track;
import org.jmusixmatch.entity.track.TrackData;
import org.springframework.stereotype.Component;

/**
 * MusixMatch API
 * Ref: https://developer.musixmatch.com/
 */
@Slf4j
@Component
public class MusixMatchClient {

    public static final String URL = "https://api.musixmatch.com/ws/1.1/matcher.lyrics.get?format={format}&q_track={track}&q_artist={artist}&apikey={apiKey}";
    private final RestTemplate restTemplate = new RestTemplate();

    public LookupSuggestion lookup(String lyrics) {
        MusixMatchResponse mmResponse = restTemplate.getForObject(URL, MusixMatchResponse.class, "json", "Suicide Blonde", "INXS", "e934fcee28b5acaef679537a54fc9e19");
        log.info("MusixMatch obj: {}", mmResponse.toString());

        MusixMatch musixMatch = new MusixMatch("inject.real.key.here");
        String trackName = "Don't stop the Party";
        String artistName = "The Black Eyed Peas";
        
        try {
            // Track Search [ Fuzzy ]
            Track track = musixMatch.getMatchingTrack(trackName, artistName);
            TrackData data = track.getTrack();
            log.info("AlbumID : {}", data.getAlbumId());
            log.info("Album Name : {}", data.getAlbumName());
            log.info("Artist ID : {}", data.getArtistId());
            log.info("Album Name : {}", data.getArtistName());
            log.info("Track ID : {}", data.getTrackId());
        } catch (MusixMatchException mme) {
            log.warn("MusixMatch exception: {}", mme.getMessage());
        }
        
        return null;
    }
}
