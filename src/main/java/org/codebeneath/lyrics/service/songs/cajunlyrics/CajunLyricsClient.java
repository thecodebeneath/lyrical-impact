/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codebeneath.lyrics.service.songs.cajunlyrics;

import lombok.extern.slf4j.Slf4j;
import org.codebeneath.lyrics.service.api.ExternalApi;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * CajunLyrics API
 * Ref: https://www.cajunlyrics.com/?page=api
 */
@Slf4j
public class CajunLyricsClient implements ExternalApi {

    public static final String URL = "http://api.cajunlyrics.com/LyricDirectSearch.php?artist={artist}&title={title}";
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void call() throws RestClientException {
        String cajun = restTemplate.getForObject(URL, String.class, "Buckwheat Zydeco", "Tee Nah Nah");
        log.info("Cajun as Str: {}", cajun);

        CajunGetLyricResult cajunLyrics = restTemplate.getForObject(URL, CajunGetLyricResult.class, "Buckwheat Zydeco", "Tee Nah Nah");
        log.info("Cajun as Obj: {}", cajunLyrics.toString());
    }

}
